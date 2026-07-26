package com.momora.ai.presentation.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.momora.ai.core.components.GlassCard
import com.momora.ai.core.components.GlassPanel
import com.momora.ai.core.theme.MomoraColors

/**
 * Chat Screen — "Ask Momora" AI conversation interface.
 *
 * Matches the HTML mockup's chat UI with:
 * - Suggested question chips
 * - User and AI message bubbles
 * - Source citation cards with confidence %
 * - Premium input bar with mic + attach + send
 * - Typing indicator animation
 */
@Composable
fun ChatScreen() {
    val messages = remember { sampleMessages }
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        // Chat Messages
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Welcome message
            item {
                WelcomeSection()
            }

            // Suggested questions
            item {
                SuggestedQuestions()
            }

            // Messages
            items(messages) { message ->
                when (message) {
                    is ChatMessage.User -> UserMessageBubble(message)
                    is ChatMessage.AI -> AIMessageBubble(message)
                }
            }
        }

        // Input Bar
        ChatInputBar(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}

@Composable
private fun WelcomeSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // AI Avatar with pulse
        Box(contentAlignment = Alignment.Center) {
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val pulseScale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 1.3f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = EaseInOut),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulseScale"
            )
            val pulseAlpha by infiniteTransition.animateFloat(
                initialValue = 0.4f,
                targetValue = 0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = EaseInOut),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulseAlpha"
            )

            // Pulse ring
            Box(
                modifier = Modifier
                    .size((48 * pulseScale).dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = pulseAlpha))
            )

            // Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MomoraColors.SurfaceContainerHigh),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = "Momora AI",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp),
                )
            }
        }

        Text(
            text = "Ask Momora",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = "Your personal AI memory assistant",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun SuggestedQuestions() {
    val suggestions = listOf(
        "📚 Any pending assignments?",
        "📅 What's my schedule today?",
        "💬 What did Rahul say about the project?",
        "📄 Summarize the last PDF I uploaded",
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(suggestions) { suggestion ->
            SuggestionChip(text = suggestion)
        }
    }
}

@Composable
private fun SuggestionChip(text: String) {
    Surface(
        modifier = Modifier.clickable { /* TODO */ },
        shape = RoundedCornerShape(50),
        color = MomoraColors.SurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, MomoraColors.GlassBorder),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
        )
    }
}

@Composable
private fun UserMessageBubble(message: ChatMessage.User) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
            border = androidx.compose.foundation.BorderStroke(
                1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            ),
        ) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .widthIn(max = 280.dp),
            )
        }
    }
}

@Composable
private fun AIMessageBubble(message: ChatMessage.AI) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // AI Avatar + Name
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(MomoraColors.SurfaceContainerHigh),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(14.dp),
                )
            }
            Text(
                text = "Momora",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        // Response text
        GlassPanel {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(16.dp),
            )
        }

        // Source citations
        if (message.sources.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "SOURCES",
                    style = MaterialTheme.typography.labelMedium.copy(
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Medium,
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                )
                message.sources.forEach { source ->
                    SourceCitationCard(source)
                }
            }
        }
    }
}

@Composable
private fun SourceCitationCard(source: SourceCitation) {
    val sourceColor = when (source.type) {
        "WhatsApp" -> MomoraColors.Tertiary
        "Email" -> MomoraColors.Secondary
        "PDF" -> MomoraColors.Primary
        "Faculty" -> MomoraColors.WarningOrange
        else -> MomoraColors.Outline
    }

    val sourceIcon = when (source.type) {
        "WhatsApp" -> Icons.Filled.Forum
        "Email" -> Icons.Filled.Email
        "PDF" -> Icons.Filled.Description
        "Faculty" -> Icons.Filled.School
        else -> Icons.Filled.Source
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = sourceColor.copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(1.dp, sourceColor.copy(alpha = 0.2f)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Icon(
                    imageVector = sourceIcon,
                    contentDescription = null,
                    tint = sourceColor,
                    modifier = Modifier.size(18.dp),
                )
                Column {
                    Text(
                        text = source.type,
                        style = MaterialTheme.typography.labelLarge,
                        color = sourceColor,
                    )
                    if (source.detail.isNotBlank()) {
                        Text(
                            text = source.detail,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            // Confidence badge
            Surface(
                shape = RoundedCornerShape(50),
                color = sourceColor.copy(alpha = 0.15f),
            ) {
                Text(
                    text = "${source.confidence}%",
                    style = MaterialTheme.typography.labelMedium,
                    color = sourceColor,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                )
            }
        }
    }
}

@Composable
private fun ChatInputBar(modifier: Modifier = Modifier) {
    var inputText by remember { mutableStateOf("") }

    GlassCard(
        modifier = modifier.fillMaxWidth(),
        cornerRadius = 28.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            // Attachment button
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    imageVector = Icons.Outlined.AttachFile,
                    contentDescription = "Attach",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            // Text input
            BasicTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    Box {
                        if (inputText.isEmpty()) {
                            Text(
                                text = "Message Momora...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            )
                        }
                        innerTextField()
                    }
                },
            )

            // Mic button
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    imageVector = Icons.Outlined.Mic,
                    contentDescription = "Voice input",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            // Send button
            IconButton(
                onClick = { /* TODO: Send message */ inputText = "" },
                enabled = inputText.isNotBlank(),
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            if (inputText.isNotBlank()) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = if (inputText.isNotBlank()) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }
    }
}

// --- Data Models ---

sealed class ChatMessage {
    data class User(val text: String) : ChatMessage()
    data class AI(
        val text: String,
        val sources: List<SourceCitation> = emptyList(),
    ) : ChatMessage()
}

data class SourceCitation(
    val type: String,
    val detail: String,
    val confidence: Int,
)

// --- Sample Data ---

private val sampleMessages = listOf(
    ChatMessage.User("Did my professor postpone the assignment deadline?"),
    ChatMessage.AI(
        text = "Yes! Based on your data, the assignment deadline has been updated.\n\n" +
                "• Original deadline: Aug 10 (from email)\n" +
                "• Extended to: Aug 12 (WhatsApp group)\n" +
                "• Final submission: Aug 13 (Faculty announcement)\n\n" +
                "The most recent and authoritative source is the Faculty announcement on Aug 11.",
        sources = listOf(
            SourceCitation("Faculty", "Aug 11 announcement", 98),
            SourceCitation("WhatsApp", "CS-301 Group Chat", 85),
            SourceCitation("Email", "dept.circular@univ.edu", 72),
        ),
    ),
    ChatMessage.User("What did Rahul mention about the Goa trip?"),
    ChatMessage.AI(
        text = "Rahul mentioned the Goa trip in 3 conversations:\n\n" +
                "1. June 14: \"Let's plan for Goa in December\"\n" +
                "2. July 2: \"I found cheap flights for Dec 20-25\"\n" +
                "3. July 18: \"Booking the Airbnb tomorrow, need ₹3000 from everyone\"\n\n" +
                "It looks like you may need to send ₹3000 to Rahul for the accommodation booking.",
        sources = listOf(
            SourceCitation("WhatsApp", "Chat with Rahul", 95),
        ),
    ),
)
