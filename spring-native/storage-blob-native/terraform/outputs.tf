output "azure_storage_account" {
  value       = azurerm_storage_account.application.name
  description = "Azure Storage account created."
}

output "azure_storage_account_key" {
  value       = azurerm_storage_account.application.primary_access_key
  sensitive = true
  description = "Azure Storage account access key."
}

output "storage_container_name" {
  value       = var.container_name
  description = "Azure Storage container name."
}

output "resource_group_name" {
  value = azurerm_resource_group.main.name
  description = "The resource group name."
}
