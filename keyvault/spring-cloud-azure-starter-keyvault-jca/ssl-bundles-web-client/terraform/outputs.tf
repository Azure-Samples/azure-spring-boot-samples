output "KEY_VAULT_SSL_BUNDLES_KEYVAULT_URI_01" {
  value       = azurerm_key_vault.kv_account_01.vault_uri
  description = "The key vault uri 01."
}

output "KEY_VAULT_SSL_BUNDLES_KEYVAULT_URI_02" {
  value       = azurerm_key_vault.kv_account_02.vault_uri
  description = "The key vault uri 02."
}

output "KEY_VAULT_SSL_BUNDLES_TENANT_ID" {
  value = data.azuread_client_config.current.tenant_id
  description = "The tenant id."
}

output "KEY_VAULT_SSL_BUNDLES_CLIENT_ID" {
  value = azuread_application.app.application_id
  description = "The application id of service principal."
}

output "KEY_VAULT_SSL_BUNDLES_CLIENT_SECRET" {
  value     = azuread_application_password.service_principal_password.value
  sensitive = true
  description = "The client secret of service principal."
}

output "KEY_VAULT_SSL_BUNDLES_RESOURCE_GROUP_NAME" {
  value = azurerm_resource_group.main.name
  description = "The resource group name."
}
