package com.momora.ai.presentation.timeline

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.momora.ai.core.components.GlassPanel
import com.momora.ai.core.theme.MomoraColors

/**
 * Timeline Screen — Chronological memory log.
 *
 * Matches the HTML mockup with:
 * - Filter chips (All Events, Academic, Personal, Work, Finance)
 * - Vertical timeline line with color-coded event nodes
 * - Date separators
 * - Event cards (Assignment, Meeting, Promise)
 * - Ping animation on critical events
 */
@Composable
fun TimelineScreen() {
    val scrollState = rememberScrollState()
    var selectedFilter by remember { mutableStateOf("All Events") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        // Header
        Column(
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Timeline",
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 40.sp),
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "Chronological overview of events, commitments, and system logs.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(Modifier.height(8.dp))

            // Filter Chips
            FilterChipsRow(
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it },
            )
        }

        Spacer(Modifier.height(16.dp))

        // Timeline Content
        Box(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
        ) {
            // Vertical timeline line
            Box(
                modifier = Modifier
                    .padding(start = 48.dp)
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(Color.White.copy(alpha = 0.05f))
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                // Today
                DateSeparator("Today, Oct 24")

                // Critical Assignment
                TimelineItem(
                    time = "10:00 AM",
                    nodeColor = MaterialTheme.colorScheme.error,
                    hasPing = true,
                    category = "Assignment",
                    categoryIcon = Icons.Filled.Assignment,
                    categoryColor = MaterialTheme.colorScheme.error,
                    title = "Advanced Systems Architecture Final",
                    description = "Submit final project repository and documentation. Ensure all CI/CD pipelines are green before merging to main.",
                    tags = listOf(
                        Tag("Academic", Color.White.copy(alpha = 0.7f)),
                        Tag("Critical Priority", MaterialTheme.colorScheme.error),
                    ),
                    rightLabel = "Due in 2h",
                )

                // Meeting
                TimelineItem(
                    time = "1:30 PM",
                    nodeColor = MaterialTheme.colorScheme.secondary,
                    hasPing = false,
                    category = "Meeting",
                    categoryIcon = Icons.Filled.Groups,
                    categoryColor = MaterialTheme.colorScheme.secondary,
                    title = "Weekly Sync: Alpha Team",
                    description = "Review Q3 objectives and discuss resource allocation for upcoming sprint.",
                    tags = emptyList(),
                    rightLabel = "1h duration",
                    showAvatars = true,
                    showActionButton = true,
                )

                Spacer(Modifier.height(24.dp))

                // Tomorrow
                DateSeparator("Tomorrow, Oct 25")

                // Promise
                TimelineItem(
                    time = "9:00 AM",
                    nodeColor = MomoraColors.WarningOrange,
                    hasPing = false,
                    category = "Promise",
                    categoryIcon = Icons.Filled.Handshake,
                    categoryColor = MomoraColors.WarningOrange,
                    title = "Call Mom for Birthday",
                    description = "Don't forget about the time zone difference.",
                    tags = listOf(Tag("Personal", Color.White.copy(alpha = 0.7f))),
                    rightLabel = "",
                    hasAccentBorder = true,
                    accentColor = MomoraColors.WarningOrange,
                    itemAlpha = 0.8f,
                )

                Spacer(Modifier.height(32.dp))

                // End marker
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                            .background(MaterialTheme.colorScheme.background)
                    )
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun FilterChipsRow(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
) {
    val filters = listOf("All Events", "Academic", "Personal", "Work", "Finance")

    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        filters.forEach { filter ->
            val isSelected = filter == selectedFilter
            Surface(
                modifier = Modifier.clickable { onFilterSelected(filter) },
                shape = RoundedCornerShape(50),
                color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                else MomoraColors.SurfaceVariant,
                border = if (isSelected) {
                    androidx.compose.foundation.BorderStroke(
                        1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    )
                } else {
                    androidx.compose.foundation.BorderStroke(1.dp, MomoraColors.GlassBorder)
                },
            ) {
                Text(
                    text = filter,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun DateSeparator(label: String) {
    Row(
        modifier = Modifier.padding(vertical = 12.dp),
    ) {
        Surface(
            shape = RoundedCornerShape(50),
            color = MomoraColors.SurfaceContainer,
            border = androidx.compose.foundation.BorderStroke(1.dp, MomoraColors.GlassBorder),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            )
        }
    }
}

data class Tag(val text: String, val color: Color)

@Composable
private fun TimelineItem(
    time: String,
    nodeColor: Color,
    hasPing: Boolean,
    category: String,
    categoryIcon: ImageVector,
    categoryColor: Color,
    title: String,
    description: String,
    tags: List<Tag>,
    rightLabel: String,
    showAvatars: Boolean = false,
    showActionButton: Boolean = false,
    hasAccentBorder: Boolean = false,
    accentColor: Color = Color.Transparent,
    itemAlpha: Float = 1f,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(itemAlpha)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Node column
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(64.dp),
        ) {
            // Node circle with optional ping
            Box(contentAlignment = Alignment.Center) {
                if (hasPing) {
                    val infiniteTransition = rememberInfiniteTransition(label = "ping")
                    val pingScale by infiniteTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = 1.5f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1500, easing = EaseOut),
                            repeatMode = RepeatMode.Restart
                        ),
                        label = "pingScale"
                    )
                    val pingAlpha by infiniteTransition.animateFloat(
                        initialValue = 0.3f,
                        targetValue = 0f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1500, easing = EaseOut),
                            repeatMode = RepeatMode.Restart
                        ),
                        label = "pingAlpha"
                    )
                    Box(
                        modifier = Modifier
                            .size((48 * pingScale).dp)
                            .clip(CircleShape)
                            .border(1.dp, nodeColor.copy(alpha = pingAlpha), CircleShape)
                    )
                }

                GlassPanel(cornerRadius = 24.dp) {
                    Box(
                        modifier = Modifier
                            .size(48.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(nodeColor)
                        )
                    }
                }
            }

            // Time label
            Text(
                text = time,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 6.dp),
            )
        }

        // Content Card
        GlassPanel(
            modifier = Modifier
                .weight(1f)
                .then(
                    if (hasAccentBorder) {
                        Modifier.border(
                            width = 1.dp,
                            color = accentColor.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    } else Modifier
                ),
        ) {
            // Optional accent left border overlay
            if (hasAccentBorder) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight()
                        .background(accentColor)
                )
            }

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // Category + right label
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Icon(
                            imageVector = categoryIcon,
                            contentDescription = null,
                            tint = categoryColor,
                            modifier = Modifier.size(16.dp),
                        )
                        Text(
                            text = category.uppercase(),
                            style = MaterialTheme.typography.labelMedium.copy(
                                letterSpacing = 1.5.sp,
                            ),
                            color = categoryColor,
                        )
                    }
                    if (rightLabel.isNotBlank()) {
                        Text(
                            text = rightLabel,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                )

                // Description
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                // Avatars (for meetings)
                if (showAvatars) {
                    Row(modifier = Modifier.padding(top = 4.dp)) {
                        repeat(2) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .offset(x = (-(it * 8)).dp)
                                    .clip(CircleShape)
                                    .border(2.dp, MomoraColors.SurfaceContainer, CircleShape)
                                    .background(MomoraColors.SurfaceVariant)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .offset(x = (-16).dp)
                                .clip(CircleShape)
                                .border(2.dp, MomoraColors.SurfaceContainer, CircleShape)
                                .background(MomoraColors.SurfaceVariant),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "+3",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }

                // Tags
                if (tags.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 4.dp),
                    ) {
                        tags.forEach { tag ->
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = if (tag.color == MaterialTheme.colorScheme.error) {
                                    tag.color.copy(alpha = 0.1f)
                                } else Color.White.copy(alpha = 0.05f),
                                border = if (tag.color == MaterialTheme.colorScheme.error) {
                                    androidx.compose.foundation.BorderStroke(
                                        1.dp, tag.color.copy(alpha = 0.2f)
                                    )
                                } else {
                                    androidx.compose.foundation.BorderStroke(
                                        1.dp, Color.White.copy(alpha = 0.1f)
                                    )
                                },
                            ) {
                                Text(
                                    text = tag.text,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = tag.color,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                )
                            }
                        }
                    }
                }

                // Action button (Join Call)
                if (showActionButton) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .clickable { /* TODO */ },
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.05f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MomoraColors.GlassBorder),
                    ) {
                        Text(
                            text = "Join Call",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(12.dp),
                        )
                    }
                }
            }
        }
    }
}
