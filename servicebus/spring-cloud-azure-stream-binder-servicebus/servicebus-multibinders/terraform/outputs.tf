output "AZURE_SERVICEBUS_NAMESPACE_1" {
  value       = azurerm_servicebus_namespace.servicebus_namespace_01.name
}

output "AZURE_SERVICEBUS_NAMESPACE_2" {
  value       = azurerm_servicebus_namespace.servicebus_namespace_01.name
}

output "AZURE_SERVICEBUS_QUEUE_NAME" {
  value       = azurerm_servicebus_queue.servicebus_namespace_01_queue.name
}

output "AZURE_SERVICEBUS_TOPIC_NAME" {
  value       = azurerm_servicebus_topic.servicebus_namespace_02_topic.name
}

output "AZURE_SERVICEBUS_TOPIC_SUBSCRIPTION_NAME" {
  value       = azurerm_servicebus_subscription.servicebus_namespace_02_sub.name
}
