package org.example
import kotlin.random.Random
import kotlin.time.measureTime
import kotlin.time.Duration

/**
 * Benchmark [KDTree] against brute force nearest neighbor.
 * 1000 test points will be generated to test against the training
 * points.
 *
 * @param k: the dimensionality of the dataset to create
 * @param numPoints: the number of points to use to match against
 *   (1000 test points will be used)
 * @return
 */
fun runExperiment(k: Int, numPoints: Int): Triple<Duration, Duration, Duration> {
    // mesh grid
    val pointsList = mutableListOf<DoubleArray>()
    val labels = mutableListOf<String>()
    for (i in 0 until numPoints) {
        val p = DoubleArray(k) { Random.nextDouble() }
        pointsList.add(p)
        labels.add("$i")
    }
    val points = pointsList.toTypedArray()
    val labelsArray = labels.toTypedArray()
    val x = KDTree<String>(k = k)
    val t1 = measureTime {
        x.buildTree(points, labelsArray)
    }
    println(x.toString())
    val t2 = measureTime {
        for (i in 0 until 1000) {
            val p = DoubleArray(k) { Random.nextDouble() }
            x.query(p)
        }
    }
    val t3 = measureTime {
        for (i in 0 until 1000) {
            val p = DoubleArray(k) { Random.nextDouble() }
            val g = points.minBy { p.squaredDistanceTo(it) }
        }
    }
    return Triple(t1,t2,t3)
}
fun main() {
    val (kdTreeBuild, kdTreeQuery, bruteForceQuery) = runExperiment(5,100000)
    println("kdTreeBuild=$kdTreeBuild, kdTreeQuery=$kdTreeQuery, bruteForceQuery=$bruteForceQuery")
}