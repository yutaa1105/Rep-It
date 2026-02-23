package project.repit.ui.components

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import project.repit.model.Routine
import project.repit.ui.theme.DarkerGray
import project.repit.ui.theme.SoftViolet

/**
 * Représente les différentes pages accessibles via la barre de navigation inférieure.
 *
 * @param label Le nom de la page à afficher.
 * @param icon L'icône associée à la page.
 */
enum class AppPage(val label: String, val icon: ImageVector) {
    Home("Accueil", Icons.Default.Home),
    Routines("Défis", Icons.Default.FitnessCenter),
    Notifications("Notifications", Icons.Default.Notifications),
    Statistics("Statistiques", Icons.Default.BarChart),
    Profile("Profil", Icons.Default.Person)
}

/**
 * Affiche la barre de navigation inférieure de l'application.
 *
 * @param navController Le contrôleur de navigation pour gérer les déplacements entre les pages.
 */
@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface(
        tonalElevation = 4.dp,
        shadowElevation = 8.dp
    ) {
        Column {
            HorizontalDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                AppPage.entries.forEach { page ->
                    val isSelected = currentRoute == page.name
                    val activeColor = SoftViolet
                    val inactiveColor = DarkerGray

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { 
                                navController.navigate(page.name) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
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

/**
 * Affiche une carte présentant les détails d'une routine.
 *
 * @param routine La routine à afficher.
 * @param onEdit Le callback à appeler lorsque l'utilisateur clique sur le bouton de modification.
 * @param onDelete Le callback à appeler lorsque l'utilisateur clique sur le bouton de suppression.
 */
@Composable
fun RoutineBox(routine: Routine, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = routine.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = routine.description, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Catégorie : ${routine.category}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Début : ${routine.startAt}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Fin : ${routine.endAt}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Périodicité : ${routine.periodicity}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Priorité : ${routine.priority}", style = MaterialTheme.typography.bodySmall)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onEdit) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Modifier la routine")
                }
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Supprimer la routine")
                }
            }
        }
    }
}

/**
 * Un champ de formulaire qui affiche une liste d'options dans un menu déroulant.
 *
 * @param label Le libellé du champ.
 * @param options La liste des options à afficher.
 * @param selectedOption L'option actuellement sélectionnée.
 * @param onOptionSelected Le callback à appeler lorsque l'utilisateur sélectionne une nouvelle option.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = { },
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
