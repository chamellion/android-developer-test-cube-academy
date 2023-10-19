package com.cube.cubeacademy.lib.di

import com.cube.cubeacademy.lib.api.ApiService
import com.cube.cubeacademy.lib.models.DataWrapper
import com.cube.cubeacademy.lib.models.Nomination
import com.cube.cubeacademy.lib.models.Nominee
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(private val api: ApiService) {
	// TODO: Add additional code if you need it

	suspend fun getAllNominations(): Response<DataWrapper<List<Nomination>>> {
		// TODO: Write the code to fetch the list nominations from the api
		return api.getAllNominations()
	}

	suspend fun getAllNominees(): Response<DataWrapper<List<Nominee>>> {
		// TODO: Write the code to fetch list of all nominees from the api
		return api.getAllNominees()
	}

	suspend fun createNomination(nomineeId: String, reason: String, process: String): Nomination? {
		// TODO: Write the code to create a new nomination using the api
		return null
	}
}