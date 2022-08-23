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
        map[1] = listOf(false, false, true)
        map[2] = listOf(false, true, false)
        map[3] = listOf(false, true, true)
        map[4] = listOf(true, false, false)
        map[5] = listOf(true, false, true)
        map[6] = listOf(true, true, false)
        map[7] = listOf(true, true, true)

        map.forEach{ defineSharedStateReversed(*it.value.toBooleanArray()) shouldBe  it.key}
    }
}