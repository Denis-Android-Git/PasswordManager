package com.example.key.presentation

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.domain.model.Item
import com.example.key.R

@Composable
fun Item(
    item: Item,
    menuItems: List<String>,
    modifier: Modifier,
    onItemClick: (String) -> Unit
) {
    var exposed by remember {
        mutableStateOf(false)
    }
    var showMenu by remember {
        mutableStateOf(false)
    }
    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    val density = LocalDensity.current
    val interactionSource = remember {
        MutableInteractionSource()
    }

    Card(
        modifier = modifier
            .padding(3.dp)
            .onSizeChanged {
                itemHeight = with(density) { it.height.toDp() }
            }
            .indication(interactionSource, LocalIndication.current)
            .pointerInput(true) {
                detectTapGestures(
                    onLongPress = {
                        showMenu = true
                        pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                    },
                    onPress = {
                        val press = PressInteraction.Press(it)
                        interactionSource.emit(press)
                        tryAwaitRelease()
                        interactionSource.emit(PressInteraction.Release(press))
                    }
                )
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
        ) {
            AsyncImage(
                model = "https://${item.url}/favicon.ico",
                error = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .padding(start = 6.dp)
                    .align(Alignment.CenterStart)
            )
            Text(
                text = item.url, modifier = Modifier
                    .padding(start = 70.dp)
                    .align(Alignment.CenterStart)
            )

            Button(
                onClick = {
                    exposed = !exposed
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 6.dp)

            ) {
                Text(
                    text = if (exposed) item.password else stringResource(R.string.show),
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )
            }
            DropdownMenuNoPaddingVeitical(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                offset = pressOffset.copy(
                    y = pressOffset.y - itemHeight
                ),
                modifier = Modifier.background(
                    color = Color(0xFFFFFFFF)
                )
            ) {
                menuItems.forEachIndexed { index, menu ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = menu,
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        },
                        onClick = {
                            onItemClick(menu)
                            showMenu = false
                        }
                    )
                    if (index == 0 || index == 1) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}