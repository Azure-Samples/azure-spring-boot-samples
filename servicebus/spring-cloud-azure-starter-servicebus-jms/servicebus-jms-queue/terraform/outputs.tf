output "SERVICEBUS_NAMESPACE_CONNECTION_STRING" {
  value       = azurerm_servicebus_namespace.servicebus_namespace.default_primary_connection_string
  description = "The connection_string of servicebus namespace."
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
