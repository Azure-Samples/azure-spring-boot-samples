output "SERVICEBUS_NAMESPACE_NAME_DLQ_QUQUE" {
  value       = azurerm_servicebus_namespace.servicebus_namespace.name
  description = "The name of servicebus namespace."
  sensitive   = true
}

output "PRICING_TIER" {
  value       = var.pricing_tier
  description = "The pricing tier of Service Bus."
}

output "RESOURCE_GROUP_NAME" {
  value = azurerm_resource_group.main.name
  description = "The resource group name."
}
