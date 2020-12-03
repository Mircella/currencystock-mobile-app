package net.vizja.currencystock

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@Suppress("unused")
object NetworkClient {
    var retrofit: Retrofit? = null

    fun getClient(logLevel: LogLevel): Retrofit {

        val interceptor = HttpLoggingInterceptor()
        when (logLevel) {
            LogLevel.LOG_NOT_NEEDED ->
                interceptor.level = HttpLoggingInterceptor.Level.NONE
            LogLevel.LOG_REQ_RES ->
                interceptor.level = HttpLoggingInterceptor.Level.BASIC
            LogLevel.LOG_REQ_RES_BODY_HEADERS ->
                interceptor.level = HttpLoggingInterceptor.Level.BODY
            LogLevel.LOG_REQ_RES_HEADERS_ONLY ->
                interceptor.level =
                    HttpLoggingInterceptor.Level.HEADERS
        }

        val client = OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.MINUTES)
            .writeTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .addInterceptor(interceptor)
            .build()
        if (null == retrofit) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder()
                    .setLenient()
                    .create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
        }

        return retrofit!!
    }

    @Provides
    fun logLevel(): LogLevel {
        return LogLevel.LOG_REQ_RES_BODY_HEADERS
    }

    @Provides
    @Reusable
    @JvmStatic
    fun getCurrencyStockApiService(logLevel: LogLevel) =
        getClient(logLevel).create(CurrencyStockApiService ::class.java)

}