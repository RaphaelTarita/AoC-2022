package com.rtarita.util.ds.heap

fun <E : Comparable<E>> arrayHeapOf(vararg elements: E): ArrayHeap<E> = ArrayHeap(elements)

fun <E : Comparable<E>> heapOf(vararg elements: E): Heap<E> = ArrayHeap(elements)

fun <E : Comparable<E>> emptyHeap(): Heap<E> = ArrayHeap(0)

fun <E> emptyHeap(comp: Comparator<E>) = ArrayHeap(0, comp)

fun <E : Comparable<E>> mutableHeapOf(vararg elements: E): MutableHeap<E> = ArrayHeap(elements)