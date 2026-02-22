package project.repit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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

        items(routines) { routine ->
            RoutineBox(routine)
        }
    }
}

@Composable
private fun RoutineBox(routine: Routine) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = routine.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = routine.description, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Priorité : ${routine.priority}", style = MaterialTheme.typography.bodySmall)
        }
    }
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
