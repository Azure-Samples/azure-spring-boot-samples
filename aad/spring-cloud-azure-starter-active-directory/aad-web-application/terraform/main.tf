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

# Configure webapp
resource "azuread_application" "webapp" {
  display_name = "webapp"

  owners = [data.azuread_client_config.current.object_id]
  # single tenant
  sign_in_audience = "AzureADMyOrg"

  api {
    requested_access_token_version = 2
  }

#   arm:
#   on-demand: true
#   scopes: https://management.core.windows.net/user_impersonation
#   graph:
#   scopes:
#  - https://graph.microsoft.com/User.Read
#  - https://graph.microsoft.com/Directory.Read.All
  required_resource_access {
    resource_app_id = "00000003-0000-0000-c000-000000000000" # Microsoft Graph

    resource_access {
      id   = "e1fe6dd8-ba31-4d61-89e7-88639da4683d" # User.Read
      type = "Scope"
    }
  }

  web {
    redirect_uris = ["http://localhost:8080/login/oauth2/code/"]

    implicit_grant {
      access_token_issuance_enabled = true
      id_token_issuance_enabled     = true
    }
  }
}

resource "azuread_service_principal" "webapp" {
  application_id               = azuread_application.webapp.application_id
  app_role_assignment_required = false
  owners                       = [data.azuread_client_config.current.object_id]
}


resource "azuread_application_password" "webapp" {
  application_object_id = azuread_application.webapp.object_id
}


# Retrieve domain information
data "azuread_domains" "example" {
  only_initial = true
}

# Create a user
resource "azuread_user" "user" {
  user_principal_name = "webapp@${data.azuread_domains.example.domains.0.domain_name}"
  display_name        = "webapp"
  password            = "Ms@123456"
}