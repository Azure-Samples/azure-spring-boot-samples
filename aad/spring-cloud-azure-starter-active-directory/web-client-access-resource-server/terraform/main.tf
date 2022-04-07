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

# used to expose api
resource "random_uuid" "webApiAOboGraph" {
}

resource "random_uuid" "webApiAOboExample" {
}

resource "random_uuid" "webApiB" {
}

resource "random_uuid" "WebApiB_ClientCredential_ExampleScope" {
}

data "azuread_client_config" "current" {}

# Configure the Azure Active Directory Provider
provider "azuread" {
}

# Create a user
resource "azuread_user" "user" {
  user_principal_name = "security-${random_string.random.result}@${data.azuread_domains.example.domains.0.domain_name}"
  display_name        = "security-${random_string.random.result}"
  password            = "Azure123456@"
}

# Create a group
resource "azuread_group" "group" {
  display_name = "group1"
  security_enabled = true
  members = [azuread_user.user.object_id]
}

# ====================Configure webApiB====================
resource "azuread_application" "webApiB" {
  display_name = "webApiB-${random_string.random.result}"

  owners = [data.azuread_client_config.current.object_id]
  # single tenant
  sign_in_audience = "AzureADMyOrg"

  api {
    requested_access_token_version = 2

    oauth2_permission_scope {
      admin_consent_description  = "WebApiB.ExampleScope"
      admin_consent_display_name = "WebApiB.ExampleScope"
      enabled                    = true
      id                         = random_uuid.webApiB.result
      type                       = "User"
      value                      = "WebApiB.ExampleScope"
    }
  }

  app_role {
    allowed_member_types = ["User"]
    description          = "WebApiB.ClientCredential.ExampleScope"
    display_name         = "WebApiB.ClientCredential.ExampleScope"
    enabled              = true
    id                   = random_uuid.WebApiB_ClientCredential_ExampleScope.result
    value                = "WebApiB.ClientCredential.ExampleScope"
  }

  required_resource_access {
    resource_app_id = "00000003-0000-0000-c000-000000000000" # Microsoft Graph

    resource_access {
      id   = "e1fe6dd8-ba31-4d61-89e7-88639da4683d" # User.Read
      type = "Scope"
    }
  }
}

resource "azuread_service_principal" "webApiB" {
  application_id               = azuread_application.webApiB.application_id
  app_role_assignment_required = false
  owners                       = [data.azuread_client_config.current.object_id]
}

# ====================Configure webapp====================
resource "azuread_application" "webapp" {
  display_name = "webapp-${random_string.random.result}"

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

    resource_access {
      id   = "06da0dbc-49e2-44d2-8312-53f166ab848a" # Directory.Read.All
      type = "Scope"
    }
  }

  required_resource_access {
    resource_app_id = "797f4846-ba00-4fd7-ba43-dac1f8f63013" # Azure Service Management

    resource_access {
      id   = "41094075-9dad-400e-a0bd-54e686782033" # user_impersonation
      type = "Scope"
    }
  }

  required_resource_access {
    resource_app_id = azuread_application.webApiB.application_id

    resource_access {
      id   = random_uuid.WebApiB_ClientCredential_ExampleScope.result # WebApiB_ClientCredential_ExampleScope
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

  group_membership_claims = []
}

resource "azuread_service_principal" "webapp" {
  application_id               = azuread_application.webapp.application_id
  app_role_assignment_required = false
  owners                       = [data.azuread_client_config.current.object_id]
}

resource "azuread_application_password" "webapp" {
  application_object_id = azuread_application.webapp.object_id
}

data "azuread_application_published_app_ids" "well_known" {}

resource "azuread_service_principal" "msgraph" {
  application_id = data.azuread_application_published_app_ids.well_known.result.MicrosoftGraph
  use_existing   = true
}

resource "azuread_service_principal" "management" {
  application_id = data.azuread_application_published_app_ids.well_known.result.AzureServiceManagement
  use_existing   = true
}

resource "azuread_service_principal_delegated_permission_grant" "graph" {
  service_principal_object_id          = azuread_service_principal.webapp.object_id
  resource_service_principal_object_id = azuread_service_principal.msgraph.object_id
  claim_values                         = ["Directory.Read.All", "User.Read"]
}

resource "azuread_service_principal_delegated_permission_grant" "management" {
  service_principal_object_id          = azuread_service_principal.webapp.object_id
  resource_service_principal_object_id = azuread_service_principal.management.object_id
  claim_values                         = ["user_impersonation"]
}

resource "azuread_service_principal_delegated_permission_grant" "webapp" {
  service_principal_object_id          = azuread_service_principal.webapp.object_id
  resource_service_principal_object_id = azuread_service_principal.webApiB.object_id
  claim_values                         = ["WebApiB_ClientCredential_ExampleScope"]
}

# ====================Configure webApiA====================
resource "azuread_application" "webApiA" {
  display_name = "webApiA-${random_string.random.result}"

  owners = [data.azuread_client_config.current.object_id]
  # single tenant
  sign_in_audience = "AzureADMyOrg"

  api {
    requested_access_token_version = 2

    oauth2_permission_scope {
      admin_consent_description  = "Obo.Graph.Read"
      admin_consent_display_name = "Obo.Graph.Read"
      enabled                    = true
      id                         = random_uuid.webApiAOboGraph.result
      type                       = "User"
      value                      = "Obo.Graph.Read"
    }

    # `Obo.Graph.Read`
    oauth2_permission_scope {
      admin_consent_description  = "Obo.Graph.Read"
      admin_consent_display_name = "Obo.Graph.Read"
      enabled                    = true
      id                         = random_uuid.webApiAOboGraph.result
      type                       = "User"
      value                      = "Obo.Graph.Read"
    }

    #  `Obo.WebApiA.ExampleScope`
    oauth2_permission_scope {
      admin_consent_description  = "Obo.WebApiA.ExampleScope"
      admin_consent_display_name = "Obo.WebApiA.ExampleScope"
      enabled                    = true
      id                         = random_uuid.webApiAOboExample.result
      type                       = "User"
      value                      = "Obo.WebApiA.ExampleScope"
    }

  }

  required_resource_access {
    resource_app_id = "00000003-0000-0000-c000-000000000000" # Microsoft Graph

    resource_access {
      id   = "e1fe6dd8-ba31-4d61-89e7-88639da4683d" # User.Read
      type = "Scope"
    }
  }

  required_resource_access {
    resource_app_id = azuread_application.webApiB.application_id # webApiB

    # need grant
    resource_access {
      id   = random_uuid.webApiB.result # WebApiB.ExampleScope
      type = "Scope"
    }
  }

}

resource "azuread_service_principal" "webApiA" {
  application_id               = azuread_application.webApiA.application_id
  app_role_assignment_required = false
  owners                       = [data.azuread_client_config.current.object_id]
}

resource "azuread_application_password" "webApiA" {
  application_object_id = azuread_application.webApiA.object_id
}

resource "azuread_service_principal_delegated_permission_grant" "webApiB" {
  service_principal_object_id          = azuread_service_principal.webApiA.object_id
  resource_service_principal_object_id = azuread_service_principal.webApiB.object_id
  claim_values                         = ["WebApiB.ExampleScope"]
}

# Retrieve domain information
data "azuread_domains" "example" {
  only_initial = true
}



