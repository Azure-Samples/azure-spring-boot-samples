variable "application_name" {
  type        = string
  description = "The name of your application"
  default     = "servicebusapp"
}

variable "service_principal_id" {
  type        = string
  description = "The Azure Service Principal object_id to assign role to, not the application id here"
  default     = "8906e162-4967-4e8f-84f1-1e7bccdf93c2"
}

variable "environment" {
  type        = string
  description = "The environment (dev, test, prod...)"
  default     = "dev"
}

variable "location" {
  type        = string
  description = "The Azure region where all resources in this example should be created"
  default     = "eastus"
}
