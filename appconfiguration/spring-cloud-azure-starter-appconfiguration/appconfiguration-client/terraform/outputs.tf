output "APPCONFIGURATION_ENDPOINT" {
  value       = azurerm_app_configuration.appconfig_resource.endpoint
  description = "The app configuration endpoint."
}

output "RESOURCE_GROUP_NAME" {
  value = azurerm_resource_group.main.name
  description = "The resource group name."
}
