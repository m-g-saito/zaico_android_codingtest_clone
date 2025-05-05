package jp.co.zaico.codingtest.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.zaico.codingtest.api.ApiConfig
import jp.co.zaico.codingtest.api.ZaicoApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * API関連の依存関係を提供するHiltモジュール
 * HTTPクライアントとRetrofitインスタンスおよびAPIインターフェースを提供
 */
@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    /**
     * 認証ヘッダーを含むOkHttpClientを提供
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = Interceptor { chain ->
            val request: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${ApiConfig.BEARER_TOKEN}")
                .addHeader("Content-Type", ApiConfig.CONTENT_TYPE)
                .build()
            chain.proceed(request)
        }
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    /**
     * Retrofitインスタンスを提供
     * GsonConverterFactoryを用いてJSONをパースする
     * @param okHttpClient HTTP通信に使用するクライアント
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * ZaicoApiインターフェースの実装を提供
     * Retrofitを通じてAPI呼び出しを行うために使用
     * @param retrofit Retrofitのインスタンス
     */
    @Provides
    @Singleton
    fun provideZaicoApi(retrofit: Retrofit): ZaicoApi {
        return retrofit.create(ZaicoApi::class.java)
    }
}