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

resource "random_uuid" "role-admin" {
}

resource "random_uuid" "role-user" {
}

# Configure the Azure Active Directory Provider
provider "azuread" {
}

# Configure an app
resource "azuread_application" "resourceserver" {
  display_name = "aad-resource-server-by-filter-stateless-${random_string.random.result}"

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

  single_page_application {
    redirect_uris = ["http://localhost:8080/"]
  }

  app_role {
    allowed_member_types = ["User"]
    description          = "Full admin access"
    display_name         = "Admin"
    enabled              = true
    id                   = random_uuid.role-admin.result
    value                = "Admin"
  }

  app_role {
    allowed_member_types = ["User"]
    description          = "User rule"
    display_name         = "UserRule"
    enabled              = true
    id                   = random_uuid.role-user.result
    value                = "UserRule"
  }

  web {
    implicit_grant {
      access_token_issuance_enabled = true
      id_token_issuance_enabled     = true
    }
  }
}

resource "azuread_service_principal" "resourceserver" {
  application_id               = azuread_application.resourceserver.application_id
  app_role_assignment_required = false
  owners                       = [data.azuread_client_config.current.object_id]
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

resource "azuread_app_role_assignment" "admin" {
  app_role_id         = random_uuid.role-admin.result
  principal_object_id = azuread_user.user.object_id
  resource_object_id  = azuread_service_principal.resourceserver.object_id
}

resource "azuread_app_role_assignment" "user_role" {
  app_role_id         = random_uuid.role-user.result
  principal_object_id = azuread_user.user.object_id
  resource_object_id  = azuread_service_principal.resourceserver.object_id
}

