terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "3.110.0"
    }
    azurecaf = {
      source  = "aztfmod/azurecaf"
      version = "1.2.26"
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

# =================== redis ================
data "azurerm_subscription" "current" { }
data "azuread_client_config" "current" {}

resource "azurecaf_name" "azurecaf_name_redis" {
  name          = var.application_name
  resource_type = "azurerm_redis_cache"
  random_length = 5
  clean_input   = true
}

resource "azurerm_redis_cache" "redis" {
  name                = azurecaf_name.azurecaf_name_redis.result
  location            = var.location
  resource_group_name = azurerm_resource_group.main.name
  capacity            = 0
  family              = "C"
  sku_name            = "Basic"
  enable_non_ssl_port = false
  minimum_tls_version = "1.2"

  redis_configuration {
    enable_authentication = true
    active_directory_authentication_enabled = true
  }
}

resource "azurerm_redis_cache_access_policy_assignment" "current_user" {
  name               = "current-user"
  redis_cache_id     = azurerm_redis_cache.redis.id
  access_policy_name = "Data Contributor"
  object_id          = data.azuread_client_config.current.object_id
  object_id_alias    = "current-user"
}