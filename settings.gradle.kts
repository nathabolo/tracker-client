pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            from(files("../trackerversioncatalog/gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "tracker-client"
include(":app")
include(":tracker-domain") // // TODO This need to be commented out to disable tracker-domain local build

project(":tracker-domain").projectDir = File("../trackerDomain/app") // // TODO This need to be commented out to disable tracker-domain local build
 