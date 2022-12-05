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

data "azurerm_subscription" "current" { }

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
    "terraform"                 = "true"
    "application-name"          = var.application_name
    "spring-cloud-azure-sample" = var.sample_tag_value
  }
}

resource "azurecaf_name" "azurecaf_name_servicebus" {
  name          = var.application_name
  resource_type = "azurerm_servicebus_namespace"
  random_length = 5
  clean_input   = true
}

resource "azurerm_servicebus_namespace" "servicebus_namespace" {
  name                = azurecaf_name.azurecaf_name_servicebus.result
  location            = var.location
  resource_group_name = azurerm_resource_group.main.name

  sku            = "Standard"
  zone_redundant = false

  tags = {
    "terraform"                 = "true"
    "application-name"          = var.application_name
    "spring-cloud-azure-sample" = var.sample_tag_value
  }
}

resource "azurerm_servicebus_topic" "servicebus_topic" {
  name                = "tpc001"
  namespace_id        = azurerm_servicebus_namespace.servicebus_namespace.id
}

resource "azurerm_servicebus_subscription" "servicebus_subscription" {
  name                = "sub001"
  topic_id            = azurerm_servicebus_topic.servicebus_topic.id

  max_delivery_count  = 1
}

data "azurerm_client_config" "client_config" {
}

resource "azurerm_role_assignment" "role_servicebus_data_owner" {
  scope                = azurerm_servicebus_namespace.servicebus_namespace.id
  role_definition_name = "Azure Service Bus Data Owner"
  principal_id         = data.azurerm_client_config.client_config.object_id
}
