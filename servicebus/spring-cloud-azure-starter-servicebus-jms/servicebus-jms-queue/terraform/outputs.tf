output "connection_string" {
  value       = azurerm_servicebus_namespace.servicebus_namespace.default_primary_connection_string
  description = "The servicebus namespace."
  sensitive = true
}
