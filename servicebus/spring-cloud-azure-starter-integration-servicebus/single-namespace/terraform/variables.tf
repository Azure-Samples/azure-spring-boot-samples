variable "application_name" {
  type        = string
  description = "The name of your application"
  default     = "servicebusapp"
}

variable "service_principal_id" {
  type        = string
  description = "The Azure Service Principal object_id to assign role to, not the application id here"
  default     = "your Azure Service Principal object_id"
}

variable "environment" {
  type        = string
  description = "The environment (dev, test, prod...)"
  default     = "dev"
}

variable "servicebusnamespace" {
  type        = string
  description = "your servicebus namespace name"
  default     = "servicebusnamespace"
}

variable "location" {
  type        = string
  description = "The Azure region where all resources in this example should be created"
  default     = "eastus"
}
