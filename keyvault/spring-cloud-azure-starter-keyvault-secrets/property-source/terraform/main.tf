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
  }
}

provider "azurerm" {
  features {}
}

// ===========resource_group===========
resource "azurecaf_name" "resource_group" {
  name          = var.application_name
  resource_type = "azurerm_resource_group"
  random_length = 5
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

// ===========azurerm_key_vault_01===========
resource "azurecaf_name" "azurecaf_name_kv_01" {
  name          = var.application_name
  resource_type = "azurerm_key_vault"
  random_length = 5
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
    object_id = data.azurerm_client_config.current.object_id

    secret_permissions = [
      "Get",
      "List",
      "Set",
      "Purge",
      "Delete"
    ]
  }

  tags = {
    terraform                 = "true"
    application-name          = var.application_name
    spring-cloud-azure-sample = var.sample_tag_value
  }
}

resource "azurerm_key_vault_secret" "key_vault_secret_01" {
  name         = "sampleProperty1"
  value        = "key_vault_secret_01/sampleProperty1Value"
  key_vault_id = azurerm_key_vault.kv_account_01.id
}

resource "azurerm_key_vault_secret" "key_vault_secret_common_01" {
  name         = "samplePropertyInMultipleKeyVault"
  value        = "key_vault_secret_01/samplePropertyInMultipleKeyVaultValue"
  key_vault_id = azurerm_key_vault.kv_account_01.id
}


// ===========azurerm_key_vault_02===========
resource "azurecaf_name" "azurecaf_name_kv_02" {
  name          = var.application_name
  resource_type = "azurerm_key_vault"
  random_length = 4
  clean_input   = true
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
    object_id = data.azurerm_client_config.current.object_id

    secret_permissions = [
      "Get",
      "List",
      "Set",
      "Purge",
      "Delete"
    ]
  }

  tags = {
    terraform                 = "true"
    application-name          = var.application_name
    spring-cloud-azure-sample = var.sample_tag_value
  }
}

resource "azurerm_key_vault_secret" "key_vault_secret_02" {
  name         = "sampleProperty2"
  value        = "key_vault_secret_02/sampleProperty2Value"
  key_vault_id = azurerm_key_vault.kv_account_02.id
}

resource "azurerm_key_vault_secret" "key_vault_secret_common_02" {
  name         = "samplePropertyInMultipleKeyVault"
  value        = "key_vault_secret_02/samplePropertyInMultipleKeyVaultValue"
  key_vault_id = azurerm_key_vault.kv_account_02.id
}
