import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test
import edu.olindsa.MyStack
import kotlin.test.assertEquals

class MyStackTest {
    @Test
    fun basicTests() {
        val x = MyStack<String>()
        assert(x.isEmpty())
        x.push("test")
        assert(!x.isEmpty())
        x.push("hello world")
        assertEquals(x.peek(), "hello world")
        assertEquals(x.pop(), "hello world")
        assertEquals(x.pop(), "test")
        assertEquals(x.pop(), null)
    }

}