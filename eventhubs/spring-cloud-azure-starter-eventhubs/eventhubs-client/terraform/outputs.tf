output "AZURE_EVENTHUBS_NAMESPACE" {
  value       = azurerm_eventhub_namespace.eventhubs_namespace.name
  description = "The event hubs namespace."
}

output "AZURE_EVENTHUB_NAME" {
  value       = azurerm_eventhub.eventhubs.name
  description = "The name of created event hubs."
}

output "RESOURCE_GROUP_NAME" {
  value = azurerm_resource_group.main.name
  description = "The resource group name."
}
