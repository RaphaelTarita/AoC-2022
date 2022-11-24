package com.rtarita.util.ds.graph

interface MutableGraph<V> : Graph<V> {
    fun addVertex(v: V)
    fun addEdge(from: V, to: V)
    fun removeVertex(v: V)
    fun removeEdge(from: V, to: V)

    fun addUndirectedEdge(v1: V, v2: V) {
        addEdge(v1, v2)
        addEdge(v2, v1)
    }

    fun removeUndirectedEdge(v1: V, v2: V) {
        removeEdge(v1, v2)
        removeEdge(v2, v1)
    }
}