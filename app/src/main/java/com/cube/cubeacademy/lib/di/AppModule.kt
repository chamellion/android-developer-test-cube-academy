package com.cube.cubeacademy.lib.di

import com.cube.cubeacademy.BuildConfig
import com.cube.cubeacademy.lib.api.ApiService
import com.cube.cubeacademy.lib.api.AuthTokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
	@Singleton
	@Provides
	fun provideApi(): ApiService = OkHttpClient().newBuilder().apply {
		//Added logging interceptor to show network traffic in logcat(for debugging)
		val httpInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
		addInterceptor(AuthTokenInterceptor())
		addInterceptor(httpInterceptor)
	}.build().let {
		Retrofit.Builder()
			.client(it)
			.addConverterFactory(GsonConverterFactory.create())
			.baseUrl(BuildConfig.API_URL).build().create(ApiService::class.java)
	}

	@Singleton
	@Provides
	fun provideRepository(api: ApiService): Repository = Repository(api)
}