package project.repit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import project.repit.ui.navigation.AppPage
import project.repit.ui.navigation.BottomNavigationBar
import project.repit.ui.screens.HomeScreen
import project.repit.ui.screens.NotificationsScreen
import project.repit.ui.screens.ProfileScreen
import project.repit.ui.screens.RoutineScreen
import project.repit.ui.screens.StatisticsScreen
import project.repit.ui.theme.RepitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RepitTheme {
                var currentPage by remember { mutableStateOf(AppPage.Home) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            when (currentPage) {
                                AppPage.Home -> HomeScreen()
                                AppPage.Routines -> RoutineScreen()
                                AppPage.Notifications -> NotificationsScreen()
                                AppPage.Statistics -> StatisticsScreen()
                                AppPage.Profile -> ProfileScreen()
                            }
                        }
                        BottomNavigationBar(
                            currentPage = currentPage,
                            onPageSelected = { currentPage = it }
                        )
                    }
                }
            }
        }
    }
}
