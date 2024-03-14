package com.example.key.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.domain.model.Item
import com.example.key.R
import com.example.key.viewmodel.DbViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    id: Int,
    dbViewModel: DbViewModel = koinViewModel(),
    navController: NavController
) {

    var item by remember {
        mutableStateOf<Item?>(null)
    }
    LaunchedEffect(Unit) {
        dbViewModel.getItem(id)
        dbViewModel.item.collect {
            item = it
        }
    }
    if (item != null) {

        var site by remember {
            mutableStateOf(item!!.url)
        }
        var password by remember {
            mutableStateOf(item!!.password)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .statusBarsPadding()
                    .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)),
                title = {
                    Row {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            },
                            Modifier.align(Alignment.CenterVertically)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                        Text(
                            text = stringResource(R.string.edit_site),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .wrapContentSize(Alignment.CenterStart),
                            color = Color.White,
                            fontSize = 22.sp
                        )
                        IconButton(
                            onClick = {
                                dbViewModel.updateItem(
                                    url = site,
                                    password = password,
                                    itemId = item!!.id
                                )
                                navController.navigate("mainScreen")
                            },
                            enabled = site.isNotEmpty() && password.isNotEmpty(),
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(end = 16.dp)
                        ) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }
            )
            Column(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxSize()
            ) {
                OutlinedTextField(
                    isError = site.isEmpty(),
                    value = site,
                    onValueChange = {
                        site = it
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    label = {
                        Text(
                            text = stringResource(R.string.www_google_com),
                            fontSize = 16.sp
                        )
                    }
                )
                OutlinedTextField(
                    isError = password.isEmpty(),
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    label = {
                        Text(
                            text = stringResource(R.string.password),
                            fontSize = 16.sp
                        )
                    }
                )
            }
        }
    }
}