package com.rtarita.days

import com.rtarita.structure.AoCDay
import com.rtarita.util.day
import com.rtarita.util.ds.graph.Graph
import com.rtarita.util.ds.graph.MutableGraph
import com.rtarita.util.ds.graph.buildGraph
import kotlinx.datetime.LocalDate
import java.util.LinkedList
import java.util.Queue

object Day12 : AoCDay {
    private data class Coord(val x: Int, val y: Int)

    override val day: LocalDate = day(12)
    private fun List<List<Char>>.atLocation(coord: Coord): Char = this[coord.y][coord.x]

    private fun MutableGraph<Coord>.processNode(
        origin: Coord,
        node: Coord,
        gridval: Int,
        grid: List<List<Char>>,
        visited: MutableSet<Coord>
    ) {
        if (node == origin) return
        if (node !in adjacentVertices(origin) && grid.atLocation(node).code - 1 <= gridval) {
            addEdge(origin, node)
            if (node !in visited) {
                processNeighbours(node, grid, visited)
            }
        }
    }

    private fun MutableGraph<Coord>.processNeighbours(
        location: Coord,
        grid: List<List<Char>>,
        visited: MutableSet<Coord>
    ) {
        visited += location
        val gridval = grid.atLocation(location).code
        val left = Coord((location.x - 1).coerceAtLeast(0), location.y)
        val up = Coord(location.x, (location.y - 1).coerceAtLeast(0))
        val right = Coord((location.x + 1).coerceAtMost(grid[location.y].lastIndex), location.y)
        val down = Coord(location.x, (location.y + 1).coerceAtMost(grid.lastIndex))
        processNode(location, left, gridval, grid, visited)
        processNode(location, up, gridval, grid, visited)
        processNode(location, right, gridval, grid, visited)
        processNode(location, down, gridval, grid, visited)
    }

    private fun parseGraph(input: String): Triple<Graph<Coord>, Coord, Coord> {
        val grid = input.lineSequence()
            .map {
                it.toMutableList()
            }.toMutableList()

        val start = grid.mapIndexed { y, line ->
            Coord(line.indexOf('S'), y)
        }.single { (x, _) -> x != -1 }

        val dest = grid.mapIndexed { y, line ->
            Coord(line.indexOf('E'), y)
        }.single { (x, _) -> x != -1 }

        grid[start.y][start.x] = 'a'
        grid[dest.y][dest.x] = 'z'

        return Triple(buildGraph {
            processNeighbours(start, grid, mutableSetOf())
        }, start, dest)
    }

    private fun bfs(graph: Graph<Coord>, initial: Coord, target: Coord): Int {
        val queue: Queue<Coord> = LinkedList()
        val visited = mutableSetOf<Coord>()
        val prev = mutableMapOf<Coord, Coord?>()
        queue.add(initial)
        visited.add(initial)
        prev[initial] = null

        while (queue.isNotEmpty()) {
            val node = queue.poll()
            if (node == target) {
                val path = mutableListOf<Coord>()
                var current: Coord? = target
                while (current != null) {
                    path += current
                    current = prev[current]
                }
                return path.size - 1
            }
            for (v in graph.adjacentVertices(node)) {
                if (v !in visited) {
                    visited.add(v)
                    queue.add(v)
                    prev[v] = node
                }
            }
        }

        return Int.MAX_VALUE
    }

    override fun executePart1(input: String): Int {
        val (graph, start, dest) = parseGraph(input)
        return bfs(graph, start, dest)
    }

    override fun executePart2(input: String): Int {
        val possibleStartingPoints = input.lineSequence()
            .flatMapIndexed { y, line ->
                line.mapIndexedNotNull { x, elem ->
                    if (elem != 'a' && elem != 'S') null else Coord(x, y)
                }
            }
        val (graph, _, dest) = parseGraph(input)

        return possibleStartingPoints.minOf { bfs(graph, it, dest) }
    }
}