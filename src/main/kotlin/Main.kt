package angrymiaucino

import io.lettuce.core.RedisClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.runBlocking
import org.redisson.Redisson
import org.redisson.api.RMapReactive
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import redis.clients.jedis.Jedis
import kotlin.system.measureTimeMillis

suspend fun benchmarkParallel(task: suspend (Int) -> Unit, iterations: Int, concurrency: Int): Long {
    return measureTimeMillis {
        coroutineScope {
            val jobs = List(concurrency) {
                async {
                    repeat(iterations / concurrency) { task(it) }
                }
            }
            jobs.awaitAll()
        }
    }
}

fun createJedisClient(redisUrl: String): Jedis {
    return Jedis(redisUrl)
}

fun createLettuceClient(redisUrl: String): RedisClient {
    return RedisClient.create(redisUrl)
}

fun createRedissonClient(redisUrl: String): RedissonClient {
    val config = Config()
    config.useSingleServer().address = redisUrl
    return Redisson.create(config)
}

fun main() {
    val redisUrl = "redis://localhost:6379"
    val dragonflyUrl = "redis://localhost:7380"
    val iterations = 100_000
    val concurrency = 10

    runBlocking {
        println("Starting Parallel Benchmarks...")

        // Jedis Benchmark (Redis)
        val jedis = createJedisClient(redisUrl)
        val jedisTime = benchmarkParallel({ jedis.set("key$it", "value$it") }, iterations, concurrency)
        val jedisGetTime = benchmarkParallel({ jedis.get("key$it") }, iterations, concurrency)
        println("Jedis (Redis) Set Time: $jedisTime ms, Get Time: $jedisGetTime ms")
        jedis.close()

        // Jedis Benchmark (Dragonfly)
        val jedisDragonfly = createJedisClient(dragonflyUrl)
        val jedisDragonflyTime = benchmarkParallel({ jedisDragonfly.set("key$it", "value$it") }, iterations, concurrency)
        val jedisDragonflyGetTime = benchmarkParallel({ jedisDragonfly.get("key$it") }, iterations, concurrency)
        println("Jedis (Dragonfly) Set Time: $jedisDragonflyTime ms, Get Time: $jedisDragonflyGetTime ms")
        jedisDragonfly.close()

        // Lettuce Reactive Benchmark (Redis)
        val lettuceClient = createLettuceClient(redisUrl)
        val lettuceConnection = lettuceClient.connect()
        val lettuceReactiveCommands = lettuceConnection.reactive()
        val lettuceTime = benchmarkParallel({ lettuceReactiveCommands.set("key$it", "value$it").awaitFirst() }, iterations, concurrency)
        val lettuceGetTime = benchmarkParallel({ lettuceReactiveCommands.get("key$it").awaitFirstOrNull() }, iterations, concurrency)
        println("Lettuce (Redis) Set Time: $lettuceTime ms, Get Time: $lettuceGetTime ms")
        lettuceConnection.close()
        lettuceClient.shutdown()  // Proper shutdown for Lettuce client resources

        // Lettuce Reactive Benchmark (Dragonfly)
        val lettuceDragonflyClient = createLettuceClient(dragonflyUrl)
        val lettuceDragonflyConnection = lettuceDragonflyClient.connect()
        val lettuceDragonflyCommands = lettuceDragonflyConnection.reactive()
        val lettuceDragonflyTime = benchmarkParallel({ lettuceDragonflyCommands.set("key$it", "value$it").awaitFirst() }, iterations, concurrency)
        val lettuceDragonflyGetTime = benchmarkParallel({ lettuceDragonflyCommands.get("key$it").awaitFirstOrNull() }, iterations, concurrency)
        println("Lettuce (Dragonfly) Set Time: $lettuceDragonflyTime ms, Get Time: $lettuceDragonflyGetTime ms")
        lettuceDragonflyConnection.close()
        lettuceDragonflyClient.shutdown()  // Proper shutdown for Lettuce client resources

        // Redisson Benchmark (Redis)
        val redisson = createRedissonClient(redisUrl)
        val redissonMap: RMapReactive<String, String> = redisson.reactive().getMap("test")
        val redissonSetTime = benchmarkParallel({ redissonMap.put("key$it", "value$it").awaitFirstOrNull() }, iterations, concurrency)
        val redissonGetTime = benchmarkParallel({ redissonMap.get("key$it").awaitFirstOrNull() }, iterations, concurrency)
        println("Redisson (Redis) Set Time: $redissonSetTime ms, Get Time: $redissonGetTime ms")
        redisson.shutdown()

        // Redisson Benchmark (Dragonfly)
        val redissonDragonfly = createRedissonClient(dragonflyUrl)
        val redissonDragonflyMap: RMapReactive<String, String> = redissonDragonfly.reactive().getMap("test")
        val redissonDragonflySetTime = benchmarkParallel({ redissonDragonflyMap.put("key$it", "value$it").awaitFirstOrNull() }, iterations, concurrency)
        val redissonDragonflyGetTime = benchmarkParallel({ redissonDragonflyMap.get("key$it").awaitFirstOrNull() }, iterations, concurrency)
        println("Redisson (Dragonfly) Set Time: $redissonDragonflySetTime ms, Get Time: $redissonDragonflyGetTime ms")
        redissonDragonfly.shutdown()
    }
}