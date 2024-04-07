plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "eaglefilm"
include("eagle-film-api")
include("eagle-film-consumer")
include("eagle-film-common")
