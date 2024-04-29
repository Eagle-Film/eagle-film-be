package org.gdsc.yonsei.eagleflim.api.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class S3Config(
	@Value("\${cloud.aws.credentials.access-key}") val accessKey: String,
	@Value("\${cloud.aws.credentials.secret-key}") val secretKey: String,
	@Value("\${cloud.aws.region.static}") val region: String,
	@Value("\${cloud.aws.s3.endpoint}") val endpointUrl: String
) {
	@Bean
	open fun amazonS3(): AmazonS3 {
		val credentials = BasicAWSCredentials(accessKey, secretKey)

		return AmazonS3ClientBuilder.standard()
			.withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(endpointUrl, region))
			.withCredentials(AWSStaticCredentialsProvider(credentials))
			.build()
	}
}
