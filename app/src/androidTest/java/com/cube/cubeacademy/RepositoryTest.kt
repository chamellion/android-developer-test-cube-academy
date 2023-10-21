package com.cube.cubeacademy

import com.cube.cubeacademy.lib.di.Repository
import com.cube.cubeacademy.lib.models.Nomination
import com.cube.cubeacademy.lib.models.Nominee
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID
import javax.inject.Inject



@HiltAndroidTest
class RepositoryTest {

	/*
	I could suggest this test be made a unit test without the complexity
	of starting the android framework or emulator. Repository could be tested as
	a unit test which makes test faster to run
	 */

	@get:Rule
	var hiltRule = HiltAndroidRule(this)

	@Inject
	lateinit var repository: Repository

	@Before
	fun setUp() {
		hiltRule.inject()
	}

	/*
	Using runBlocking coroutine block to make code block and
	wait as opposed to normal coroutine behaviour of suspending thread and
	resuming execution. RunBlocking runs test in a synchronous manner which is
	expected for writing test.
	 */

	@Test
	fun getNominationsTest() {
		// TODO: Write a test for getting all the nominations from the mock api
		runBlocking {
			//Given
			val response = repository.getAllNominations()
			//When
			val dataWrapper = response.body()
			val data = dataWrapper?.data
			//Then
			Truth.assertThat(data).isNotEmpty()
			Truth.assertThat(data?.get(0)).isInstanceOf(Nomination::class.java)
		}

	}

	@Test
	fun getNomineesTest() {
		// TODO: Write a test for getting all the nominees from the mock api
		runBlocking {
			//Given
			val response = repository.getAllNominees()

			//When
			val dataWrapper = response.body()
			val data = dataWrapper?.data

			//Then
			Truth.assertThat(data).isNotEmpty()
			Truth.assertThat(data?.get(0)).isInstanceOf(Nominee::class.java)
		}
	}

	@Test
	fun createNominationTest() {
		// TODO: Write a test for creating a new nomination using the mock api
		runBlocking {
			//Given
			val nomineeId = UUID.randomUUID().toString()
			val reason = "He is a pleasant guy"
			val process = "very_fair"

			//When
			val response = repository.createNomination(nomineeId, reason, process)
			val dataWrapper = response.body()
			val data = dataWrapper?.data

			//Then
			Truth.assertThat(data).isNotNull()
			Truth.assertThat(data?.process).contains("very_fair")
			Truth.assertThat(data?.reason).contains("He is a pleasant guy")
			Truth.assertThat(data).isInstanceOf(Nomination::class.java)
		}
	}
}