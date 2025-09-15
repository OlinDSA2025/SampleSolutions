package edu.olindsa

class MyStack<T>: Stack<T> {
    class StackNode<T>(val data: T,
                       var next: StackNode<T>?)

    var top: StackNode<T>? = null

    override fun push(data: T) {
        top = StackNode<T>(data, top)
    }

    override fun pop(): T? {
        val returnValue = top?.data
        top = top?.next
        return returnValue
    }

    override fun peek(): T? {
        return top?.data
    }

    override fun isEmpty(): Boolean {
        return top == null
    }
}

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val x = MyStack<Double>()
    x.push(5.0)
    x.push(2.0)
    println(x.peek())
    println(x.pop())
    println(x.peek())
    x.pop()
    println(x.isEmpty())
}