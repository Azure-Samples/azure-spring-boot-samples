output "resource_group" {
  value       = azurerm_resource_group.main.name
  description = "The resource group."
}


output "SERVICEBUS_NAMESPACE" {
  value       = azurerm_servicebus_namespace.servicebus_namespace.name
  description = "The servicebus namespace."
}

output "SERVICEBUS_NAMESPACE_ID" {
  value       = azurerm_servicebus_namespace.servicebus_namespace.id
  description = "The servicebus namespace id."
}

#output "AZURE_CLIENT_ID" {
#  value       = azuread_service_principal_password.example.service_principal_id
#  description = "The AZURE_CLIENT_ID"
#}
#
#
#output "AZURE_CLIENT_SECRET" {
#  value       = azuread_service_principal_password.example.value
#  sensitive   = true # Note that you might not want to print this in out in the console all the time
#  description = "The AZURE_CLIENT_SECRET ."
#}

#output "AZURE_CLIENT_SECRET" {
#  value = random_string.password.result
#  sensitive = false # Note that you might not want to print this in out in the console all the time
#  description = "The AZURE_CLIENT_SECRET ."
#}