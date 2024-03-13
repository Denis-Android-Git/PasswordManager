package com.example.key.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.example.key.REQUEST_CODE
import java.util.concurrent.Executor

fun Context.findActivity(): FragmentActivity? {
    var currentContext = this
    var previousContext: Context? = null
    while (currentContext is ContextWrapper && previousContext != currentContext) {
        if (currentContext is FragmentActivity) {
            return currentContext
        }
        previousContext = currentContext
        currentContext = currentContext.baseContext
    }
    return null
}

@RequiresApi(Build.VERSION_CODES.R)
fun showBiometricPrompt(
    activity: FragmentActivity?,
    executor: Executor,
    navController: NavController,
    biometricManager: BiometricManager,
    promptInfo: BiometricPrompt.PromptInfo,
    sharedPreferences: SharedPreferences
) {
    val biometricPrompt = BiometricPrompt(activity!!, executor,
        object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                super.onAuthenticationSucceeded(result)
                navController.navigate("mainScreen")
            }

        })
    when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
            biometricPrompt.authenticate(promptInfo)
        }

        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
            val editor = sharedPreferences.edit()
            editor.putBoolean("biometric", false)
            editor.putBoolean("dialog", false)
                .apply()
        }

        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {}

        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(
                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                )
            }
            ActivityCompat.startActivityForResult(activity, enrollIntent, REQUEST_CODE, null)
        }

        BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
        }

        BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
        }

        BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
        }
    }
}
