package com.cinemate

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import presentation.root.RootComponent
import presentation.root.RootScreen

@Composable
@Preview
fun App(root: RootComponent) {
    MaterialTheme {
        Children(
            stack = root.childStack,
            animation = stackAnimation(slide())
        ) {
            RootScreen(child = it.instance, component = root)
        }
    }
}