package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * A highly creative Jetpack Compose graphic renderer component.
 * Renders multiple vector background grids, geometric accent motifs, glow rings, and dynamic alignments.
 */
@Composable
fun GraphicsPreviewCanvas(
    designType: String,
    businessName: String,
    tagline: String,
    styleSelected: String,
    colors: List<Color>,
    iconType: String,
    scale: Float,
    rotation: Float,
    gradAngle: Float,
    modifier: Modifier = Modifier
) {
    // Elegant pulsing animation for the glow rings and elements
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.2f)
            .clip(RoundedCornerShape(24.dp))
            .background(SlateBlack)
            .border(2.dp, SlateCard, RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Main Generative Vector Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val center = Offset(width / 2f, height / 2f)

            // 1. BACKGROUND DRAWING
            when (styleSelected) {
                "Neon Cyberpunk" -> {
                    // Futuristic grid lines
                    val brushColors = if (colors.size >= 2) colors.take(2) else listOf(Color(0xFF8A00FF), Color(0xFFFF007F))
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(SlateBlack, Color(0xFF070014)),
                            start = Offset(0f, 0f),
                            end = Offset(width, height)
                        )
                    )

                    // Technical grid
                    val step = 40f
                    for (x in 0..(width / step).toInt()) {
                        drawLine(
                            color = brushColors[0].copy(alpha = 0.12f),
                            start = Offset(x * step, 0f),
                            end = Offset(x * step, height),
                            strokeWidth = 1f
                        )
                    }
                    for (y in 0..(height / step).toInt()) {
                        drawLine(
                            color = brushColors[1].copy(alpha = 0.12f),
                            start = Offset(0f, y * step),
                            end = Offset(width, y * step),
                            strokeWidth = 1f
                        )
                    }

                    // Cyberpunk diagonal accent stripes
                    drawLine(
                        color = brushColors[0].copy(alpha = 0.4f),
                        start = Offset(width - 50f, 0f),
                        end = Offset(width, 50f),
                        strokeWidth = 6f
                    )
                    drawLine(
                        color = brushColors[1].copy(alpha = 0.4f),
                        start = Offset(width - 40f, 0f),
                        end = Offset(width, 40f),
                        strokeWidth = 2f
                    )

                    // Pulsing cyber rings
                    drawCircle(
                        color = brushColors[1].copy(alpha = 0.2f),
                        radius = (height * 0.32f) * pulseScale,
                        center = center,
                        style = Stroke(width = 3f)
                    )
                    drawCircle(
                        color = brushColors[0].copy(alpha = 0.3f),
                        radius = (height * 0.28f),
                        center = center,
                        style = Stroke(width = 1.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f))
                    )
                }

                "Minimalist Slate" -> {
                    // Fine luxury canvas dark slate background with fine border frame
                    drawRect(color = SlateDark)
                    
                    // Interior micro frame
                    val borderPadding = 30f
                    drawRect(
                        color = colors.firstOrNull()?.copy(alpha = 0.4f) ?: GlowAmber.copy(alpha = 0.4f),
                        style = Stroke(width = 2f),
                        size = Size(width - (borderPadding * 2), height - (borderPadding * 2)),
                        topLeft = Offset(borderPadding, borderPadding)
                    )

                    // Delicate fine geometry circles in corners
                    drawCircle(
                        color = (colors.firstOrNull() ?: GlowAmber).copy(alpha = 0.2f),
                        radius = 20f,
                        center = Offset(borderPadding + 40f, borderPadding + 40f),
                        style = Stroke(width = 1f)
                    )
                    drawCircle(
                        color = (colors.firstOrNull() ?: GlowAmber).copy(alpha = 0.2f),
                        radius = 20f,
                        center = Offset(width - borderPadding - 40f, height - borderPadding - 40f),
                        style = Stroke(width = 1f)
                    )
                }

                "Royal Retro" -> {
                    // Radial gold-burst effect simulation, deep velvet burgundy gold
                    val bgGold = colors.firstOrNull() ?: GlowAmber
                    val bgSecondary = colors.getOrNull(1) ?: CreativeOrange

                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFF2C050B), SlateBlack),
                            center = center,
                            radius = height * 0.7f
                        )
                    )

                    // Classic retro seals / burst rays
                    val rayCount = 16
                    for (i in 0 until rayCount) {
                        val angle = (i * (360f / rayCount) + shimmerOffset / 4f)
                        rotate(angle, center) {
                            val path = Path().apply {
                                moveTo(center.x, center.y)
                                lineTo(center.x - 40f, 0f)
                                lineTo(center.x + 40f, 0f)
                                close()
                            }
                            drawPath(path, color = bgGold.copy(alpha = 0.05f))
                        }
                    }

                    // Intricate golden retro badge rings
                    drawCircle(
                        color = bgGold.copy(alpha = 0.35f),
                        radius = height * 0.35f,
                        center = center,
                        style = Stroke(width = 4f)
                    )
                    drawCircle(
                        color = bgSecondary.copy(alpha = 0.25f),
                        radius = height * 0.32f,
                        center = center,
                        style = Stroke(width = 1.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f))
                    )
                }

                "Modern Brutalist" -> {
                    // Heavy contrast brutalist color cards & stripes
                    val primaryBrutal = colors.firstOrNull() ?: CreativeOrange
                    val secondaryBrutal = colors.getOrNull(1) ?: ElectricBlue

                    drawRect(color = Color(0xFFF0EFEB)) // Stark cream light background for brutalism

                    // Bold black grid pattern (brutalist style likes harsh raw black coordinates)
                    val brutStep = 60f
                    for (x in 0..(width / brutStep).toInt()) {
                        drawLine(Color.Black.copy(alpha = 0.07f), Offset(x * brutStep, 0f), Offset(x * brutStep, height), 2f)
                    }

                    // Solid brutal shape blocks with thick black borders (drawn offset)
                    val offsetVal = 10f
                    val rWidth = width * 0.5f
                    val rHeight = height * 0.38f
                    val rectTopLeft = Offset(center.x - rWidth / 2f, center.y - rHeight / 2f)

                    // Brutalist Background solid offset shadow
                    drawRoundRect(
                        color = Color.Black,
                        topLeft = rectTopLeft + Offset(offsetVal, offsetVal),
                        size = Size(rWidth, rHeight),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                    // High-contrast brutalist fill card
                    drawRoundRect(
                        color = primaryBrutal,
                        topLeft = rectTopLeft,
                        size = Size(rWidth, rHeight),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                    drawRoundRect(
                        color = Color.Black,
                        topLeft = rectTopLeft,
                        size = Size(rWidth, rHeight),
                        cornerRadius = CornerRadius(4f, 4f),
                        style = Stroke(width = 4f)
                    )

                    // Sharp warning stripes block
                    val stripePath = Path().apply {
                        moveTo(0f, height - 30f)
                        lineTo(30f, height)
                        lineTo(0f, height)
                        close()
                    }
                    drawPath(stripePath, Color.Black)
                }

                else -> { // "Playful Pop"
                    // Warm candy gradients with circles
                    val pColor = colors.firstOrNull() ?: ElectricBlue
                    val sColor = colors.getOrNull(1) ?: CreativeOrange

                    drawRect(
                        brush = Brush.sweepGradient(
                            colors = listOf(SlateBlack, Color(0xFF151932), SlateBlack),
                            center = center
                        )
                    )

                    // Polka dots
                    val spacing = 50f
                    for (x in 20..(width).toInt() step spacing.toInt()) {
                        for (y in 20..(height).toInt() step spacing.toInt()) {
                            drawCircle(
                                color = pColor.copy(alpha = 0.05f),
                                radius = 3f,
                                center = Offset(x.toFloat(), y.toFloat())
                            )
                        }
                    }

                    // Floating bouncy bubbly shapes
                    drawCircle(
                        color = sColor.copy(alpha = 0.2f),
                        radius = 80f * pulseScale,
                        center = Offset(width * 0.2f, height * 0.3f)
                    )
                    drawCircle(
                        color = pColor.copy(alpha = 0.15f),
                        radius = 60f / pulseScale,
                        center = Offset(width * 0.8f, height * 0.7f)
                    )
                }
            }


            // 2. EMBLEM OR CORE GRAPHIC SYMBOL DRAWING (Based on user selection)
            val emblemColor = colors.firstOrNull() ?: CreativeOrange
            val subColor = colors.getOrNull(1) ?: GlowAmber

            withTransform({
                // Adjust translation offset, rotation and scaling factors
                translate(center.x, center.y - (if (designType == "Logo Emblem") 30f else 60f))
                scale(scale * pulseScale, scale * pulseScale, Offset.Zero)
                rotate(rotation)
            }) {
                when (iconType) {
                    "Brush" -> {
                        // Drawing custom modern vector brush design geometry
                        val pathBrush = Path().apply {
                            // Brushes crossing
                            moveTo(-20f, 40f)
                            lineTo(20f, -40f)
                            lineTo(40f, -30f)
                            lineTo(0f, 50f)
                            close()
                        }
                        drawPath(pathBrush, color = emblemColor)
                        // Brushes tip bristles
                        val bristles = Path().apply {
                            moveTo(20f, -40f)
                            cubicTo(25f, -55f, 35f, -55f, 40f, -30f)
                            close()
                        }
                        drawPath(bristles, color = subColor)
                        // Metallic ring band
                        drawLine(Color.White, Offset(5f, -10f), Offset(20f, -5f), strokeWidth = 5f)
                    }

                    "Palette" -> {
                        // Artistic oil palette plate with micro paint wells
                        val palettePath = Path().apply {
                            moveTo(0f, -40f)
                            cubicTo(50f, -40f, 50f, 30f, 20f, 40f)
                            cubicTo(-10f, 45f, -15f, 15f, -25f, 10f)
                            cubicTo(-40f, 5f, -30f, -40f, 0f, -40f)
                            close()
                        }
                        drawPath(palettePath, color = emblemColor)

                        // Thumbhole
                        drawCircle(color = SlateBlack, radius = 9f, center = Offset(-12f, -5f))

                        // Paint Wells (different colorful paint spots)
                        drawCircle(color = Color(0xFFEF4444), radius = 6f, center = Offset(18f, -15f))
                        drawCircle(color = Color(0xFF10B981), radius = 6f, center = Offset(25f, 8f))
                        drawCircle(color = Color(0xFF00B4D8), radius = 6f, center = Offset(5f, 24f))
                        drawCircle(color = Color(0xFFFFB703), radius = 6f, center = Offset(-18f, -18f))
                    }

                    "Crown" -> {
                        // Royal design crown
                        val crownPath = Path().apply {
                            moveTo(-35f, 25f)
                            lineTo(-45f, -15f)
                            lineTo(-20f, 5f)
                            lineTo(0f, -30f)
                            lineTo(20f, 5f)
                            lineTo(45f, -15f)
                            lineTo(35f, 25f)
                            close()
                        }
                        drawPath(crownPath, color = emblemColor)
                        // Base ribbon decoration
                        drawRect(
                            color = subColor,
                            topLeft = Offset(-30f, 20f),
                            size = Size(60f, 6f)
                        )
                        // Crown gems
                        drawCircle(Color.White, 3f, Offset(-45f, -15f))
                        drawCircle(Color.White, 3.5f, Offset(0f, -30f))
                        drawCircle(Color.White, 3f, Offset(45f, -15f))
                    }

                    "Sparkles" -> {
                        // Creative sparkles star cluster
                        for (i in 0 until 3) {
                            val offsetS = when (i) {
                                1 -> Offset(-35f, 20f)
                                2 -> Offset(30f, -25f)
                                else -> Offset(0f, -5f)
                            }
                            val starSize = when (i) {
                                1 -> 15f
                                2 -> 12f
                                else -> 30f
                            }
                            val starCol = if (i == 0) emblemColor else subColor

                            val sparkPath = Path().apply {
                                moveTo(offsetS.x, offsetS.y - starSize)
                                cubicTo(offsetS.x + 2f, offsetS.y - 2f, offsetS.x + starSize, offsetS.y, offsetS.x + starSize, offsetS.y)
                                cubicTo(offsetS.x + 2f, offsetS.y + 2f, offsetS.x, offsetS.y + starSize, offsetS.x, offsetS.y + starSize)
                                cubicTo(offsetS.x - 2f, offsetS.y + 2f, offsetS.x - starSize, offsetS.y, offsetS.x - starSize, offsetS.y)
                                cubicTo(offsetS.x - 2f, offsetS.y - 2f, offsetS.x, offsetS.y - starSize, offsetS.x, offsetS.y - starSize)
                                close()
                            }
                            drawPath(sparkPath, color = starCol)
                        }
                    }

                    "Rocket" -> {
                        // Bold vector rocket ascending (motivational design branding)
                        // Rocket body
                        val rPath = Path().apply {
                            moveTo(0f, -40f)
                            cubicTo(15f, -15f, 15f, 15f, 12f, 30f)
                            lineTo(-12f, 30f)
                            cubicTo(-15f, 15f, -15f, -15f, 0f, -40f)
                            close()
                        }
                        drawPath(rPath, color = emblemColor)
                        // Wings
                        val wingL = Path().apply {
                            moveTo(-12f, 15f)
                            lineTo(-26f, 30f)
                            lineTo(-12f, 30f)
                            close()
                        }
                        drawPath(wingL, color = subColor)
                        val wingR = Path().apply {
                            moveTo(12f, 15f)
                            lineTo(26f, 30f)
                            lineTo(12f, 30f)
                            close()
                        }
                        drawPath(wingR, color = subColor)
                        // Fire thrusters
                        val fire = Path().apply {
                            moveTo(-6f, 31f)
                            lineTo(0f, 48f)
                            lineTo(6f, 31f)
                            close()
                        }
                        drawPath(fire, color = CreativeOrange)
                        // Window bubble
                        drawCircle(Color.White, 6f, Offset(0f, -5f))
                    }

                    else -> { // "Diamond" (Branding geometric prism)
                        val prism = Path().apply {
                            moveTo(0f, -32f)
                            lineTo(32f, -10f)
                            lineTo(0f, 32f)
                            lineTo(-32f, -10f)
                            close()
                        }
                        drawPath(prism, color = emblemColor)

                        // Facet lines
                        drawLine(subColor.copy(alpha = 0.8f), Offset(-32f, -10f), Offset(32f, -10f), strokeWidth = 1.5f)
                        drawLine(Color.White, Offset(0f, -32f), Offset(0f, 32f), strokeWidth = 1.5f)
                    }
                }
            }
        }

        // 3. BRAND TYPOGRAPHY TEXT LAYERS (Superimposed dynamically with perfect text wrapping and padding layout)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val primaryTextColor = when (styleSelected) {
                "Modern Brutalist" -> Color.Black
                else -> CleanWhite
            }

            val mutedTextColor = when (styleSelected) {
                "Modern Brutalist" -> Color.Black.copy(alpha = 0.7f)
                else -> TextMuted
            }

            // Brand/Business Name with majestic styling suitable to chosen theme
            Text(
                text = businessName.uppercase(),
                color = primaryTextColor,
                fontSize = if (businessName.length > 20) 14.sp else 18.sp,
                fontFamily = when (styleSelected) {
                    "Minimalist Slate" -> FontFamily.SansSerif
                    "Royal Retro" -> FontFamily.Serif
                    "Modern Brutalist" -> FontFamily.Monospace
                    else -> FontFamily.Default
                },
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = when (styleSelected) {
                    "Minimalist Slate" -> 4.sp
                    "Royal Retro" -> 1.5.sp
                    else -> 0.5.sp
                },
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Subtitle tagline
            Text(
                text = tagline,
                color = mutedTextColor,
                fontSize = 11.sp,
                fontFamily = when (styleSelected) {
                    "Minimalist Slate" -> FontFamily.SansSerif
                    "Modern Brutalist" -> FontFamily.Monospace
                    else -> FontFamily.Default
                },
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Category tag overlay (e.g. "DESIGN SPEC: LOGO") shown beautifully at top corner
            Spacer(modifier = Modifier.height(6.dp))
        }

        // Small tag showing active design template specifications
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .background(
                    if (styleSelected == "Modern Brutalist") Color.Black else SlateBlack.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    1.dp,
                    if (styleSelected == "Modern Brutalist") Color.Black else colors.firstOrNull()?.copy(alpha = 0.4f) ?: SlateCard,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = designType.uppercase(),
                color = if (styleSelected == "Modern Brutalist") Color.White else (colors.firstOrNull() ?: GlowAmber),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}
