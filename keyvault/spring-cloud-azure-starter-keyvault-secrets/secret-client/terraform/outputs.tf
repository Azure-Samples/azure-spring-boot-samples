output "KEYVAULT_URI" {
  value       = azurerm_key_vault.kv_account.vault_uri
  description = "The key vault uri."
}

output "RESOURCE_GROUP_NAME" {
  value = azurerm_resource_group.main.name
  description = "The resource group name."
}
