# Aerospike and Kafka
How, Why and When to use Aerospike with Kafka

# Companion Code
The companion code is deliberately simple to focus on Kafka integration with Aerospike, without the distraction of idiomatic language patterns or frameworks. Examples are provided in Java, C# and JavaScript ES6.

Clone the repository using:
```bash
git clone https://github.com/helipilot50/aerospike-counters.git
```

# Local Aerospike database using docker
```bash
sudo docker-compose -f aerospike-single-docker-compose.yml up
```
# Run C# example
```bash
dotnet build
dotnet run
```

# Run Java example
```bash
mvn build
mvn exec:java
```
# Run JavaScript/Node example
```bash
npm install
npm start
```
