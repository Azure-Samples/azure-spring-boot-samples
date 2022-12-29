terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "3.36.0"
    }
    azurecaf = {
      source  = "aztfmod/azurecaf"
      version = "1.2.23"
    }
  }
}

provider "azurerm" {
  features {}
}

provider "azuread" {

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

# ==================user assigned identity=====
resource "azurerm_user_assigned_identity" "identity" {
  location            = var.location
  name                = var.user_assigned_identity
  resource_group_name = azurerm_resource_group.main.name
}

# =================== MySQL ================
resource "azurecaf_name" "myql_server_name" {
  name          = var.application_name
  resource_type = "azurerm_mysql_database"
  random_length = 5
  clean_input   = true
}

resource "random_password" "password" {
  length           = 32
  special          = true
  override_special = "_%@"
}

resource "azurerm_mysql_flexible_server" "mysql_server" {
  name                   = azurecaf_name.myql_server_name.result
  resource_group_name    = azurerm_resource_group.main.name
  location               = var.location
  administrator_login    = var.administrator_login
  administrator_password = random_password.password.result
  sku_name               = "B_Standard_B1s"
}

resource "azurerm_mysql_flexible_database" "database" {
  name                = var.database_name
  resource_group_name = azurerm_resource_group.main.name
  server_name         = azurerm_mysql_flexible_server.mysql_server.name
  charset             = "utf8"
  collation           = "utf8_unicode_ci"
}

data "http" "myip" {
  url = "http://whatismyip.akamai.com"
}

locals {
  myip = chomp(data.http.myip.body)
}

resource "azurerm_mysql_flexible_server_firewall_rule" "firewall_clientip" {
  name                = "${var.application_name}-deployagent"
  resource_group_name = azurerm_resource_group.main.name
  server_name         = azurerm_mysql_flexible_server.mysql_server.name
  start_ip_address    = local.myip
  end_ip_address      = local.myip
}
