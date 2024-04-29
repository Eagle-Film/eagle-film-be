package org.gdsc.yonsei.eagleflim.api.infra

import org.gdsc.yonsei.eagleflim.api.exception.ErrorCd
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

/**
 * 임시
 * 추후 NCP S3 계열로 변경 예정
 */
@Component
class DiskFileHelper(@Value("\${file.dir}") val location: String): AbstractFileHelper {
	override fun uploadFile(fileName: String, multipartFile: MultipartFile): String {
		val totalPath = Paths.get(location + File.separator + StringUtils.cleanPath(fileName))
		try {
			Files.copy(multipartFile.inputStream, totalPath, StandardCopyOption.REPLACE_EXISTING)
		} catch (e: IOException) {
			throw ErrorCd.SERVER_ERROR.serviceException("image upload failed")
		}

		return totalPath.toString()
	}
}
