output "AZURE_CACHE_REDIS_HOST" {
  value       = azurerm_redis_cache.redis.hostname
  description = "The host name of the Redis instance."
}

output "AZURE_CACHE_REDIS_USERNAME" {
  value = data.azuread_client_config.current.object_id
  description = "The username of the Redis instance."
}