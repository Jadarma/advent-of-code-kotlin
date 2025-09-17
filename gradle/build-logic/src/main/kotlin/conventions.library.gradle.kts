import CompileOptions.AocKt.GROUP_ID

plugins {
    id("conventions.kotlin")
    id("conventions.kotest")
    id("conventions.detekt")
    id("conventions.dokka")
    id("conventions.publish")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
}

group = GROUP_ID
version = buildVersion.get()
