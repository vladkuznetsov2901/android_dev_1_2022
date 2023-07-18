package com.example.m11

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

private const val PREF_NAME = "pref_name"
private const val PREF_KEY = "pref_key"
private lateinit var prefs: SharedPreferences

class Repository {
    private var localValue: String? = null

    private fun getDataFromSharedPreference(context: Context): String? {
        prefs = getPrefs(context)
        return prefs.getString(PREF_KEY, null)
    }

    private fun getDataFromLocalVariable(): String? {
        return localValue
    }

    fun saveText(text: String, context: Context) {
        prefs = getPrefs(context)
        val editor = prefs.edit()
        editor.putString(PREF_KEY, text)
        editor.apply()
        localValue = text
    }


    fun clearText(context: Context) {
        prefs = getPrefs(context)
        prefs.edit().clear().apply()
        localValue = null
    }

    fun getText(context: Context): String? {
        return when {
            getDataFromLocalVariable() != null -> getDataFromLocalVariable()
            getDataFromSharedPreference(context) != null -> getDataFromSharedPreference(context)
            else -> context.getString(R.string.nothing)
        }
    }

    private fun getPrefs(context: Context): SharedPreferences {
        prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        return prefs
    }


}