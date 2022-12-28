variable "application_name" {
  type        = string
  description = "The name of your PostgreSQL flexible server."
  default     = "postgresql-sample"
}

variable "location" {
  type        = string
  description = "The Azure region where all resources in this example should be created."
  default     = "eastus"
}

variable "administrator_login" {
  type        = string
  description = "The administrator name of the PostgreSQL server."
  default     = "azure"
}

variable "database_name" {
  type        = string
  description = "The database name of the PostgreSQL server."
  default     = "demo"
}

variable "aad_administrator_name" {
  type        = string
  description = "The Azure Active Directory administrator name."
  default     = "spring"
}

variable "sample_tag_value" {
  type        = string
  description = "The value of spring-cloud-azure-sample tag."
  default     = "true"
}
