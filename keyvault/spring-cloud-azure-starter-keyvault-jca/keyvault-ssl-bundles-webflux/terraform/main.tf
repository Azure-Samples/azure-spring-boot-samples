terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "3.9.0"
    }
    azurecaf = {
      source  = "aztfmod/azurecaf"
      version = "1.2.16"
    }
    azuread = {
      source  = "hashicorp/azuread"
      version = "2.19.0"
    }
  }
}

provider "azurerm" {
  features {
    key_vault {
      purge_soft_delete_on_destroy = true  # Purge soft-deleted vaults when destroyed
      purge_soft_deleted_certificates_on_destroy = true
      purge_soft_deleted_keys_on_destroy = true
      purge_soft_deleted_secrets_on_destroy = true
      recover_soft_deleted_key_vaults = false  # Don’t recover, we want to destroy
    }
  }
}

data "azuread_client_config" "current" {}

// ===========resource_group===========
resource "azurecaf_name" "resource_group" {
  name          = var.application_name
  resource_type = "azurerm_resource_group"
  random_length = 4
  clean_input   = true
}

resource "azurerm_resource_group" "main" {
  name     = azurecaf_name.resource_group.result
  location = var.location

  tags = {
    "terraform"                 = "true"
    "application-name"          = var.application_name
    "spring-cloud-azure-sample" = var.sample_tag_value
  }
}

data "azurerm_client_config" "current" {
}

resource "azuread_application" "app" {
  display_name = "Spring Cloud Azure sample - keyvault-ssl-bundles-webflux"
}

resource "azuread_service_principal" "service_principal" {
  application_id    = azuread_application.app.application_id
}

resource "azuread_application_password" "service_principal_password" {
  application_object_id = azuread_application.app.object_id
}

// ===========azurerm_key_vault_01===========
resource "azurecaf_name" "azurecaf_name_kv_01" {
  name          = "ssl-bundles-webflux-01"
  resource_type = "azurerm_key_vault"
  random_length = 4
  clean_input   = true
}

resource "azurerm_key_vault" "kv_account_01" {
  name                        = azurecaf_name.azurecaf_name_kv_01.result
  location                    = azurerm_resource_group.main.location
  resource_group_name         = azurerm_resource_group.main.name
  enabled_for_disk_encryption = true
  tenant_id                   = data.azurerm_client_config.current.tenant_id
  soft_delete_retention_days  = 7
  purge_protection_enabled    = false

  sku_name = "standard"

  access_policy {
    tenant_id = data.azurerm_client_config.current.tenant_id
    object_id = azuread_service_principal.service_principal.object_id

    secret_permissions = [
      "Get"
    ]
    certificate_permissions = [
      "Get",
      "List"
    ]
  }

  access_policy {
    tenant_id = data.azurerm_client_config.current.tenant_id
    object_id = data.azurerm_client_config.current.object_id

    certificate_permissions = [
      "Create",
      "Get",
      "List",
      "Delete",
      "Purge"
    ]
  }

  tags = {
    terraform                 = "true"
    application-name          = var.application_name
    spring-cloud-azure-sample = var.sample_tag_value
  }
}

resource "azurerm_key_vault_certificate" "self_signed" {
  name         = "self-signed"
  key_vault_id = azurerm_key_vault.kv_account_01.id

  certificate_policy {
    issuer_parameters {
      name = "Self"
    }

    key_properties {
      exportable = true
      key_size   = 2048
      key_type   = "RSA"
      reuse_key  = true
    }

    lifetime_action {
      action {
        action_type = "AutoRenew"
      }

      trigger {
        days_before_expiry = 30
      }
    }

    secret_properties {
      content_type = "application/x-pkcs12"
    }

    x509_certificate_properties {
      # Server Authentication = 1.3.6.1.5.5.7.3.1
      # Client Authentication = 1.3.6.1.5.5.7.3.2
      extended_key_usage = ["1.3.6.1.5.5.7.3.1"]

      key_usage = [
        "cRLSign",
        "dataEncipherment",
        "digitalSignature",
        "keyAgreement",
        "keyCertSign",
        "keyEncipherment",
      ]

      subject            = "CN=localhost"
      validity_in_months = 12
    }
  }
}

// ===========azurerm_key_vault_02===========
resource "azurecaf_name" "azurecaf_name_kv_02" {
  name          = "ssl-bundles-webflux-02"
  resource_type = "azurerm_key_vault"
  random_length = 4
}

resource "azurerm_key_vault" "kv_account_02" {
  name                        = azurecaf_name.azurecaf_name_kv_02.result
  location                    = azurerm_resource_group.main.location
  resource_group_name         = azurerm_resource_group.main.name
  enabled_for_disk_encryption = true
  tenant_id                   = data.azurerm_client_config.current.tenant_id
  soft_delete_retention_days  = 7
  purge_protection_enabled    = false

  sku_name = "standard"

  access_policy {
    tenant_id = data.azurerm_client_config.current.tenant_id
    object_id = azuread_service_principal.service_principal.object_id

    secret_permissions = [
      "Get"
    ]
    certificate_permissions = [
      "Get",
      "List"
    ]
  }

  access_policy {
    tenant_id = data.azurerm_client_config.current.tenant_id
    object_id = data.azurerm_client_config.current.object_id

    certificate_permissions = [
      "Create",
      "Get",
      "List",
      "Delete",
      "Purge"
    ]
  }

  tags = {
    terraform                 = "true"
    application-name          = var.application_name
    spring-cloud-azure-sample = var.sample_tag_value
  }
}

resource "azurerm_key_vault_certificate" "tomcat" {
  name         = "tomcat"
  key_vault_id = azurerm_key_vault.kv_account_02.id

  certificate_policy {
    issuer_parameters {
      name = "Self"
    }

    key_properties {
      exportable = true
      key_size   = 2048
      key_type   = "RSA"
      reuse_key  = true
    }

    lifetime_action {
      action {
        action_type = "AutoRenew"
      }

      trigger {
        days_before_expiry = 30
      }
    }

    secret_properties {
      content_type = "application/x-pkcs12"
    }

    x509_certificate_properties {
      # Server Authentication = 1.3.6.1.5.5.7.3.1
      # Client Authentication = 1.3.6.1.5.5.7.3.2
      extended_key_usage = ["1.3.6.1.5.5.7.3.1"]

      key_usage = [
        "cRLSign",
        "dataEncipherment",
        "digitalSignature",
        "keyAgreement",
        "keyCertSign",
        "keyEncipherment",
      ]

      subject            = "CN=test"
      validity_in_months = 12
    }
  }
}
