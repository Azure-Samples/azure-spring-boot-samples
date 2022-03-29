output "azure_cosmos_endpoint" {
  value       = azurerm_cosmosdb_account.application.endpoint
  description = "Azure Cosmos DB endpoint."
}

output "azure_cosmos_account" {
  value       = azurerm_cosmosdb_account.application.name
  description = "Azure Cosmos DB account created."
}

output "azure_cosmos_key" {
  value       = azurerm_cosmosdb_account.application.primary_key
  description = "Azure Cosmos DB account key."
  sensitive   = true
}

output "resource_group_name" {
  value       = azurerm_resource_group.main.name
  description = "The Azure resource group name."
}

output "cosmos_application_id" {
  value       = azurerm_cosmosdb_account.application.id
  description = "Cosmos account application id."
}

output "keyvault_url" {
  value       = azurerm_key_vault.kv_account.vault_uri
  description = "The key vault uri."
}

output "azurerm_key_vault_account" {
  value       = azurerm_key_vault.kv_account.name
  description = "The key vault account created."
}

output "cosmos_db_location" {
  value = azurerm_cosmosdb_account.application.location
  description = "The Azure region name."
}

output "redis_name" {
  value = azurecaf_name.redis.result
  description = "The redis name created."
}
output "redis_hostname" {
  value       = azurerm_redis_cache.redis.hostname
  description = "The host name of the Redis instance."
}
output "redis_password" {
  value       = azurerm_redis_cache.redis.primary_access_key
  description = "The primary access key of the Redis instance."
  sensitive = true
}
output "service_principal_name" {
  value = azuread_application.azure_key_vault_service_principal.display_name
  description = "The service principal name created."
}
output "azure_key_vault_service_principal_client_id" {
  value = azuread_application.azure_key_vault_service_principal.application_id
  sensitive = true
  description = "The client id of the key vault service principal."
}
output "azure_key_vault_service_principal_client_secret" {
  value = azuread_application_password.azure_key_vault_service_principal.value
  sensitive = true
  description = "The client secret of the key vault service principal."
}
output "azure_key_vault_tenant_id" {
  value = azurerm_key_vault.kv_account.tenant_id
  description = "The tenant id of the key vault."
}
