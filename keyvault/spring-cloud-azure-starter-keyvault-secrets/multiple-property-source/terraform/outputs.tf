output "keyvault_uri_01" {
  value       = azurerm_key_vault.kv_account_01.vault_uri
  description = "The key vault uri 01."
}

output "keyvault_uri_02" {
  value       = azurerm_key_vault.kv_account_02.vault_uri
  description = "The key vault uri 02."
}
