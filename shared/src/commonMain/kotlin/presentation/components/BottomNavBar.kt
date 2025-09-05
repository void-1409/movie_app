package presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import presentation.root.MainNavTab
import presentation.root.RootComponent
import presentation.screens.home.HomeViewModel

@Composable
fun BottomNavBar(
    stack: Value<ChildStack<*, RootComponent.Child>>,
    onTabClick: (MainNavTab) -> Unit
) {
    val activeChild = stack.value.active.instance

    NavigationBar {
        NavigationBarItem(
            selected = activeChild is RootComponent.Child.Home,
            onClick = { onTabClick(MainNavTab.HOME) }, // Temporary
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = activeChild is RootComponent.Child.Movies,
            onClick = { onTabClick(MainNavTab.MOVIES) },
            icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Movies") },
            label = { Text("Movies") }
        )
        NavigationBarItem(
            selected = activeChild is RootComponent.Child.Tickets,
            onClick = { onTabClick(MainNavTab.TICKETS) },
            icon = { Icon(Icons.Default.Star, contentDescription = "Tickets") },
            label = { Text("Tickets") }
        )
        NavigationBarItem(
            selected = activeChild is RootComponent.Child.Profile,
            onClick = { onTabClick(MainNavTab.PROFILE) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}