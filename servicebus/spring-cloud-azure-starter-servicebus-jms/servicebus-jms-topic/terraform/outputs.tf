output "connection_string" {
  value       = azurerm_servicebus_namespace.servicebus_namespace.default_primary_connection_string
  description = "The servicebus namespace."
  sensitive = true
}


output "topic_client_id" {
  value       = azurerm_servicebus_topic.application
  description = "The servicebus namespace."
  sensitive = true
}
