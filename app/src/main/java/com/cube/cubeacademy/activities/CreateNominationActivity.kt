package com.cube.cubeacademy.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import cn.pedant.SweetAlert.SweetAlertDialog
import com.cube.cubeacademy.R
import com.cube.cubeacademy.databinding.ActivityCreateNominationBinding
import com.cube.cubeacademy.lib.di.Repository
import com.cube.cubeacademy.lib.models.Nomination
import com.cube.cubeacademy.lib.models.Nominee
import com.cube.cubeacademy.utils.ApiResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CreateNominationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNominationBinding

    private val createNominationViewModel: CreateNominationViewModel by viewModels()

    @Inject
    lateinit var repository: Repository

    /*Suppressing cast warning because type has been checked
    on postman and its a list of Nomination
     */
    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_CubeAcademy)

        binding = ActivityCreateNominationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                createNominationViewModel.viewState.collect { apiResult ->
                    when (apiResult) {
                        is ApiResult.Loading -> {
                            binding.circularIndicator.visibility = View.VISIBLE
                        }

                        is ApiResult.Error -> {
                            val errorMessage = apiResult.error
                            binding.circularIndicator.visibility = View.GONE
                            SweetAlertDialog(
                                applicationContext,
                                SweetAlertDialog.ERROR_TYPE
                            ).apply {
                                titleText = "Error"
                                contentText = errorMessage ?: "Error loading nominee list"
                                setConfirmButton("Ok") {
                                    it.dismissWithAnimation()
                                }
                                show()
                            }
                        }

                        is ApiResult.Success<*> -> {
                            binding.circularIndicator.visibility = View.GONE
                            if (apiResult.data is List<*>) {
                                val nominationLists = apiResult.data as? List<Nominee>
                                nominationLists?.let { nominationList ->
                                    Timber.d("Nominee list are $nominationList")
                                    val nomineeNames = nominationList.map {
                                        "${it.firstName} ${it.lastName}"
                                    }
                                    populateUI(
                                        binding,
                                        nomineeNames,
                                        createNominationViewModel,
                                        nominationList
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private fun populateUI(
        binding: ActivityCreateNominationBinding,
        nomineeName: List<String>,
        viewModel: CreateNominationViewModel,
        nomineeList: List<Nominee>
    ) {
        /**
         * TODO: Populate the form after having added the views to the xml file (Look for TODO comments in the xml file)
         * 		 Add the logic for the views and at the end, add the logic to create the new nomination using the api
         * 		 The nominees drop down list items should come from the api (By fetching the nominee list)
         */
        var feedbackValue = ""
        binding.dropdownMenu.setAdapter(
            ArrayAdapter(
                applicationContext,
                android.R.layout.simple_dropdown_item_1line,
                nomineeName
            )
        )
        binding.radioGroupContainer.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.very_unfair -> {
                    feedbackValue = "very_unfair"
                }

                R.id.fair -> {
                    feedbackValue = "fair"
                }

                R.id.not_sure -> {
                    feedbackValue = "not_sure"
                }

                R.id.good -> {
                    feedbackValue = "unfair"
                }

                R.id.very_good -> {
                    feedbackValue = "very_fair"
                }
            }
        }
        binding.submitButton.setOnClickListener {
            lifecycleScope.launch {
                val reason = binding.reasonForChoosing.text.toString().trim()
                val chosenNominee = binding.dropdownMenu.text.toString().trim()
                if (reason.isBlank() || feedbackValue.isBlank() || chosenNominee.isBlank()) {
                    SweetAlertDialog(this@CreateNominationActivity, SweetAlertDialog.ERROR_TYPE).apply {
                        titleText = "Validation Error"
                        contentText = "Please make sure all fields are correctly filled"
                        setConfirmButton("Ok") {
                            it.dismissWithAnimation()
                        }
                        show()
                    }
                    Timber.d("I got here")
                } else {
                    try {
                        viewModel.createUserNomination(
                            nomineeName = chosenNominee,
                            reason = reason,
                            process = feedbackValue,
                            nomineeList = nomineeList
                        ) {nominee->
                            // TODO:
                            if (nominee != null){
                                startActivity(Intent(this@CreateNominationActivity, NominationSubmittedActivity::class.java))
                            }else{
                                SweetAlertDialog(this@CreateNominationActivity, SweetAlertDialog.ERROR_TYPE).apply {
                                    titleText = "Error"
                                    contentText = "One or more error has occurred"
                                    setConfirmButton("Ok") {
                                        it.dismissWithAnimation()
                                    }
                                    show()
                                }
                            }
                        }
                    } catch (error: Exception) {
                        Timber.d(error)
                        SweetAlertDialog(this@CreateNominationActivity, SweetAlertDialog.ERROR_TYPE).apply {
                            titleText = "Validation Error"
                            contentText =
                                error.message ?: "Please make sure all fields are correctly filled"
                            setConfirmButton("Ok") {
                                it.dismissWithAnimation()
                            }
                            show()
                        }
                    }
                }

            }
        }
    }
}