package com.momora.ai.presentation.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.momora.ai.core.components.*
import com.momora.ai.core.theme.MomoraColors
import java.time.LocalTime

/**
 * Home Screen — "Ask Momora" dashboard.
 *
 * Matches the HTML mockup's bento-grid layout with:
 * - Greeting with gradient text
 * - Premium search bar
 * - Quick Actions 2x2 grid
 * - Daily AI Summary card
 * - Upcoming Deadlines card
 * - Pending Promises card
 */
@Composable
fun HomeScreen(
    onNavigateToChat: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        // Greeting Section
        GreetingSection()

        // Search / Ask Momora Bar
        SearchBar(onNavigateToChat = onNavigateToChat)

        // Quick Actions Grid
        QuickActionsSection()

        // Daily AI Summary Card
        DailyAISummaryCard()

        // Smart Cards Row
        UpcomingDeadlinesCard()

        // Pending Promises
        PendingPromisesCard()

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun GreetingSection() {
    val greeting = remember {
        when (LocalTime.now().hour) {
            in 5..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            else -> "Good Night"
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$greeting ",
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 36.sp),
                color = MaterialTheme.colorScheme.onSurface,
            )
            GradientText(
                text = "Dharma 👋",
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 36.sp),
            )
        }
        Text(
            text = "Here is your daily overview.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun SearchBar(onNavigateToChat: () -> Unit) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onNavigateToChat,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            val infiniteTransition = rememberInfiniteTransition(label = "sparkle")
            val rotation by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(3000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "sparkleRotation"
            )

            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.rotate(rotation),
            )

            Text(
                text = "Ask Momora anything...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.weight(1f),
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .padding(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Mic,
                    contentDescription = "Voice input",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

@Composable
private fun QuickActionsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        val actions = listOf(
            QuickAction("Upload PDF", Icons.Outlined.UploadFile, MomoraColors.Primary),
            QuickAction("Scan Notice", Icons.Outlined.DocumentScanner, MomoraColors.Secondary),
            QuickAction("Import Chat", Icons.Outlined.Forum, MomoraColors.Tertiary),
            QuickAction("Voice Note", Icons.Outlined.Mic, MomoraColors.PrimaryContainer),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            actions.forEach { action ->
                QuickActionButton(
                    action = action,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

private data class QuickAction(
    val label: String,
    val icon: ImageVector,
    val tint: Color,
)

@Composable
private fun QuickActionButton(
    action: QuickAction,
    modifier: Modifier = Modifier,
) {
    GlassCard(
        modifier = modifier,
        onClick = { /* TODO */ },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            IconCircle {
                Icon(
                    imageVector = action.icon,
                    contentDescription = action.label,
                    tint = action.tint,
                    modifier = Modifier.size(20.dp),
                )
            }
            Text(
                text = action.label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun DailyAISummaryCard() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Daily AI Summary",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.fillMaxWidth()) {
                // Background sparkle icon (subtle)
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 16.dp, y = (-16).dp),
                )

                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    // Insights Active badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Insights,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(18.dp),
                        )
                        Text(
                            text = "INSIGHTS ACTIVE",
                            style = MaterialTheme.typography.labelMedium.copy(
                                letterSpacing = 2.sp,
                                fontWeight = FontWeight.Medium,
                            ),
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }

                    // Main summary text
                    Text(
                        text = "Today you have 2 Assignments due and 3 meetings scheduled.",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    // Detail text
                    Text(
                        text = "Momora has pre-drafted responses for your morning emails and organized the PDF documents you uploaded last night.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Spacer(Modifier.height(8.dp))

                    // Review Drafts button
                    GlowButton(
                        text = "Review Drafts",
                        onClick = { /* TODO */ },
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun UpcomingDeadlinesCard() {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header with divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Event,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(22.dp),
                    )
                    Text(
                        text = "Upcoming Deadlines",
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                StatusBadge(text = "2 Urgent", color = MaterialTheme.colorScheme.error)
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = Color.White.copy(alpha = 0.05f),
            )

            // Deadline items
            DeadlineItem(
                title = "Submit Q3 Report",
                timeLabel = "Today, 5:00 PM",
                isUrgent = true,
            )
            Spacer(Modifier.height(4.dp))
            DeadlineItem(
                title = "Review Contract V2",
                timeLabel = "Tomorrow, 10:00 AM",
                isUrgent = false,
            )
        }
    }
}

@Composable
private fun DeadlineItem(
    title: String,
    timeLabel: String,
    isUrgent: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { /* TODO */ }
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = timeLabel,
                style = MaterialTheme.typography.labelMedium,
                color = if (isUrgent) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "View",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp),
        )
    }
}

@Composable
private fun PendingPromisesCard() {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Handshake,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(22.dp),
                    )
                    Text(
                        text = "Pending Promises",
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                StatusBadge(text = "3 Active", color = MaterialTheme.colorScheme.tertiary)
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = Color.White.copy(alpha = 0.05f),
            )

            PromiseItem(
                quote = "\"I'll send the files by EOD\"",
                recipient = "To: Sarah Jenkins",
            )
            Spacer(Modifier.height(4.dp))
            PromiseItem(
                quote = "\"Follow up on pricing\"",
                recipient = "To: Vendor Team",
            )
        }
    }
}

@Composable
private fun PromiseItem(quote: String, recipient: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { /* TODO */ }
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = quote,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = recipient,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "View",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp),
        )
    }
}
