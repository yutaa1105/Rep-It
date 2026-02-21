package project.repit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import project.repit.data.model.Series
import project.repit.data.repository.FitRepository
import project.repit.ui.components.FitBottomBar
import project.repit.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val user   = FitRepository.getUser()
    val series = FitRepository.getSeries()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mon profil", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Settings, contentDescription = "Paramètres")
                    }
                },
            )
        },
        bottomBar      = { FitBottomBar(navController) },
        containerColor = BackgroundGray,
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(16.dp))
            Box(modifier = Modifier.size(88.dp).clip(CircleShape).background(Purple),
                contentAlignment = Alignment.Center) {
                Text(user.name.first().toString(), color = Color.White,
                    fontSize = 36.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(12.dp))
            Text(user.name, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text("Niveau ${user.levelLabel} - ${user.totalChallenges} défis",
                color = TextSecondary, fontSize = 14.sp)
            Spacer(Modifier.height(20.dp))

            Card(shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(1.dp),
                modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceAround) {
                    PhysicalStat("${user.age}",      "Ans")
                    VerticalDivider(modifier = Modifier.height(48.dp))
                    PhysicalStat("${user.heightCm}", "cm")
                    VerticalDivider(modifier = Modifier.height(48.dp))
                    PhysicalStat("${user.weightKg}", "kg")
                }
            }

            Spacer(Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically) {
                Text("Mes séries", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                TextButton(onClick = {}) { Text("+ Nouvelle", color = Purple) }
            }
            Spacer(Modifier.height(10.dp))
            series.forEach { s ->
                SeriesCard(s)
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun PhysicalStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Text(label, color = TextSecondary, fontSize = 14.sp)
    }
}

@Composable
private fun SeriesCard(series: Series) {
    Card(shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically) {
                Column {
                    Text(series.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(
                        if (series.isFinished)
                            "${series.currentDay}/${series.totalDays} jours · 100% complété"
                        else
                            "jour ${series.currentDay}/${series.totalDays} · ${series.progressPercent}% complété",
                        color = TextSecondary, fontSize = 13.sp,
                    )
                }
                if (series.isFinished) {
                    Surface(shape = RoundedCornerShape(20.dp), color = Green) {
                        Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Check, contentDescription = null,
                                tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Terminé", color = Color.White, fontWeight = FontWeight.SemiBold)
                        }
                    }
                } else {
                    Surface(shape = RoundedCornerShape(20.dp), color = Orange) {
                        Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            Text("🔥", fontSize = 14.sp)
                            Spacer(Modifier.width(4.dp))
                            Text("${series.streakDays}", color = Color.White,
                                fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            LinearProgressIndicator(
                progress   = { series.progressPercent / 100f },
                modifier   = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color      = if (series.isFinished) Green else Orange,
                trackColor = Color(0xFFE5E7EB),
            )
            Spacer(Modifier.height(8.dp))
            Text("${series.challengesCompleted} Défis réussis", color = TextSecondary, fontSize = 13.sp)
        }
    }
}
