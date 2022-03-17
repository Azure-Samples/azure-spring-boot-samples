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

resource "random_string" "random" {
  length    = 5
  min_lower = 5
  special   = false
}

resource "random_uuid" "resource-server-1-scope-1" {
}

resource "random_uuid" "resource-server-1-scope-2" {
}

resource "random_uuid" "resource-server-2-scope-1" {
}

resource "random_uuid" "resource-server-2-scope-2" {
}

resource "random_uuid" "resource-server-1-role-1" {
}

resource "random_uuid" "resource-server-1-role-2" {
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
    redirect_uris = ["http://localhost:8080/login/oauth2/code/",
      "http://localhost:8080/login/oauth2/code/client-1-resource-server-1",
    "http://localhost:8080/login/oauth2/code/client-1-resource-server-2"]

    implicit_grant {
      access_token_issuance_enabled = true
      id_token_issuance_enabled     = true
    }
  }
}


# Configure resource-server-2
resource "azuread_application" "resource-server-2" {
  display_name = "resource-server-2-${random_string.random.result}"

  owners = [data.azuread_client_config.current.object_id]
  # single tenant
  sign_in_audience = "AzureADMyOrg"

  api {
    requested_access_token_version = 2

    oauth2_permission_scope {
      admin_consent_description  = "resource-server-2.scope-1"
      admin_consent_display_name = "resource-server-2.scope-1"
      enabled                    = true
      id                         = random_uuid.resource-server-2-scope-1.result
      type                       = "User"
      value                      = "resource-server-2.scope-1"
    }

    oauth2_permission_scope {
      admin_consent_description  = "resource-server-2.scope-2"
      admin_consent_display_name = "resource-server-2.scope-2"
      enabled                    = true
      id                         = random_uuid.resource-server-2-scope-2.result
      type                       = "User"
      value                      = "resource-server-2.scope-2"
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


# Configure resource-server-1
resource "azuread_application" "resource-server-1" {
  display_name = "resource-server-1-${random_string.random.result}"

  owners = [data.azuread_client_config.current.object_id]
  # single tenant
  sign_in_audience = "AzureADMyOrg"

  api {
    requested_access_token_version = 2

    oauth2_permission_scope {
      admin_consent_description  = "resource-server-1.scope-1"
      admin_consent_display_name = "resource-server-1.scope-1"
      enabled                    = true
      id                         = random_uuid.resource-server-1-scope-1.result
      type                       = "User"
      value                      = "resource-server-1.scope-1"
    }

    oauth2_permission_scope {
      admin_consent_description  = "resource-server-1.scope-2"
      admin_consent_display_name = "resource-server-1.scope-2"
      enabled                    = true
      id                         = random_uuid.resource-server-1-scope-2.result
      type                       = "User"
      value                      = "resource-server-1.scope-2"
    }
  }

  app_role {
    allowed_member_types = ["User"]
    description          = "resource-server-1-role-2"
    display_name         = "resource-server-1-role-2"
    enabled              = true
    id                   = random_uuid.resource-server-1-role-2.result
    value                = "resource-server-1-role-2"
  }

  app_role {
    allowed_member_types = ["User"]
    description          = "resource-server-1-role-1"
    display_name         = "resource-server-1-role-1"
    enabled              = true
    id                   = random_uuid.resource-server-1-role-1.result
    value                = "resource-server-1-role-1"
  }

  required_resource_access {
    resource_app_id = "00000003-0000-0000-c000-000000000000" # Microsoft Graph

    resource_access {
      id   = "e1fe6dd8-ba31-4d61-89e7-88639da4683d" # User.Read
      type = "Scope"
    }
  }

  required_resource_access {
    resource_app_id = azuread_application.resource-server-2.application_id # Resource server 2

    # need grant
    resource_access {
      id   = random_uuid.resource-server-2-scope-1.result # resource-server-2.scope-1
      type = "Scope"
    }
  }

  web {
    redirect_uris = ["http://localhost:8080/login/oauth2/code/"]
  }
}

resource "azuread_service_principal_delegated_permission_grant" "resource-server-1" {
  service_principal_object_id          = azuread_service_principal.resource-server-1.object_id
  resource_service_principal_object_id = azuread_service_principal.resource-server-2.object_id
  claim_values                         = ["resource-server-2.scope-1"]
}

resource "azuread_service_principal" "client-1" {
  application_id               = azuread_application.client-1.application_id
  app_role_assignment_required = false
  owners                       = [data.azuread_client_config.current.object_id]
}

resource "azuread_service_principal" "resource-server-1" {
  application_id               = azuread_application.resource-server-1.application_id
  app_role_assignment_required = false
  owners                       = [data.azuread_client_config.current.object_id]
}

resource "azuread_service_principal" "resource-server-2" {
  application_id               = azuread_application.resource-server-2.application_id
  app_role_assignment_required = false
  owners                       = [data.azuread_client_config.current.object_id]
}


resource "azuread_application_password" "client-1" {
  application_object_id = azuread_application.client-1.object_id
}


resource "azuread_application_password" "resource-server-1" {
  application_object_id = azuread_application.resource-server-1.object_id
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

resource "null_resource" "set_env" {
  depends_on = [azuread_service_principal.resource-server-2]

  provisioner "local-exec" {
    command = "/bin/bash set_identifier_uris.sh"
  }
}
