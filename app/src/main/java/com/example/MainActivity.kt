package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.data.GraphicsDatabase
import com.example.ui.GraphicsViewModel
import com.example.ui.GraphicsViewModelFactory
import com.example.ui.screens.MainDashboard
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    // Initialize Database and ViewModel
    val database = GraphicsDatabase.getDatabase(applicationContext)
    val dao = database.graphicsDao()
    val viewModelFactory = GraphicsViewModelFactory(dao)
    val viewModel = ViewModelProvider(this, viewModelFactory)[GraphicsViewModel::class.java]

    setContent {
      MyApplicationTheme {
        MainDashboard(viewModel = viewModel)
      }
    }
  }
}
