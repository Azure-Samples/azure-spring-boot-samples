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
    terraform                 = "true"
    application-name          = var.application_name
    spring-cloud-azure-sample = var.sample_tag_value
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
    terraform                 = "true"
    application-name          = var.application_name
    spring-cloud-azure-sample = var.sample_tag_value
  }
}

# Used to get object_id
data "azurerm_client_config" "current" {
}

# https://docs.microsoft.com/azure/cosmos-db/how-to-setup-rbac#built-in-role-definitions
# https://github.com/hashicorp/terraform-provider-azurerm/issues/13907#issuecomment-1070916339
resource "azurerm_cosmosdb_sql_role_assignment" "assignment" {
  resource_group_name = azurerm_resource_group.main.name
  account_name        = azurerm_cosmosdb_account.application.name
  role_definition_id  = "/subscriptions/${data.azurerm_client_config.current.subscription_id}/resourceGroups/${azurerm_resource_group.main.name}/providers/Microsoft.DocumentDB/databaseAccounts/${azurerm_cosmosdb_account.application.name}/sqlRoleDefinitions/00000000-0000-0000-0000-000000000002"
  principal_id        = data.azurerm_client_config.current.object_id
  scope               = "/subscriptions/${data.azurerm_client_config.current.subscription_id}/resourceGroups/${azurerm_resource_group.main.name}/providers/Microsoft.DocumentDB/databaseAccounts/${azurerm_cosmosdb_account.application.name}"
}

resource "azurerm_cosmosdb_sql_database" "db" {
  name                = "products"
  resource_group_name = azurerm_resource_group.main.name
  account_name        = azurerm_cosmosdb_account.application.name
  throughput          = 400
}

resource "azurerm_cosmosdb_sql_container" "application" {
  name                  = "users"
  resource_group_name   = azurerm_cosmosdb_account.application.resource_group_name
  account_name          = azurerm_cosmosdb_account.application.name
  database_name         = azurerm_cosmosdb_sql_database.db.name
  partition_key_path    = "/id"
  partition_key_version = 1
  throughput            = 400
}


