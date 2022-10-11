@file:OptIn(ExperimentalMaterial3Api::class)

package com.taeyeon.dongdae

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.taeyeon.dongdae.ui.theme.DongdaeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DongdaeTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text(text = stringResource(id = R.string.app_name)) }
                        )
                    },
                    bottomBar = {
                        NavigationBar() {
                            NavigationBarItem(
                                selected = true,
                                onClick = { /*TODO*/ },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null
                                    )
                                },
                                label = {
                                    Text(text = "afd")
                                }
                            )
                            NavigationBarItem(
                                selected = true,
                                onClick = { /*TODO*/ },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null
                                    )
                                },
                                label = {
                                    Text(text = "afd")
                                }
                            )
                        }
                    }
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize().padding(it),
                        color = MaterialTheme.colorScheme.background
                    ) {

                        Greeting("Android")
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DongdaeTheme {
        Greeting("Android")
    }
}