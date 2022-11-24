package com.rtarita.util

import kotlinx.datetime.LocalDate
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.Path

fun composePath(root: Path, child: Path): Path = root.resolve(child)
fun composePath(root: Path, child: String): Path = root.resolve(child)
fun readFromPath(path: Path): String = Files.readAllBytes(path).decodeToString()
fun readFromPath(path: String): String = readFromPath(Paths.get(path))
fun readFromFile(location: File): String = readFromPath(location.toPath())

fun Path.renameTo(new: String): Path {
    ignoreError<FileAlreadyExistsException> {
        Files.move(this, resolveSibling(new))
    }
    return this
}

fun existsOrNull(file: File): File? = if (file.exists()) file else null
fun existsOrNull(file: Path): Path? = if (Files.exists(file)) file else null

fun pathOfDay(day: LocalDate): Path = Path("input/day${day.dayOfMonth}.txt")
fun pathOfDay(num: Int): Path = pathOfDay(day(num))
fun pathToday(): Path = pathOfDay(today())

fun fileOfDay(day: LocalDate): File = File("input/day${day.dayOfMonth}.txt")
fun fileOfDay(num: Int): File = fileOfDay(day(num))
fun fileToday(): File = fileOfDay(today())

fun outputPathOfDay(day: LocalDate): Path = Path("output/day${day.dayOfMonth}.txt")
fun outputPathOfDay(num: Int): Path = outputPathOfDay(day(num))
fun outputPathToday(): Path = outputPathOfDay(today())

fun outputFileOfDay(day: LocalDate): File = File("output/day${day.dayOfMonth}.txt")
fun outputFileOfDay(num: Int): File = outputFileOfDay(day(num))
fun outputFileToday(): File = outputFileOfDay(today())