package com.example.photogallery

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhotoGalleryApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoGalleryApp() {
    val imageUrls = remember { mutableStateListOf<String>() }
    var expandedImageUrl by remember { mutableStateOf<String?>(null) } // State to track the expanded image URL

    // Load initial set of images
    LaunchedEffect(Unit) {
        loadMoreImages(imageUrls)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Photo Gallery") }
            )
        }
    ) { paddingValues ->
        Box(Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(8.dp)
            ) {
                itemsIndexed(imageUrls) { index, imageUrl ->
                    ImageCard(imageUrl) {
                        expandedImageUrl = imageUrl // Set the expanded image URL on click
                    }

                    // Load more images when the user scrolls near the end
                    if (index == imageUrls.size - 1) {
                        LaunchedEffect(Unit) {
                            loadMoreImages(imageUrls)
                        }
                    }
                }
            }

            // Show expanded image in a dialog if an image URL is set
            expandedImageUrl?.let { imageUrl ->
                ExpandedImageDialog(imageUrl) {
                    expandedImageUrl = null // Clear the expanded image URL when the dialog is dismissed
                }
            }
        }
    }
}

// Function to generate a random Picsum image URL
fun generateRandomImageUrl(): String {
    val randomSeed = System.currentTimeMillis() + (0..1000).random()
    return "https://picsum.photos/seed/$randomSeed/200/300"
}

// Function to load more images
fun loadMoreImages(imageUrls: MutableList<String>) {
    repeat(10) {
        val imageUrl = generateRandomImageUrl()
        Log.d("PhotoGalleryApp", "Generated URL: $imageUrl")
        imageUrls.add(imageUrl)
    }
}

@Composable
fun ImageCard(imageUrl: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .size(150.dp) // Fixed size for each image
            .clickable { onClick() } // Handle image click
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Image",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ExpandedImageDialog(imageUrl: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Expanded Image",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}