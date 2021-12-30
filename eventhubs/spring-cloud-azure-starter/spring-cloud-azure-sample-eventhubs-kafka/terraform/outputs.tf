output "AZURE_EVENTHUBS_NAMESPACE" {
  value = azurerm_eventhub_namespace.eventhubs_namespace.name
}

output "AZURE_EVENTHUBS_CONNECTION_STRING" {
  value     = azurerm_eventhub_namespace.eventhubs_namespace.default_primary_connection_string
  sensitive = true
}

output "EVENTHUBS_KAFKA" {
  value     = azurerm_eventhub.eventhubs.name
  sensitive = true
}