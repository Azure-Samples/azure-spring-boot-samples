variable "application_name" {
  type        = string
  description = "The name of your MySQL flexible server."
  default     = "mysql-sample"
}

variable "location" {
  type        = string
  description = "The Azure region where all resources in this example should be created."
  default     = "eastus"
}

variable "user_assigned_identity" {
  type        = string
  description = "Create user identities for assignment."
  default     = "mysql-user-mi"
}

variable "administrator_login" {
  type        = string
  description = "The administrator name of the MySQL server."
  default     = "azure"
}

variable "database_name" {
  type        = string
  description = "The database name of the MySQL server."
  default     = "demo"
}

variable "aad_user_name" {
  type        = string
  description = "The Azure Active Directory user name."
  default     = "spring"
}

variable "sample_tag_value" {
  type        = string
  description = "The value of spring-cloud-azure-sample tag."
  default     = "true"
}
