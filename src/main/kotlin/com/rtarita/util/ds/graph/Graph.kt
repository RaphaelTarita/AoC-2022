package com.rtarita.util.ds.graph

interface Graph<V> {
    val vertices: Set<V>
    fun isAdjacent(from: V, to: V): Boolean
    fun adjacentVertices(v: V): Set<V>
    fun isBidiAdjacent(v1: V, v2: V): Boolean {
        return isAdjacent(v1, v2) && isAdjacent(v2, v1)
    }

    operator fun get(v: V): Set<V> = adjacentVertices(v)
}