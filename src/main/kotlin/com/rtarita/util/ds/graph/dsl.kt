package com.rtarita.util.ds.graph

import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
inline fun <V> buildGraph(@BuilderInference builderAction: MutableGraph<V>.() -> Unit): Graph<V> {
    val mutable = AdjacencyListGraph<V>()
    mutable.builderAction()
    return mutable
}