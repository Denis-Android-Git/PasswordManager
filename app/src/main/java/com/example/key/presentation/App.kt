package com.example.key.presentation

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.keygenerator.KeyGenerator

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun Navigation(
    modifier: Modifier,
    sharedPreferences: SharedPreferences,
    keyGenerator: KeyGenerator
) {
    val navController = rememberNavController()

    var startDestination by remember {
        mutableStateOf("")
    }

    val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)

    startDestination = if (isFirstRun) {
        "SetPasswordScreen"
    } else {
        "loginScreen"
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = "mainScreen") {
            MainScreen(
                modifier = modifier,
                navController = navController
            )
        }
        composable(route = "addScreen") {
            AddScreen(
                navController = navController
            )
        }
        composable(route = "SetPasswordScreen") {
            SetPasswordScreen(
                modifier = modifier,
                keyGenerator = keyGenerator,
                navController = navController,
                sharedPreferences = sharedPreferences
            )
        }
        composable(route = "loginScreen") {
            LoginScreen(
                modifier = modifier,
                keyGenerator = keyGenerator,
                navController = navController,
                sharedPreferences = sharedPreferences
            )
        }
        composable(
            route = "editScreen/{id}",
            arguments = listOf(
                navArgument(name = "id") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) {
            val id = it.arguments?.getString("id")?.toInt()
            if (id != null) {
                EditScreen(
                    id = id,
                    navController = navController
                )
            }
        }
    }
}