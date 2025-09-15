package edu.olindsa
import edu.olindsa.MutableStringList

class MyMutableStringList: MutableStringList {
    /**
     * Store the current number of entries in the list.  This
     * differs from the capacity of the list
     */
    var count: Int = 0

    /**
     * [backingArray] is the underlying data structure that stores
     * the elements in the mutable list.
     */
    var backingArray = Array(1) { "" }
    override fun add(data: String) {
        val index = count
        if (count >= backingArray.count()) {
            expandArray()
        }
        backingArray[index] = data
        count++
    }

    /**
     * Double the size of the array by creating a new backing array.
     * Copy the old elements over to the new backing array.
     */
    private fun expandArray() {
        val newBackingArray = Array(backingArray.count()*2) { "" }
        for ((idx, elem) in backingArray.withIndex()) {
            newBackingArray[idx] = elem
        }
        backingArray = newBackingArray
    }

    override fun count(): Int {
        return count
    }

    override fun get(index: Int): String {
        return backingArray[index]
    }

    override fun set(index: Int, value: String) {
        backingArray[index] = value
    }
}

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val x = MyMutableStringList()
    for (i in 0 until 100) {
        x.add("this value is for index ${i}")
    }
    for (i in 0 until 100) {
        println(x[i])
    }
    // TODO: make some proper unit tests
}