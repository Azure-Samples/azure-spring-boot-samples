output "CONNECTION_STRING" {
  value       = azurerm_servicebus_namespace.servicebus_namespace.default_primary_connection_string
  description = "The connection_string of servicebus namespace."
  sensitive   = true
}

output "PRICING_TIER" {
  value       = var.pricing_tier
  description = "The pricing tier of Service Bus."
}

output "TOPIC_CLIENT_ID" {
  value       = var.topic_client_id
  description = "The topic client id."
}