terraform {
  required_providers {
    azuread = {
      source  = "hashicorp/azuread"
      version = "~> 2.15.0"
    }
  }
}

data "azuread_client_config" "current" {}

# Configure the Azure Active Directory Provider
provider "azuread" {
}

# Configure an app
resource "azuread_application" "aadresourceserverbyfilter" {
  display_name = "aad-resource-server-by-filter"

  owners           = [data.azuread_client_config.current.object_id]
  sign_in_audience = "AzureADMultipleOrgs"

  required_resource_access {
    resource_app_id = "00000003-0000-0000-c000-000000000000" # Microsoft Graph

    resource_access {
      id   = "df021288-bdef-4463-88db-98f22de89214" # User.Read.All
      type = "Role"
    }

    resource_access {
      id   = "b4e74841-8e56-480b-be8b-910348b18b4c" # User.ReadWrite
      type = "Scope"
    }

    resource_access {
      id   = "06da0dbc-49e2-44d2-8312-53f166ab848a" # Directory.Read.All
      type = "Scope"
    }
  }

  required_resource_access {
    resource_app_id = "c5393580-f805-4401-95e8-94b7a6ef2fc2" # Office 365 Management

    resource_access {
      id   = "594c1fb6-4f81-4475-ae41-0c394909246c" # ActivityFeed.Read
      type = "Role"
    }
  }

  single_page_application {
    redirect_uris = ["http://localhost:8080/"]
  }

  web {
    implicit_grant {
      access_token_issuance_enabled = true
      id_token_issuance_enabled     = true
    }
  }
}

resource "azuread_service_principal" "aadresourceserverbyfilter" {
  application_id               = azuread_application.aadresourceserverbyfilter.application_id
  app_role_assignment_required = false
  owners                       = [data.azuread_client_config.current.object_id]
}

resource "azuread_application_password" "aadresourceserverbyfilter" {
  application_object_id = azuread_application.aadresourceserverbyfilter.object_id
}

# Retrieve domain information
data "azuread_domains" "current" {
  only_initial = true
}

# Create a user
resource "azuread_user" "user" {
  user_principal_name = "aadresourceserverbyfilter@${data.azuread_domains.current.domains.0.domain_name}"
  display_name        = "aadresourceserverbyfilter"
  password            = "Azure123456@"
}

resource "azuread_group" "group1" {
  display_name     = "group1"
  owners           = [data.azuread_client_config.current.object_id]
  security_enabled = true
}

resource "azuread_group_member" "group1" {
  group_object_id  = azuread_group.group1.id
  member_object_id = azuread_user.user.id
}
