package project.repit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.navigation.NavController
import project.repit.data.repository.FitRepository
import project.repit.ui.components.FitBottomBar
import project.repit.ui.theme.*

@Composable
fun StatisticsScreen(navController: NavController) {
    val stats = FitRepository.getStats()

    Scaffold(
        bottomBar      = { FitBottomBar(navController) },
        containerColor = BackgroundGray,
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(Brush.linearGradient(listOf(Purple, Color(0xFF9F67F5))))
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically) {
                    Column {
                        Text("Statistiques", color = Color.White,
                            fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        Text("Votre progression", color = Color.White.copy(.8f), fontSize = 14.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Semaine ${stats.weekNumber}", color = Color.White,
                            fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("+${stats.vsLastWeek}% VS la semaine dernière",
                            color = Color.White.copy(.9f), fontSize = 13.sp)
                    }
                }
            }

            Column(modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                ProgressCircle(percent = stats.progressPercent)
                Spacer(Modifier.height(16.dp))
                Text("Objectifs hebdomadaires", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("${stats.weeklyGoalDone}/${stats.weeklyGoalTotal} objectifs atteints",
                    color = TextSecondary, fontSize = 14.sp)
                Spacer(Modifier.height(24.dp))

                StatRow(icon = Icons.Filled.LocalFireDepartment, iconBg = Color(0xFF3B82F6),
                    value = "${"%,d".format(stats.kcalBurned)} kcal",
                    label = "Brulées cette semaine", changePct = "+${stats.kcalChange}%")
                Spacer(Modifier.height(12.dp))
                StatRow(icon = Icons.Filled.DirectionsWalk, iconBg = Green,
                    value = "${"%,d".format(stats.totalSteps)} pas",
                    label = "Moyenne : ${"%,d".format(stats.avgStepsPerDay)} pas/jour",
                    changePct = "+${stats.stepsChange}%")
                Spacer(Modifier.height(12.dp))
                StatRow(icon = Icons.Filled.EmojiEvents, iconBg = Purple,
                    value = "${stats.challengesDone} défis",
                    label = "Terminés ce mois-ci", changePct = "+${stats.challengesChange}")
            }
        }
    }
}

@Composable
private fun ProgressCircle(percent: Int, size: Int = 200) {
    Box(modifier = Modifier.size(size.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(size.dp)) {
            val stroke = 16.dp.toPx()
            drawArc(color = Color(0xFFE5E7EB), startAngle = -90f, sweepAngle = 360f,
                useCenter = false, style = Stroke(stroke, cap = StrokeCap.Round))
            drawArc(color = Color(0xFF22C55E), startAngle = -90f,
                sweepAngle = 360f * (percent / 100f),
                useCenter = false, style = Stroke(stroke, cap = StrokeCap.Round))
        }
        Text("$percent%", fontWeight = FontWeight.Bold, fontSize = 40.sp)
    }
}

@Composable
private fun StatRow(
    icon: ImageVector, iconBg: Color,
    value: String, label: String, changePct: String,
) {
    Card(shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(14.dp)).background(iconBg),
                contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = Color.White)
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(label, color = TextSecondary, fontSize = 13.sp)
            }
            Text(changePct, color = Green, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

