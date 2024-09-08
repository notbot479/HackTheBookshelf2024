package kz.nikitos.hackingthebookshelf

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.sharp.Notifications
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable
import kz.nikitos.hackingthebookshelf.ui.AchievmentsViewModel
import kz.nikitos.hackingthebookshelf.ui.BooksViewModel
import kz.nikitos.hackingthebookshelf.ui.Events
import kz.nikitos.hackingthebookshelf.ui.EventsViewModel
import kz.nikitos.hackingthebookshelf.ui.composables.AchievmentsScreen
import kz.nikitos.hackingthebookshelf.ui.composables.BooksScreen
import kz.nikitos.hackingthebookshelf.ui.composables.EventsScreen
import kz.nikitos.hackingthebookshelf.ui.theme.HackingTheBookShelfTheme

@AndroidEntryPoint
class LibraryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HackingTheBookShelfTheme {
                val navController = rememberNavController()
                val bottomNavigationItems = listOf(
                    BottomNavigationItem(
                        "Events",
                        Icons.Filled.DateRange,
                        Icons.Outlined.DateRange,
                        NavigationDestination.EventsScreenDest
                    ),
                    BottomNavigationItem(
                        "Books",
                        Icons.Sharp.Notifications,
                        Icons.Outlined.Notifications,
                        NavigationDestination.BooksScreenDestination
                    ),
                    BottomNavigationItem(
                        "Achievements",
                        Icons.Filled.AccountBox,
                        Icons.Outlined.AccountBox,
                        NavigationDestination.AchievmentsScreenDestination
                    )
                )

                var selectedTab by rememberSaveable {
                    mutableIntStateOf(0)
                }
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            bottomNavigationItems.forEachIndexed { index, bottomNavigationItem ->
                                val isCurrentTab = selectedTab == index
                                NavigationBarItem(
                                    selected = isCurrentTab,
                                    onClick = {
                                        selectedTab = index
                                        navController.navigate(bottomNavigationItem.destination)
                                    },
                                    icon = {
                                        BadgedBox(badge = {}) {
                                            Icon(
                                                imageVector = if (isCurrentTab) bottomNavigationItem.selectedIcon else bottomNavigationItem.unselectedIcon,
                                                contentDescription = bottomNavigationItem.title
                                            )
                                        }
                                    },
                                    label = {
                                        Text(text = bottomNavigationItem.title)
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = NavigationDestination.EventsScreenDest,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<NavigationDestination.EventsScreenDest> {
                            val viewModel = hiltViewModel<EventsViewModel>()

                            val errorMessage by viewModel.errorMessage.observeAsState("")
                            if (errorMessage.isNotEmpty()) {
                                Toast.makeText(this@LibraryActivity, errorMessage, Toast.LENGTH_LONG).show()
                            }
                            LaunchedEffect(key1 = Unit) {
                                viewModel.getEvents()
                            }
                            val allEvents by viewModel.allEvents.observeAsState(emptyMap())
                            val subscribedEvents by viewModel.subscribedEvent.observeAsState(emptyList())

                            EventsScreen(allEvents = allEvents, subscribedEvents, viewModel::registerOnEvent, modifier = Modifier.padding(horizontal = 6.dp))
                        }
                        composable<NavigationDestination.BooksScreenDestination> {
                            val viewModel = hiltViewModel<BooksViewModel>()
                            LaunchedEffect(key1 = Unit) {
                                viewModel.getBooks()
                            }
                            val books by viewModel.books.observeAsState(emptyList())
                            BooksScreen(books = books, viewModel::registerReminder)
                        }
                        composable<NavigationDestination.AchievmentsScreenDestination> {
                            val viewModel = hiltViewModel<AchievmentsViewModel>()
                            val achievments by viewModel.achievments.observeAsState(emptyList())
                            LaunchedEffect(key1 = Unit) {
                                viewModel.getAchievments()
                            }
                            AchievmentsScreen(achievments)
                        }
                    }
                }
            }
        }
    }
}

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val destination: NavigationDestination
)

@Serializable
sealed interface NavigationDestination {
    @Serializable
    object EventsScreenDest : NavigationDestination

    @Serializable
    object BooksScreenDestination : NavigationDestination

    @Serializable
    object AchievmentsScreenDestination : NavigationDestination
}