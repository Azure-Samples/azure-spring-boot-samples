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

output "AZURE_SERVICE_PRINCIPAL_CLIENT_ID" {
  value = azuread_application.servicebusqueuebinder.application_id
  description = "The client ID of the service principal."
}

output "AZURE_SERVICE_PRINCIPAL_CLIENT_SECRET" {
  value = azuread_application_password.servicebusqueuebinder.value
  sensitive = true
  description = "The client secret of the service principal."
}

output "AZURE_SERVICE_PRINCIPAL_TENANT_ID" {
  value = data.azuread_client_config.current.tenant_id
  description = "The tenant ID of the service principal."
}
