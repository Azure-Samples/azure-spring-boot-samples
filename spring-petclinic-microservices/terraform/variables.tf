variable "application_name" {
  type        = string
  description = "The name of your application."
  default     = "data-cosmos-sample"
}

variable "location" {
  type        = string
  description = "The Azure region where all resources in this example should be created."
  default     = "eastus"
}

variable "cosmos_database_name" {
  type        = string
  description = "The cosmos database name"
  default     = "cosmos-database"
}

variable "keyvault_url" {
  type = string
  description = "The key vault"
  default     = "keyvault"
}

variable "sample_tag_value" {
  type        = string
  description = "The value of spring-cloud-azure-sample tag."
  default     = "true"
}
