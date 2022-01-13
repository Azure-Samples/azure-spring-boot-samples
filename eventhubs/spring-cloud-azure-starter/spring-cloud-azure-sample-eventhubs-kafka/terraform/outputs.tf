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