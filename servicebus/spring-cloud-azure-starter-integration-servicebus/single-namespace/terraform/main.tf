terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = ">= 2.75"
    }
    azurecaf = {
      source  = "aztfmod/azurecaf"
      version = "1.2.6"
    }
  }
}


provider "azurerm" {
  features {}
}

locals {
  // If an environment is set up (dev, test, prod...), it is used in the application name
  environment = var.environment == "" ? "dev" : var.environment
}

resource "azurecaf_name" "resource_group" {
  name          = var.application_name
  resource_type = "azurerm_resource_group"
  suffixes      = [local.environment]
#  suffixes      = "sb_dev"
}

resource "azurerm_resource_group" "main" {
  name     = azurecaf_name.resource_group.result
  location = var.location

  tags = {
    "terraform"        = "true"
    "environment"      = local.environment
    "application-name" = var.application_name
  }
}

data "azurerm_client_config" "current" {}

resource "azurecaf_name" "servicebus_namespace" {
   name          = var.servicebusnamespace
   resource_type = "azurerm_servicebus_namespace"
 # suffixes      = [var.environment]
}

resource "azurerm_servicebus_namespace" "application" {
  name                = azurecaf_name.servicebus_namespace.result
  location            = var.location
  resource_group_name = azurerm_resource_group.main.name

  sku            = "Standard"
  zone_redundant = false
}

resource "azurecaf_name" "servicebus_namespace_authorization_rule" {
  name          = var.application_name
  resource_type = "azurerm_servicebus_namespace_authorization_rule"
  #suffixes      = [var.environment]
}

resource "azurerm_servicebus_namespace_authorization_rule" "application" {
  name                = azurecaf_name.servicebus_namespace_authorization_rule.result
  namespace_name      = azurerm_servicebus_namespace.application.name
  resource_group_name = azurerm_resource_group.main.name

  listen = true
  send   = true
  manage = true
}


resource "azurerm_servicebus_queue" "application" {
  name                = "queue1"
  namespace_name      = azurerm_servicebus_namespace.application.name
  resource_group_name = azurerm_resource_group.main.name

  enable_partitioning   = false
  max_delivery_count    = 10
  lock_duration         = "PT30S"
  max_size_in_megabytes = 1024
  requires_session      = false
  default_message_ttl   = "P14D"
}

#resource "azurecaf_name" "topic" {
#  name          = "topic1"
#  resource_type = "azurerm_servicebus_topic"
#  #suffixes      = [var.environment]
#}

resource "azurerm_servicebus_topic" "application" {
  name                = "topic1"
  namespace_name      = azurerm_servicebus_namespace.application.name
  resource_group_name = azurerm_resource_group.main.name
}

#resource "azurecaf_name" "subscription" {
#  name          = "group1"
#  resource_type = "azurerm_servicebus_subscription"
#  #suffixes      = [var.environment]
#}

resource "azurerm_servicebus_subscription" "application" {
  name                = "group1"
  resource_group_name = azurerm_resource_group.main.name
  namespace_name      = azurerm_servicebus_namespace.application.name
  topic_name          = azurerm_servicebus_topic.application.name
  max_delivery_count  = 1
}

resource "azurerm_role_assignment" "servicebus_data_owner" {
  scope                = azurerm_servicebus_namespace.application.id
  role_definition_name = "Azure Service Bus Data Owner"
  principal_id         = var.service_principal_id
}




