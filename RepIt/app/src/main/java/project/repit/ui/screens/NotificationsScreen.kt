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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import project.repit.data.model.AppNotification
import project.repit.data.model.NotificationType
import project.repit.data.repository.FitRepository
import project.repit.ui.components.FitBottomBar
import project.repit.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(navController: NavController) {
    val notifications = FitRepository.getNotifications()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
                actions = {
                    Box {
                        IconButton(onClick = {}) {
                            Icon(Icons.Filled.Notifications, contentDescription = null)
                        }
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape)
                            .background(Color.Red).align(Alignment.TopEnd)
                            .offset(x = (-4).dp, y = 4.dp))
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically) {
                Column {
                    Text("Rappels intelligents", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Basés sur votre activité", color = TextSecondary, fontSize = 13.sp)
                }
                Surface(shape = RoundedCornerShape(20.dp), color = Color(0xFFE5E7EB)) {
                    Text("${notifications.size} nouveaux", color = TextSecondary, fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp))
                }
            }
            Spacer(Modifier.height(16.dp))
            notifications.forEach { notif ->
                NotificationCard(notif)
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun NotificationCard(notif: AppNotification) {
    val (bgColor, iconBg) = when (notif.type) {
        NotificationType.STEPS    -> Color(0xFFEFF6FF) to Color(0xFF3B82F6)
        NotificationType.WEATHER  -> Color(0xFFF0FDF4) to Color(0xFF22C55E)
        NotificationType.CALORIES -> Color(0xFFFFF1F2) to Red
    }

    Card(shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(iconBg),
                    contentAlignment = Alignment.Center) {
                    val icon = when (notif.type) {
                        NotificationType.STEPS    -> Icons.Filled.DirectionsWalk
                        NotificationType.WEATHER  -> Icons.Filled.WbSunny
                        NotificationType.CALORIES -> Icons.Filled.LocalFireDepartment
                    }
                    Icon(icon, contentDescription = null, tint = Color.White)
                }
                Spacer(Modifier.width(12.dp))
                Text(notif.title, fontWeight = FontWeight.Bold, fontSize = 16.sp,
                    modifier = Modifier.weight(1f))
                Text(notif.time, color = TextSecondary, fontSize = 13.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text(notif.body, color = TextSecondary, fontSize = 14.sp,
                fontStyle = FontStyle.Italic, lineHeight = 20.sp)

            if (notif.type == NotificationType.STEPS && notif.actionLabel != null) {
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(onClick = {}, shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                        modifier = Modifier.weight(1f)) {
                        Icon(Icons.Filled.DirectionsWalk, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text(notif.actionLabel)
                    }
                    OutlinedButton(onClick = {}, shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)) {
                        Icon(Icons.Filled.Schedule, contentDescription = null,
                            tint = Color(0xFF3B82F6))
                        Spacer(Modifier.width(4.dp))
                        Text("Rappeler plus tard", color = Color(0xFF3B82F6))
                    }
                }
            }

            if (notif.type == NotificationType.WEATHER && notif.actionLabel != null) {
                Spacer(Modifier.height(12.dp))
                Button(onClick = {}, shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Green),
                    modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Filled.FlashOn, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text(notif.actionLabel)
                }
            }
        }
    }
}

