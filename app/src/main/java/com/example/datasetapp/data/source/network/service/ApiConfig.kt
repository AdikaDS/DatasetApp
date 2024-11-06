package com.example.datasetapp.data.source.network.service

import android.util.Base64
import com.example.datasetapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiConfig {
    companion object {
        private val okHttpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .addInterceptor(BasicAuthInterceptor("DATASET_APP", "yu6W742s9BcEAvFm3VQCNgZUzjfeqhRDpSrGPYdM8"))
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()
        }

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }

        val apiService: DatasetApiService by lazy {
            retrofit.create(DatasetApiService::class.java)
        }
    }

    class BasicAuthInterceptor(username: String, password: String) : Interceptor {
        private val credentials: String = "Basic " + Base64.encodeToString(
            "$username:$password".toByteArray(), Base64.NO_WRAP
        )

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader("Authorization", credentials)
                .build()
            return chain.proceed(request)
        }
    }
}
/*
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "https://sbf-gcs-api.bmsecure.id/"

    private val client by lazy {
        val username = "DATASET_APP"
        val password = "yu6W742s9BcEAvFm3VQCNgZUzjfeqhRDpSrGPYdM8"
        val authToken = Credentials.basic(username, password)

        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", authToken)
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    val api: DatasetApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DatasetApiService::class.java)
    }
}*/

