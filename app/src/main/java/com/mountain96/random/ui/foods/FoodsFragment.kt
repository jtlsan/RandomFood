package com.mountain96.random.ui.foods

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.mountain96.random.R

class FoodsFragment : Fragment() {

    private lateinit var dashboardViewModel: FoodsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProviders.of(this).get(FoodsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_foods, container, false)
        return root
    }
}