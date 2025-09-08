import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.example.findPeak

class FindPeakTest {
    @Test
    fun findPeakTest() {
        assertEquals(findPeak(listOf(2.0, 5.0, 1.0)),1)
        assertEquals(findPeak(listOf()),null)
        assertEquals(findPeak(listOf(8.0, 2.0)),0)
        assertEquals(findPeak(listOf(8.0, 2.0, 5.0)),0)
        // could also test if returned value is a peak directly.
    }
}