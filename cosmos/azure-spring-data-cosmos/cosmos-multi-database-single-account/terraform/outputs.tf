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


output "cosmos_database_name" {
  value = var.cosmos_database_name
  description = "Azure Cosmos database name."
}
