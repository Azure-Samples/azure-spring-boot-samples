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
    publicip = {
      source = "nxt-engineering/publicip"
      version = "0.0.7"
    }
  }
}

provider "azurerm" {
  features {}
}

provider "publicip" {
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

#======cosmosdb account 1=========
resource "azurecaf_name" "cosmos_01" {
  name          = var.application_name
  resource_type = "azurerm_cosmosdb_account"
  random_length = 5
  clean_input   = true
}

resource "azurerm_cosmosdb_account" "application_01" {
  name                = azurecaf_name.cosmos_01.result
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

resource "azurerm_cosmosdb_sql_database" "db_01" {
  name                = var.cosmos_database_name
  resource_group_name = azurerm_resource_group.main.name
  account_name        = azurerm_cosmosdb_account.application_01.name
  throughput          = 400
}

#======cosmosdb account 2=========
resource "azurecaf_name" "cosmos_02" {
  name          = var.application_name
  resource_type = "azurerm_cosmosdb_account"
  random_length = 4
  clean_input   = true
}

resource "azurerm_cosmosdb_account" "application_02" {
  name                = azurecaf_name.cosmos_02.result
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

resource "azurerm_cosmosdb_sql_database" "db_02" {
  name                = var.cosmos_database_name
  resource_group_name = azurerm_resource_group.main.name
  account_name        = azurerm_cosmosdb_account.application_02.name
  throughput          = 400
}

#======azurerm_mysql_server=========
resource "azurecaf_name" "mysql" {
  name          = var.application_name
  resource_type = "azurerm_mysql_server"
  random_length = 5
  clean_input   = true
}

resource "azurecaf_name" "mysql_login_name" {
  name          = "admin"
  resource_type = "azurerm_mysql_server"
  random_length = 2
  clean_input   = true
}

resource "random_password" "mysql_login_password" {
  length           = 16
  special          = true
  override_special = "!$#%"
}

resource "azurerm_mysql_server" "mysql" {
  name                = azurecaf_name.mysql.result
  location            = var.location
  resource_group_name = azurerm_resource_group.main.name

  administrator_login          = azurecaf_name.mysql_login_name.result
  administrator_login_password = random_password.mysql_login_password.result

  sku_name   = "GP_Gen5_2"
  storage_mb = 5120
  version    = "5.7"

  auto_grow_enabled                 = true
  backup_retention_days             = 7
  geo_redundant_backup_enabled      = true
  infrastructure_encryption_enabled = true
  public_network_access_enabled     = true
  ssl_enforcement_enabled           = true
  ssl_minimal_tls_version_enforced  = "TLS1_2"
}

resource "azurerm_mysql_database" "database" {
  name                = "db_example"
  resource_group_name = azurerm_resource_group.main.name
  server_name         = azurerm_mysql_server.mysql.name
  charset             = "utf8"
  collation           = "utf8_unicode_ci"
}


data "publicip_address" "default_v4" {
  source_ip = "0.0.0.0"
}

resource "azurerm_mysql_firewall_rule" "client_ip" {
  name                = "allowip"
  resource_group_name = azurerm_resource_group.main.name
  server_name         = azurerm_mysql_server.mysql.name
  start_ip_address    = data.publicip_address.default_v4.ip
  end_ip_address      = data.publicip_address.default_v4.ip
}
