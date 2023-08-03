terraform {
  required_providers {
    azurerm  = {
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
  }
}

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

resource "azurerm_cosmosdb_sql_database" "db" {
  name                = var.cosmos_database_name
  resource_group_name = azurerm_resource_group.main.name
  account_name        = azurerm_cosmosdb_account.application.name
  throughput          = 400
}
