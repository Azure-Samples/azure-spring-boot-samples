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
    "terraform"                 = "true"
    "application-name"          = var.application_name
  }
}

# =================== storage ================
resource "azurecaf_name" "storage_account" {
  name          = var.application_name
  resource_type = "azurerm_storage_account"
  random_length = 5
  clean_input   = true
}

resource "azurerm_storage_account" "application" {
  name                     = azurecaf_name.storage_account.result
  resource_group_name      = azurerm_resource_group.main.name
  location                 = var.location
  account_tier             = "Standard"
  account_replication_type = "LRS"

  tags = {
    "spring-cloud-azure-sample" = var.sample_tag_value
    "terraform"                 = "true"
    "application-name"          = var.application_name
  }
}

resource "azurerm_storage_share" "application" {
  name                 = var.share_name
  storage_account_name = azurerm_storage_account.application.name
  quota                = 50
}
