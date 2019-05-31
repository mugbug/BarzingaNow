package com.barzinga.restClient

import com.barzinga.viewmodel.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

interface ProductRecognitionService {

    @retrofit2.http.POST("predict")
    fun recogniseProduct(@retrofit2.http.Body parameter: String): io.reactivex.Observable<String>

    companion object Factory {
        fun create(): ProductRecognitionService {

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val httpClient = OkHttpClient.Builder()

            httpClient.addInterceptor(logging)

            val retrofit = retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.create())
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .client(httpClient.build())
                .baseUrl(Constants.BASE_ML_URL)
                .build()

            return retrofit.create(ProductRecognitionService::class.java)
        }
    }
}