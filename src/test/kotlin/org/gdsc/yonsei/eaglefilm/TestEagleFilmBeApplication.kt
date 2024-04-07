package org.gdsc.yonsei.eaglefilm

import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.boot.with
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration(proxyBeanMethods = false)
class TestEagleFilmBeApplication {

	@Bean
	@ServiceConnection
	fun mongoDbContainer(): MongoDBContainer {
		return MongoDBContainer(DockerImageName.parse("mongo:latest"))
	}

	@Bean
	@ServiceConnection
	fun rabbitContainer(): RabbitMQContainer {
		return RabbitMQContainer(DockerImageName.parse("rabbitmq:latest"))
	}

	@Bean
	@ServiceConnection(name = "redis")
	fun redisContainer(): GenericContainer<*> {
		return GenericContainer(DockerImageName.parse("redis:latest")).withExposedPorts(6379)
	}

}

fun main(args: Array<String>) {
	fromApplication<EagleFilmBeApplication>().with(TestEagleFilmBeApplication::class).run(*args)
}
