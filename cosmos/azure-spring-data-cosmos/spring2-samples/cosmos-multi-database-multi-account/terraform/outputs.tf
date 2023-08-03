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

output "azurerm_resource_group_name" {
  value = azurerm_resource_group.main.name
  description = "Azure resource group name."
}

output "cosmos_database_name" {
  value = var.cosmos_database_name
  description = "Azure Cosmos database name."
}

output "mysql_username" {
  value = azurerm_mysql_server.mysql.administrator_login
  description = "Azure MySQL username."
}

output "mysql_password" {
  value = azurerm_mysql_server.mysql.administrator_login_password
  description = "Azure MySQL password."
  sensitive = true
}

output "mysql_url" {
  value = azurerm_mysql_server.mysql.name
  description = "Azure MySQL URL."
}
