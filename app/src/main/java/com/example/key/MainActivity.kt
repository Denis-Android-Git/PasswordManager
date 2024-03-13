package com.example.key

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.example.data.keygenerator.KeyGenerator
import com.example.key.presentation.Navigation
import com.example.key.ui.theme.KeyTheme

const val PREFS = "prefs"
const val REQUEST_CODE = 123

class MainActivity : FragmentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val keyGen = KeyGenerator()

        val sharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE)

        setContent {
            KeyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(
                        modifier = Modifier.padding(innerPadding),
                        keyGenerator = keyGen,
                        sharedPreferences = sharedPreferences
                    )
                }
            }
        }
    }
}