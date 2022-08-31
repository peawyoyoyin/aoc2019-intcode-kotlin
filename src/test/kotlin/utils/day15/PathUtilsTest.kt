package utils.day15

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class PathUtilsTest {
    @Test
    fun test_partition() {
        val (commonPrefix, toReverse, toApply) = partition(pathFromString("N"), pathFromString(""))
        assertIterableEquals(commonPrefix, listOf<Direction>())
        assertIterableEquals(toReverse, listOf<Direction>())
        assertIterableEquals(toApply, pathFromString("N"))
    }

    @Test
    fun test_partition2() {
        val (commonPrefix, toReverse, toApply) = partition(pathFromString("NNWWW"), pathFromString("NNWWS"))
        assertIterableEquals(commonPrefix, pathFromString("NNWW"))
        assertIterableEquals(toReverse, pathFromString("S"))
        assertIterableEquals(toApply, pathFromString("W"))
    }
}