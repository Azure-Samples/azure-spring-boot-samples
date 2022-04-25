output "STORAGE_QUEUE_ACCOUNT_NAME" {
  value       = azurerm_storage_account.storage_account.name
  description = "The name of storage account."
}

output "RESOURCE_GROUP_NAME" {
  value = azurerm_resource_group.main.name
  description = "The resource group name."
}
