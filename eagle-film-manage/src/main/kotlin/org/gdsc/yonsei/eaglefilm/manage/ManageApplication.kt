package org.gdsc.yonsei.eaglefilm.manage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class ManageApplication

fun main(args: Array<String>) {
	runApplication<ManageApplication>(*args)
}

