package org.gdsc.yonsei.eaglefilm.manage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
open class ManageApplication

fun main(args: Array<String>) {
	runApplication<ManageApplication>(*args)
}

