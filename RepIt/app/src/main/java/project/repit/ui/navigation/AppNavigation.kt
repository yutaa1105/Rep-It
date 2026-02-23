package project.repit.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

enum class AppPage(val label: String, val icon: ImageVector) {
    Home("Accueil", Icons.Default.Home),
    Routines("Défis", Icons.Default.FitnessCenter),
    Notifications("Notifications", Icons.Default.Notifications),
    Statistics("Statistiques", Icons.Default.BarChart),
    Profile("Profil", Icons.Default.Person)
}

@Composable
fun BottomNavigationBar(
    currentPage: AppPage,
    onPageSelected: (AppPage) -> Unit
) {
    Surface(
        tonalElevation = 4.dp,
        shadowElevation = 8.dp
    ) {
        Column {
            Divider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                AppPage.entries.forEach { page ->
                    val isSelected = page == currentPage
                    val activeColor = MaterialTheme.colorScheme.primary
                    val inactiveColor = MaterialTheme.colorScheme.onSurfaceVariant

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onPageSelected(page) }
                            .padding(vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = page.icon,
                            contentDescription = page.label,
                            tint = if (isSelected) activeColor else inactiveColor
                        )
                        Text(
                            text = page.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) activeColor else inactiveColor
                        )
                        Box(
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .size(if (isSelected) 6.dp else 0.dp)
                                .clip(MaterialTheme.shapes.small)
                                .background(if (isSelected) activeColor else MaterialTheme.colorScheme.surface)
                        )
                    }
                }
            }
        }
    }
}
