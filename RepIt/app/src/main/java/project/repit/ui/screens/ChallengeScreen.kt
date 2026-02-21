package project.repit.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import project.repit.data.model.Challenge
import project.repit.data.repository.FitRepository
import project.repit.ui.components.*
import project.repit.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeScreen(navController: NavController) {
    val suggested = FitRepository.getSuggestedChallenges()
    var selectedFilter by remember { mutableStateOf("Rapide") }
    val filters = listOf("Rapide", "Extérieur", "Force", "Cardio")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nouveau Défi", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Tune, contentDescription = "Filtres")
                    }
                },
            )
        },
        bottomBar      = { FitBottomBar(navController) },
        containerColor = BackgroundGray,
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                filters.forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick  = { selectedFilter = filter },
                        label    = { Text(filter) },
                        colors   = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Purple,
                            selectedLabelColor     = Color.White,
                        ),
                    )
                }
            }

            WeatherBanner()
            Spacer(Modifier.height(20.dp))

            Column(Modifier.padding(horizontal = 16.dp)) {
                Text("Adapté à votre niveau", fontWeight = FontWeight.Bold,
                    fontSize = 18.sp, color = Purple)
                Spacer(Modifier.height(10.dp))
                LevelSelector()
                Spacer(Modifier.height(6.dp))
                Text("Basé sur vos 30 derniers jours d'activité",
                    fontSize = 12.sp, color = TextSecondary)
            }

            Spacer(Modifier.height(20.dp))

            Column(Modifier.padding(horizontal = 16.dp)) {
                Text("Défis suggérées", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(10.dp))
                suggested.forEach { challenge ->
                    SuggestedChallengeCard(challenge)
                    Spacer(Modifier.height(12.dp))
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun WeatherBanner() {
    Card(
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA)),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.Top,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("⛅", fontSize = 32.sp)
                    Text("Recommandé pour\naujourd'hui",
                        fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Surface(shape = RoundedCornerShape(12.dp), color = Teal) {
                    Text("22°C · Ensoleillé", color = Color.White,
                        fontWeight = FontWeight.SemiBold, fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp))
                }
            }
            Spacer(Modifier.height(12.dp))
            Surface(shape = RoundedCornerShape(12.dp), color = Color.White.copy(.7f),
                modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {
                        Text("Conditions idéales", fontWeight = FontWeight.Bold)
                        Icon(Icons.Filled.Star, contentDescription = null, tint = StarYellow)
                    }
                    Text("parfait pour les activités extérieures",
                        fontSize = 12.sp, color = TextSecondary, fontStyle = FontStyle.Italic)
                    Spacer(Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround) {
                        ActivityItem("Course",  "Recommandé")
                        ActivityItem("Yoga",    "Adapté")
                        ActivityItem("Vélo",    "Parfait")
                    }
                }
            }
        }
    }
}

@Composable
private fun ActivityItem(activity: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(activity, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Text(label, color = Green, fontSize = 12.sp)
    }
}

@Composable
private fun LevelSelector() {
    data class Level(val label: String, val sub: String, val selected: Boolean)
    val levels = listOf(
        Level("Débutant",      "Facile",       false),
        Level("Intermédiaire", "Votre niveau", true),
        Level("Avancé",        "Difficile",    false),
    )
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        levels.forEach { level ->
            Surface(
                shape    = RoundedCornerShape(12.dp),
                color    = if (level.selected) Purple else Color.White,
                modifier = Modifier.weight(1f).border(
                    1.dp,
                    if (level.selected) Purple else Color(0xFFE5E7EB),
                    RoundedCornerShape(12.dp)
                ),
            ) {
                Column(modifier = Modifier.padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(level.label, fontWeight = FontWeight.Bold,
                        color = if (level.selected) Color.White else TextPrimary, fontSize = 14.sp)
                    Text(level.sub,
                        color = if (level.selected) Color.White.copy(.8f) else TextSecondary,
                        fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun SuggestedChallengeCard(challenge: Challenge) {
    Card(
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier  = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(modifier = Modifier.size(48.dp).then(
                        Modifier.padding(0.dp)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.DirectionsWalk, contentDescription = null, tint = Green)
                    }
                    Column {
                        Text(challenge.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("${challenge.durationMin} min · ${challenge.subtitle}",
                            color = TextSecondary, fontSize = 13.sp)
                        Row {
                            StarsRow(filled = challenge.difficultyStars)
                            Spacer(Modifier.width(6.dp))
                            Text("(${challenge.successRate}% réussite)",
                                color = TextSecondary, fontSize = 12.sp)
                        }
                    }
                }
                Button(onClick = {}, shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Green)) {
                    Text("Démarrer")
                }
            }
            Spacer(Modifier.height(10.dp))
            HorizontalDivider(color = Color(0xFFF3F4F6))
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                if (challenge.preparationMin > 0) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Filled.Timer, contentDescription = null,
                            tint = TextSecondary, modifier = Modifier.size(16.dp))
                        Text("Préparation: ${challenge.preparationMin}min",
                            color = TextSecondary, fontSize = 13.sp)
                    }
                    Text("👣 ≈ ${"%,d".format(challenge.estimatedSteps)} pas",
                        color = TextSecondary, fontSize = 13.sp)
                }
                if (challenge.estimatedKcal > 0) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Filled.FlashOn, contentDescription = null,
                            tint = TextSecondary, modifier = Modifier.size(16.dp))
                        Text("Energie: ${challenge.energyLevel}",
                            color = TextSecondary, fontSize = 13.sp)
                    }
                    Text("💧 ${challenge.estimatedKcal} kcal",
                        color = TextSecondary, fontSize = 13.sp)
                }
            }
        }
    }
}

