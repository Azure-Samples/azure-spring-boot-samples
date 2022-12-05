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

data "azurerm_client_config" "current" {
}

# =================== eventhubs ================
resource "azurecaf_name" "azurecaf_name_eventhubs" {
  name          = var.application_name
  resource_type = "azurerm_eventhub_namespace"
  random_length = 5
  clean_input   = true
}

resource "azurerm_eventhub_namespace" "eventhubs_namespace" {
  name                = azurecaf_name.azurecaf_name_eventhubs.result
  location            = var.location
  resource_group_name = azurerm_resource_group.main.name
  sku                 = "Standard"
  capacity            = 1

  tags = {
    terraform                 = "true"
    application-name          = var.application_name
    spring-cloud-azure-sample = var.sample_tag_value
  }
}

resource "azurerm_eventhub" "eventhubs" {
  name                = "eh1"
  namespace_name      = azurerm_eventhub_namespace.eventhubs_namespace.name
  resource_group_name = azurerm_resource_group.main.name
  partition_count     = 2
  message_retention   = 1
}

resource "azurerm_role_assignment" "role_eventhubs_data_owner" {
  scope                = azurerm_eventhub.eventhubs.id
  role_definition_name = "Azure Event Hubs Data Owner"
  principal_id         = data.azurerm_client_config.current.object_id
}
