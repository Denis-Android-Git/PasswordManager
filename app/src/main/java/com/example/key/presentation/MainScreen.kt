package com.example.key.presentation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.key.R
import com.example.key.viewmodel.DbViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    modifier: Modifier,
    navController: NavController,
    dbViewModel: DbViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val menuItems by remember {
        mutableStateOf(
            listOf(
                context.getString(R.string.edit),
                context.getString(R.string.delete)
            )
        )
    }

    val list = dbViewModel.list.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .statusBarsPadding()
                    .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)),
                title = {
                    Box(modifier = modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.site_list),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .align(Alignment.Center),
                            fontSize = 22.sp
                        )
                    }
                }
            )

            LazyColumn(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(16.dp)
            ) {
                items(list.value,
                    key = {
                        it.id
                    }) { item ->
                    Item(
                        item = item,
                        menuItems = menuItems,
                        modifier = Modifier.animateItemPlacement(
                            //tween(durationMillis = 650)
                            spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    ) { menu ->
                        when (menu) {
                            context.getString(R.string.edit) -> {
                                navController.navigate("editScreen/${item.id}")
                            }

                            context.getString(R.string.delete) -> {
                                dbViewModel.delete(item)
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }

        FloatingActionButton(
            onClick = {
                navController.navigate("addScreen")
            },
            modifier = modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ) {
            Icon(
                Icons.Filled.Add, "Floating action button.",
                modifier = Modifier.size(50.dp)
            )
        }
    }
}