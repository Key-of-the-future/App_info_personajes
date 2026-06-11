package com.example.appdates

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.appdates.navigation.AppNavigation
import com.example.appdates.ui.theme.AppDatesTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {

            AppDatesTheme {

                AppNavigation()

            }
        }
    }
}