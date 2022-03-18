terraform {
  required_providers {
    azuread = {
      source  = "hashicorp/azuread"
      version = "2.19.0"
    }
    random = {
      source  = "hashicorp/random"
      version = "3.1.0"
    }
  }
}

resource "random_string" "random" {
  length    = 5
  min_lower = 5
  special   = false
}

data "azuread_client_config" "current" {}

# Configure the Azure Active Directory Provider
provider "azuread" {
}

# Configure an app
resource "azuread_application" "aadresourceserverbyfilter" {
  display_name = "aad-resource-server-by-filter-${random_string.random.result}"

  owners           = [data.azuread_client_config.current.object_id]
  sign_in_audience = "AzureADMultipleOrgs"

  required_resource_access {
    resource_app_id = "00000003-0000-0000-c000-000000000000" # Microsoft Graph

    resource_access {
      id   = "e1fe6dd8-ba31-4d61-89e7-88639da4683d" # User.Read
      type = "Scope"
    }

    resource_access {
      id   = "06da0dbc-49e2-44d2-8312-53f166ab848a" # Directory.Read.All
      type = "Scope"
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

data "azuread_application_published_app_ids" "well_known" {}

resource "azuread_service_principal" "msgraph" {
  application_id = data.azuread_application_published_app_ids.well_known.result.MicrosoftGraph
  use_existing   = true
}

resource "azuread_service_principal_delegated_permission_grant" "graph" {

  service_principal_object_id          = azuread_service_principal.aadresourceserverbyfilter.object_id
  resource_service_principal_object_id = azuread_service_principal.msgraph.object_id
  claim_values                         = ["Directory.Read.All", "User.Read"]
}

# Retrieve domain information
data "azuread_domains" "current" {
  only_initial = true
}

# Create a user
resource "azuread_user" "user" {
  user_principal_name = "security-${random_string.random.result}@${data.azuread_domains.current.domains.0.domain_name}"
  display_name        = "security-${random_string.random.result}"
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
