package com.cube.cubeacademy.di

import com.cube.cubeacademy.lib.api.ApiService
import com.cube.cubeacademy.lib.models.DataWrapper
import com.cube.cubeacademy.lib.models.Nomination
import com.cube.cubeacademy.lib.models.Nominee
import retrofit2.Response

class MockApiService : ApiService {

	//Modified test to wrap mocked data as response objects from retrofit
	override suspend fun getAllNominations(): Response<DataWrapper<List<Nomination>>> {

		return Response.success( DataWrapper(
			listOf(
				Nomination("1", "1", "reason", "process", "2023-10-11", "2023-11-11"),
				Nomination("2", "2", "reason", "process", "2023-10-11", "2023-11-11"),
				Nomination("1", "3", "reason", "process", "2023-10-11", "2023-11-11"),
				Nomination("2", "4", "reason", "process", "2023-10-11", "2023-11-11"),
			)
		))
	}

	override suspend fun getAllNominees(): Response<DataWrapper<List<Nominee>>> {
		return Response.success(DataWrapper(
			listOf(
				Nominee("1", "FirstTest1", "LastTest1"),
				Nominee("2", "FirstTest2", "LastTest2"),
				Nominee("3", "FirstTest3", "LastTest3"),
			)
		))
	}

	override suspend fun createNomination(nomineeId: String, reason: String, process: String): Response<DataWrapper<Nomination>> {
		return Response.success(DataWrapper(
			Nomination("3", nomineeId, reason, process, "2023-10-11", "2023-11-12")
		))
	}
}