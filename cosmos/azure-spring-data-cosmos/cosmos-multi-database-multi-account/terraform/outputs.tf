output "azure_cosmos01_endpoint" {
  value       = azurerm_cosmosdb_account.application_01.endpoint
  description = "Azure Cosmos DB endpoint."
}

output "azure_cosmos01_primary_key" {
  value = azurerm_cosmosdb_account.application_01.primary_key
  description = "Azure Cosmos DB primary_key."
  sensitive = true
}

output "azure_cosmos01_secondary_key" {
  value = azurerm_cosmosdb_account.application_01.secondary_key
  description = "Azure Cosmos DB secondary_key."
  sensitive = true
}

output "azure_cosmos02_endpoint" {
  value       = azurerm_cosmosdb_account.application_02.endpoint
  description = "Azure Cosmos DB endpoint."
}

output "azure_cosmos02_primary_key" {
  value = azurerm_cosmosdb_account.application_02.primary_key
  description = "Azure Cosmos DB primary_key."
  sensitive = true
}

output "azure_cosmos02_secondary_key" {
  value = azurerm_cosmosdb_account.application_02.secondary_key
  description = "Azure Cosmos DB secondary_key."
  sensitive = true
}

output "object_id" {
  value       = data.azurerm_client_config.current.object_id
  description = "Current user's object_id."
}

output "cosmos_database_name" {
  value = var.cosmos_database_name
}
