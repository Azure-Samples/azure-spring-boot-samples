output "AZURE_STORAGE_ACCOUNT_NAME" {
  value       = azurerm_storage_account.storage_account.name
  description = "The storage account name."
}

output "AZURE_STORAGE_CONTAINER_NAME" {
  value       = azurerm_storage_container.storage_container.name
  description = "The container name created in storage account."
}

# ==============namespace01==============
output "EVENTHUB_NAMESPACE_01" {
  value       = azurerm_eventhub_namespace.eventhubs_namespace_01.name
  description = "The eventhubs namespace 01."
}

output "AZURE_EVENTHUB_NAME_01" {
  value       = azurerm_eventhub.eventhubs_01.name
  description = "The name of created event hubs in the eventhubs namespace 01."
}

# the default consumer group
output "AZURE_EVENTHUB_CONSUMER_GROUP_01" {
  value       = "$Default"
  description = "The value of default consumer group 01."
}

# ==============namespace02==============
output "EVENTHUB_NAMESPACE_02" {
  value       = azurerm_eventhub_namespace.eventhubs_namespace_02.name
  description = "The eventhubs namespace 02."
}

output "AZURE_EVENTHUB_NAME_02" {
  value       = azurerm_eventhub.eventhubs_02.name
  description = "The name of created event hubs in the eventhubs namespace 02."
}

# the default consumer group
output "AZURE_EVENTHUB_CONSUMER_GROUP_02" {
  value       = "$Default"
  description = "The value of default consumer group 02."
}

output "RESOURCE_GROUP_NAME" {
  value = azurerm_resource_group.main.name
  description = "The resource group name."
}
