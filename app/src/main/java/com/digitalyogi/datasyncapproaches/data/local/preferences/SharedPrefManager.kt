package com.digitalyogi.datasyncapproaches.data.local.preferences


import android.content.Context
import android.content.SharedPreferences

private const val PREF_NAME = "my_shared_prefs"

class SharedPrefManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun setString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return prefs.getString(key, defaultValue) ?: defaultValue
    }
}
