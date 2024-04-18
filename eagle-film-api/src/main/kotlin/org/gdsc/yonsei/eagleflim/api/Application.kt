package org.gdsc.yonsei.eagleflim.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["org.gdsc.yonsei.eagleflim.common"])
open class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
