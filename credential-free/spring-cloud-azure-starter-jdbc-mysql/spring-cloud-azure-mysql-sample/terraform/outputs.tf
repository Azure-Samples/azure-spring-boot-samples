output "azure_mysql_server_name" {
  value       = azurerm_mysql_flexible_server.mysql_server.name
  description = "Azure MySQL server name created."
}

output "azure_mysql_server_database_name" {
  value       = azurerm_mysql_flexible_database.database.name
  description = "Azure MySQL server database name created."
}

output "resource_group_name" {
  value = azurerm_resource_group.main.name
  description = "The resource group name."
}

output "aad_user_name" {
  value = "spring"
  description = "The Azure Active Directory user name."
}

output "user_assigned_identity" {
  value = azurerm_user_assigned_identity.identity.name
  description = "The user assigned identity name."
}
