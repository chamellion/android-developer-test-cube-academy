package com.cube.cubeacademy.activities.createnominationactivity

import androidx.lifecycle.ViewModel
import com.cube.cubeacademy.lib.di.Repository
import com.cube.cubeacademy.lib.models.Nomination
import com.cube.cubeacademy.lib.models.Nominee
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateNominationViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {


    suspend fun createUserNomination(
        nomineeName: String,
        reason: String,
        process: String,
        nomineeList: List<Nominee>,
        onUserNominationCreated: (Nomination?) -> Unit
    ) {
        val nameArray = nomineeName.split(" ")
        /*Accessing the arrays without checking if index exist because it is assumed that all
        nominees have a first and last name which is a prerequisite by the API and I also checked
        name list in my logs, therefore splitting by space should give first and last name
         */
        val nomineeData = nomineeList.find { nominee: Nominee ->
            nominee.firstName == nameArray[0] && nominee.lastName == nameArray[1]
        }
        nomineeData?.let {nominee ->
           val response = repository.createNomination(nominee.nomineeId, reason, process)
            if (response.isSuccessful){
                val body = response.body()
                val data = body?.data
                onUserNominationCreated.invoke(data)
            }
        }
    }

}