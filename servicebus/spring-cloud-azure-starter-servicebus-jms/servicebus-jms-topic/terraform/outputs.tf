output "CONNECTION_STRING" {
  value = azurerm_servicebus_namespace.servicebus_namespace.default_primary_connection_string
  description = "The connection_string of servicebus namespace."
  sensitive = true
}
