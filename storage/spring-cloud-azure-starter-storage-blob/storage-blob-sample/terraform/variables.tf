variable "application_name" {
  type        = string
  description = "The name of your application"
  default     = "storagesample"
}

variable "location" {
  type        = string
  description = "The Azure region where all resources in this example should be created"
  default     = "eastus"
}

variable "container_name" {
  type        = string
  description = "The container name of the blob storage"
  default     = "blobcontainer"
}