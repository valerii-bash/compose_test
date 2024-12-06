package com.vbash.compose.di

import com.vbash.api.PaymentsApi
import com.vbash.compose.BuildConfig
import com.vbash.compose.home.domain.PaymentsRepository
import com.vbash.compose.home.data.PaymentsRepository as RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideExchangeApi(okHttpClient: OkHttpClient, json: Json): PaymentsApi {
        return PaymentsApi(
            okHttpClient = okHttpClient,
            json = json,
            url = BuildConfig.API_BASE_URL,
        )
    }

    @Provides
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
        }
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging =
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO
}

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindRepository(impl: RepositoryImpl): PaymentsRepository

}