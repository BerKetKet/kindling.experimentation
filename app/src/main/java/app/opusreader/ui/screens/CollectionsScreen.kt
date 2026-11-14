package app.opusreader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.opusreader.data.CollectionItem
import app.opusreader.data.SampleBooks
import app.opusreader.ui.components.OpusSearchBar
import app.opusreader.ui.components.OpusTopBar
import app.opusreader.ui.theme.OpusAccentBlue
import app.opusreader.ui.theme.OpusBackground
import app.opusreader.ui.theme.OpusDivider
import app.opusreader.ui.theme.OpusReaderTheme
import app.opusreader.ui.theme.OpusSpacing
import app.opusreader.ui.theme.OpusSurface
import app.opusreader.ui.theme.OpusTextPrimary
import app.opusreader.ui.theme.OpusTextSecondary

@Composable
fun CollectionsScreen(
    collections: List<CollectionItem> = emptyList(),
    onBackClick: () -> Unit = {},
    onCreateCollection: (String) -> Unit = {},
    onSortClick: () -> Unit = {},
    onCollectionClick: (CollectionItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var showCreateDialog by remember { mutableStateOf(false) }
    var newCollectionName by remember { mutableStateOf("") }

    val filteredCollections = remember(collections, searchQuery) {
        if (searchQuery.isBlank()) collections
        else collections.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = {
                showCreateDialog = false
                newCollectionName = ""
            },
            title = {
                Text(
                    text = "New Collection",
                    color = OpusTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                OutlinedTextField(
                    value = newCollectionName,
                    onValueChange = { newCollectionName = it },
                    placeholder = { Text("Collection name", color = OpusTextSecondary) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = OpusTextPrimary,
                        unfocusedTextColor = OpusTextPrimary,
                        focusedBorderColor = OpusAccentBlue,
                        unfocusedBorderColor = OpusDivider
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newCollectionName.isNotBlank()) {
                            onCreateCollection(newCollectionName.trim())
                            showCreateDialog = false
                            newCollectionName = ""
                        }
                    }
                ) {
                    Text("CREATE", color = OpusAccentBlue, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showCreateDialog = false
                        newCollectionName = ""
                    }
                ) {
                    Text("CANCEL", color = OpusTextSecondary)
                }
            },
            containerColor = OpusSurface
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OpusBackground)
    ) {
        OpusTopBar(
            title = "Collections",
            navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
            onNavigationClick = onBackClick,
            actions = {
                IconButton(onClick = { showCreateDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Collection",
                        tint = OpusTextPrimary
                    )
                }
                IconButton(onClick = onSortClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Sort,
                        contentDescription = "Sort Collections",
                        tint = OpusTextPrimary
                    )
                }
            }
        )

        OpusSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholder = "Search Collections"
        )

        if (filteredCollections.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(OpusSpacing.xlarge)
                ) {
                    Text(
                        text = "There are no collections in your library.",
                        color = OpusTextSecondary,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(OpusSpacing.large))

                    OutlinedButton(
                        onClick = { showCreateDialog = true },
                        modifier = Modifier.height(OpusSpacing.minTouchTarget)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = OpusTextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(OpusSpacing.small))
                        Text(
                            text = "CREATE A NEW COLLECTION",
                            color = OpusTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp)
            ) {
                items(filteredCollections, key = { it.id }) { col ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCollectionClick(col) }
                            .padding(horizontal = OpusSpacing.standard, vertical = OpusSpacing.medium),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = null,
                            tint = OpusTextSecondary,
                            modifier = Modifier.size(28.dp)
                        )

                        Spacer(modifier = Modifier.width(OpusSpacing.medium))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = col.name,
                                color = OpusTextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${col.bookCount} items",
                                color = OpusTextSecondary,
                                fontSize = 13.sp
                            )
                        }

                        IconButton(onClick = { /* collection menu */ }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Collection options",
                                tint = OpusTextSecondary
                            )
                        }
                    }

                    HorizontalDivider(
                        color = OpusDivider.copy(alpha = 0.5f),
                        thickness = 0.5.dp,
                        modifier = Modifier.padding(start = 56.dp)
                    )
                }
            }
        }
    }
}

@Preview(name = "08 Collections Screen Preview", showBackground = true, backgroundColor = 0xFF101112)
@Composable
fun CollectionsScreenPreview() {
    OpusReaderTheme {
        CollectionsScreen(collections = SampleBooks.sampleCollections)
    }
}
