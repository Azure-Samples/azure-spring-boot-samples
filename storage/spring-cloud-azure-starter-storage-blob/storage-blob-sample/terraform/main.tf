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
    azuread = {
      source  = "hashicorp/azuread"
      version = "~> 2.10.0"
    }
    nullresource = {
      source  = "hashicorp/null"
      version = "~> 3.1.0"
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
  }
}


resource "null_resource" "null" {
  provisioner "local-exec" {
    command = "rm -f environment_values.sh"
  }
}

# =================== storage ================
resource "azurecaf_name" "storage_account" {
  name          = var.application_name
  resource_type = "azurerm_storage_account"
  random_length = 5
  clean_input   = true
}

resource "azurerm_storage_account" "application" {
  name                     = azurecaf_name.storage_account.result
  resource_group_name      = azurerm_resource_group.main.name
  location                 = var.location
  account_tier             = "Standard"
  account_replication_type = "LRS"
  allow_blob_public_access = true

  provisioner "local-exec" {
    command = <<EOT
              echo 'export AZURE_STORAGE_ACCOUNT=${azurerm_storage_account.application.name}' >> environment_values.sh
              echo 'export STORAGE_CONTAINER_NAME=${var.container_name}' >> environment_values.sh
              EOT
  }
}

data "azurerm_client_config" "current" {
}

resource "azurerm_storage_container" "application" {
  name                  = var.container_name
  storage_account_name  = azurerm_storage_account.application.name
  container_access_type = "container"
}

resource "azurerm_role_assignment" "storage_blob_contributor" {
  scope                = azurerm_storage_container.application.resource_manager_id
  role_definition_name = "Storage Blob Data Contributor"
  principal_id         = data.azurerm_client_config.current.object_id
}
