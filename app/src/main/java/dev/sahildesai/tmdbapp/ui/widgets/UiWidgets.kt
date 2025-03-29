package dev.sahildesai.tmdbapp.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import dev.sahildesai.tmdbapp.R

@Composable
fun LoadingData(){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

data class ErrorData(
    val message: String,
    val buttonText: String = "Back",
    val action: () -> Unit
)

@Composable
fun ShowError(errorData: ErrorData){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column (modifier = Modifier.fillMaxWidth().padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
            Text(text = errorData.message, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.fillMaxWidth().height(10.dp))
            Button(onClick = errorData.action, modifier = Modifier.fillMaxWidth()) {
                Text(text = errorData.buttonText, fontSize = 15.sp)
            }
        }
    }
}

@Composable
fun LoadImageFromUrl(
    modifier: Modifier = Modifier,
    title: String,
    imageUrl: String,
    contentScale: ContentScale = ContentScale.Crop
){
    AsyncImage(
        model = imageUrl,
        error = painterResource(R.drawable.ic_error),
        contentDescription = title,
        modifier = modifier,
        contentScale = contentScale,
        placeholder = painterResource(R.drawable.ic_downloading),
    )
}