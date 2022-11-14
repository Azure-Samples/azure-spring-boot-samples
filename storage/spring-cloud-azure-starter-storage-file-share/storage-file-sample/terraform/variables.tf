variable "application_name" {
  type        = string
  description = "The name of your application."
  default     = "file-sample"
}

variable "location" {
  type        = string
  description = "The Azure region where all resources in this example should be created."
  default     = "eastus"
}

variable "share_name" {
  type        = string
  description = "The share name of the file share storage"
  default     = "fileshare"
}

variable "sample_tag_value" {
  type        = string
  description = "The value of spring-cloud-azure-sample tag."
  default     = "true"
}
