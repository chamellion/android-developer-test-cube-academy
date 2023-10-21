package com.cube.cubeacademy.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.cube.cubeacademy.R
import com.cube.cubeacademy.databinding.ActivityCreateNominationBinding
import com.cube.cubeacademy.lib.di.Repository
import com.cube.cubeacademy.lib.models.Nominee
import com.cube.cubeacademy.utils.isOnline
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CreateNominationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNominationBinding

    private val createNominationViewModel: CreateNominationViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    @Inject
    lateinit var repository: Repository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateNominationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val bottomModalFragment = BottomSheetModal()
                bottomModalFragment.show(supportFragmentManager, bottomModalFragment.tag)
            }

        }

        onBackPressedDispatcher.addCallback(backPressedCallback)

        binding.backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        if (!isOnline()){
            val snackbar = Snackbar.make(binding.root, "You are offline. This app only works when you are online", Snackbar.LENGTH_INDEFINITE)
            snackbar.show()
        }

        lifecycleScope.launch {
            mainActivityViewModel.nomineeList.collect { nominationList ->
                nominationList.let {
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
        binding.radioGroupContainer.setOnCheckedChangeListener { _, checkedId ->
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
                    SweetAlertDialog(
                        this@CreateNominationActivity,
                        SweetAlertDialog.ERROR_TYPE
                    ).apply {
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
                        ) { nominee ->
                            // TODO:
                            if (nominee != null) {
                                startActivity(
                                    Intent(
                                        this@CreateNominationActivity,
                                        NominationSubmittedActivity::class.java
                                    ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                )
                            } else {
                                SweetAlertDialog(
                                    this@CreateNominationActivity,
                                    SweetAlertDialog.ERROR_TYPE
                                ).apply {
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
                        SweetAlertDialog(
                            this@CreateNominationActivity,
                            SweetAlertDialog.ERROR_TYPE
                        ).apply {
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