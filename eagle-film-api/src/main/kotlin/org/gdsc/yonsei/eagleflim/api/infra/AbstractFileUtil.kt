package org.gdsc.yonsei.eagleflim.api.infra

import org.springframework.web.multipart.MultipartFile

interface AbstractFileHelper {
	fun uploadFile(fileName: String, multipartFile: MultipartFile): String
}
