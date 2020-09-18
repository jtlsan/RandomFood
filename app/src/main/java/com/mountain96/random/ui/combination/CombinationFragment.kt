package com.mountain96.random.ui.combination

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mountain96.random.R
import kotlinx.android.synthetic.main.fragment_combination.view.*

class CombinationFragment : Fragment() {

    private lateinit var notificationsViewModel: CombinationViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProviders.of(this).get(CombinationViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_combination, container, false)

        var spinner = root.spinner_combination_count
        var spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.combination_count))
        spinner.adapter = spinnerAdapter


        /*
        var image = root.imageview_food1
        var  params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(LayoutParams.)
        params.gravity = Gravity.TOP + Gravity.END
        image.layoutParams = params
        image.setBackgroundColor(resources.getColor(R.color.colorDivision))

         */



        return root
    }
}