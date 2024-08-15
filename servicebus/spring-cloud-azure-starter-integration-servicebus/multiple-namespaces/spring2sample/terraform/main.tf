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
    "terraform"                 = "true"
    "application-name"          = var.application_name
    "spring-cloud-azure-sample" = var.sample_tag_value
  }
}

# =================== servicebus_01 ================
resource "azurecaf_name" "servicebus_01" {
  name          = var.application_name
  resource_type = "azurerm_servicebus_namespace"
  random_length = 5
  random_seed = 1
  clean_input   = true
}

resource "azurerm_servicebus_namespace" "servicebus_namespace_01" {
  name                = azurecaf_name.servicebus_01.result
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

resource "azurerm_servicebus_queue" "application_queue_01" {
  name         = "queue1"
  namespace_id = azurerm_servicebus_namespace.servicebus_namespace_01.id

  enable_partitioning   = false
  max_delivery_count    = 10
  lock_duration         = "PT30S"
  max_size_in_megabytes = 1024
  requires_session      = false
  default_message_ttl   = "P14D"
}

# =================== servicebus_02 ================
resource "azurecaf_name" "servicebus_02" {
  name          = var.application_name
  resource_type = "azurerm_servicebus_namespace"
  random_length = 5
  clean_input   = true
}

resource "azurerm_servicebus_namespace" "servicebus_namespace_02" {
  name                = azurecaf_name.servicebus_02.result
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

resource "azurerm_servicebus_queue" "application_queue_02" {
  name         = "queue2"
  namespace_id = azurerm_servicebus_namespace.servicebus_namespace_02.id

  enable_partitioning   = false
  max_delivery_count    = 10
  lock_duration         = "PT30S"
  max_size_in_megabytes = 1024
  requires_session      = false
  default_message_ttl   = "P14D"
}

data "azurerm_client_config" "client_config" {
}

resource "azurerm_role_assignment" "role_servicebus_01_data_sender" {
  scope                = azurerm_servicebus_queue.application_queue_01.id
  role_definition_name = "Azure Service Bus Data Sender"
  principal_id         = data.azurerm_client_config.client_config.object_id
}

resource "azurerm_role_assignment" "role_servicebus_01_data_receiver" {
  scope                = azurerm_servicebus_queue.application_queue_01.id
  role_definition_name = "Azure Service Bus Data Receiver"
  principal_id         = data.azurerm_client_config.client_config.object_id
}

resource "azurerm_role_assignment" "role_servicebus_02_data_sender" {
  scope                = azurerm_servicebus_queue.application_queue_02.id
  role_definition_name = "Azure Service Bus Data Sender"
  principal_id         = data.azurerm_client_config.client_config.object_id
}

resource "azurerm_role_assignment" "role_servicebus_02_data_receiver" {
  scope                = azurerm_servicebus_queue.application_queue_02.id
  role_definition_name = "Azure Service Bus Data Receiver"
  principal_id         = data.azurerm_client_config.client_config.object_id
}
