output "SERVICEBUS_NAMESPACE_CONNECTION_STRING" {
  value = azurerm_servicebus_namespace.servicebus_namespace.default_primary_connection_string
  description = "The servicebus namespace."
  sensitive = true
}
