output "AZURE_SERVICEBUS_NAMESPACE" {
  value     = azurerm_servicebus_namespace.servicebus_namespace.name
  sensitive = true
}

output "AZURE_SERVICEBUS_QUEUE_NAME" {
  value     = azurerm_servicebus_queue.queue.name
  sensitive = true
}
