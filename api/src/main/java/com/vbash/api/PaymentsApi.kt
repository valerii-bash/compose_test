package com.vbash.api

import arrow.core.Either
import arrow.retrofit.adapter.either.EitherCallAdapterFactory
import arrow.retrofit.adapter.either.networkhandling.CallError
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import retrofit2.http.GET

interface PaymentsApi {
    @GET("records/99ca2a2e-2beb-480d-bfe3-3724a8fab5ae")
    suspend fun getCurrencies(): Either<CallError, PaymentResponse>
}

fun PaymentsApi(
    okHttpClient: OkHttpClient,
    json: Json,
    url: String,
): PaymentsApi {
    return retrofit(okHttpClient, json, url).create()
}

fun retrofit(
    okHttpClient: OkHttpClient,
    json: Json,
    url: String,
): Retrofit {
    val contentType = "application/json".toMediaType()
    return Retrofit.Builder()
        .baseUrl(url)
        .addCallAdapterFactory(EitherCallAdapterFactory())
        .addConverterFactory(json.asConverterFactory(contentType))
        .client(okHttpClient)
        .build()
}
