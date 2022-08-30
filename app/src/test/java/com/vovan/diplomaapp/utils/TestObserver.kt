package com.vovan.diplomaapp.utils

import androidx.lifecycle.Observer
import io.kotest.matchers.shouldBe

class TestObserver<T> : Observer<T> {
    private val history: MutableList<T> = mutableListOf()

    override fun onChanged(value: T) {
        history.add(value)
    }

    fun assertAllEmitted(values: List<T>) {
        values.count() shouldBe history.count()

        history.forEachIndexed { index, t ->
            values[index] shouldBe t
        }
    }
}