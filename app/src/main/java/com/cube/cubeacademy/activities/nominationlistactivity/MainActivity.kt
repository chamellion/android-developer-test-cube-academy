package com.cube.cubeacademy.activities.nominationlistactivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import cn.pedant.SweetAlert.SweetAlertDialog
import com.cube.cubeacademy.activities.createnominationactivity.CreateNominationActivity
import com.cube.cubeacademy.databinding.ActivityMainBinding
import com.cube.cubeacademy.lib.adapters.NominationsRecyclerViewAdapter
import com.cube.cubeacademy.lib.models.Nomination
import com.cube.cubeacademy.lib.models.Nominee
import com.cube.cubeacademy.utils.ApiResult
import com.cube.cubeacademy.utils.isOnline
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


	private lateinit var binding: ActivityMainBinding

	private val mainActivityViewModel: MainActivityViewModel by viewModels()

	/*Suppressing cast warning because type has been checked
	on postman and its a list of Nomination as opposed to using a
	wild card due to erasure at runtime of kotlin generics
	 */
	@Suppress("UNCHECKED_CAST")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		binding.createButton.setOnClickListener {
			startActivity(Intent(this, CreateNominationActivity::class.java))
		}

		if (!isOnline()){
			SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).apply {
				titleText = "No Connection"
				contentText = "You don't seem to have an internet connection. Try coming online and try again"
				setConfirmButton("Ok"){
					it.dismissWithAnimation()
				}
				show()
			}
		}

		lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED){
				mainActivityViewModel.viewState.collect{apiResult ->
					when(apiResult){
						is ApiResult.Loading -> {
							binding.loadingIndicator.visibility = View.VISIBLE
							binding.emptyContainer.visibility = View.GONE
						}
						is ApiResult.Error -> {
							binding.loadingIndicator.visibility = View.GONE
							SweetAlertDialog(applicationContext, SweetAlertDialog.ERROR_TYPE).apply {
								titleText = "Error"
								contentText = apiResult.error
								setConfirmButton("Ok"){
									it.dismissWithAnimation()
								}
								show()
							}
						}
						is ApiResult.Success<*> -> {
							if (apiResult.data is List<*>){
								val nominationLists = apiResult.data as? List<Nomination>
								nominationLists?.let {nominationList ->
									Timber.d("List is empty: ${nominationList.isEmpty()}")
									if (nominationList.isEmpty()){
										binding.emptyContainer.visibility = View.VISIBLE
									}else{
										binding.emptyContainer.visibility = View.GONE
										mainActivityViewModel.nomineeList.collect{
											populateUI(nominationList, applicationContext, it)
										}
									}

								}
							}
						}
					}
				}
			}
		}

	}

	private fun populateUI(nominationList: List<Nomination>, context: Context, nomineeList: List<Nominee>) {
		/**
		 * TODO: Populate the UI with data in this function
		 * 		 You need to fetch the list of user's nominations from the api and put the data in the recycler view
		 * 		 And also add action to the "Create new nomination" button to go to the CreateNominationActivity
		 */
		val adapter = NominationsRecyclerViewAdapter(nomineeList, context)
		binding.nominationsList.adapter = adapter
		adapter.submitList(nominationList)
	}
}