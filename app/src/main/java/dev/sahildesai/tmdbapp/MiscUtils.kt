package dev.sahildesai.tmdbapp

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


fun formatDate(localDate: LocalDate): String = DateTimeFormatter
    .ofPattern("E d MMM, yyyy")
    .withLocale(Locale.getDefault())
    .format(localDate)