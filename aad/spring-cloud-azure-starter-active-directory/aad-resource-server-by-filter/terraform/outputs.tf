
output "sp-password" {
  value       = azuread_application_password.example
  sensitive = true
}


output "output-user" {
  value       = azuread_user.example.user_principal_name
}

output "clientid" {
  value = azuread_application.gzh-app.application_id
}