package com.finter.india.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class HttpRequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder().url(originalRequest.url).build()
        Timber.d(request.toString())

        val response = chain.proceed(request)
        Log.d("MyApp", "Code : " + response.code)
        return if (response.code == 403) {
            // Magic is here ( Handle the error as your way )
            response
        } else response
    }
}