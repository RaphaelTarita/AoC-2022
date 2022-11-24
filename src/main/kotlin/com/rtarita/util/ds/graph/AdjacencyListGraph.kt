package com.rtarita.util.ds.graph

class AdjacencyListGraph<V> private constructor(
    private val adj: MutableMap<V, MutableSet<V>>,
    @Suppress("UNUSED_PARAMETER") ctorFlag: Unit
) : MutableGraph<V> {
    constructor() : this(mutableMapOf(), Unit)
    constructor(adj: Map<V, Set<V>>) : this(adj.mapValues { (_, v) -> v.toMutableSet() }.toMutableMap(), Unit)
    constructor(copyFrom: Graph<V>) : this(copyFrom.vertices.associateWith { copyFrom[it] })

    override val vertices
        get() = adj.keys.toSet()

    override fun isAdjacent(from: V, to: V): Boolean {
        return adj[from]?.contains(to) ?: false
    }

    override fun adjacentVertices(v: V): Set<V> {
        return adj[v] ?: emptySet()
    }

    override fun addVertex(v: V) {
        if (v !in adj) {
            adj[v] = mutableSetOf()
        }
    }

    override fun addEdge(from: V, to: V) {
        val existing = adj[from]
        if (existing != null) {
            existing.add(to)
        } else {
            adj[from] = mutableSetOf(to)
        }
    }

    override fun removeVertex(v: V) {
        adj.remove(v)
        for (vertices in adj.values) {
            vertices.remove(v)
        }
    }

    override fun removeEdge(from: V, to: V) {
        adj[from]?.remove(to)
    }
}