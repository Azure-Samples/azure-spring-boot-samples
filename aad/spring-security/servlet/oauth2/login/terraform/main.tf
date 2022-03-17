terraform {
  required_providers {
    azuread = {
      source  = "hashicorp/azuread"
      version = "2.19.0"
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

# Configure client-1
resource "azuread_application" "client-1" {
  display_name = "client-1-${random_string.random.result}"

  owners = [data.azuread_client_config.current.object_id]
  # single tenant
  sign_in_audience = "AzureADMyOrg"

  api {
    requested_access_token_version = 2
  }

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


resource "azuread_service_principal" "client-1" {
  application_id               = azuread_application.client-1.application_id
  app_role_assignment_required = false
  owners                       = [data.azuread_client_config.current.object_id]
}


resource "azuread_application_password" "client-1" {
  application_object_id = azuread_application.client-1.object_id
}


# Retrieve domain information
data "azuread_domains" "example" {
  only_initial = true
}

# Create a user
resource "azuread_user" "user" {
  user_principal_name = "security-${random_string.random.result}@${data.azuread_domains.example.domains.0.domain_name}"
  display_name        = "security"
  password            = "Azure123456@"
}
