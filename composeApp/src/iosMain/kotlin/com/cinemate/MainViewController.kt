package com.cinemate

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import di.initKoin
import presentation.root.RootComponent

@OptIn(ExperimentalDecomposeApi::class)
fun MainViewController() = ComposeUIViewController {
    initKoin()

    val root = RootComponent(DefaultComponentContext(LifecycleRegistry()))

    App(root)
}