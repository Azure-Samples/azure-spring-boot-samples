variable "application_name" {
  type        = string
  description = "The name of your application."
  default     = "redis-sample"
}

variable "location" {
  type        = string
  description = "The Azure region where all resources in this example should be created."
  default     = "eastus"
}

variable "sample_tag_value" {
  type        = string
  description = "The value of spring-cloud-azure-sample tag."
  default     = "true"
}

variable "azure_subscription_id" {
  type        = string
  description = "After executing command 'az login', copy the 'id' value form the response."
  default     = "<your-azure-subscription-id>"
}

variable "azure_tenant_id" {
  type        = string
  description = "After executing command 'az login', copy the 'tenantId' value form the response."
  default     = "<your-azure_tenant_id>"
}
