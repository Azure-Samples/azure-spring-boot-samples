terraform {
  required_providers {
    azurerm = {
      source = "hashicorp/azurerm"
      version = ">= 2.75"
    }
    azurecaf = {
      source = "aztfmod/azurecaf"
      version = "1.2.10"
    }
  }
}

provider "azurerm" {
  features {}
}

// ===========resource_group===========
resource "azurecaf_name" "resource_group" {
  name = var.application_name
  resource_type = "azurerm_resource_group"
  random_length = 5
  clean_input = true
}

resource "azurerm_resource_group" "main" {
  name = azurecaf_name.resource_group.result
  location = var.location

  tags = {
    "terraform" = "true"
    "application-name" = var.application_name
    "spring-cloud-azure-sample" = var.sample_tag_value
  }
}

data "azurerm_client_config" "current" {
}

// ===========azurerm_key_vault_01===========
resource "azurecaf_name" "kv_01" {
  name = var.application_name
  resource_type = "azurerm_resource_group"
  random_length = 5
  clean_input = true
}

resource "azurerm_key_vault" "kv_account_01" {
  name = azurecaf_name.kv_01.result
  location = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name
  enabled_for_disk_encryption = true
  tenant_id = data.azurerm_client_config.current.tenant_id
  soft_delete_retention_days = 7
  purge_protection_enabled = false

  sku_name = "standard"

  access_policy {
    tenant_id = data.azurerm_client_config.current.tenant_id
    object_id = data.azurerm_client_config.current.object_id

    key_permissions = [
      "Get",
      "Delete",
    ]

    secret_permissions = [
      "Get",
      "List",
      "Set",
      "Purge",
      "Delete"
    ]

    storage_permissions = [
      "Get",
      "Delete",
    ]
  }

  tags = {
    "terraform" = "true"
    "spring-cloud-azure-sample" = var.sample_tag_value
  }
}

resource "azurerm_key_vault_secret" "kv_01" {
  name = "secret-name-in-key-vault-1"
  value = "This is a single test value in key vault 1"
  key_vault_id = azurerm_key_vault.kv_account_01.id
}

resource "azurerm_key_vault_secret" "kv_both_01" {
  name = "secret-name-in-key-vault-both"
  value = "This is a both test value in key vault 1"
  key_vault_id = azurerm_key_vault.kv_account_01.id
}


// ===========azurerm_key_vault_02===========
resource "azurecaf_name" "kv_02" {
  name = var.application_name
  resource_type = "azurerm_resource_group"
  random_length = 5
  clean_input = true
}

resource "azurerm_key_vault" "kv_account_02" {
  name = azurecaf_name.kv_02.result
  location = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name
  enabled_for_disk_encryption = true
  tenant_id = data.azurerm_client_config.current.tenant_id
  soft_delete_retention_days = 7
  purge_protection_enabled = false

  sku_name = "standard"

  access_policy {
    tenant_id = data.azurerm_client_config.current.tenant_id
    object_id = data.azurerm_client_config.current.object_id

    key_permissions = [
      "Get",
      "Delete",
    ]

    secret_permissions = [
      "Get",
      "List",
      "Set",
      "Purge",
      "Delete"
    ]

    storage_permissions = [
      "Get",
      "Delete",
    ]
  }

  tags = {
    "terraform" = "true"
    "spring-cloud-azure-sample" = var.sample_tag_value
  }
}

resource "azurerm_key_vault_secret" "kv_02" {
  name = "secret-name-in-key-vault-2"
  value = "This is a single test value in key vault 2"
  key_vault_id = azurerm_key_vault.kv_account_02.id
}


resource "azurerm_key_vault_secret" "kv_02_both" {
  name = "secret-name-in-key-vault-both"
  value = "This is a both test value in key vault 2"
  key_vault_id = azurerm_key_vault.kv_account_02.id
}

