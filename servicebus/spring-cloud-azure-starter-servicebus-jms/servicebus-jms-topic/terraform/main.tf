terraform {
  required_providers {
    azurerm = {
      source = "hashicorp/azurerm"
      version = ">= 2.75"
    }
    azurecaf = {
      source = "aztfmod/azurecaf"
      version = "1.2.10"
    }
  }
}

provider "azurerm" {
  features {}
}

resource "azurecaf_name" "resource_group" {
  name = var.application_name
  resource_type = "azurerm_resource_group"
  random_length = 5
  clean_input = true
}

resource "azurerm_resource_group" "main" {
  name = azurecaf_name.resource_group.result
  location = var.location

  tags = {
    "terraform" = "true"
    "application-name" = var.application_name
    "spring-cloud-azure-sample" = var.sample_tag_value
  }
}

resource "azurecaf_name" "azurecaf_name_servicebus" {
  name = var.application_name
  resource_type = "azurerm_servicebus_namespace"
  random_length = 5
  clean_input = true
}

resource "azurerm_servicebus_namespace" "servicebus_namespace" {
  name = azurecaf_name.azurecaf_name_servicebus.result
  location = var.location
  resource_group_name = azurerm_resource_group.main.name

  sku = "Standard"
  zone_redundant = false

  tags = {
    "spring-cloud-azure-sample" = var.sample_tag_value
  }
}

resource "azurecaf_name" "servicebus_namespace_authorization_rule" {
  name = var.application_name
  resource_type = "azurerm_servicebus_namespace_authorization_rule"
}

resource "azurerm_servicebus_namespace_authorization_rule" "authorization_rule" {
  name = azurecaf_name.servicebus_namespace_authorization_rule.result
  namespace_name = azurerm_servicebus_namespace.servicebus_namespace.name
  resource_group_name = azurerm_resource_group.main.name

  listen = true
  send = true
  manage = true
}

resource "azurerm_servicebus_topic" "application" {
  name = "tpc001"
  namespace_name = azurerm_servicebus_namespace.servicebus_namespace.name
  resource_group_name = azurerm_resource_group.main.name
}

resource "azurerm_servicebus_subscription" "application" {
  name = "sub001"
  resource_group_name = azurerm_resource_group.main.name
  namespace_name = azurerm_servicebus_namespace.servicebus_namespace.name
  topic_name = azurerm_servicebus_topic.application.name
  max_delivery_count = 1
}