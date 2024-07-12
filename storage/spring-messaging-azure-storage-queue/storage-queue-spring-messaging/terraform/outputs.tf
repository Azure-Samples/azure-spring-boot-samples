output "STORAGE_QUEUE_ACCOUNT_NAME" {
  value       = azurerm_storage_account.storage_account.name
  description = "The name of storage account."
}
