package org.gdsc.yonsei.eagleflim.api.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory

@Configuration
open class MongoConfig {
	@Bean
	open fun mongoTemplate(
		@Value("\${mongodb.connectionString}") connectionString: String,
	): MongoTemplate {
		return MongoTemplate(SimpleMongoClientDatabaseFactory(connectionString))
	}
}
