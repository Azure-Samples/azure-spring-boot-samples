output "AZURE_SERVICEBUS_NAMESPACE" {
  value     = azurerm_servicebus_namespace.servicebus_namespace.name
  sensitive = true
}

output "AZURE_SERVICEBUS_TOPIC_NAME" {
  value     = azurerm_servicebus_topic.application.name
  sensitive = true
}


output "AZURE_SERVICEBUS_TOPIC_SUBSCRIPTION_NAME" {
  value     = azurerm_servicebus_subscription.application.name
  sensitive = true
}

