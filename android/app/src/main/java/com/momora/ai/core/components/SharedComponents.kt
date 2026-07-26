package com.momora.ai.core.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.momora.ai.core.theme.MomoraColors

/**
 * Glassmorphism card matching the HTML mockup's `.glass-card` class.
 * Solid dark card with subtle white border, rounded corners.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(100),
        label = "pressScale"
    )

    Column(
        modifier = modifier
            .scale(scale)
            .clip(shape)
            .background(MomoraColors.CardBackground)
            .border(1.dp, MomoraColors.GlassBorder, shape)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                } else Modifier
            ),
        content = content
    )
}

/**
 * Glass panel with backdrop blur effect simulation.
 * Matches the HTML `.glass-panel` class.
 */
@Composable
fun GlassPanel(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    Column(
        modifier = modifier
            .clip(shape)
            .background(MomoraColors.GlassBackground)
            .border(1.dp, MomoraColors.GlassBorder, shape),
        content = content
    )
}

/**
 * Gradient text matching the HTML `.gradient-text` class.
 * Primary → Secondary gradient (indigo → cyan).
 */
@Composable
fun GradientText(
    text: String,
    style: TextStyle = MaterialTheme.typography.displayLarge,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = style.copy(
            brush = Brush.linearGradient(
                colors = listOf(MomoraColors.GradientStart, MomoraColors.GradientEnd)
            )
        ),
        modifier = modifier,
    )
}

/**
 * Primary CTA button with glow effect.
 * Matches the HTML `.glow-effect` styling.
 */
@Composable
fun GlowButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(100),
        label = "btnScale"
    )

    Row(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = MomoraColors.Primary.copy(alpha = 0.3f),
                spotColor = MomoraColors.Primary.copy(alpha = 0.3f),
            )
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primary)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        if (icon != null) {
            Spacer(Modifier.width(8.dp))
            icon()
        }
    }
}

/**
 * Status badge / chip (e.g., "2 Urgent", "3 Active").
 */
@Composable
fun StatusBadge(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = color,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
    )
}

/**
 * Circular icon container used in Quick Actions.
 */
@Composable
fun IconCircle(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MomoraColors.SurfaceContainer,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center,
        content = content,
    )
}
