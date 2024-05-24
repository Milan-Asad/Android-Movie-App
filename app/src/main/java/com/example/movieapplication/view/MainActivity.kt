package com.example.movieapplication.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.movieapplication.ui.theme.MovieApplicationTheme
import coil.compose.rememberImagePainter
import com.example.movieapplication.model.Result
import com.example.movieapplication.view.theme.MovieApplicationTheme
import com.example.movieapplication.viewmodel.MovieViewModel
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost


// MAIN ACTIVITY + NAVIGATION
class MainActivity : ComponentActivity() {

    // API KEY + VIEW MODEL VARIABLES
    private val apiKey = "c9856d0cb57c3f14bf75bdc6c063b8f3"
    private val movieViewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieApplicationTheme {

                // GRADIENT CUSTOM COLOUR
                val customColor1 = Color(0xFF000000)
                val customColor2 = Color(0xFF1A1A1A)

                // BRUSH FOR GRADIENT
                val gradientBrush = Brush.verticalGradient(
                    colors = listOf(customColor1, customColor2)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(gradientBrush)
                ) {

                    // NAV CONTROLLER (HOME)
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "movies") {

                        // HOME
                        composable("movies") {
                            MoviesScreen(movieViewModel, apiKey, navController)
                        }

                        // MOVIE DETAIL SCREEN
                        composable("movieDetails/{movieId}") { backStackEntry ->
                            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
                            movieId?.let {
                                val movie = movieViewModel.getMovieById(it)
                                if (movie != null) {
                                    MovieDetailsScreen(movie, navController)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


// SINGLE MOVIE ITEM DISPLAYED IN A CARD FORMAT
@Composable
fun MovieItem(movie: Result, modifier: Modifier = Modifier, onItemClick: (Result) -> Unit) {
    val imageUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"
    val customGrey = Color(0xFF212121)


    Card(
        // CARD VIEW GRAY COLOUR
        backgroundColor = customGrey,
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .clickable { onItemClick(movie) }
            .padding(5.dp)
            .height(350.dp)
    ) {
        Column(modifier = Modifier.padding(0.dp)) {
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = movie.title,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )

            Text(
                text = movie.title,
                color = Color.White,
                style = MaterialTheme.typography.h6.copy(fontSize = 16.sp),
                modifier = Modifier.padding(top = 8.dp, start = 10.dp, end = 10.dp)
            )
            Text(
                text = movie.release_date.substring(0, 4),
                color = Color.White,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp, start = 10.dp, end = 10.dp)
            )
        }
    }
}

// MAIN SCREEN TO DISPLAY MOVIES
@Composable
fun MoviesScreen(
    viewModel: MovieViewModel,
    apiKey: String,
    navController: NavHostController
) {
    val movies by viewModel.movies.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)

    // FETCHES MOVIE
    LaunchedEffect(Unit) {
        viewModel.fetchMovies(apiKey)
    }

    Column {
        // RED BAR AT TOP
        Surface(
            color = Color.Red,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
            ) {
                // SHOWTIME TEXT
                Text(
                    text = "SHOWTIME",
                    color = Color.White,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        // LOADING ICON
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {

            // ***MAIN SECTION***

            // MAIN LAZY COLUMN
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp, vertical = 10.dp)
            ) {
                item {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            // DAILY PICKS FOR YOU TEXT
                            text = "DAILY PICKS FOR YOU",
                            color = Color.White,
                            style = MaterialTheme.typography.h5.copy(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(top = 15.dp, bottom = 20.dp)
                        )
                    }
                }

                // NUMBER OF MOVIES IN A ROW
                items(movies.chunked(2)) { movieRow ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(bottom = 10.dp)
                    ) {

                        // MOVIE DETAILS ON CLICK
                        for (movie in movieRow) {
                            MovieItem(movie = movie, modifier = Modifier.weight(1f)) { clickedMovie ->
                                navController.navigate("movieDetails/${clickedMovie.id}")
                            }
                        }
                    }
                }
            }
        }
    }
}

// DETAIL SCREEN *FETCHING OVERVIEW WHEN CLICKING A MOVIE*
@Composable
fun MovieDetailsScreen(movie: Result, navController: NavHostController) {
    val imageUrl = "https://image.tmdb.org/t/p/w500${movie.backdrop_path}"

    Column(modifier = Modifier.fillMaxSize()) {
        // RED BAR
        Surface(
            color = Color.Red,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text(
                    text = "SHOWTIME",
                    color = Color.White,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.width(48.dp))
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .clip(shape = RoundedCornerShape(6.dp))
                ) {
                    Image(
                        painter = rememberImagePainter(imageUrl),
                        contentDescription = movie.title,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                    )
                }

                // Display movie details
                // Display movie details
                Text(
                    text = movie.title,
                    color = Color.White,
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 18.dp, start = 10.dp, end = 10.dp)
                )
                MovieDetailItem("Release Date", movie.release_date)
                MovieDetailItem("Popularity", movie.popularity.toString())
                MovieDetailItem("Vote Average", movie.vote_average.toString())
                MovieDetailItem("Vote Count", movie.vote_count.toString())
                MovieDetailItem("", movie.overview, Modifier.padding(top = 20.dp))

            }
        }
    }
}

// TEXT STYLE FOR ABOVE
@Composable
fun MovieDetailItem(label: String, value: String, modifier: Modifier = Modifier) {
    val labelText = if (label.isNotEmpty()) "$label: " else ""
    Text(
        text = "$labelText$value",
        color = Color.White,
        style = MaterialTheme.typography.body1.copy(fontSize = 17.sp),
        modifier = modifier.padding(bottom = 1.dp, start = 10.dp, end = 10.dp)
    )
}
