output "AZURE_EVENTHUBS_NAMESPACE" {
  value       = azurerm_eventhub_namespace.eventhubs_namespace.name
  description = "The event hubs namespace."
}

output "AZURE_STORAGE_ACCOUNT_NAME" {
  value       = azurerm_storage_account.storage_account.name
  description = "The storage account name."
}

output "AZURE_STORAGE_CONTAINER_NAME" {
  value       = azurerm_storage_container.storage_container.name
  description = "The container name created in storage account."
}

output "RESOURCE_GROUP_NAME" {
  value = azurerm_resource_group.main.name
  description = "The resource group name."
}
