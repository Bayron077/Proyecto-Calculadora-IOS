package com.utp.ioscalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.utp.ioscalculator.navigation.AppNavigation
import com.utp.ioscalculator.ui.theme.IOSCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IOSCalculatorTheme {
                AppNavigation()
            }
        }
    }
}
