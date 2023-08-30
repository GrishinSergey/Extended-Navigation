package com.sagrishin.extended.navigation.library

import com.google.gson.Gson
import com.google.gson.GsonBuilder

val gson: Gson by lazy {
    GsonBuilder()
        .serializeNulls()
        .create()
}

fun Any.toJson(): String {
    return gson.toJson(this)
}

inline fun <reified T> String.fromJson(): T {
    return gson.fromJson(this, T::class.java)
}
