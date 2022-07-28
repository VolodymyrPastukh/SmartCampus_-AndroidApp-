package com.vovan.diplomaapp

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class UtilTest {

    @Test
    fun defineSharedStateTest() {
        val map = HashMap<Int, List<Boolean>>()
        // red  green  blue
        // reversed order
        map[0] = listOf(false, false, false)
        map[7] = listOf(true, true, true)
        map[3] = listOf(false, true, true)

        map[15] = listOf(true, true, true, true)
        map[14] = listOf(true, true, true, false)
        map[10] = listOf(true, false, true, false)
        map.forEach{ defineSharedStateReversed(*it.value.toBooleanArray()) shouldBe  it.key}
    }
}