output "azure_cosmos_endpoint" {
  value       = azurerm_cosmosdb_account.application.endpoint
  description = "Azure Cosmos DB endpoint."
}

output "azure_cosmos_primary_key" {
  value = azurerm_cosmosdb_account.application.primary_key
  description = "Azure Cosmos DB primary_key."
  sensitive = true
}

output "azure_cosmos_secondary_key" {
  value = azurerm_cosmosdb_account.application.secondary_key
  description = "Azure Cosmos DB secondary_key."
  sensitive = true
}

output "cosmos_application_id" {
  value       = azurerm_cosmosdb_account.application.id
  description = "Cosmos account application id."
}

output "object_id" {
  value       = data.azurerm_client_config.current.object_id
  description = "Current user's object_id."
}

output "cosmos_database_name" {
  value = var.cosmos_database_name
}
