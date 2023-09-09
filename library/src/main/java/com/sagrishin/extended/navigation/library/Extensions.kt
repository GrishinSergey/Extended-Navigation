package com.sagrishin.extended.navigation.library

import android.os.Bundle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.sagrishin.extended.navigation.library.ExtendedNavigationGsonUtils.Companion.fromJson
import com.sagrishin.extended.navigation.library.ExtendedNavigationGsonUtils.Companion.toJson
import kotlin.reflect.KClass

fun stringArgument(name: String, default: String? = null, isNullable: Boolean = false): NamedNavArgument {
    return navArgument(name) {
        nullable = isNullable
        type = NavType.StringType

        if (isNullable) {
            defaultValue = null
        } else if (default != null) {
            defaultValue = default
        }
    }
}

inline fun <reified T> Bundle?.argument(name: String = T::class.java.simpleName,  required: Boolean = null is T): T {
    return if (required) {
        require(this?.containsKey(name) == true) { "Required value on key '$name' not passed" }
        this!!.get(name) as T
    } else {
        this?.get(name) as T
    }
}

inline fun <reified T : Arguments> T.toNavArgument(): NamedNavArgument {
    return T::class.toNavArgument(this, false)
}

fun <T : Arguments> KClass<T>.toNavArgument(default: T? = null, isNullable: Boolean = false): NamedNavArgument {
    return stringArgument(this.java.simpleName, default?.toJson(), isNullable)
}

inline fun <reified T : Arguments> NavBackStackEntry.takeNavArgument(name: String = T::class.java.simpleName): T {
    return arguments.argument<String>(name, true).fromJson()
}
