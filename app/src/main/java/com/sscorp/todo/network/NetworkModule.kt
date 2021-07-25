package com.sscorp.todo.network

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    companion object {

        private const val BASE_URL = "https://d5dps3h13rv6902lp5c8.apigw.yandexcloud.net/"
        private const val TOKEN = "15de31c12d344ad7aea69e30be4790cf"
    }

    @Provides
    @Singleton
    fun provideTasksApiService(): TasksApiService {
        val interceptor = Interceptor {
            val requestBuilder = it.request().newBuilder()
            requestBuilder.header("Authorization", "Bearer $TOKEN")
            it.proceed(requestBuilder.build())
        }

        val httpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

        return retrofit.create(TasksApiService::class.java)
    }
}