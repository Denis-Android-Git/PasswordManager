package com.example.key.presentation

import android.content.SharedPreferences
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.data.keygenerator.KeyGenerator
import com.example.key.R
import com.example.key.utils.findActivity
import com.example.key.utils.showBiometricPrompt
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.Executor

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun LoginScreen(
    modifier: Modifier,
    keyGenerator: KeyGenerator,
    navController: NavController,
    sharedPreferences: SharedPreferences
) {

    val firstRun = sharedPreferences.getBoolean("dialog", true)
    val useBiometric = sharedPreferences.getBoolean("biometric", false)

    val context = LocalContext.current
    val executor: Executor = ContextCompat.getMainExecutor(context)
    val activity = LocalContext.current.findActivity()
    val biometricManager = BiometricManager.from(context)
    val promptInfo: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(stringResource(R.string.use_finger))
        //.setSubtitle("Log in using your biometric credential")
        //.setNegativeButtonText("Use account password")
        .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        .build()

    var password by remember {
        mutableStateOf("")
    }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    var showDialog by remember {
        mutableStateOf(firstRun)
    }
    var showBiometric by remember {
        mutableStateOf(useBiometric)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                text = {
                    Text(
                        text = stringResource(R.string.show_bio),
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("biometric", true)
                        editor.putBoolean("dialog", false)
                            .apply()
                        showBiometric = true
                        showDialog = false
                    }) {
                        Text(
                            text = stringResource(R.string.yes)
                        )
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("biometric", false)
                        editor.putBoolean("dialog", false)
                            .apply()
                        showDialog = false
                    }) {
                        Text(
                            text = stringResource(R.string.no)
                        )
                    }
                }
            )
        }
        if (showBiometric) {
            LaunchedEffect(key1 = Unit) {
                showBiometricPrompt(
                    activity,
                    executor,
                    navController,
                    biometricManager,
                    promptInfo,
                    sharedPreferences
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            TextField(
                value = password,
                onValueChange = { password = it },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Rounded.Visibility
                    else Icons.Filled.VisibilityOff

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, null)
                    }
                },
                placeholder = {
                    Text(text = stringResource(R.string.enter_password))
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val file = File(context.filesDir, "1.txt")
                    val text = keyGenerator.decrypt(FileInputStream(file)).decodeToString()
                    if (password == text) {
                        navController.navigate("mainScreen")
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.wrong_password), Toast.LENGTH_LONG
                        ).show()
                    }
                },
            ) {
                Text(text = stringResource(R.string.enter))
            }
        }
    }
}