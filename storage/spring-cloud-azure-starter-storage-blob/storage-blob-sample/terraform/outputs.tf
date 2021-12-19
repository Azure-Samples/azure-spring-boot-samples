output "azure_storage_account" {
  value       = azurerm_storage_account.application.name
  description = "Azure Storage account created."
}

output "storage_container_name" {
  value       = var.container_name
  description = "Azure Storage container name."
}
