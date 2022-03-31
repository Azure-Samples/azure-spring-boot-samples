terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "2.99"
    }
    azurecaf = {
      source  = "aztfmod/azurecaf"
      version = "1.2.16"
    }
    random = {
      source  = "hashicorp/random"
      version = "3.1.0"
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

# Used to get object_id
data "azurerm_client_config" "current" {
}

resource "random_string" "random" {
  length = 5
  min_lower = 5
  special = false
}

resource "azurerm_cosmosdb_sql_role_definition" "role" {
  name                = "cosmosdb-sql-role-definition-${random_string.random.result}"
  resource_group_name = azurerm_resource_group.main.name
  account_name        = azurerm_cosmosdb_account.application.name
  type                = "CustomRole"
  assignable_scopes   = ["/subscriptions/${data.azurerm_client_config.current.subscription_id}/resourceGroups/${azurerm_resource_group.main.name}/providers/Microsoft.DocumentDB/databaseAccounts/${azurerm_cosmosdb_account.application.name}"]

  permissions {
    data_actions = ["Microsoft.DocumentDB/databaseAccounts/readMetadata",
      "Microsoft.DocumentDB/databaseAccounts/sqlDatabases/containers/items/read",
      "Microsoft.DocumentDB/databaseAccounts/sqlDatabases/containers/executeQuery",
      "Microsoft.DocumentDB/databaseAccounts/sqlDatabases/containers/readChangeFeed",
      "Microsoft.DocumentDB/databaseAccounts/sqlDatabases/containers/*",
      "Microsoft.DocumentDB/databaseAccounts/sqlDatabases/containers/items/*"]
  }
}

resource "azurerm_cosmosdb_sql_role_assignment" "assignment" {
  resource_group_name = azurerm_resource_group.main.name
  account_name        = azurerm_cosmosdb_account.application.name
  role_definition_id  = azurerm_cosmosdb_sql_role_definition.role.id
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


