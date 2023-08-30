package com.sagrishin.extended.navigation.library

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

interface NavGraph : NavGraphItem {

    val destinations: List<Destination>
    val startDestination: String

    fun build(builder: NavGraphBuilder) {
        require(destinations.isNotEmpty()) {
            "List of destinations must not be empty"
        }

        destinations.forEach { destination ->
            val arguments = when (destination.route.startsWith(startDestination)) {
                true -> destination.args.map { (name, _) ->
                    val defaultValue = args.associateBy { it.name }.getValue(name).argument.defaultValue
                    stringArgument(name, defaultValue as String?)
                }
                false -> destination.args
            }

            builder.composable(
                route = destination.route,
                arguments = arguments,
                content = { destination.Composable(it) }
            )
        }
    }

}


interface Destination : NavGraphItem {

    @Composable
    fun Composable(navBackStackEntry: NavBackStackEntry)

}


interface NavGraphItem {

    val args: List<NamedNavArgument>
        get() = emptyList()

    val route: String

}


interface Arguments {

}
