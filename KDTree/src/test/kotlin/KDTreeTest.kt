import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.example.KDTree

class KDTreeTest {
    @Test fun test() {
        val x = KDTree<String>(3)
        val points = arrayOf<DoubleArray>(
            doubleArrayOf(3.0, 2.0, -1.0),
            doubleArrayOf(2.0, 4.0, -2.0),
            doubleArrayOf(1.0, 6.0, 4.0),
            doubleArrayOf(7.0, 8.0, 3.0),
            doubleArrayOf(9.0, 1.0, 2.0),
            doubleArrayOf(-4.0, -9.0, 7.0),
            doubleArrayOf(4.0, 0.0, 17.0),
            )
        x.buildTree(points, arrayOf("item 1",
                                            "item 2",
                                            "item 3",
                                            "item 4",
                                            "item 5",
                                            "item 6",
                                            "item 7"))
        assertEquals(2.0, x.getMedian(indices=listOf(1, 2, 4, 5, 6),
            0))
        assertEquals(3.0, x.getMedian(indices=listOf(0, 1, 2, 3, 4, 5, 6),
            0))
    }
}