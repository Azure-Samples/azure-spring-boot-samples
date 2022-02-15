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

output "location" {
  value = azurerm_cosmosdb_account.application.location
  description = "The Azure region name."
}

output "redis-name" {
  value = azurecaf_name.redis.result
  description = "The redis name created."
}

output "service-principal-name" {
  value = azurecaf_name.service-principal.result
  description = "The service principal name created."
}
