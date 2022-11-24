package com.rtarita.util.ds.heap

import com.rtarita.util.ComparatorByComparable

class ArrayHeap<E> private constructor(private val backingList: ArrayList<E>, private val comp: Comparator<E>) : MutableHeap<E> {
    companion object {
        private fun children(pos: Int): Pair<Int, Int> = (2 * pos + 1) to (2 * pos + 2)
        private fun child(pos: Int): Int = 2 * pos + 1
        private fun parent(pos: Int): Int = (pos - 1) / 2
        private fun <E> MutableList<E>.swap(a: Int, b: Int) {
            val temp = this[a]
            this[a] = this[b]
            this[b] = temp
        }

        // pseudo-constructors
        @Suppress("UNCHECKED_CAST")
        operator fun <E : Comparable<E>> invoke(): ArrayHeap<E> {
            return ArrayHeap(ComparatorByComparable as Comparator<E>)
        }

        @Suppress("UNCHECKED_CAST")
        operator fun <E : Comparable<E>> invoke(initialCapacity: Int): ArrayHeap<E> {
            return ArrayHeap(initialCapacity, ComparatorByComparable as Comparator<E>)
        }

        @Suppress("UNCHECKED_CAST")
        operator fun <E : Comparable<E>> invoke(other: Collection<E>): ArrayHeap<E> {
            return ArrayHeap(other, ComparatorByComparable as Comparator<E>)
        }

        @Suppress("UNCHECKED_CAST")
        operator fun <E : Comparable<E>> invoke(other: Array<out E>): ArrayHeap<E> {
            return ArrayHeap(other, ComparatorByComparable as Comparator<E>)
        }
    }

    private tailrec fun heapify(i: Int) {
        val (left, right) = children(i)
        val smallest = when {
            left < backingList.size && comp.compare(backingList[left], backingList[i]) < 0 -> left
            right < backingList.size && comp.compare(backingList[right], backingList[i]) < 0 -> right
            else -> return
        }

        backingList.swap(i, smallest)
        heapify(smallest)
    }

    private fun heapify() {
        for (i in backingList.size / 2 - 1 downTo 0) {
            heapify(i)
        }
    }

    private fun deleteAt(pos: Int): E {
        val deleted = backingList[pos]
        backingList[pos] = backingList.last()
        backingList.removeLast()
        var parentPos = pos
        var childPos = child(pos)
        while (childPos < backingList.size) {
            if (childPos + 1 < backingList.size && comp.compare(backingList[childPos], backingList[childPos + 1]) > 0) ++childPos
            if (comp.compare(backingList[childPos], backingList[parentPos]) < 0) {
                backingList.swap(parentPos, childPos)
                parentPos = childPos
                childPos = child(childPos)
            } else break
        }

        return deleted
    }

    constructor(comp: Comparator<E>) : this(ArrayList(10), comp)
    constructor(initialCapacity: Int, comp: Comparator<E>) : this(ArrayList(initialCapacity), comp)
    constructor(other: Collection<E>, comp: Comparator<E>) : this(ArrayList(other), comp) {
        heapify()
    }

    constructor(other: Array<out E>, comp: Comparator<E>) : this(arrayListOf(*other), comp) {
        heapify()
    }

    override val size: Int = backingList.size
    override fun isEmpty(): Boolean = backingList.isEmpty()
    override operator fun contains(element: E): Boolean = backingList.contains(element)
    override fun containsAll(elements: Collection<E>): Boolean = backingList.containsAll(elements)

    override fun add(element: E): Boolean {
        backingList.add(element)
        var childPos = backingList.lastIndex
        var parentPos = parent(childPos)
        while (childPos != parentPos) {
            if (comp.compare(backingList[childPos], backingList[parentPos]) < 0) {
                backingList.swap(parentPos, childPos)
                childPos = parentPos
                parentPos = parent(parentPos)
            } else break
        }
        return true
    }

    override fun addAll(elements: Collection<E>): Boolean {
        for (e in elements) {
            add(e)
        }
        return true
    }

    override fun clear() = backingList.clear()

    override fun iterator(): MutableIterator<E> = backingList.iterator()

    override fun remove(element: E): Boolean {
        if (isEmpty()) return false
        val index = indexOf(element)
        if (index < 0) return false
        deleteAt(index)
        return true
    }

    override fun removeAll(elements: Collection<E>): Boolean = elements.map { remove(it) }.any { it }

    override fun retainAll(elements: Collection<E>): Boolean {
        var modified = false
        for (e in backingList) {
            if (e !in elements) {
                remove(e)
                modified = true
            }
        }
        return modified
    }

    override operator fun get(pos: Int): E {
        return backingList[pos]
    }

    override fun minOrNull(): E? {
        return backingList.firstOrNull()
    }

    override fun popMin(): E {
        if (isEmpty()) throw NoSuchElementException("Heap is empty")
        return deleteAt(0)
    }

    override fun decreaseKey(pos: Int, newVal: E) {
        var current = pos
        var parent = parent(current)
        backingList[current] = newVal
        while (current != 0 && comp.compare(backingList[parent], backingList[current]) > 0) {
            backingList.swap(current, parent)
            current = parent
            parent = parent(current)
        }
    }
}