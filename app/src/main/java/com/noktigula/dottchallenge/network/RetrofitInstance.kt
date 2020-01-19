package com.noktigula.dottchallenge.network

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.noktigula.dottchallenge.R
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.foursquare.com/v2/"

object RetrofitInstance {
    private lateinit var okHttpClient:OkHttpClient
    private lateinit var retrofit:Retrofit
    lateinit var foursquareApi:FoursquareApi

    private fun initOkHttp(foursquareId:String, foursquareSecret:String) {
        okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor { chain ->
                val original = chain.request()
                val originalHttpUrl = original.url()

                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("client_id", foursquareId)
                    .addQueryParameter("client_secret", foursquareSecret)
                    .addQueryParameter("v", "20200119")
                    .build()

                // Request customization: add request headers
                val requestBuilder = original.newBuilder()
                    .url(url)

                val request = requestBuilder.build()
                chain.proceed(request)

            }
            .build()
    }

    private fun initRetrofit(foursquareId: String, foursquareSecret: String) {
        initOkHttp(foursquareId, foursquareSecret)
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun initApi(foursquareId: String, foursquareSecret: String) {
        initRetrofit(foursquareId, foursquareSecret)
        foursquareApi = retrofit.create(FoursquareApi::class.java)
    }
}
