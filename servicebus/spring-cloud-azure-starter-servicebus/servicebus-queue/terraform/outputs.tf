output "SERVICEBUS_NAMESPACE" {
  value       = azurerm_servicebus_namespace.servicebus_namespace.name
  description = "The servicebus namespace."
}
