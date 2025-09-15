package edu.olindsa

interface MutableStringList {
    /**
     * Adds [data] to the end of the list
     */
    fun add(data: String)
    /**
     * Returns the number of elements in this collection.
     */
    fun count(): Int
    /**
     * Return the element at the specified index
     *
     * @return the value if it exists.   The behavior is undefined if the value does not exist.
     */
    operator fun get(index: Int): String
    /**
     * Set the element at the specified index.  If the index does not exist, the behavior is undefined.
     */
    operator fun set(index: Int, value: String)
}