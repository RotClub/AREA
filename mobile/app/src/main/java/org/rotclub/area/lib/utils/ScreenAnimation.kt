package org.rotclub.area.lib.utils

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.animatedSlideFullRightComposable(
    route: String,
    deepLinks: List<NavDeepLink> = emptyList(),
    argument: List<NamedNavArgument> = emptyList(),
    content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
) {
    composable(
        route = route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(700)
            )
        },
        deepLinks = deepLinks,
        arguments = argument,
        content = content
    )
}

fun NavGraphBuilder.animatedSlideFullLeftComposable(
    route: String,
    deepLinks: List<NavDeepLink> = emptyList(),
    argument: List<NamedNavArgument> = emptyList(),
    content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
) {
    composable(
        route = route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(700)
            )
        },
        deepLinks = deepLinks,
        arguments = argument,
        content = content
    )
}

fun NavGraphBuilder.animatedSlideFullTopComposable(
    route: String,
    deepLinks: List<NavDeepLink> = emptyList(),
    argument: List<NamedNavArgument> = emptyList(),
    content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
) {
    composable(
        route = route,
        enterTransition = {
            slideInVertically (
                initialOffsetY = { it },
                animationSpec = tween(700)
            )
        },
        exitTransition = {
            slideOutVertically (
                targetOffsetY = { it },
                animationSpec = tween(700)
            )
        },
        deepLinks = deepLinks,
        arguments = argument,
        content = content
    )
}

fun NavGraphBuilder.noAnimationComposable(
    route: String,
    deepLinks: List<NavDeepLink> = emptyList(),
    argument: List<NamedNavArgument> = emptyList(),
    content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
) {
    composable(
        route = route,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        },
        deepLinks = deepLinks,
        arguments = argument,
        content = content
    )
}
