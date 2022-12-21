// https://adventofcode.com/2022/day/7

import java.io.File as JavaFile
import kotlin.math.min

fun input() = JavaFile("input/aoc-7.txt").readLines()

class File(val name: String, val path: String, val size: Int)

class Dir(val name: String, val path: String, val parent: Dir?, val files: MutableList<File> = mutableListOf(), val dirs: MutableList<Dir> = mutableListOf()) {
    val size: Int get() = files.sumOf { it.size } + dirs.sumOf { it.size }
}

// Part 1.

fun parse(input: List<String>): Dir {
    val root = Dir("/", "/", null, mutableListOf(), mutableListOf())
    var wd = root
    input.forEach { line ->
        when {
            line.substring(0, 4) == "$ cd" -> {
                val name = line.split(" ").last()
                wd = when {
                    name == "/" -> root
                    name == ".." -> wd.parent!!
                    else -> wd.dirs.first { it.name == name }
                }
            }
            line.substring(0, 4) == "$ ls" -> {
                // nothing to do
            }
            line.substring(0, 3) == "dir" -> {
                val name = line.split(" ").last()
                wd.dirs += Dir(name, "${wd.path}/$name", wd)
            }
            else -> {
                val (size, name) = line.split(" ")
                wd.files += File(name, "${wd.path}/$name", size.toInt())
            }
        }
    }
    return root
}

fun p1(input: List<String>): Int {
    val root = parse(input)
    fun Dir.sumIfSmall(): Int = (size.takeIf { it <= 100_000 } ?: 0) + dirs.sumOf { it.sumIfSmall() }
    return root.sumIfSmall()
}

// Part 2.

fun p2(input: List<String>): Int {
    val root = parse(input)
    val need = 30_000_000 - (70_000_000 - root.size)
    fun Dir.findCandidates(): List<Dir> = (if (size >= need) listOf(this) else emptyList()) + dirs.flatMap { it.findCandidates() }
    return root.findCandidates().map { it.size }.min()
}

with(input()) {
    println(p1(this)) // 1908462
    println(p2(this)) // 3979145
}
