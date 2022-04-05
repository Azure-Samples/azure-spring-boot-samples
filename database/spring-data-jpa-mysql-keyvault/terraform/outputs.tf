
output "resource_group" {
  value       = azurerm_resource_group.main.name
  description = "The resource group."
}

output "database_username" {
  value       = module.database.database_username
  description = "The MySQL server user name."
}

output "database_password" {
  value       = module.database.database_password
  sensitive   = true
  description = "The MySQL server password."
}

output "database_url" {
  value       = module.database.database_url
  description = "The MySQL server database url."
}

output "server_name" {
  value       = module.database.server_name
  description = "The MySQL server name."
}


output "keyvault_url" {
  value       = module.key-vault.vault_uri
  description = "The keyvault URL."
}