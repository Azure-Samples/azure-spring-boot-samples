output "resource_group" {
  value       = azurerm_resource_group.main.name
  description = "The resource group."
}


output "servicebus_namespace" {
  value       = azurerm_servicebus_namespace.application.name
  description = "The servicebus namespace."
}
