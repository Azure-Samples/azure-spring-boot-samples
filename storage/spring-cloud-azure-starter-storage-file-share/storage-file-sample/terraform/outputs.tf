output "azure_storage_account" {
  value       = azurerm_storage_account.application.name
  description = "Azure Storage account created."
}

output "storage_share_name" {
  value       = var.share_name
  description = "Azure Storage share name."
}

output "resource_group_name" {
  value       = azurerm_resource_group.main.name
  description = "The resource group name."
}

output "storage_account_key" {
  value       = azurerm_storage_account.application.primary_access_key
  description = "The account key of this storage account."
  sensitive   = true
}
