package com.cube.cubeacademy.activities.createnominationactivity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.cube.cubeacademy.activities.nominationlistactivity.MainActivity
import com.cube.cubeacademy.databinding.BottomModalMenuBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetModal : BottomSheetDialogFragment() {

    private lateinit var binding: BottomModalMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomModalMenuBinding.inflate(layoutInflater)

        binding.noButton.setOnClickListener {
            dismiss()
        }
        binding.yesButton.setOnClickListener {
            activity?.applicationContext?.startActivity(
                Intent(
                    activity?.applicationContext,
                    MainActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
        applyBackgroundTint()
        return binding.root
    }

    //Create a new transparent black view and apply on the background to give a tinted effect
    private fun applyBackgroundTint() {
        val rootView = (activity as? AppCompatActivity)?.window?.decorView?.rootView
        val overlay = View(activity)
        overlay.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        overlay.setBackgroundColor(Color.parseColor("#80000000"))
        (rootView as? ViewGroup)?.addView(overlay)
    }

    override fun onDestroyView() {
        // Reset the background when the modal is dismissed
        removeBackgroundTint()
        super.onDestroyView()
    }

    //Remove recently added view from the view stack
    private fun removeBackgroundTint() {
        val rootView = (activity as? AppCompatActivity)?.window?.decorView?.rootView
        if (rootView is ViewGroup && rootView.childCount > 0) {
            rootView.removeViewAt(rootView.childCount - 1)
        }
    }

}