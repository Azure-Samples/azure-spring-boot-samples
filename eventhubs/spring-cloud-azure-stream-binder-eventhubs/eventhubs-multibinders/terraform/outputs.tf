output "AZURE_STORAGE_ACCOUNT_NAME" {
  value = azurerm_storage_account.storage_account.name
}

output "AZURE_STORAGE_CONTAINER_NAME" {
  value = azurerm_storage_container.storage_container.name
}

# ==============namespace01==============
output "EVENTHUB_NAMESPACE_01" {
  value = azurerm_eventhub_namespace.eventhubs_namespace_01.name
}

output "AZURE_EVENTHUB_NAME_01" {
  value = azurerm_eventhub.eventhubs_01.name
}

# the default consumer group
output "AZURE_EVENTHUB_CONSUMER_GROUP_01" {
  value = "$Default"
}

# ==============namespace02==============
output "EVENTHUB_NAMESPACE_02" {
  value = azurerm_eventhub_namespace.eventhubs_namespace_02.name
}

output "AZURE_EVENTHUB_NAME_02" {
  value = azurerm_eventhub.eventhubs_02.name
}

# the default consumer group
output "AZURE_EVENTHUB_CONSUMER_GROUP_02" {
  value = "$Default"
}