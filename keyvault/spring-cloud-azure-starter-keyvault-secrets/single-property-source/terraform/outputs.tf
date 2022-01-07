output "keyvault_uri" {
  value       = azurerm_key_vault.kv_account.vault_uri
  description = "The key vault uri."
}
