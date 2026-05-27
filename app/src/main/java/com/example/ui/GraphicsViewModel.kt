package com.example.ui

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.DesignRequest
import com.example.data.GraphicsDao
import com.example.data.SavedPalette
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GraphicsViewModel(private val dao: GraphicsDao) : ViewModel() {

    // Database flows
    val designRequests: StateFlow<List<DesignRequest>> = dao.getAllDesignRequests()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedPalettes: StateFlow<List<SavedPalette>> = dao.getAllSavedPalettes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Sandbox Designer State
    private val _sandboxDesignType = MutableStateFlow("Logo Emblem")
    val sandboxDesignType = _sandboxDesignType.asStateFlow()

    private val _sandboxName = MutableStateFlow("Hariom Sharma Graphics")
    val sandboxName = _sandboxName.asStateFlow()

    private val _sandboxTagline = MutableStateFlow("Designs That Define Excellence")
    val sandboxTagline = _sandboxTagline.asStateFlow()

    private val _sandboxStyle = MutableStateFlow("Neon Cyberpunk")
    val sandboxStyle = _sandboxStyle.asStateFlow()

    private val _sandboxComplexity = MutableStateFlow("Creative Masterpiece")
    val sandboxComplexity = _sandboxComplexity.asStateFlow()

    private val _sandboxColors = MutableStateFlow(listOf("#FB8500", "#FFB703", "#00B4D8"))
    val sandboxColors = _sandboxColors.asStateFlow()

    private val _sandboxIconType = MutableStateFlow("Brush") // "Brush", "Palette", "Crown", "Sparkles", "Rocket", "Diamond"
    val sandboxIconType = _sandboxIconType.asStateFlow()

    private val _sandboxScale = MutableStateFlow(1.0f)
    val sandboxScale = _sandboxScale.asStateFlow()

    private val _sandboxRotation = MutableStateFlow(0f)
    val sandboxRotation = _sandboxRotation.asStateFlow()

    private val _sandboxBgGradAngle = MutableStateFlow(45f)
    val sandboxBgGradAngle = _sandboxBgGradAngle.asStateFlow()

    // Form inputs (Separate from active sandbox template to allow independent creation)
    private val _formType = MutableStateFlow("Logo Emblem")
    val formType = _formType.asStateFlow()

    private val _formName = MutableStateFlow("")
    val formName = _formName.asStateFlow()

    private val _formTagline = MutableStateFlow("")
    val formTagline = _formTagline.asStateFlow()

    private val _formStyle = MutableStateFlow("Neon Cyberpunk")
    val formStyle = _formStyle.asStateFlow()

    private val _formComplexity = MutableStateFlow("Creative Masterpiece")
    val formComplexity = _formComplexity.asStateFlow()

    private val _formNotes = MutableStateFlow("")
    val formNotes = _formNotes.asStateFlow()

    // Status / Message State
    private val _uiMessage = MutableStateFlow<String?>(null)
    val uiMessage = _uiMessage.asStateFlow()

    // Utility update methods
    fun updateSandboxDesignType(type: String) { _sandboxDesignType.value = type }
    fun updateSandboxName(name: String) { _sandboxName.value = name }
    fun updateSandboxTagline(tagline: String) { _sandboxTagline.value = tagline }
    fun updateSandboxStyle(style: String) { _sandboxStyle.value = style }
    fun updateSandboxComplexity(comp: String) { _sandboxComplexity.value = comp }
    fun updateSandboxIcon(icon: String) { _sandboxIconType.value = icon }
    fun updateSandboxScale(scale: Float) { _sandboxScale.value = scale }
    fun updateSandboxRotation(rot: Float) { _sandboxRotation.value = rot }
    fun updateSandboxGradAngle(angle: Float) { _sandboxBgGradAngle.value = angle }

    fun updateSandboxColors(colors: List<String>) {
        _sandboxColors.value = colors
    }

    // Set Form inputs from Sandbox parameters
    fun applySandboxToForm() {
        _formType.value = _sandboxDesignType.value
        _formName.value = _sandboxName.value
        _formTagline.value = _sandboxTagline.value
        _formStyle.value = _sandboxStyle.value
        _formComplexity.value = _sandboxComplexity.value
        _formNotes.value = "Draft customized from interactive designer sandbox model."
    }

    // Update form properties directly
    fun updateFormType(type: String) { _formType.value = type }
    fun updateFormName(name: String) { _formName.value = name }
    fun updateFormTagline(tag : String) { _formTagline.value = tag }
    fun updateFormStyle(style: String) { _formStyle.value = style }
    fun updateFormComplexity(comp: String) { _formComplexity.value = comp }
    fun updateFormNotes(notes: String) { _formNotes.value = notes }

    // Live price calculator based on design metrics
    val liveEstimatedPrice: StateFlow<Double> = combine(
        _formType, _formStyle, _formComplexity
    ) { type, style, complexity ->
        var basePrice = when (type) {
            "Logo Emblem" -> 120.0
            "Business Card" -> 80.0
            "Social Banner" -> 90.0
            "Vector Portrait" -> 160.0
            "App Mockup" -> 220.0
            else -> 100.0
        }

        val styleMultiplier = when (style) {
            "Minimalist Slate" -> 1.0
            "Neon Cyberpunk" -> 1.2
            "Royal Retro" -> 1.3
            "Modern Brutalist" -> 1.15
            else -> 1.0
        }

        val complexityBonus = when (complexity) {
            "Standard Effort" -> 0.0
            "Detailed Artistic" -> 80.0
            "Creative Masterpiece" -> 180.0
            else -> 0.0
        }

        (basePrice * styleMultiplier) + complexityBonus
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 120.0)


    fun showMessage(msg: String) {
        _uiMessage.value = msg
    }

    fun clearMessage() {
        _uiMessage.value = null
    }

    // DB actions
    fun saveDesignRequest() {
        viewModelScope.launch {
            if (_formName.value.isBlank()) {
                showMessage("Please enter a Brand/Business Name for the brief.")
                return@launch
            }

            val request = DesignRequest(
                designType = _formType.value,
                businessName = _formName.value,
                tagline = _formTagline.value,
                styleSelected = _formStyle.value,
                complexity = _formComplexity.value,
                notes = _formNotes.value,
                colorsString = _sandboxColors.value.joinToString(","),
                estimatedPrice = liveEstimatedPrice.value,
                status = "Submitted"
            )

            val id = dao.insertDesignRequest(request)
            showMessage("Order request submitted successfully (ID: #$id)!")
            // Reset form fields
            _formName.value = ""
            _formTagline.value = ""
            _formNotes.value = ""
        }
    }

    fun deleteRequest(request: DesignRequest) {
        viewModelScope.launch {
            dao.deleteDesignRequest(request)
            showMessage("Design brief #${request.id} deleted successfully.")
        }
    }

    fun saveCurrentPalette(name: String) {
        viewModelScope.launch {
            val paletteName = name.ifBlank { "Palette " + (System.currentTimeMillis() % 1000) }
            val colorsStr = _sandboxColors.value.joinToString(",")
            val palette = SavedPalette(
                name = paletteName,
                colorsString = colorsStr
            )
            dao.insertSavedPalette(palette)
            showMessage("Branding palette '$paletteName' saved!")
        }
    }

    fun deletePalette(palette: SavedPalette) {
        viewModelScope.launch {
            dao.deleteSavedPalette(palette)
            showMessage("Deleted Palette '${palette.name}'.")
        }
    }
}

class GraphicsViewModelFactory(private val dao: GraphicsDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GraphicsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GraphicsViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
