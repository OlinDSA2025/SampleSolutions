package org.example

fun findPeak(L: List<Double>, startIndex: Int=0): Int? {
    if (L.isEmpty()) {
        return null
    } else if (L.size == 1) {
        return startIndex
    } else if (L.size == 2) {
        return if (L[0] >= L[1]) {
            startIndex
        } else {
            startIndex + 1
        }
    }
    val midPointIndex = L.size/2
    val (left, midPoint, right) = Triple(L[midPointIndex - 1],
        L[midPointIndex],L[midPointIndex + 1])
    if (midPoint >= left && midPoint >= right) {
        // midPoint represents a peak
        return midPointIndex+startIndex
    } else if (midPoint < left) {
        // there is guaranteed to be a peak to the left
        return findPeak(L.slice(0 until midPointIndex), startIndex)
    } else {
        // there is guaranteed to be a peak to the right
        return findPeak(L.slice(midPointIndex+1 until L.count()), startIndex +
                midPointIndex + 1)
    }
}

fun printPeak(L: List<Double>, peakIndex: Int) {
    if (peakIndex - 1 >= 0) {
        print("left ${L[peakIndex-1]} ")
    }
    print("center ${L[peakIndex]}")
    if (peakIndex + 1 < L.size) {
        println(" left ${L[peakIndex+1]}")
    } else {
        println()
    }
}
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val x = listOf(1.0, 5.0, 2.1, 3.3, 5.0)
    val peak = findPeak(x)
    if (peak != null) {
        printPeak(x, peak)
    }
}