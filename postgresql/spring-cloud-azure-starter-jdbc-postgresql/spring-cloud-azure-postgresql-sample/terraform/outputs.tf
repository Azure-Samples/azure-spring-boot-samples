output "azure_postgresql_server_name" {
  value       = azurerm_postgresql_flexible_server.postgresql_server.name
  description = "Azure PostgreSQL server name created."
}

output "azure_postgresql_server_database_name" {
  value       = azurerm_postgresql_flexible_server_database.database.name
  description = "Azure PostgreSQL server database name created."
}

output "resource_group_name" {
  value = azurerm_resource_group.main.name
  description = "The resource group name."
}

output "aad_admin_user_name" {
  value = azurerm_postgresql_flexible_server_active_directory_administrator.current_aad_user_admin.principal_name
  description = "The Azure AD admin name."
}
