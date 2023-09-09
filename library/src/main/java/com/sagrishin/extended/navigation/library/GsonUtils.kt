package com.sagrishin.extended.navigation.library

import com.google.gson.Gson
import com.google.gson.GsonBuilder

class ExtendedNavigationGsonUtils {

    companion object {
        lateinit var gson: Gson

        init {
            gson = GsonBuilder()
                .serializeNulls()
                .create()
        }

        fun Any.toJson(): String {
            return gson.toJson(this)
        }

        inline fun <reified T> String.fromJson(): T {
            return gson.fromJson(this, T::class.java)
        }
    }

}
