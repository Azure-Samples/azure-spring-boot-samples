output "AZURE_CACHE_REDIS_HOST" {
  value       = azurerm_redis_cache.redis.hostname
  description = "The host name of the Redis instance."
}
