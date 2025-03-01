variable "application_name" {
  type        = string
  description = "The name of your application."
  default     = "servicebus-jms-dlq-queue"
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

variable "pricing_tier" {
  type        = string
  description = "The pricing tier of Service Bus."
  default     = "Standard"
}
