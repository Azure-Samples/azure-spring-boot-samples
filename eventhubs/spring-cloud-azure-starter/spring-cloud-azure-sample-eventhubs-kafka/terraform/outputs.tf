output "AZURE_EVENTHUBS_NAMESPACE" {
  value       = azurerm_eventhub_namespace.eventhubs_namespace.name
  description = "The event hubs namespace."
}

output "AZURE_EVENTHUBS_CONNECTION_STRING" {
  value       = azurerm_eventhub_namespace.eventhubs_namespace.default_primary_connection_string
  description = "Connection String to connect event hubs."
  sensitive   = true
}

output "EVENTHUBS_KAFKA" {
  value       = azurerm_eventhub.eventhubs.name
  description = "The name of created event hubs."
}

output "AZURE_EVENTHUBS_RESOURCE_GROUP" {
  value = azurerm_resource_group.main.name
  description = "The Event Hubs resource group name."
}

output "AZURE_EVENTHUBS_SUBSCRIPTION_ID" {
  value       = data.azurerm_subscription.current.subscription_id
  description = "The subscription ID of the resource."
}
