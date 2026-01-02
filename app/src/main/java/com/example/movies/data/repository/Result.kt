package com.example.movies.data.repository

class Result<B, E>(val body: B?, val error: E? = null) {
    val isSuccess: Boolean
        get() = body != null && error == null
}