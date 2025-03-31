
# Redis and Dragonfly Benchmarking

This project benchmarks three popular Redis clients — **Jedis (Blocking Client)**, **Lettuce (Reactive Client)**, and **Redisson (Reactive Client)** — on both **Redis** and **Dragonfly** instances. The benchmark tests both **SET** and **GET** operations, measuring performance in a **parallel execution** environment.

## Project Structure

- **Jedis**: A simple, blocking Redis client.
- **Lettuce**: A non-blocking, reactive Redis client.
- **Redisson**: A distributed and reactive Redis client.
- **Redis** and **Dragonfly**: Redis-like data stores used for benchmarking.

## Prerequisites

### Software Required:

- **Redis**: A fast, open-source in-memory key-value store.
- **Dragonfly**: A Redis-compatible database optimized for performance.
- **Docker**: To easily run Redis and Dragonfly instances in containers.

### Dependencies:

- **Kotlin**: Programming language used for the project.
- **Gradle**: Build automation tool used to compile and run the project.
- **Redis & Dragonfly Docker Containers**: Running instances of Redis and Dragonfly for the benchmarking.

## Setup

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/AncutaIoan/redis-benchs.git
   ```

2. **Docker Setup**:
   Use the provided `docker-compose.yml` to set up Redis and Dragonfly containers:

   ```bash
   docker-compose up -d
   ```

   This command will bring up two containers: one for **Redis** on port `6379` and one for **Dragonfly** on port `7380`.

3. **Install Dependencies**:
   If you're using **Gradle**, simply run:

   ```bash
   ./gradlew build
   ```

   This will download the necessary dependencies for the project.

## How to Run the Benchmark

### 1. Run the Benchmark Test:

To run the benchmark, simply execute the `main` function in `RedisBenchmark.kt`. This will benchmark the **SET** and **GET** operations for all three clients (`Jedis`, `Lettuce`, and `Redisson`) on both **Redis** and **Dragonfly**.

```bash
./gradlew run
```

### 2. Benchmark Output:

After running the program, you'll see the time taken for **SET** and **GET** operations for each client on both **Redis** and **Dragonfly**.

Example output:
```
Starting Parallel Benchmarks...
Jedis (Redis) Set Time: 623 ms, Get Time: 201 ms
Jedis (Dragonfly) Set Time: 709 ms, Get Time: 211 ms
Lettuce (Redis) Set Time: 886 ms, Get Time: 298 ms
Lettuce (Dragonfly) Set Time: 860 ms, Get Time: 285 ms
Redisson (Redis) Set Time: 1.4s, Get Time: 511 ms
Redisson (Dragonfly) Set Time: 1.6s, Get Time: 520 ms
```

### 3. Adjusting Test Parameters:

The following parameters can be configured for your benchmark:

- **Iterations**: The number of iterations for the benchmark. Adjust this based on your performance testing needs.
- **Concurrency**: The number of concurrent requests to simulate.

These can be modified in the `main()` function of `RedisBenchmark.kt`.

## Code Breakdown

### **Benchmarking Function** (`benchmarkParallel`):
This function runs the **SET** and **GET** operations in parallel using **Kotlin Coroutines**. It divides the total number of iterations among the specified number of concurrent workers.

### **Redis Client Setup**:
- **Jedis**: A blocking Redis client that is used for simple set/get operations.
- **Lettuce**: A reactive Redis client that performs asynchronous operations.
- **Redisson**: Another reactive Redis client that supports distributed collections and other advanced features.

### **Parallel Execution**:
All operations are executed in parallel to simulate real-world conditions. The **SET** and **GET** operations are executed for each client type and database instance.

### **Resource Cleanup**:
After the benchmark completes, the **Lettuce** client is explicitly shut down to avoid memory leaks. The **Redisson** and **Jedis** clients are closed gracefully.

## Troubleshooting

- **If Redis or Dragonfly containers are not starting**: 
  - Ensure that **Docker** is properly installed and running.
  - Use `docker-compose ps` to check the status of the containers.

- **If you get a timeout or connection error**:
  - Verify that Redis and Dragonfly are correctly running on their respective ports (`6379` for Redis and `7380` for Dragonfly).
  - Ensure no firewall is blocking the connection to these ports.

## Conclusion

This benchmarking project is designed to help you compare the performance of various Redis clients (Jedis, Lettuce, and Redisson) when interacting with Redis and Dragonfly.
 You can easily adjust the number of iterations and concurrency to test different scenarios. 
 This can help you make an informed decision about which Redis client and database are best suited for your project.
