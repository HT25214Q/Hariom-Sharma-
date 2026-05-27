package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.DesignRequest
import com.example.ui.GraphicsViewModel
import com.example.ui.components.GraphicsPreviewCanvas
import com.example.ui.theme.*

// Safe color parser to prevent crashes when splitting data or loading templates
fun safeParseColor(hexStr: String, fallback: Color = Color.Gray): Color {
    return try {
        val trimmed = hexStr.trim()
        if (trimmed.isEmpty()) return fallback
        val formatted = if (trimmed.startsWith("#")) trimmed else "#$trimmed"
        Color(android.graphics.Color.parseColor(formatted))
    } catch (e: Exception) {
        fallback
    }
}

// Mock Portfolio Projects
data class PortfolioProject(
    val title: String,
    val category: String,
    val description: String,
    val style: String,
    val iconType: String,
    val hexColors: List<String>,
    val complexity: String,
    val clientName: String,
    val rating: Float
)

val mockProjects = listOf(
    PortfolioProject(
        title = "Ethereal Aura Brand Pack",
        category = "Branding & Typography",
        description = "Chic celestial identity design, cosmic metallic palette with gilded micro-borders, elegant customized packaging templates.",
        style = "Royal Retro",
        iconType = "Crown",
        hexColors = listOf("#FFD700", "#8B0000", "#FFB703"),
        complexity = "Creative Masterpiece",
        clientName = "Ethereal Inc.",
        rating = 4.9f
    ),
    PortfolioProject(
        title = "AstroVibe App HUD",
        category = "UI/UX & Mobile HUD",
        description = "Neon sci-fi dashboard layout, custom user experience interfaces and responsive, cyberpunk circular vector guides.",
        style = "Neon Cyberpunk",
        iconType = "Rocket",
        hexColors = listOf("#00F0FF", "#FF007F", "#3B0066"),
        complexity = "Creative Masterpiece",
        clientName = "AstroVibe Lab",
        rating = 5.0f
    ),
    PortfolioProject(
        title = "Retro Revival Coffee",
        category = "Vector Illustration",
        description = "Warm organic heritage seal featuring vintage custom emblem guides, distressed gold elements, coffee-shop vector graphics.",
        style = "Minimalist Slate",
        iconType = "Palette",
        hexColors = listOf("#FB8500", "#1B1E28", "#E9ECEF"),
        complexity = "Detailed Artistic",
        clientName = "Revival Brews",
        rating = 4.8f
    ),
    PortfolioProject(
        title = "CyberStream Esport Pack",
        category = "Stream Graphics Bundle",
        description = "Stark heavy-border brutalist branding for esports, containing streaming card layouts, vector typography, high intensity neon.",
        style = "Modern Brutalist",
        iconType = "Diamond",
        hexColors = listOf("#10B981", "#EF4444", "#111827"),
        complexity = "Standard Effort",
        clientName = "StreamVortex",
        rating = 4.9f
    ),
    PortfolioProject(
        title = "Purity Clean Cosmetics",
        category = "Logo Design & Identity",
        description = "Soft dynamic pastel geometry representing luxury clean cosmetics, focusing on gentle circular paths and elegant negative space.",
        style = "Playful Pop",
        iconType = "Sparkles",
        hexColors = listOf("#00B4D8", "#F8F9FA", "#FB8500"),
        complexity = "Detailed Artistic",
        clientName = "Purity Skincare",
        rating = 5.0f
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDashboard(
    viewModel: GraphicsViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var activeTab by remember { mutableIntStateOf(0) }

    // Collect App state values
    val requests by viewModel.designRequests.collectAsStateWithLifecycle()
    val savedPalettes by viewModel.savedPalettes.collectAsStateWithLifecycle()
    val uiMessage by viewModel.uiMessage.collectAsStateWithLifecycle()

    // Trigger toast messages when update triggers occur
    LaunchedEffect(uiMessage) {
        uiMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Brush,
                                contentDescription = "Studio Logo",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(22.dp)
                                    .rotate(-15f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            // HTML-inspired badge visual
                            Box(
                                modifier = Modifier
                                    .height(24.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(horizontal = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Portfolio 2026",
                                    color = BoldThemeDeepPurple,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "HARIOM SHARMA",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = (-0.5).sp,
                                lineHeight = 20.sp
                            ),
                            color = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BoldThemeNavbarBg,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Welcome to Hariom Sharma Graphics, pandithariomsharma01@gmail.com", Toast.LENGTH_LONG).show()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "User Hub",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = BoldThemeNavbarBg,
                tonalElevation = 8.dp,
                windowInsets = WindowInsets.navigationBars,
                modifier = Modifier.height(80.dp)
            ) {
                NavigationBarItem(
                    selected = activeTab == 0,
                    onClick = { activeTab = 0 },
                    icon = { Icon(Icons.Filled.PhotoLibrary, "Showcase") },
                    label = { Text("Showcase", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BoldThemeTextDark,
                        selectedTextColor = Color.White,
                        indicatorColor = BoldThemePillBg,
                        unselectedIconColor = BoldThemeMutedText,
                        unselectedTextColor = BoldThemeMutedText
                    )
                )
                NavigationBarItem(
                    selected = activeTab == 1,
                    onClick = { activeTab = 1 },
                    icon = { Icon(Icons.Filled.AutoAwesome, "Sandbox") },
                    label = { Text("Sandbox", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BoldThemeTextDark,
                        selectedTextColor = Color.White,
                        indicatorColor = BoldThemePillBg,
                        unselectedIconColor = BoldThemeMutedText,
                        unselectedTextColor = BoldThemeMutedText
                    )
                )
                NavigationBarItem(
                    selected = activeTab == 2,
                    onClick = { activeTab = 2 },
                    icon = { Icon(Icons.Filled.Create, "Order Hub") },
                    label = { Text("Brief Hub", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BoldThemeTextDark,
                        selectedTextColor = Color.White,
                        indicatorColor = BoldThemePillBg,
                        unselectedIconColor = BoldThemeMutedText,
                        unselectedTextColor = BoldThemeMutedText
                    )
                )
                NavigationBarItem(
                    selected = activeTab == 3,
                    onClick = { activeTab = 3 },
                    icon = { Icon(Icons.Filled.ContactMail, "About Studio") },
                    label = { Text("Branding", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BoldThemeTextDark,
                        selectedTextColor = Color.White,
                        indicatorColor = BoldThemePillBg,
                        unselectedIconColor = BoldThemeMutedText,
                        unselectedTextColor = BoldThemeMutedText
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            AnimatedContent(
                targetState = activeTab,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220, easing = EaseInOutCubic)) togetherWith
                            fadeOut(animationSpec = tween(220, easing = EaseInOutCubic))
                },
                label = "tabChange"
            ) { currentTab ->
                when (currentTab) {
                    0 -> ShowcaseTab(viewModel = viewModel) { activeTab = 1 }
                    1 -> SandboxTab(viewModel = viewModel, savedPalettes = savedPalettes) { activeTab = 2 }
                    2 -> RequestHubTab(viewModel = viewModel, requests = requests)
                    3 -> ProfileContactTab()
                }
            }
        }
    }
}

// ================= TAB 1: SHOWCASE & PORTFOLIO FEED =================
@Composable
fun ShowcaseTab(
    viewModel: GraphicsViewModel,
    onNavigateToSandbox: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Branding & Typography", "UI/UX & Mobile HUD", "Vector Illustration", "Stream Graphics Bundle", "Logo Design & Identity")

    val filteredProjects = remember(selectedCategory) {
        if (selectedCategory == "All") mockProjects
        else mockProjects.filter { it.category == selectedCategory }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                Text(
                    text = "AESTHETIC\nSHOWCASE",
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.White
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "DESIGN ARCHIVE 2026",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = BoldThemeBorder,
                        thickness = 1.dp
                    )
                }

                Text(
                    text = "Explore top design drafts from Hariom Sharma Graphics. Tap 'Load Preset' to open any project style in the sandbox generator playground.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Category horizontally scrollable row
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        label = { Text(category.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Black) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = BoldThemeCardBackground,
                            labelColor = BoldThemeMutedText,
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = BoldThemeDeepPurple
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = BoldThemeBorder,
                            borderWidth = 1.dp,
                            selectedBorderColor = MaterialTheme.colorScheme.primary,
                            enabled = true,
                            selected = isSelected
                        )
                    )
                }
            }
        }

        // Portfolio project cards feed
        itemsIndexed(filteredProjects) { index, project ->
            PortfolioCard(index = index, project = project, applyPreset = {
                viewModel.updateSandboxDesignType(project.category.replace(" Design & Identity", "").replace(" & Typography", ""))
                viewModel.updateSandboxStyle(project.style)
                viewModel.updateSandboxIcon(project.iconType)
                viewModel.updateSandboxColors(project.hexColors)
                viewModel.updateSandboxComplexity(project.complexity)
                viewModel.updateSandboxName(project.clientName)
                viewModel.updateSandboxTagline("Creative Master Draft Preset Style")
                viewModel.showMessage("Style preset load: Applied '${project.title}' configuration to sandbox!")
                onNavigateToSandbox()
            })
        }
    }
}

@Composable
fun PortfolioCard(
    index: Int,
    project: PortfolioProject,
    applyPreset: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = BoldThemeCardBackground),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, BoldThemeBorder)
    ) {
        Column {
            // Simulated Art Rendering Canvas top section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        Brush.radialGradient(
                            colors = project.hexColors.map { safeParseColor(it) } + listOf(BoldThemeBackground),
                            radius = 450f
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Large styled index indicator top right, matching the HTML mockup design
                Text(
                    text = String.format("%02d", index + 1),
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 38.sp),
                    color = Color.White.copy(alpha = 0.25f),
                    fontWeight = FontWeight.Black,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = when (project.iconType) {
                            "Crown" -> Icons.Filled.Star
                            "Rocket" -> Icons.Filled.DoubleArrow
                            "Palette" -> Icons.Filled.ColorLens
                            "Diamond" -> Icons.Filled.Hexagon
                            else -> Icons.Filled.AutoAwesome
                        },
                        contentDescription = "Emblem Icon",
                        tint = Color.White,
                        modifier = Modifier
                            .size(54.dp)
                            .shadow(8.dp, CircleShape)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = project.clientName.uppercase(),
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontSize = 15.sp,
                        letterSpacing = 2.sp
                    )
                }

                // Category overlay chip
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(BoldThemeBackground.copy(alpha = 0.82f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = project.style.uppercase(),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Information and body details
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = project.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, contentDescription = "Rating", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(project.rating.toString(), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Text(
                    text = project.category.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                )

                Text(
                    text = project.description,
                    fontSize = 13.sp,
                    color = BoldThemeMutedText,
                    lineHeight = 18.sp
                )

                // Swatch list preview
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Palette Swatch: ", fontSize = 11.sp, color = BoldThemeMutedText, fontWeight = FontWeight.Medium)
                    project.hexColors.forEach { colorString ->
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(safeParseColor(colorString))
                                .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                        )
                    }
                }

                HorizontalDivider(color = BoldThemeBorder, thickness = 1.dp)

                // Actions row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = applyPreset,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Filled.AutoAwesome, "Load in Playground", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Customize Preset", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}


// ================= TAB 2: INTERACTIVE DESIGN SANDBOX / PLAYGROUND =================
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SandboxTab(
    viewModel: GraphicsViewModel,
    savedPalettes: List<com.example.data.SavedPalette>,
    onNavigateToBrief: () -> Unit
) {
    val dType by viewModel.sandboxDesignType.collectAsStateWithLifecycle()
    val name by viewModel.sandboxName.collectAsStateWithLifecycle()
    val tagline by viewModel.sandboxTagline.collectAsStateWithLifecycle()
    val style by viewModel.sandboxStyle.collectAsStateWithLifecycle()
    val rawColors by viewModel.sandboxColors.collectAsStateWithLifecycle()
    val iconType by viewModel.sandboxIconType.collectAsStateWithLifecycle()
    val scale by viewModel.sandboxScale.collectAsStateWithLifecycle()
    val rotation by viewModel.sandboxRotation.collectAsStateWithLifecycle()
    val gradAngle by viewModel.sandboxBgGradAngle.collectAsStateWithLifecycle()

    var paletteSavingName by remember { mutableStateOf("") }

    val activeColors = remember(rawColors) {
        rawColors.map {
            safeParseColor(it, CreativeOrange)
        }
    }

    // Color swatches templates to immediately tap
    val swatchTemplates = listOf(
        listOf("#FB8500", "#FFB703", "#023047"), // Sunset Amber
        listOf("#00B4D8", "#0077B6", "#90E0EF"), // Electric Cyber Ocean
        listOf("#EF4444", "#8B5CF6", "#F472B6"), // Cosmic Lavender Crimson
        listOf("#10B981", "#1E293B", "#34D399"), // Emerald Charcoal Sage
        listOf("#FECDD3", "#F43F5E", "#FFF1F2")  // Retro Bubblegum Rose
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        item {
            Column {
                Text(
                    text = "BLUEPRINT\nSANDBOX",
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.White
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PORTFOLIO CLIENT GENERATOR",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = BoldThemeBorder,
                        thickness = 1.dp
                    )
                }

                Text(
                    text = "Interactive generative graphics designer workspace. Customize canvas assets instantly to align with your corporate style sheet.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Live Vector Drawing Screen
        item {
            GraphicsPreviewCanvas(
                designType = dType,
                businessName = name,
                tagline = tagline,
                styleSelected = style,
                colors = activeColors,
                iconType = iconType,
                scale = scale,
                rotation = rotation,
                gradAngle = gradAngle,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Action Toolbar matching canvas
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        viewModel.applySandboxToForm()
                        viewModel.showMessage("Canvas pre-sets transferred successfully! Brief setup sheet loaded.")
                        onNavigateToBrief()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = BoldThemeDeepPurple),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1.2f)
                ) {
                    Icon(Icons.Filled.AssignmentTurnedIn, contentDescription = "Checkout")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("INITIATE STUDIO TRANSMISSION", fontSize = 11.sp, fontWeight = FontWeight.Black)
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        viewModel.updateSandboxRotation(0f)
                        viewModel.updateSandboxScale(1.0f)
                        viewModel.updateSandboxName("Hariom Sharma Graphics")
                        viewModel.updateSandboxTagline("Designs That Define Excellence")
                        viewModel.updateSandboxIcon("Brush")
                        viewModel.updateSandboxStyle("Neon Cyberpunk")
                        viewModel.updateSandboxColors(listOf("#FB8500", "#FFB703", "#00B4D8"))
                        viewModel.showMessage("Sandpit space configurations successfully normalized.")
                    },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = BoldThemeCardBackground),
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, BoldThemeBorder, RoundedCornerShape(12.dp))
                ) {
                    Icon(Icons.Filled.RestartAlt, contentDescription = "Reset Workspace", tint = CrimsonRed)
                }
            }
        }

        // Template Controllers
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BoldThemeCardBackground),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, BoldThemeBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("CORE TEMPLATE CONFIG", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Text Field for business Name input
                    OutlinedTextField(
                        value = name,
                        onValueChange = { viewModel.updateSandboxName(it) },
                        label = { Text("Client Business Heading") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = BoldThemeBorder,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = BoldThemeMutedText
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Text Field for tagline copy
                    OutlinedTextField(
                        value = tagline,
                        onValueChange = { viewModel.updateSandboxTagline(it) },
                        label = { Text("Subtext / Brand Tagline") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = BoldThemeBorder,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = BoldThemeMutedText
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        // Graphic Stylists & Dropdowns
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BoldThemeCardBackground),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, BoldThemeBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("DIGITAL THEME STYLESHEET", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Art Theme Atmosphere", fontSize = 11.sp, color = BoldThemeMutedText, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("Neon Cyberpunk", "Minimalist Slate", "Royal Retro", "Modern Brutalist", "Playful Pop").forEach { filterStyle ->
                            val isSelected = style == filterStyle
                            Box(
                                modifier = Modifier
                                    .clickable { viewModel.updateSandboxStyle(filterStyle) }
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary else BoldThemeBackground,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else BoldThemeBorder, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    filterStyle,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) BoldThemeDeepPurple else Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Active Motif Vector Symbol", fontSize = 11.sp, color = BoldThemeMutedText, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("Brush", "Palette", "Crown", "Sparkles", "Rocket", "Diamond").forEach { motif ->
                            val isSelected = iconType == motif
                            Box(
                                modifier = Modifier
                                    .clickable { viewModel.updateSandboxIcon(motif) }
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary else BoldThemeBackground,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else BoldThemeBorder, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    motif,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) BoldThemeDeepPurple else Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Asset Output Blueprint Format", fontSize = 11.sp, color = BoldThemeMutedText, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("Logo Emblem", "Business Card", "Social Banner", "Vector Portrait", "App Mockup").forEach { rawDType ->
                            val isSelected = dType == rawDType
                            Box(
                                modifier = Modifier
                                    .clickable { viewModel.updateSandboxDesignType(rawDType) }
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary else BoldThemeBackground,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else BoldThemeBorder, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    rawDType,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) BoldThemeDeepPurple else Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        // Geometric Slider Knobs
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BoldThemeCardBackground),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, BoldThemeBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("MATRIX COORDINATES CALIBRATION", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Angle of scale slider
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("Scale Factor: ", fontSize = 11.sp, color = BoldThemeMutedText, modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold)
                        Slider(
                            value = scale,
                            onValueChange = { viewModel.updateSandboxScale(it) },
                            valueRange = 0.5f..1.6f,
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colorScheme.primary,
                                activeTrackColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Text(String.format("%.2f", scale), fontSize = 11.sp, color = Color.White, modifier = Modifier.width(36.dp), fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Angle of rotation slider
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("Rotation angle: ", fontSize = 11.sp, color = BoldThemeMutedText, modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold)
                        Slider(
                            value = rotation,
                            onValueChange = { viewModel.updateSandboxRotation(it) },
                            valueRange = -180f..180f,
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colorScheme.primary,
                                activeTrackColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Text(String.format("%.0f°", rotation), fontSize = 11.sp, color = Color.White, modifier = Modifier.width(36.dp), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Color Swatches Palette manager
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BoldThemeCardBackground),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, BoldThemeBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("WORKSPACE PALETTE MIXER", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Monospace)
                    Text("Swap brand contrast profiles instantly or lock a custom formulation in local persistent system storage.", fontSize = 11.sp, color = BoldThemeMutedText)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Color swatches row to tap
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        swatchTemplates.forEachIndexed { idx, templ ->
                            Row(
                                modifier = Modifier
                                    .clickable { viewModel.updateSandboxColors(templ) }
                                    .background(BoldThemeBackground, RoundedCornerShape(8.dp))
                                    .border(
                                        1.dp,
                                        if (rawColors == templ) MaterialTheme.colorScheme.primary else BoldThemeBorder,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                templ.forEach { cS ->
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(safeParseColor(cS))
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = BoldThemeBorder)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Saved palettes and database operations
                    Text("Save Custom Swatch Config", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = paletteSavingName,
                            onValueChange = { paletteSavingName = it },
                            placeholder = { Text("E.g., Neon Horizon", fontSize = 12.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = BoldThemeBorder,
                                focusedLabelColor = MaterialTheme.colorScheme.primary,
                                unfocusedLabelColor = BoldThemeMutedText
                            ),
                            maxLines = 1,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                viewModel.saveCurrentPalette(paletteSavingName)
                                paletteSavingName = ""
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = BoldThemeDeepPurple),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Filled.Save, contentDescription = "Save Palette")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Store", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Display saved palettes from Room
                    if (savedPalettes.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Saved Palette Ledger:", fontSize = 10.sp, color = BoldThemeMutedText, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            savedPalettes.forEach { savedP ->
                                val pColorsList = savedP.colorsString.split(",")
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(BoldThemeBackground, RoundedCornerShape(12.dp))
                                        .border(1.dp, BoldThemeBorder, RoundedCornerShape(12.dp))
                                        .padding(horizontal = 8.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        // Colors drawing preview
                                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.clickable {
                                            viewModel.updateSandboxColors(pColorsList)
                                            viewModel.showMessage("Palette lock: loaded color scheme '${savedP.name}' into sandbox.")
                                        }) {
                                            pColorsList.forEach { cString ->
                                                Box(
                                                    modifier = Modifier
                                                        .size(14.dp)
                                                        .clip(CircleShape)
                                                        .background(safeParseColor(cString))
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(savedP.name, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                                    }

                                    IconButton(
                                        onClick = { viewModel.deletePalette(savedP) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Filled.Delete, "Delete Palette", tint = CrimsonRed.copy(alpha = 0.8f), modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


// ================= TAB 3: DESIGN BRIEF & SAVED ORDERS LIST =================
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RequestHubTab(
    viewModel: GraphicsViewModel,
    requests: List<DesignRequest>
) {
    val fType by viewModel.formType.collectAsStateWithLifecycle()
    val fName by viewModel.formName.collectAsStateWithLifecycle()
    val fTagline by viewModel.formTagline.collectAsStateWithLifecycle()
    val fStyle by viewModel.formStyle.collectAsStateWithLifecycle()
    val fComplexity by viewModel.formComplexity.collectAsStateWithLifecycle()
    val fNotes by viewModel.formNotes.collectAsStateWithLifecycle()
    val livePrice by viewModel.liveEstimatedPrice.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        item {
            Column {
                Text(
                    text = "BRIEF\nHUB",
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.White
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "STUDIO TRANSMISSION PORTAL",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = BoldThemeBorder,
                        thickness = 1.dp
                    )
                }

                Text(
                    text = "Transmit creative design instructions directly to artist Hariom Sharma. Our proprietary algorithm provides immediate itemized budget estimations.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Project Brief Sheet input form
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BoldThemeCardBackground),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, BoldThemeBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("DESIGN SUBMISSION SPECS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Brief Heading input
                    OutlinedTextField(
                        value = fName,
                        onValueChange = { viewModel.updateFormName(it) },
                        label = { Text("Brand / Business Name Copy (*)") },
                        placeholder = { Text("E.g., Sharma Graphics Inc.") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = BoldThemeBorder,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = BoldThemeMutedText
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Tagline input
                    OutlinedTextField(
                        value = fTagline,
                        onValueChange = { viewModel.updateFormTagline(it) },
                        label = { Text("Brand Subtext / Core Slogan") },
                        placeholder = { Text("E.g., Est. 2026") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = BoldThemeBorder,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = BoldThemeMutedText
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Selection Row: Style Selection
                    Text("Art Blueprint style: ", fontSize = 11.sp, color = BoldThemeMutedText, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("Neon Cyberpunk", "Minimalist Slate", "Royal Retro", "Modern Brutalist", "Playful Pop").forEach { filterStyle ->
                            val isSelected = fStyle == filterStyle
                            Box(
                                modifier = Modifier
                                    .clickable { viewModel.updateFormStyle(filterStyle) }
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary else BoldThemeBackground,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else BoldThemeBorder, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    filterStyle,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) BoldThemeDeepPurple else Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Selection Row: Blueprint category
                    Text("Product category target: ", fontSize = 11.sp, color = BoldThemeMutedText, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("Logo Emblem", "Business Card", "Social Banner", "Vector Portrait", "App Mockup").forEach { rawDType ->
                            val isSelected = fType == rawDType
                            Box(
                                modifier = Modifier
                                    .clickable { viewModel.updateFormType(rawDType) }
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary else BoldThemeBackground,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else BoldThemeBorder, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    rawDType,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) BoldThemeDeepPurple else Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Selection Row: Complexity
                    Text("Illustrative Execution Intensity: ", fontSize = 11.sp, color = BoldThemeMutedText, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("Standard Effort", "Detailed Artistic", "Creative Masterpiece").forEach { comp ->
                            val isSelected = fComplexity == comp
                            Box(
                                modifier = Modifier
                                    .clickable { viewModel.updateFormComplexity(comp) }
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary else BoldThemeBackground,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else BoldThemeBorder, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    comp,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) BoldThemeDeepPurple else Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Long Notes field
                    OutlinedTextField(
                        value = fNotes,
                        onValueChange = { viewModel.updateFormNotes(it) },
                        label = { Text("Detailed Creative Directives") },
                        placeholder = { Text("E.g., Specify hex color codes, typography details, formatting, target demographic, or canvas sizes...", fontSize = 12.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = BoldThemeBorder,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = BoldThemeMutedText
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        maxLines = 4,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Live Cost Breakdown Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BoldThemeBackground, RoundedCornerShape(12.dp))
                            .border(1.dp, BoldThemeBorder, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Budget Estimation: ", fontSize = 11.sp, color = BoldThemeMutedText)
                                Text("Itemized Corporate Rate", fontSize = 9.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            }

                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = String.format("$%.2f", livePrice),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(" USD", fontSize = 11.sp, color = BoldThemeMutedText, modifier = Modifier.padding(bottom = 3.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Submit Brief Sheet Button
                    Button(
                        onClick = { viewModel.saveDesignRequest() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = BoldThemeDeepPurple),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.Send, contentDescription = "Transmit Brief")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Transmit Brief to Sharma Studio", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Saved Order Briefs Checklist history from Room
        item {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = "Submissions Register (#" + requests.size + ")",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Browse status coordinates or cancel/modify your saved briefs.",
                    style = MaterialTheme.typography.bodySmall,
                    color = BoldThemeMutedText
                )
            }
        }

        if (requests.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BoldThemeCardBackground, RoundedCornerShape(24.dp))
                        .border(1.dp, BoldThemeBorder, RoundedCornerShape(24.dp))
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.History,
                            contentDescription = "No Briefs List",
                            tint = BoldThemeMutedText,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "No Submitted Briefs Located",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Create and transmit a design layout brief to build a records history.",
                            color = BoldThemeMutedText,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(requests) { request ->
                SubmittedBriefCard(request = request, onDelete = {
                    viewModel.deleteRequest(request)
                })
            }
        }
    }
}

@Composable
fun SubmittedBriefCard(
    request: DesignRequest,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = BoldThemeCardBackground),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, BoldThemeBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(
                                when (request.status) {
                                    "Submitted" -> MaterialTheme.colorScheme.primary
                                    "Reviewing" -> BoldThemeMutedText
                                    "Completed" -> MaterialTheme.colorScheme.primary
                                    else -> BoldThemeMutedText
                                }
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Brief ID: #${request.id}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = request.status.uppercase(),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = request.businessName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            if (request.tagline.isNotBlank()) {
                Text(
                    text = request.tagline,
                    fontSize = 11.sp,
                    color = BoldThemeMutedText,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(BoldThemeBackground, RoundedCornerShape(6.dp))
                        .border(1.dp, BoldThemeBorder, RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        request.designType,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .background(BoldThemeBackground, RoundedCornerShape(6.dp))
                        .border(1.dp, BoldThemeBorder, RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        request.styleSelected,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .background(BoldThemeBackground, RoundedCornerShape(6.dp))
                        .border(1.dp, BoldThemeBorder, RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        request.complexity,
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (request.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = '"' + request.notes + '"',
                    fontSize = 12.sp,
                    color = BoldThemeMutedText,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = BoldThemeBorder)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Budget lock: ", fontSize = 10.sp, color = BoldThemeMutedText)
                    Text(
                        String.format("$%.2f USD", request.estimatedPrice),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Cancel request",
                        tint = CrimsonRed,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}


// ================= TAB 4: BRAND CREATIVE OFFICE & BIO =================
@Composable
fun ProfileContactTab() {
    val context = LocalContext.current
    var contactName by remember { mutableStateOf("") }
    var contactMsg by remember { mutableStateOf("") }

    val coreSkills = listOf(
        "Vector Illustration" to 0.95f,
        "Corporate Brand Systems" to 0.98f,
        "Cyberpunk Neon Aesthetics" to 0.90f,
        "Brutalist UI Design" to 0.85f,
        "Pre-press & Offset Merch" to 0.92f
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        item {
            Column {
                Text(
                    text = "STUDIO\nBIO",
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.White
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "EXECUTIVE PROFILE",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = BoldThemeBorder,
                        thickness = 1.dp
                    )
                }

                Text(
                    text = "Design headquarters and executive philosophy of primary illustrator Hariom Sharma.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Headshot/Bio Brand Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BoldThemeCardBackground),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, BoldThemeBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile illustration avatar
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .shadow(6.dp, CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(MaterialTheme.colorScheme.primary, BoldThemeDeepPurple, BoldThemeBackground)
                                ),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Brush,
                            contentDescription = "Studio Head",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "Hariom Sharma",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    Text(
                        "CHIEF EXECUTIVE GRAPHIC DESIGNER",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        "Designing visual worlds since 2018. Over 450 corporate entities worldwide trust Hariom Sharma Graphics with standard logos, complex geometric vector packaging systems, modern responsive interfaces, and dynamic artwork overlays.",
                        color = BoldThemeMutedText,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Dynamic Studio Skills Gauge Meters
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BoldThemeCardBackground),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, BoldThemeBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("GUARANTEED SERVICE CALIBRATIONS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(14.dp))

                    coreSkills.forEach { (name, ratio) ->
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(name, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Medium)
                                Text(String.format("%.0f%%", ratio * 100), fontSize = 11.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Black)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { ratio },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(CircleShape),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = BoldThemeBackground
                            )
                        }
                    }
                }
            }
        }

        // studio location and email coordinates
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BoldThemeCardBackground),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, BoldThemeBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("CORPORATE HQ COORDINATES", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 6.dp)) {
                        Icon(Icons.Filled.Mail, contentDescription = "Email", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("pandithariomsharma01@gmail.com", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 6.dp)) {
                        Icon(Icons.Filled.LocationOn, contentDescription = "Office", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Metro Design Center, Gwalior IN", fontSize = 12.sp, color = Color.White)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 6.dp)) {
                        Icon(Icons.Filled.AccessTime, contentDescription = "Business hours", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("09:00 - 18:00 (Mon - Sat) GMT+5:30", fontSize = 12.sp, color = Color.White)
                    }
                }
            }
        }

        // Direct Messaging Sandbox Client Feedback Form
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BoldThemeCardBackground),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, BoldThemeBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("INSTANT TELEGRAM CHANNEL", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Monospace)
                    Text("Have urgent specifications? Pipe a live transmission memo directly off this portal.", fontSize = 11.sp, color = BoldThemeMutedText)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = contactName,
                        onValueChange = { contactName = it },
                        label = { Text("Client Representative Name") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = BoldThemeBorder,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = BoldThemeMutedText
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = contactMsg,
                        onValueChange = { contactMsg = it },
                        label = { Text("Brief Memo / Message Description") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = BoldThemeBorder,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = BoldThemeMutedText
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        maxLines = 3,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (contactName.isNotBlank() && contactMsg.isNotBlank()) {
                                Toast.makeText(context, "Memo dispatched to primary channel! Sharma Graphics returns replies in 30 minutes.", Toast.LENGTH_LONG).show()
                                contactName = ""
                                contactMsg = ""
                            } else {
                                Toast.makeText(context, "Please configure both name and message description.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = BoldThemeDeepPurple),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Transmit Message")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Transmit Urgent Memo", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
