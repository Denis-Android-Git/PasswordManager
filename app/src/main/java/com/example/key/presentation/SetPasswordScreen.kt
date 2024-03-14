package com.example.key.presentation

import android.content.SharedPreferences
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import com.example.data.keygenerator.KeyGenerator
import com.example.key.R
import java.io.File
import java.io.FileOutputStream

@Composable
fun SetPasswordScreen(
    modifier: Modifier,
    keyGenerator: KeyGenerator,
    navController: NavController,
    sharedPreferences: SharedPreferences
) {

    val context = LocalContext.current
    var password by remember {
        mutableStateOf("")
    }
    var confirmPassword by remember {
        mutableStateOf("")
    }

    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var passwordVisible2 by rememberSaveable { mutableStateOf(false) }


    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
                    Text(text = stringResource(R.string.set_password))
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = confirmPassword,
                visualTransformation = if (passwordVisible2) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible2)
                        Icons.Rounded.Visibility
                    else Icons.Filled.VisibilityOff

                    IconButton(onClick = { passwordVisible2 = !passwordVisible2 }) {
                        Icon(imageVector = image, null)
                    }
                },
                onValueChange = { confirmPassword = it },
                placeholder = {
                    Text(text = stringResource(R.string.confirm))
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val bytes = password.encodeToByteArray()
                    val file = File(context.filesDir, "1.txt")
                    val fos = FileOutputStream(file)
                    keyGenerator.encrypt(bytes, fos)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isFirstRun", false)
                    editor.apply()
                    navController.navigate("mainScreen")
                },
                enabled = password == confirmPassword && password.isNotEmpty() && confirmPassword.isNotEmpty()
            ) {
                Text(text = stringResource(R.string.next))
            }
        }
    }
}