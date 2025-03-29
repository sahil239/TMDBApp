package dev.sahildesai.tmdbapp.data.api

import com.google.gson.annotations.SerializedName

data class GetMovieResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResult: Int,
    @SerializedName("results")
    val movies: List<Movie>?
)

data class Movie(
    @SerializedName("id")
    val id: Long,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("video")
    val video: Boolean,
    @SerializedName("genre_ids")
    val genreIds : List<Int>,
    @SerializedName("vote_average")
    val voteAvg: Float,
    @SerializedName("vote_count")
    val voteCount: Long,
    @SerializedName("budget")
    val budget: Int,
    @SerializedName("belongs_to_collection")
    val belongsToCollection: BelongsToCollection,
    @SerializedName("genres")
    val genres: List<Genres>,
    @SerializedName("homepage")
    val homePage: String,
    @SerializedName("runtime")
    val runTime: Int
)

data class BelongsToCollection(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("backdrop_path")
    val backdropPath: String
)

data class Genres(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String
)

/*
{
  "adult": false,
  "backdrop_path": "/mDfJG3LC3Dqb67AZ52x3Z0jU0uB.jpg",

  "homepage": "https://www.marvel.com/movies/avengers-infinity-war",
  "id": 299536,
  "imdb_id": "tt4154756",
  "origin_country": [
    "US"
  ],
  "original_language": "en",
  "original_title": "Avengers: Infinity War",
  "overview": "As the Avengers and their allies have continued to protect the world from threats too large for any one hero to handle, a new danger has emerged from the cosmic shadows: Thanos. A despot of intergalactic infamy, his goal is to collect all six Infinity Stones, artifacts of unimaginable power, and use them to inflict his twisted will on all of reality. Everything the Avengers have fought for has led up to this moment - the fate of Earth and existence itself has never been more uncertain.",
  "popularity": 27.6338,
  "poster_path": "/7WsyChQLEftFiDOVTGkv3hFpyyt.jpg",
  "production_companies": [
    {
      "id": 420,
      "logo_path": "/hUzeosd33nzE5MCNsZxCGEKTXaQ.png",
      "name": "Marvel Studios",
      "origin_country": "US"
    }
  ],
  "production_countries": [
    {
      "iso_3166_1": "US",
      "name": "United States of America"
    }
  ],
  "release_date": "2018-04-25",
  "revenue": 2052415039,
  "runtime": 149,
  "spoken_languages": [
    {
      "english_name": "English",
      "iso_639_1": "en",
      "name": "English"
    },
    {
      "english_name": "Xhosa",
      "iso_639_1": "xh",
      "name": ""
    }
  ],
  "status": "Released",
  "tagline": "Destiny arrives all the same.",
  "title": "Avengers: Infinity War",
  "video": false,
  "vote_average": 8.2,
  "vote_count": 30340
}
* */