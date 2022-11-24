package com.rtarita.util.ds.heap

interface MutableHeap<E> : MutableCollection<E>, Heap<E> {
    fun popMin(): E
    fun decreaseKey(pos: Int, newVal: E)
    fun decreaseKey(key: E, newVal: E) = decreaseKey(indexOf(key), newVal)
    fun decreaseKeyIfExists(key: E, newVal: E) {
        val index = indexOf(key)
        if (index < 0) return
        decreaseKey(index, newVal)
    }
}