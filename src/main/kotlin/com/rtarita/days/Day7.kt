package com.rtarita.days

import com.rtarita.structure.AoCDay
import com.rtarita.util.day
import kotlinx.datetime.LocalDate

object Day7 : AoCDay {
    private sealed class TreeNode {
        abstract val parent: IntermediateNode?
        abstract val size: Int

        fun root(): IntermediateNode {
            var current = parent ?: return this as IntermediateNode
            while (current.parent != null) {
                current = current.parent!!
            }
            return current
        }
    }

    private class IntermediateNode(override val parent: IntermediateNode?) : TreeNode() {
        private val children: MutableMap<String, TreeNode> = mutableMapOf()

        fun children(): Map<String, TreeNode> = children

        fun intermediate(name: String): IntermediateNode {
            return children.getOrPut(name) { IntermediateNode(this) } as IntermediateNode
        }

        fun leaf(name: String, size: Int): LeafNode {
            return children.getOrPut(name) { LeafNode(this, size) } as LeafNode
        }

        override val size
            get() = children.values.sumOf { it.size }
    }

    private class LeafNode(override val parent: IntermediateNode?, override val size: Int) : TreeNode()

    override val day: LocalDate = day(7)

    private tailrec fun scanTree(currentNode: IntermediateNode, current: String, lines: List<String>) {
        when (current[2]) {
            'c' -> {
                val next = lines.getOrNull(0) ?: return
                val newLines = lines.subList(1, lines.size)
                when (val dirname = current.substring(5).trimEnd()) {
                    ".." -> scanTree(currentNode.parent!!, next, newLines)
                    "/" -> scanTree(currentNode.root(), next, newLines)
                    else -> scanTree(currentNode.intermediate(dirname), next, newLines)
                }
            }

            'l' -> {
                val offset = lines.indexOfFirst { it.startsWith('$') }.let { if (it == -1) lines.size else it }
                for (line in lines.subList(0, offset)) {
                    val (sizeOrDir, name) = line.split(' ')
                    if (sizeOrDir == "dir") {
                        currentNode.intermediate(name)
                    } else {
                        currentNode.leaf(name, sizeOrDir.toInt())
                    }
                }
                if (offset == lines.size) return
                scanTree(currentNode, lines[offset], lines.subList(offset + 1, lines.size))
            }
        }
    }

    private fun getTree(input: String): IntermediateNode {
        val tree = IntermediateNode(null)
        val lines = input.lines()
        scanTree(tree, lines[0], lines.subList(1, lines.size))
        return tree
    }

    private fun IntermediateNode.dirSizesBelow(): Int {
        val ownSize = size.let { if (it <= 100_000) it else 0 }
        return ownSize + children().values.sumOf {
            when (it) {
                is LeafNode -> 0
                is IntermediateNode -> it.dirSizesBelow()
            }
        }
    }

    private fun IntermediateNode.findSmallestAbove(threshold: Int): Int {
        if (size < threshold) return Int.MAX_VALUE
        val childMin = children().values.minOf {
            when (it) {
                is LeafNode -> Int.MAX_VALUE
                is IntermediateNode -> if (it.size < threshold) Int.MAX_VALUE else it.findSmallestAbove(threshold)
            }
        }
        return if (childMin == Int.MAX_VALUE) size else childMin
    }

    override fun executePart1(input: String): Int {
        return getTree(input).dirSizesBelow()
    }

    override fun executePart2(input: String): Int {
        val tree = getTree(input)
        val target = 30_000_000 - (70_000_000 - tree.size)

        return tree.findSmallestAbove(target)
    }
}