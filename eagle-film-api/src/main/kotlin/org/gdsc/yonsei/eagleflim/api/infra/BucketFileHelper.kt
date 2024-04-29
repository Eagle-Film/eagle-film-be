package org.gdsc.yonsei.eagleflim.api.infra

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.gdsc.yonsei.eagleflim.api.exception.ErrorCd
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class BucketFileHelper(
	private val amazonS3: AmazonS3,
	@Value("\${cloud.aws.credentials.bucket}") val bucket: String,
	@Value("\${cloud.aws.s3.endpoint}") val baseUrl: String,
) : AbstractFileHelper {
	override fun uploadFile(fileName: String, multipartFile: MultipartFile): String {
		val objectMetadata = ObjectMetadata()

		objectMetadata.contentLength = multipartFile.size
		objectMetadata.contentType = multipartFile.contentType

		val objectRequest = PutObjectRequest(bucket, fileName, multipartFile.inputStream, objectMetadata)
			.withCannedAcl(CannedAccessControlList.PublicRead)

		try {
			amazonS3.putObject(objectRequest)
		} catch (e: Exception) {
			throw ErrorCd.SERVER_ERROR.serviceException("file upload failed")
		}

		return "${baseUrl}/${bucket}/${objectRequest.key}"
	}
}
