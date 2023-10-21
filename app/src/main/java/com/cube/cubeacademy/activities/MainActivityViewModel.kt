package com.cube.cubeacademy.activities

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cube.cubeacademy.MainApplication
import com.cube.cubeacademy.lib.di.Repository
import com.cube.cubeacademy.lib.models.Nominee
import com.cube.cubeacademy.utils.ApiResult
import com.cube.cubeacademy.utils.isOnline
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {


    private val _viewState: MutableStateFlow<ApiResult> = MutableStateFlow(ApiResult.Loading)
    val viewState = _viewState.asStateFlow()

    private val _nomineeList = MutableStateFlow(emptyList<Nominee>())
    val nomineeList
        get() = _nomineeList.asStateFlow()

    init {
        getAllNominees(application)
        viewModelScope.launch {
           if (application.isOnline()){
               try {
                   val response = repository.getAllNominations()
                   if (response.isSuccessful) {
                       val value = response.body()
                       val data = value?.data
                       _viewState.value = ApiResult.Success(data)
                   } else {
                       _viewState.value =
                           ApiResult.Error("One or more error has occurred. Try again later")
                   }
               } catch (exception: Exception) {
                   _viewState.value = ApiResult.Error(exception.message)
                   Timber.e(exception)
               }
           }
        }
    }

    private fun getAllNominees(application: Application){
        viewModelScope.launch {
           if (application.isOnline()){
               try {
                   val response = repository.getAllNominees()
                   if (response.isSuccessful) {
                       val value = response.body()
                       val data = value?.data
                       data?.let { nominees ->
                           _nomineeList.value = nominees
                       }
                   }
               } catch (exception: Exception) {
                   Timber.d(exception)
               }
           }
        }
    }


}