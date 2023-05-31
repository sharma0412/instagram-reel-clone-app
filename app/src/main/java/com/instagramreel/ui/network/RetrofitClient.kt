package com.instagramreel.ui.network


import android.content.Context
import com.instagramreel.ui.base.BaseActivity
import com.instagramreel.ui.sharedPref.AppPrefs
import com.instagramreel.ui.utils.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient  {

    init    {
        val context : Context ? =null
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            .callTimeout(200, TimeUnit.MINUTES)
            .connectTimeout(200, TimeUnit.SECONDS)
            .readTimeout(200, TimeUnit.SECONDS)
            .writeTimeout(200, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor(ResponseInterceptor())

        httpClient.addInterceptor(Interceptor { chain ->
            val request: Request
            val token= AppPrefs(BaseActivity.getContext()).getStringKey("token")
            println("token $token")
            request = if(!token.isNullOrEmpty()) {
                chain.request().newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "text/plain")
                    .addHeader("Authorization", "Bearer $token").build()

            } else {
                chain.request().newBuilder()
                    .build()
            }
            chain.proceed(request)
        })

        retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }


    fun getapi(): ApiInterface {
        return retrofit?.create(ApiInterface::class.java)!!

    }

    class ResponseInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val response = chain.proceed(chain.request())
            return response.newBuilder()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .build()
        }
    }

    companion object{
        private  var retrofit : Retrofit?=null
        private var instance: RetrofitClient? = null

        @Synchronized
        fun getInstance(): RetrofitClient {
            if(instance ==null){
                instance = RetrofitClient()
            }
            return instance as RetrofitClient


        }
    }
}