output "azurerm_eventhub_namespace" {
  value = azurerm_eventhub_namespace.eventhubs_namespace.default_primary_connection_string
  sensitive = true
}

#output "azurerm_eventhub_namespace_all" {
#  value = azurerm_eventhub_namespace.eventhubs_namespace
#}