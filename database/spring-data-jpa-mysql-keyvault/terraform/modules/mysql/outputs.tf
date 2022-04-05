output "server_name" {
  value       = azurerm_mysql_server.database.name
  sensitive   = false   
  description = "The MySQL server name"
}

output "database_url" {
  value       = "${azurerm_mysql_server.database.name}.mysql.database.azure.com:3306/${azurerm_mysql_database.database.name}"
  description = "The MySQL server URL."
}

output "database_username" {
  value       = var.administrator_login
  description = "The MySQL server user name."
}

output "database_password" {
  value       = random_password.password.result
  sensitive   = false   #To change it later
  description = "The MySQL server password."
}
