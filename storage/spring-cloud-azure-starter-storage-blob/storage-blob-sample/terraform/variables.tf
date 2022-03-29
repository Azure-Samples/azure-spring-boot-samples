variable "application_name" {
  type        = string
  description = "The name of your application."
  default     = "blob-sample"
}

variable "location" {
  type        = string
  description = "The Azure region where all resources in this example should be created."
  default     = "eastus"
}

variable "container_name" {
  type        = string
  description = "The container name of the blob storage"
  default     = "blobcontainer"
}

variable "sample_tag_value" {
  type        = string
  description = "The value of spring-cloud-azure-sample tag."
  default     = "true"
}