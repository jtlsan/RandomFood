package com.mountain96.random.ui.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.mountain96.random.R
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_favorites.view.*

class FavoritesFragment : Fragment() {

    private lateinit var homeViewModel: FavoritesViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(FavoritesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_favorites, container, false)
        /*
        val image : String = "https://cdn.pixabay.com/photo/2015/03/12/05/35/chicken-669637_960_720.jpg"

        Glide.with(requireActivity()).load("https://cdn.pixabay.com/photo/2015/03/12/05/35/chicken-669637_960_720.jpg").into(root.test_image)

         */
        return root
    }
}