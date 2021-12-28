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
    "terraform"        = "true"
    "application-name" = var.application_name
    "spring-cloud-azure-sample" = var.sample_tag_value
  }
}

resource "azurecaf_name" "servicebus" {
  name          = var.application_name
  resource_type = "azurerm_servicebus_namespace"
  random_length = 5
  clean_input   = true
}

resource "azurerm_servicebus_namespace" "servicebus_namespace" {
  name                = azurecaf_name.servicebus.result
  location            = var.location
  resource_group_name = azurerm_resource_group.main.name

  sku            = "Standard"
  zone_redundant = false

  tags = {
    "spring-cloud-azure-sample" = var.sample_tag_value
  }
}

resource "azurecaf_name" "servicebus_namespace_authorization_rule" {
  name          = var.application_name
  resource_type = "azurerm_servicebus_namespace_authorization_rule"
}

resource "azurerm_servicebus_namespace_authorization_rule" "application" {
  name                = azurecaf_name.servicebus_namespace_authorization_rule.result
  namespace_name      = azurerm_servicebus_namespace.servicebus_namespace.name
  resource_group_name = azurerm_resource_group.main.name

  listen = true
  send   = true
  manage = true
}


resource "azurerm_servicebus_queue" "queue" {
  name                = "que001"
  namespace_name      = azurerm_servicebus_namespace.servicebus_namespace.name
  resource_group_name = azurerm_resource_group.main.name

  enable_partitioning   = false
  max_delivery_count    = 10
  lock_duration         = "PT30S"
  max_size_in_megabytes = 1024
  requires_session      = false
  default_message_ttl   = "P14D"
}