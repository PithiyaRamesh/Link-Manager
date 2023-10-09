package com.redvings.linkmanager.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.redvings.linkmanager.models.CollectionModel

class AppPreferences private constructor() {
    private val gson: Gson = Gson()

    companion object {
        private var instance: AppPreferences? = null
        private var sharedPreferences: SharedPreferences? = null
        private val sharedPref get() = sharedPreferences!!

        fun getInstance(context: Context): AppPreferences {
            if (instance == null) {
                instance = AppPreferences()
            }
            if (sharedPreferences == null) {
                sharedPreferences =
                    context.getSharedPreferences(Keys.SHARED_PREF_NAME, Context.MODE_PRIVATE)
            }
            return instance!!
        }
    }

    fun getString(key: String, defValue: String = ""): String {
        return sharedPref.getString(key, defValue) ?: ""
    }

    fun putString(key: String, value: String) {
        sharedPref.edit {
            putString(key, value)
        }
    }

    fun getInt(key: String, defValue: Int): Int {
        return sharedPref.getInt(key, defValue)
    }

    fun getInt(key: String): Int? {
        return if (sharedPreferences?.contains(key) == true) {
            sharedPref.getInt(key, 0)
        } else {
            null
        }
    }

    fun putInt(key: String, value: Int) {
        sharedPref.edit { putInt(key, value) }
    }

    private fun putStringSet(key: String, value: String) {
        sharedPref.edit {
            putString(key, value)
        }
    }

    var tabs: ArrayList<CollectionModel>?
        get() = gson.fromJson(
            getString(Keys.LINK_COLLECTION),
            object : TypeToken<ArrayList<CollectionModel>>() {}.type
        )
        set(value) {
            putStringSet(Keys.LINK_COLLECTION, gson.toJson(value?.toSet()))
        }

    object Keys {
        const val SHARED_PREF_NAME = "LinkManagerPreferences"
        const val LINK_COLLECTION = "LINK_COLLECTION"
        const val SELECTED_APPEARANCE = "SELECTED_APPEARANCE"
    }
}