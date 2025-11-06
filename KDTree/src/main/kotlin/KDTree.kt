package org.example
import kotlin.math.pow

/**
 * This class implements a KDTree for nearest neighbor search.
 *
 * @param T the type of data to associate with each point
 * @property d the dimensionality of each point
 * @property root is the root node of the KDTree
 * @property points is the data that has been entered into the KDTree
 * @property values are the values that correspond to the data in [points].
 * @constructor Creates an empty KDTree with the specified dimensionality
 */
class KDTree<T>(val d: Int) {
    private var root: KDTreeNode? = null
    private var points: Array<DoubleArray>? = null
    private var values: Array<T>? = null

    /**
     * Represents a node in our tree
     * @property d the dimensionality of the points
     * @property splitDim the dimension to split the points on (null if this is a leaf)
     * @property indices the indices, corresponding to the original points,
     *      that are stored at this node
     * @property left the node corresponding to the left child (or null if this is a leaf)
     * @property right the node corresponding to the right child (or null if this is a leaf)
     */
    private class KDTreeNode(val d: Int,
                             val splitDim: Int?,
                             val splitValue: Double?,
                             val indices: List<Int>,
                             var left: KDTreeNode?,
                             var right: KDTreeNode?) {
        /**
         * Converts the node into a [String].  The [level] variable
         * is used to properly indent the text.
         */
        fun toStringHelper(level: Int): String {
            val indent = " ".repeat(level*2)
            var stringRepresentation = "${indent}d={$d} splitDim={$splitDim} splitValue={$splitValue} indices={$indices}\n"
            if (left == null) {
                stringRepresentation += "${indent}left: null\n"
            } else {
                stringRepresentation += "${indent}left:\n${left?.toStringHelper(level+1)}"
            }
            if (right == null) {
                stringRepresentation += "${indent}right: null\n"
            } else {
                stringRepresentation += "${indent}right:\n${right?.toStringHelper(level + 1)}"
            }
            return stringRepresentation
        }
    }

    /**
     * Populate the [KDTree] with the specified points and values.
     * @param points the points to be added to the tree
     * @param values the values that correspond to each point
     */
    fun buildTree(points: Array<DoubleArray>, values: Array<T>) {
        require(points.count() == values.count(),
                { "mismatch in size of values and points" })
        this.points = points
        this.values = values

        root = buildTreeHelper(List<Int>(points.count()) { it },
                               splitDim=0)
    }

    /** Convert the tree to a [String] with indentation used to show the hierarhcy */
    override fun toString(): String {
        var stringRepresentation = ""
        stringRepresentation += root?.toStringHelper(0) ?: ""
        stringRepresentation += "\n"
        return stringRepresentation
    }

    /**
     * Create a tree node from the specified indices and by splitting along
     * the median of [splitDim]
     */
    private fun buildTreeHelper(indices: List<Int>,
                                splitDim: Int): KDTreeNode? {
        val points = points?: return null
        if (indices.count() <= 1) {
            return KDTreeNode(
                d=d,
                null,
                null,
                indices = indices,
                left = null,
                right = null,
            )
        }
        val median = getMedian(indices, splitDim) ?: return null
        // segment into
        val smallerThanMedianIndices = indices.filter { points[it][splitDim] <
                median }
        val largerThanMedianIndices = indices.filter { points[it][splitDim] >=
                median }
        // if we have a tie along our split dimension, we will end up with one empty list
        // and the other list with more than 1 element.  This can cause an infinite loop
        // if we don't default back to a base case.

        if (smallerThanMedianIndices.count() == 0 || largerThanMedianIndices.count() == 0) {
            return KDTreeNode(
                d=d,
                null,
                null,
                indices = indices,
                left = null,
                right = null,
            )
        }
        val left = buildTreeHelper(smallerThanMedianIndices, (splitDim + 1) % d)
        val right = buildTreeHelper(largerThanMedianIndices, (splitDim + 1) % d)

        return KDTreeNode(
            d = d,
            splitDim = splitDim,
            splitValue = median,
            indices = indices,
            left = left,
            right = right
        )
    }

    /**
     * Return the value corresponding to the closets point to [point]
     * @param point the query point
     */
    fun query(point: DoubleArray):T? {
        require(point.count() == d, { "point has the wrong dimensions" })
        val values = values ?: return null
        val root = root ?: return null
        val closestInd = closestPointIndex(point, root) ?: return null
        return values[closestInd]
    }

    /**
     * Finds the index of the closest point to [point] in the subtree rooted
     * at [node]
     * @param point the query point
     * @param node the subtree to search
     */
    private fun closestPointIndex(point: DoubleArray, node: KDTreeNode):
            Int? {
        val points = points ?: return null
        if (node.splitDim == null || node.splitValue == null) {
            // brute force
            if (node.indices.isEmpty()) { return null }
            return node.indices.minBy { point.squaredDistanceTo(points[it]) }
        } else {
            val nodesToSearch = if (point[node.splitDim] < node.splitValue) listOf(node.left, node.right) else listOf(node.right, node.left)
            val firstToSearch = nodesToSearch[0] ?: return null
            val closestFirst = closestPointIndex(point, firstToSearch) ?: return null
            val squaredDistanceToSplittingPlane = (point[node.splitDim] - node.splitValue).pow(2.0)
            val squaredDistanceToClosestFirst =
                point.squaredDistanceTo(points[closestFirst])
            // see if we need to check the other
            if (squaredDistanceToClosestFirst < squaredDistanceToSplittingPlane) {
                return closestFirst
            } else {
                // let's check the second and return the overall closest
                val second = nodesToSearch[1] ?: return closestFirst
                val closestSecond =
                    closestPointIndex(point, second) ?: return closestFirst
                val squaredDistanceToClosestSecond =
                    point.squaredDistanceTo(points[closestSecond])
                return if (squaredDistanceToClosestSecond < squaredDistanceToClosestFirst) closestSecond else closestFirst
            }
        }
    }

    /**
     * Gets the median of a subset of points along dimensions [dim]
     * Note: this could use quick select for faster performance
     * @param indices the subset of points to use for median computation
     * @param dim the dimension to calculate the median on
     */
    internal fun getMedian(indices: List<Int>, dim: Int): Double? {
        val points = points ?: return null
        val splittingData = indices.map( { points[it][dim] })
        // could be sped up to O(n) (currently O(n log n))
        val sortedData = splittingData.sorted()
        return sortedData[sortedData.count() / 2]
    }
}

/** Return the squared distance from [this] to [other] */
fun DoubleArray.squaredDistanceTo(other: DoubleArray): Double {
    return this.zip(other).fold(0.0) { acc, next -> acc + (next.first - next.second).pow(2.0) }
}
