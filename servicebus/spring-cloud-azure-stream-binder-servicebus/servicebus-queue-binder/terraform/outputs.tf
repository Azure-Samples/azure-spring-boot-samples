output "AZURE_SERVICEBUS_NAMESPACE" {
  value       = azurerm_servicebus_namespace.servicebus_namespace.name
  description = "The name of service bus namespace."
}

output "AZURE_SERVICEBUS_QUEUE_NAME" {
  value       = azurerm_servicebus_queue.queue.name
  description = "The name of created queue in the service bus namespace."
}

output "AZURE_SERVICEBUS_RESOURCE_GROUP" {
  value = azurerm_resource_group.main.name
  description = "The Service Bus resource group name."
}

output "AZURE_SERVICEBUS_SUBSCRIPTION_ID" {
  value       = data.azurerm_subscription.current.subscription_id
  description = "The subscription ID of the resource."
}
