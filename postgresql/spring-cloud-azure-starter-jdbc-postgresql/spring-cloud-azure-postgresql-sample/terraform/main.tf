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

# =================== PostgreSQL ================
resource "azurecaf_name" "postgresql_server_name" {
  name          = var.application_name
  resource_type = "azurerm_postgresql_database"
  random_length = 5
  clean_input   = true
}

resource "random_password" "password" {
  length           = 32
  special          = true
  override_special = "_%@"
}

resource "azurerm_postgresql_flexible_server" "postgresql_server" {
  name                   = azurecaf_name.postgresql_server_name.result
  resource_group_name    = azurerm_resource_group.main.name
  location               = var.location
  version                = "12"
  administrator_login    = var.administrator_login
  administrator_password = random_password.password.result
  storage_mb             = 32768
  sku_name               = "GP_Standard_D2s_v3"
  zone                   = "2"

  authentication {
    password_auth_enabled         = true
    active_directory_auth_enabled = true
    tenant_id                     = data.azurerm_client_config.current.tenant_id
  }
}

resource "azurerm_postgresql_flexible_server_database" "database" {
  name      = var.database_name
  charset   = "utf8"
  collation = "en_US.utf8"
  server_id = azurerm_postgresql_flexible_server.postgresql_server.id
}

data "azurerm_client_config" "current" {}

resource "azurerm_postgresql_flexible_server_active_directory_administrator" "current_aad_user_admin" {
  server_name         = azurerm_postgresql_flexible_server.postgresql_server.name
  resource_group_name = azurerm_resource_group.main.name
  tenant_id           = data.azurerm_client_config.current.tenant_id
  object_id           = data.azurerm_client_config.current.object_id
  principal_name      = var.aad_administrator_name
  principal_type      = "User"
}

data "http" "myip" {
  url = "http://whatismyip.akamai.com"
}

locals {
  myip = chomp(data.http.myip.body)
}

resource "azurerm_postgresql_flexible_server_firewall_rule" "firewall_clientip" {
  name                = "${var.application_name}-deployagent"
  start_ip_address    = local.myip
  end_ip_address      = local.myip
  server_id           = azurerm_postgresql_flexible_server.postgresql_server.id
}
