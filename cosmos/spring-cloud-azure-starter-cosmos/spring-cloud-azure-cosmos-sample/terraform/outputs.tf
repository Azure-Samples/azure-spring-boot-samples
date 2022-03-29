output "azure_cosmos_endpoint" {
  value       = azurerm_cosmosdb_account.application.endpoint
  description = "Azure Cosmos DB endpoint."
}

output "azure_cosmos_account" {
  value       = azurerm_cosmosdb_account.application.name
  description = "Azure Cosmos DB account created."
}

output "cosmos_application_id" {
  value       = azurerm_cosmosdb_account.application.id
  description = "Azure Cosmos DB account application id."
}

output "object_id" {
  value       = data.azurerm_client_config.current.object_id
  description = "Current user's object_id."
}

output "resource_group_name" {
  value       = azurerm_resource_group.main.name
  description = "The Azure resource group name."
}
