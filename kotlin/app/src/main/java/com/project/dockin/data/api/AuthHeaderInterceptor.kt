package com.project.dockin.data.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthHeaderInterceptor(private val store: TokenStore) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val b = chain.request().newBuilder()
        store.jwt?.let { b.addHeader("Authorization", "Bearer $it") }
        return chain.proceed(b.build())
    }
}