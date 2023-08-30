package com.sagrishin.tools.extended_navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sagrishin.extended.navigation.annotations.NavDestination
import com.sagrishin.extended.navigation.library.Arguments

@Composable
@NavDestination
fun FirstScreen(modifier: Modifier = Modifier, args: FirstScreenArgs) {
}


data class FirstScreenArgs constructor(
    val id: Long,
) : Arguments




@Composable
@NavDestination
fun SecondScreen(modifier: Modifier = Modifier) {
}
