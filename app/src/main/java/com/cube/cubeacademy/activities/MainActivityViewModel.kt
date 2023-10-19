package com.cube.cubeacademy.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cube.cubeacademy.lib.di.Repository
import com.cube.cubeacademy.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val repository: Repository): ViewModel() {


    private val _viewState: MutableStateFlow<ApiResult> = MutableStateFlow(ApiResult.Loading)
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val response = repository.getAllNominations()
                if (response.isSuccessful){
                    val value = response.body()
                    val data = value?.data
                    _viewState.value = ApiResult.Success(data)
                }else{
                    _viewState.value = ApiResult.Error("One or more error has occurred. Try again later")
                }
            }catch (exception: Exception){
                _viewState.value = ApiResult.Error(exception.message)
                Timber.e(exception)
            }

        }
    }




}