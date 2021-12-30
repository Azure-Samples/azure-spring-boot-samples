# =======================namespace_01=======================
output "AZURE_SERVICEBUS_NAMESPACE_01" {
  value       = azurerm_servicebus_namespace.servicebus_namespace_01.name
}

output "AZURE_SERVICEBUS_TOPIC_NAME" {
  value       = azurerm_servicebus_topic.servicebus_namespace_01_topic.name
}

output "AZURE_SERVICEBUS_TOPIC_SUBSCRIPTION_NAME" {
  value       = azurerm_servicebus_subscription.servicebus_namespace_01_sub.name
}

# =======================namespace_02=======================
output "AZURE_SERVICEBUS_NAMESPACE_02" {
  value       = azurerm_servicebus_namespace.servicebus_namespace_02.name
}

output "AZURE_SERVICEBUS_QUEUE_NAME" {
  value       = azurerm_servicebus_queue.servicebus_namespace_02_queue.name
}


