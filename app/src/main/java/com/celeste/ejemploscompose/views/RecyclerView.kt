package com.celeste.ejemploscompose.views

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.celeste.ejemploscompose.R
import com.celeste.ejemploscompose.model.SuperHero
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


@Composable
fun SimpleRecyclerView() {
    LazyColumn {
        item {
            Text(text = "Primer Item")
            Text(text = "Primer Item")
            Text(text = "Primer Item")
            Text(text = "Primer Item")
            Text(text = "Primer Item")
            Text(text = "Primer Item")
        }
    }
}

@Composable
fun SimpleRecyclerViewItems() {
    LazyColumn {
        items(7) {
            Text(text = "Este es el item $it")
        }
    }
}

@Composable
fun SimpleRecyclerViewList() {
    val list = listOf("Jorge", "Jose", "Maria")
    LazyColumn {
        items(list) {
            Text(text = "Este es el item $it")
        }
    }
}

@Composable
fun SuperHeroViewRow() {
    val context = LocalContext.current
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(getSuperHeroes()) {
            ItemHero(superHero = it) { superHero ->
                Toast.makeText(
                    context, superHero.superHeroName, Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}

@Composable
fun SuperHeroViewColumn() {
    val context = LocalContext.current
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(getSuperHeroes()) { it ->
            ItemHero(superHero = it) { superHero ->
                Toast.makeText(
                    context, superHero.superHeroName, Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}

@Composable
fun SuperHeroViewGrid() {
    val context = LocalContext.current
    LazyVerticalGrid(columns = GridCells.Fixed(2), content = {
        items(getSuperHeroes()) { it ->
            ItemHero(superHero = it) { superHero ->
                Toast.makeText(
                    context, superHero.superHeroName, Toast.LENGTH_LONG
                ).show()
            }
        }
    })
}

@Composable
fun SuperHeroViewWithControls() {
    val state = rememberLazyListState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column {
        LazyColumn(
            state = state,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(getSuperHeroes()) { it ->
                ItemHero(superHero = it) { superHero ->
                    Toast.makeText(
                        context, superHero.superHeroName, Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        val showButton by remember {
            derivedStateOf {
                state.firstVisibleItemIndex > 0
            }
        }

        //state.firstVisibleItemScrollOffset

        if (showButton) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        state.animateScrollToItem(0)
                    }

                },
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(16.dp)
            ) {
                Text(text = "Soy un botón")
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SuperHeroStickyViewColumn() {
    val context = LocalContext.current
    val superHero: Map<String, List<SuperHero>> = getSuperHeroes()
        .groupBy { it.publisher }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        superHero.forEach {(publisher, itemsSuperHero) ->
            stickyHeader {
                Text(
                    text = publisher,
                    modifier = Modifier.fillMaxWidth().
                            background(Color.Green),
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
            items(itemsSuperHero) { it ->
                ItemHero(superHero = it) { superHero ->
                    Toast.makeText(
                        context, superHero.superHeroName, Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}

@Composable
fun ItemHero(superHero: SuperHero, onItemSelected: (SuperHero) -> Unit) {
    Card(
        border = BorderStroke(2.dp, Color.Red),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemSelected(superHero) }
    ) {
        Column {
            Image(
                painter = painterResource(id = superHero.photo),
                contentDescription = "SuperHero Avatar",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Text(
                text = superHero.superHeroName,
                modifier = Modifier.align(CenterHorizontally)
            )

            Text(
                text = superHero.realName,
                modifier = Modifier.align(CenterHorizontally),
                fontSize = 12.sp
            )

            Text(
                text = superHero.publisher,
                modifier = Modifier
                    .align(End)
                    .padding(4.dp),
                fontSize = 10.sp
            )
        }

    }
}

fun getSuperHeroes(): List<SuperHero> =
    listOf(
        SuperHero(
            "Spiderman",
            "Peter Parker",
            "Marvel",
            R.drawable.spiderman
        ),

        SuperHero(
            "Wolwerine",
            "Peter Parker",
            "Marvel",
            R.drawable.logan
        ),

        SuperHero(
            "Batman",
            "Peter Parker",
            "Marvel",
            R.drawable.batman
        ),

        SuperHero(
            "Thor",
            "Peter Parker",
            "Marvel",
            R.drawable.thor
        ),

        SuperHero(
            "Flash",
            "Peter Parker",
            "DC",
            R.drawable.flash
        ),

        SuperHero(
            "Green Lantern",
            "Peter Parker",
            "DC",
            R.drawable.green_lantern
        ),

        SuperHero(
            "Wonder Woman",
            "Peter Parker",
            "DC",
            R.drawable.wonder_woman
        )
    )