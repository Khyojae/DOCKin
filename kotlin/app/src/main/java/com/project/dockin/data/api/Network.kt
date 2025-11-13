package com.project.dockin.data.api

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.project.dockin.BuildConfig
object Network {
    fun retrofit(ctx: Context): Retrofit {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val ok = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(AuthHeaderInterceptor(TokenStore(ctx)))
            .build()

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)   // 여기만 믿고 가기
            .client(ok)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}