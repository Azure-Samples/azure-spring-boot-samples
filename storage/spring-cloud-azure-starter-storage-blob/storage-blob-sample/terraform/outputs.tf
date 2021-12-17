output "AZURE_STORAGE_ACCOUNT" {
  value       = azurerm_storage_account.application.name
  description = "Azure Storage account created"
}

