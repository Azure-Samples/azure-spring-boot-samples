terraform {
  required_providers {
    azurerm  = {
      source  = "hashicorp/azurerm"
      version = "2.99"
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
    "spring-cloud-azure-sample" = var.sample_tag_value
  }
}

// ===========azurerm_cosmosdb===========
resource "azurecaf_name" "cosmos" {
  name          = var.application_name
  resource_type = "azurerm_cosmosdb_account"
  random_length = 5
  clean_input   = true
}

resource "azurerm_cosmosdb_account" "application" {
  name                = azurecaf_name.cosmos.result
  location            = var.location
  resource_group_name = azurerm_resource_group.main.name
  offer_type          = "Standard"
  kind                = "GlobalDocumentDB"

  enable_automatic_failover = true

  consistency_policy {
    consistency_level = "Session"
  }

  geo_location {
    location          = var.location
    failover_priority = 0
  }

  tags = {
    "spring-cloud-azure-sample" = var.sample_tag_value
  }
}

data "azurerm_client_config" "current" {
}

data "azuread_client_config" "current" {
}


// ===========azurerm_redis_name===========
resource "azurecaf_name" "redis" {
  name          = var.application_name
  resource_type = "azurerm_redis_cache"
  random_length = 5
  clean_input   = true
}
resource "azurerm_redis_cache" "redis" {
  name                = azurecaf_name.redis.result
  location            = var.location
  resource_group_name = azurerm_resource_group.main.name
  capacity            = 0
  family              = "C"
  sku_name            = "Basic"
  enable_non_ssl_port = false
  minimum_tls_version = "1.2"

  redis_configuration {
  }
}

// ===========azurerm_key_vault===========
resource "azurecaf_name" "kv" {
  name          = var.application_name
  resource_type = "azurerm_key_vault"
  random_length = 5
  clean_input   = true
}

resource "azurerm_key_vault" "kv_account" {
  name                        = azurecaf_name.kv.result
  location                    = azurerm_resource_group.main.location
  resource_group_name         = azurerm_resource_group.main.name
  enabled_for_disk_encryption = true
  tenant_id                   = data.azurerm_client_config.current.tenant_id
  soft_delete_retention_days  = 7
  purge_protection_enabled    = false

  sku_name = "standard"

  access_policy {
    tenant_id = data.azurerm_client_config.current.tenant_id
    object_id = data.azurerm_client_config.current.object_id

    secret_permissions = [
      "Get",
      "List",
      "Set",
      "Purge",
      "Delete"
    ]
  }

  tags = {
    "terraform"                 = "true"
    "spring-cloud-azure-sample" = var.sample_tag_value
  }
}

// ===========service_principal_name===========
resource "random_string" "service_principal_name" {
  length = 5
  min_lower = 5
  special = false
}
resource "azuread_application" "azure_key_vault_service_principal" {
  display_name = "pet_clinic_${random_string.service_principal_name.result}"
  owners       = [data.azuread_client_config.current.object_id]
}
resource "azuread_application_password" "azure_key_vault_service_principal" {
  application_object_id = azuread_application.azure_key_vault_service_principal.object_id
}
resource "azuread_service_principal" "azure_key_vault_service_principal" {
  application_id               = azuread_application.azure_key_vault_service_principal.application_id
  app_role_assignment_required = false
  owners                       = [data.azuread_client_config.current.object_id]
}

resource "azurerm_role_assignment" "azure_keyvault_assignment_sp_contributor" {
  scope                = azurerm_key_vault.kv_account.id
  role_definition_name = "Contributor"
  principal_id         = azuread_service_principal.azure_key_vault_service_principal.object_id
}

resource "azurerm_key_vault_access_policy" "access_policy" {
  key_vault_id = azurerm_key_vault.kv_account.id
  tenant_id    = data.azurerm_client_config.current.tenant_id
  object_id    = azuread_application.azure_key_vault_service_principal.application_id

  key_permissions = [
    "Get",
    "List"
  ]

  secret_permissions = [
    "Get",
    "List"
  ]

  certificate_permissions = [
    "Get",
    "List"
  ]
}

resource "azurerm_key_vault_key" "generated" {
  name         = "generated-certificate"
  key_vault_id = azurerm_key_vault.example.id
  key_type     = "RSA"
  key_size     = 2048

  key_opts = [
    "decrypt",
    "encrypt",
    "sign",
    "unwrapKey",
    "verify",
    "wrapKey",
  ]
}

resource "azurerm_key_vault_secret" "key_vault_secret_cosmosdb_uri" {
  name         = "cosmosdburi"
  value        = azurerm_cosmosdb_account.application.endpoint
  key_vault_id = azurerm_key_vault.kv_account.id
}
resource "azurerm_key_vault_secret" "key_vault_secret_primary_key" {
  name         = "cosmosdbkey"
  value        = azurerm_cosmosdb_account.application.primary_key
  key_vault_id = azurerm_key_vault.kv_account.id
}
resource "azurerm_key_vault_secret" "key_vault_secret_redis_uri" {
  name         = "redisuri"
  value        = azurerm_redis_cache.redis.hostname
  key_vault_id = azurerm_key_vault.kv_account.id
}
resource "azurerm_key_vault_secret" "key_vault_secret_redis_password" {
  name         = "redispassword"
  value        = azurerm_redis_cache.redis.primary_access_key
  key_vault_id = azurerm_key_vault.kv_account.id
}
