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
    null = {
      source  = "hashicorp/null"
      version = "3.1.0"
    }
  }
}

resource "random_uuid" "webapiB" {
}

resource "random_string" "random" {
  length           = 5
  min_lower        = 5
  special          = false
}

data "azuread_client_config" "current" {}

# Configure the Azure Active Directory Provider
provider "azuread" {
}

# Configure webapiB
resource "azuread_application" "webapiB" {
  display_name = "webapiB"

  owners = [data.azuread_client_config.current.object_id]
  # single tenant
  sign_in_audience = "AzureADMyOrg"

  api {
    requested_access_token_version = 2

    oauth2_permission_scope {
      admin_consent_description  = "WebApiB.ExampleScope"
      admin_consent_display_name = "WebApiB.ExampleScope"
      enabled                    = true
      id                         = random_uuid.webapiB.result
      type                       = "User"
      value                      = "WebApiB.ExampleScope"
    }

  }

  required_resource_access {
    resource_app_id = "00000003-0000-0000-c000-000000000000" # Microsoft Graph

    resource_access {
      id   = "e1fe6dd8-ba31-4d61-89e7-88639da4683d" # User.Read
      type = "Scope"
    }
  }
}


resource "azuread_service_principal" "webapiB" {
  application_id               = azuread_application.webapiB.application_id
  app_role_assignment_required = false
  owners                       = [data.azuread_client_config.current.object_id]
}

resource "azuread_application_password" "webapiB" {
  application_object_id = azuread_application.webapiB.object_id
}

# Retrieve domain information
data "azuread_domains" "example" {
  only_initial = true
}

# Create a user
resource "azuread_user" "user" {
  user_principal_name = "aadresourceserver-${random_string.random.result}@${data.azuread_domains.example.domains.0.domain_name}"
  display_name        = "aadresourceserver-${random_string.random.result}"
  password            = "Azure123456@"
}

resource "null_resource" "set_env" {
  triggers = {
    application_id = azuread_service_principal.webapiB.application_id
  }

  provisioner "local-exec" {
    command = "/bin/bash set_identifier_uris.sh"
  }
}