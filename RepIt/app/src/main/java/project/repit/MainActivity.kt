package project.repit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import project.repit.model.Routine
import project.repit.ui.theme.RepitTheme
import project.repit.util.RoutineFileUtil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RepitTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RoutineScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun RoutineScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var routines by remember { mutableStateOf(emptyList<Routine>()) }
    var editingIndex by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        routines = RoutineFileUtil.readRoutines(context).sortedBy { priorityWeight(it.priority) }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Mes Routines",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        itemsIndexed(routines) { index, routine ->
            RoutineBox(
                routine = routine,
                onEdit = { editingIndex = index },
                onDelete = {
                    routines = routines.filterIndexed { currentIndex, _ -> currentIndex != index }
                    RoutineFileUtil.saveRoutines(context, routines)
                }
            )
        }
    }

    editingIndex?.let { index ->
        EditRoutineDialog(
            routine = routines[index],
            onDismiss = { editingIndex = null },
            onSave = { updatedRoutine ->
                routines = routines.toMutableList().also { it[index] = updatedRoutine }
                    .sortedBy { priorityWeight(it.priority) }
                RoutineFileUtil.saveRoutines(context, routines)
                editingIndex = null
            }
        )
    }
}

@Composable
private fun RoutineBox(routine: Routine, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = routine.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = routine.description, style = MaterialTheme.typography.bodyMedium)
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

@Composable
private fun EditRoutineDialog(
    routine: Routine,
    onDismiss: () -> Unit,
    onSave: (Routine) -> Unit
) {
    var name by remember(routine) { mutableStateOf(routine.name) }
    var description by remember(routine) { mutableStateOf(routine.description) }
    var priority by remember(routine) { mutableStateOf(routine.priority) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Modifier la routine") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom") }
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )
                OutlinedTextField(
                    value = priority,
                    onValueChange = { priority = it },
                    label = { Text("Priorité") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(
                        routine.copy(
                            name = name.trim(),
                            description = description.trim(),
                            priority = priority.trim()
                        )
                    )
                }
            ) {
                Text("Enregistrer")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}

private fun priorityWeight(priority: String): Int = when (priority) {
    "Élevée" -> 0
    "Moyenne" -> 1
    "Faible" -> 2
    else -> 3
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RoutinePreview() {
    RepitTheme {
        RoutineScreen()
    }
}
