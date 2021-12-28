export AZURE_SERVICEBUS_NAMESPACE_1
export AZURE_SERVICEBUS_NAMESPACE_2
export AZURE_SERVICEBUS_TOPIC_NAME
export AZURE_SERVICEBUS_TOPIC_SUBSCRIPTION_NAME
export AZURE_SERVICEBUS_TOPIC_NAME








output "AZURE_SERVICEBUS_NAMESPACE" {
  value       = azurerm_servicebus_namespace.servicebus_namespace.name
  sensitive = true
}

output "AZURE_SERVICEBUS_QUEUE_NAME" {
  value       = azurerm_servicebus_queue.queue.name
  sensitive = true
}
