@file:Suppress("IMPLICIT_CAST_TO_ANY")

package com.vovan.diplomaapp.data.sharedPreference

import android.content.SharedPreferences

class SharedPreferenceDataSource(val sharedPreferences: SharedPreferences) {

    fun <T> putValueTo(key: String, value: T) = with(sharedPreferences.edit()){
        when(value){
            is Boolean -> putBoolean(key, value)
            is Int -> putInt(key, value)
            is Float -> putFloat(key, value)
            is String -> putString(key, value)
            is Long -> putLong(key, value)
        }
        apply()
    }

    inline fun <reified T> getValue(key: String): T = with(sharedPreferences){
        return@with when(T::class){
            Boolean::class -> getBoolean(key, false)
            Int::class -> getInt(key, 0)
            Float::class -> getFloat(key, 0f)
            String::class -> getString(key, "")
            Long::class -> getLong(key, 0)
            else -> {}
        } as T
    }

}