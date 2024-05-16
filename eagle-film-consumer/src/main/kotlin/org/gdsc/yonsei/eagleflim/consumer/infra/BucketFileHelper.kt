package org.gdsc.yonsei.eagleflim.consumer.infra

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.util.IOUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class BucketFileHelper(
	private val amazonS3: AmazonS3,
	@Value("\${cloud.aws.credentials.bucket}") val bucket: String,
	@Value("\${cloud.aws.s3.endpoint}") val baseUrl: String,
) {
	fun uploadFile(fileName: String, byteArray: ByteArray): String {
		val objectMetadata = ObjectMetadata()

		objectMetadata.contentLength = byteArray.size.toLong()

		val objectRequest = PutObjectRequest(bucket, fileName, byteArray.inputStream(), objectMetadata)
			.withCannedAcl(CannedAccessControlList.PublicRead)

		try {
			amazonS3.putObject(objectRequest)
		} catch (e: Exception) {
			throw error("file upload failed")
		}

		return "${baseUrl}/${bucket}/${objectRequest.key}"
	}

	fun downloadFile(fileName: String): String {
		amazonS3.getObject(bucket, fileName).use {
			val objectInputStream = it.objectContent
			val bytes = IOUtils.toByteArray(objectInputStream)

			return convertToBase64(bytes)
		}
	}

	fun convertToBase64(byteArray: ByteArray): String {
		return Base64.getEncoder().encodeToString(byteArray)
	}

	fun convertToFile(encodedString: String): ByteArray {
		return Base64.getDecoder().decode(encodedString)
	}
}

