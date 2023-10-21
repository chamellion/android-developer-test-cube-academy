package com.cube.cubeacademy.lib.api

import com.cube.cubeacademy.lib.models.DataWrapper
import com.cube.cubeacademy.lib.models.Nomination
import com.cube.cubeacademy.lib.models.Nominee
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

	/*
	Response from api were wrapped in a retrofit
	Response wrapper which will provide more details
	about if http status codes and error returned back from
	server
	 */
	@GET("api/nomination")
	suspend fun getAllNominations(): Response<DataWrapper<List<Nomination>>>

	@GET("api/nominee")
	suspend fun getAllNominees(): Response<DataWrapper<List<Nominee>>>
	
	@FormUrlEncoded
	@POST("api/nomination")
	suspend fun createNomination(
		@Field("nominee_id") nomineeId: String,
		@Field("reason") reason: String,
		@Field("process") process: String
	): Response<DataWrapper<Nomination>>

}