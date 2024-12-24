# =======================namespace_01=======================
output "AZURE_SERVICEBUS_NAMESPACE_01" {
  value       = azurerm_servicebus_namespace.servicebus_namespace_01.name
  description = "The service bus namespace 01."
}

output "AZURE_SERVICEBUS_TOPIC_NAME" {
  value       = azurerm_servicebus_topic.servicebus_namespace_01_topic.name
  description = "The topic name in service bus namespace 01."
}

output "AZURE_SERVICEBUS_TOPIC_SUBSCRIPTION_NAME" {
  value       = azurerm_servicebus_subscription.servicebus_namespace_01_sub.name
  description = "The topic subscription name in service bus namespace 01."
}

# =======================namespace_02=======================
output "AZURE_SERVICEBUS_NAMESPACE_02" {
  value       = azurerm_servicebus_namespace.servicebus_namespace_02.name
  description = "The service bus namespace 02."
}

output "AZURE_SERVICEBUS_QUEUE_NAME" {
  value       = azurerm_servicebus_queue.servicebus_namespace_02_queue.name
  description = "The queue name in service bus namespace 02."
}

output "RESOURCE_GROUP_NAME" {
  value = azurerm_resource_group.main.name
  description = "The resource group name."
}
