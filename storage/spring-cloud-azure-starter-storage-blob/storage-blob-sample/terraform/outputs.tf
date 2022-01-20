output "azure_storage_account" {
  value       = azurerm_storage_account.application.name
  description = "Azure Storage account created."
}

output "storage_container_name" {
  value       = var.container_name
  description = "Azure Storage container name."
}

output "resource_group_name" {
  value = azurerm_resource_group.main.name
  description = "The resource group name."
}
