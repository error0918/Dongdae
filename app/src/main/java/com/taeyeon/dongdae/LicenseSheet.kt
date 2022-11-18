package com.taeyeon.dongdae

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.taeyeon.core.Core

object LicenseSheet {

    @Composable
    fun License() {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(com.taeyeon.core.License.Licenses) { license ->
                LicenseViewer(
                    license = license
                )
            }
        }
    }

    @Composable
    fun LicenseViewer(
        license: com.taeyeon.core.License.License
    ) {
        LicenseViewer(
            title = license.title,
            license = license.license,
            link = license.link
        )
    }

    @Composable
    fun LicenseViewer(
        title: String,
        license: String? = null,
        link: String? = null,
        defaultExpanded: Boolean = false
    ) {
        var expanded by rememberSaveable { mutableStateOf(defaultExpanded) }

        Column {
            TextButton(
                onClick = {
                    expanded = !expanded
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape,
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(0.dp)
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) null else null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .size(
                                LocalDensity.current.run {
                                    MaterialTheme.typography.labelMedium.fontSize
                                        .toPx()
                                        .toDp()
                                }
                                        + ButtonDefaults.TextButtonContentPadding.calculateTopPadding()
                                        + ButtonDefaults.TextButtonContentPadding.calculateBottomPadding()
                            )
                            .align(Alignment.CenterEnd)
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {

                Column {

                    Divider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (license != null) {
                        SelectionContainer {
                            Text(
                                text = license,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    if (link != null) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                                Core.getActivity().startActivity(intent)
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(text = "모두 보기") // TODO
                        }
                    }

                }
            }

            Divider(modifier = Modifier.fillMaxWidth())
        }
    }

}