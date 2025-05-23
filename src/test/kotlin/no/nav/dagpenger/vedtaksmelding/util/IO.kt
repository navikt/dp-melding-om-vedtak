package no.nav.dagpenger.vedtaksmelding.util

import java.nio.file.Files
import java.nio.file.Paths

fun writeStringToFile(
    filePath: String,
    content: String,
) {
    val path = Paths.get(filePath)
    Files.createDirectories(path.parent)
    Files.writeString(path, content)
}

private val resourseRetriever = object {}.javaClass

fun String.readFile(): String =
    resourseRetriever.getResource(this)?.readText()
        ?: throw RuntimeException("Fant ikke ressurs $this")
