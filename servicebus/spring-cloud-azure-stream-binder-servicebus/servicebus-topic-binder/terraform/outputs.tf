output "AZURE_SERVICEBUS_NAMESPACE" {
  value       = azurerm_servicebus_namespace.servicebus_namespace.name
  description = "The name of service bus namespace."
}

output "AZURE_SERVICEBUS_TOPIC_NAME" {
  value       = azurerm_servicebus_topic.servicebus_topic.name
  description = "The name of created topic in the service bus namespace."
}

output "AZURE_SERVICEBUS_TOPIC_SUBSCRIPTION_NAME" {
  value       = azurerm_servicebus_subscription.servicebus_subscription.name
  description = "The name of created subscription in the service bus namespace."
}
