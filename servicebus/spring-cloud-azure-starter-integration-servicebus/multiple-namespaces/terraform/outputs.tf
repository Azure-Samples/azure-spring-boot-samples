output "AZURE_SERVICEBUS_NAMESPACE_01" {
  value       = azurerm_servicebus_namespace.servicebus_namespace_01.name
  description = "The name of servicebus_01 namespace."
}

output "AZURE_SERVICEBUS_NAMESPACE_02" {
  value       = azurerm_servicebus_namespace.servicebus_namespace_02.name
  description = "The name of servicebus_02 namespace."
}

output "AZURE_SERVICEBUS_NAMESPACE_01_QUEUE_NAME" {
  value       = azurerm_servicebus_queue.application_queue_01.name
  description = "The name the queue in servicebus_01 namespace."
}

output "AZURE_SERVICEBUS_NAMESPACE_02_QUEUE_NAME" {
  value       = azurerm_servicebus_queue.application_queue_02.name
  description = "The name the queue in servicebus_02 namespace."
}

output "RESOURCE_GROUP_NAME" {
  value = azurerm_resource_group.main.name
  description = "The resource group name."
}
