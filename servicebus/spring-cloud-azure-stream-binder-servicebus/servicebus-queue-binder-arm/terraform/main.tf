terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "3.9.0"
    }
    azurecaf = {
      source  = "aztfmod/azurecaf"
      version = "1.2.10"
    }
    azuread = {
      source  = "hashicorp/azuread"
      version = "2.19.0"
    }
    random = {
      source  = "hashicorp/random"
      version = "3.1.0"
    }
  }
}

data "azuread_client_config" "current" {}

# Configure the Azure Active Directory Provider
provider "azuread" {
}

resource "random_string" "random" {
  length = 5
  min_lower = 5
  special = false
}

# Configure an app
resource "azuread_application" "servicebusqueuebinder" {
  display_name = "servicebus-queue-binder-arm-${random_string.random.result}"
  owners           = [data.azuread_client_config.current.object_id]
}

resource "azuread_service_principal" "servicebusqueuebinder" {
  application_id               = azuread_application.servicebusqueuebinder.application_id
  app_role_assignment_required = false
  owners                       = [data.azuread_client_config.current.object_id]
}

resource "azuread_application_password" "servicebusqueuebinder" {
  application_object_id = azuread_application.servicebusqueuebinder.object_id
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

resource "azurerm_servicebus_queue" "queue" {
  name         = "que001"
  namespace_id = azurerm_servicebus_namespace.servicebus_namespace.id

  enable_partitioning   = false
  max_delivery_count    = 10
  lock_duration         = "PT30S"
  max_size_in_megabytes = 1024
  requires_session      = false
  default_message_ttl   = "P14D"
}

resource "azurerm_role_assignment" "role_servicebus_contributor" {
  scope                = azurerm_servicebus_namespace.servicebus_namespace.id
  role_definition_name = "Contributor"
  principal_id         = azuread_service_principal.servicebusqueuebinder.object_id
}
