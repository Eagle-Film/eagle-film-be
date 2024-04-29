package org.gdsc.yonsei.eagleflim.api.infra

import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path

interface AbstractFileHelper {
	fun uploadFile(fileName: String, multipartFile: MultipartFile): Path
	fun downloadFile(url: String)
}
