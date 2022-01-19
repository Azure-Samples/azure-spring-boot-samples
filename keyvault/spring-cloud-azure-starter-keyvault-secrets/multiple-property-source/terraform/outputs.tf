output "KEYVAULT_URI_01" {
  value       = azurerm_key_vault.kv_account_01.vault_uri
  description = "The key vault uri 01."
}

output "KEYVAULT_URI_02" {
  value       = azurerm_key_vault.kv_account_02.vault_uri
  description = "The key vault uri 02."
}

output "RESOURCE_GROUP_NAME" {
  value = azurerm_resource_group.main.name
  description = "The resource group name."
}
