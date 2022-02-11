terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = ">= 2.75"
    }
    azurecaf = {
      source  = "aztfmod/azurecaf"
      version = "1.2.10"
    }
  }
}

provider "azurerm" {
  features {}
}

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
    "spring-cloud-azure-sample" = var.sample_tag_value
  }
}

// ===========azurerm_cosmosdb===========
resource "azurecaf_name" "cosmos" {
  name          = var.application_name
  resource_type = "azurerm_cosmosdb_account"
  random_length = 5
  clean_input   = true
}

resource "azurerm_cosmosdb_account" "application" {
  name                = azurecaf_name.cosmos.result
  location            = var.location
  resource_group_name = azurerm_resource_group.main.name
  offer_type          = "Standard"
  kind                = "GlobalDocumentDB"

  enable_automatic_failover = true

  consistency_policy {
    consistency_level = "Session"
  }

  geo_location {
    location          = var.location
    failover_priority = 0
  }

  tags = {
    "spring-cloud-azure-sample" = var.sample_tag_value
  }
}

data "azurerm_client_config" "current" {
}

// ===========azurerm_key_vault===========
resource "azurecaf_name" "kv" {
  name          = var.application_name
  resource_type = "azurerm_key_vault"
  random_length = 5
  clean_input   = true
}

resource "azurerm_key_vault" "kv_account" {
  name                        = azurecaf_name.kv.result
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
    "terraform"                 = "true"
    "spring-cloud-azure-sample" = var.sample_tag_value
  }
}
