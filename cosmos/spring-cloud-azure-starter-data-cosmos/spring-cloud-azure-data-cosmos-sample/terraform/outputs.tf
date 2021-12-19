output "azure_cosmos_endpoint" {
  value       = azurerm_cosmosdb_account.application.endpoint
  description = "Azure Storage endpoint."
}

output "azure_cosmos_account" {
  value       = azurerm_cosmosdb_account.application.name
  description = "Azure Storage account created."
}

output "resource_group_name" {
  value       = azurerm_resource_group.main.name
  description = "The Azure resource group name."
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
  value       = var.cosmos_database_name
}



