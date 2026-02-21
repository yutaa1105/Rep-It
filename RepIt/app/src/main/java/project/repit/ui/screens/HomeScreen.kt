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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import project.repit.data.model.Challenge
import project.repit.data.model.Series
import project.repit.data.repository.FitRepository
import project.repit.ui.components.*
import project.repit.ui.theme.*

@Composable
fun HomeScreen(navController: NavController) {
    val user         = FitRepository.getUser()
    val weather      = FitRepository.getWeather()
    val steps        = FitRepository.getSteps()
    val daily        = FitRepository.getDailyChallenge()
    val myChallenges = FitRepository.getMyChallenge()
    val series       = FitRepository.getSeries()

    Scaffold(
        bottomBar      = { FitBottomBar(navController) },
        containerColor = BackgroundGray,
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            HomeHeader(
                userName  = user.name.substringBefore(" "),
                level     = user.level,
                weather   = weather.description,
                tempC     = weather.tempCelsius,
                steps     = steps.current,
                stepsGoal = steps.goal,
            )

            Spacer(Modifier.height(20.dp))

            Column(Modifier.padding(horizontal = 16.dp)) {
                SectionHeader(title = "Défi du jour")
                Spacer(Modifier.height(10.dp))
                DailyChallengeCard(challenge = daily)
            }

            Spacer(Modifier.height(24.dp))

            Column(Modifier.padding(horizontal = 16.dp)) {
                SectionHeader(title = "Mes défis", actionLabel = "Voir tout")
                Spacer(Modifier.height(10.dp))
                myChallenges.forEach { challenge ->
                    MyChallengeCard(challenge)
                    Spacer(Modifier.height(10.dp))
                }
            }

            Spacer(Modifier.height(24.dp))

            Column(Modifier.padding(horizontal = 16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Série en cours", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(4.dp))
                    Text("🔥", fontSize = 18.sp)
                }
                Spacer(Modifier.height(10.dp))
                series.firstOrNull()?.let { StreakCard(it) }
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun HomeHeader(
    userName: String, level: Int,
    weather: String, tempC: Int,
    steps: Int, stepsGoal: Int,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.linearGradient(listOf(Color(0xFF7C3AED), Color(0xFF9F67F5))))
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.Top,
            ) {
                Column {
                    Text("Bonjour, $userName !", color = Color.White,
                        fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("Prêt pour ton défi du jour ?",
                        color = Color.White.copy(.8f), fontSize = 14.sp)
                }
                AvatarCircle(initial = userName.first().toString(), level = level)
            }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("☀️ ", fontSize = 20.sp)
                    Column {
                        Text(weather, color = Color.White, fontWeight = FontWeight.SemiBold)
                        Text("${tempC}°C", color = Color.White.copy(.85f), fontSize = 13.sp)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("👣 ", fontSize = 20.sp)
                    Column(horizontalAlignment = Alignment.End) {
                        Text("${"%,d".format(steps)} / ${"%,d".format(stepsGoal)} pas",
                            color = Color.White, fontWeight = FontWeight.SemiBold)
                        LinearProgressIndicator(
                            progress  = { steps.toFloat() / stepsGoal },
                            modifier  = Modifier.width(100.dp).height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color     = Green,
                            trackColor = Color.White.copy(.3f),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DailyChallengeCard(challenge: Challenge) {
    Card(
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier  = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp))
                            .background(PurpleLight),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Filled.DirectionsRun, contentDescription = null, tint = Purple)
                    }
                    Column {
                        Text(challenge.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("${challenge.durationMin} min · ${challenge.subtitle}",
                            color = TextSecondary, fontSize = 13.sp)
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Difficulté", fontSize = 11.sp, color = TextSecondary)
                    StarsRow(filled = challenge.difficultyStars)
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Surface(shape = RoundedCornerShape(20.dp), color = Green) {
                    Text("Recommandé", color = Color.White,
                        fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp))
                }
                Button(
                    onClick = {},
                    shape  = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Green),
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Démarrer")
                }
            }
        }
    }
}

@Composable
private fun MyChallengeCard(challenge: Challenge) {
    Card(
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier  = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp))
                    .background(OrangeLight),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Filled.FitnessCenter, contentDescription = null, tint = Orange)
            }
            Column(Modifier.weight(1f)) {
                Text(challenge.title, fontWeight = FontWeight.SemiBold)
                Text("${challenge.durationMin} min · ${challenge.subtitle}",
                    color = TextSecondary, fontSize = 13.sp)
            }
            Text("${challenge.progressPercent}%", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
private fun StreakCard(series: Series) {
    Card(
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier  = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Column {
                    Text("jour ${series.currentDay}/${series.totalDays}",
                        fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Challenge décembre", color = TextSecondary, fontSize = 13.sp)
                }
                Surface(shape = RoundedCornerShape(20.dp), color = Orange) {
                    Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Text("🔥", fontSize = 14.sp)
                        Spacer(Modifier.width(4.dp))
                        Text("${series.streakDays} jours",
                            color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            Text("progression", color = TextSecondary, fontSize = 13.sp)
            Spacer(Modifier.height(4.dp))
            LinearProgressIndicator(
                progress   = { series.progressPercent / 100f },
                modifier   = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color      = Orange,
                trackColor = Color(0xFFE5E7EB),
            )
            Spacer(Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround) {
                StatChip(value = "${series.challengesCompleted}", label = "Défis réussis")
                StatChip(value = "18", label = "Objectifs atteints")
                StatChip(value = "92%", label = "Taux de réussite")
            }
        }
    }
}

