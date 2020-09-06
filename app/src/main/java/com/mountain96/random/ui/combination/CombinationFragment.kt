package com.mountain96.random.ui.combination

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mountain96.random.R

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

        return root
    }
}