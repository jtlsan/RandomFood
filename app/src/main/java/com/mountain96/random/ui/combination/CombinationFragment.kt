package com.mountain96.random.ui.combination

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mountain96.random.R
import com.mountain96.random.model.AppDatabase
import com.mountain96.random.model.Food
import kotlinx.android.synthetic.main.fragment_combination.view.*

class CombinationFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var notificationsViewModel: CombinationViewModel
    var db: AppDatabase? = null
    var spinner: Spinner? = null
    var foodContainerList: ArrayList<LinearLayout> = arrayListOf()
    var TAG : String = "CombinationFragment"

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProviders.of(this).get(CombinationViewModel::class.java)
        val rootView = inflater.inflate(R.layout.fragment_combination, container, false)

        spinner = rootView!!.spinner_combination_count
        var spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.combination_count))
        spinner!!.adapter = spinnerAdapter
        db = AppDatabase.getInstance(requireContext())
        countSelectedFoods(rootView)
        foodContainerInit(rootView)

        spinner!!.onItemSelectedListener = this

        /*
        var image = root.imageview_food1
        var  params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(LayoutParams.)
        params.gravity = Gravity.TOP + Gravity.END
        image.layoutParams = params
        image.setBackgroundColor(resources.getColor(R.color.colorDivision))

         */
    return rootView
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        //spinner!!.setSelection(0)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val paramsList: ArrayList<FrameLayout.LayoutParams> = arrayListOf()
        for(i in 0..3) {
            paramsList.add(FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT))
        }
        when(p2) {
            0 -> {
                paramsList.get(0).gravity = Gravity.CENTER
                foodContainerList.get(0).layoutParams = paramsList.get(0)
                foodContainerList.get(0).visibility = View.VISIBLE
                for(i in 1..3) {
                    foodContainerList.get(i).visibility = View.INVISIBLE
                }
            }
            1 -> {
                paramsList.get(0).gravity = Gravity.CENTER_VERTICAL + Gravity.START
                paramsList.get(1).gravity = Gravity.CENTER_VERTICAL + Gravity.END
                for(i in 0..1) {
                    paramsList.get(i).setMargins((resources.getDimension(R.dimen.food_container_margin)).toInt())
                    foodContainerList.get(i).layoutParams = paramsList.get(i)
                    foodContainerList.get(i).visibility = View.VISIBLE
                }
                for(i in 2..3) {
                    foodContainerList.get(i).visibility = View.INVISIBLE
                }
            }
            2 -> {
                paramsList.get(0).gravity = Gravity.TOP + Gravity.START
                paramsList.get(1).gravity = Gravity.TOP + Gravity.END
                paramsList.get(2).gravity = Gravity.BOTTOM + Gravity.CENTER_HORIZONTAL
                for(i in 0..2) {
                    paramsList.get(i).setMargins((resources.getDimension(R.dimen.food_container_margin)).toInt())
                    foodContainerList.get(i).layoutParams = paramsList.get(i)
                    foodContainerList.get(i).visibility = View.VISIBLE
                }
                foodContainerList.get(3).visibility = View.INVISIBLE
            }
            3 -> {
                paramsList.get(0).gravity = Gravity.TOP + Gravity.START
                paramsList.get(1).gravity = Gravity.TOP + Gravity.END
                paramsList.get(2).gravity = Gravity.BOTTOM + Gravity.START
                paramsList.get(3).gravity = Gravity.BOTTOM + Gravity.END
                for(i in 0..3) {
                    paramsList.get(i).setMargins((resources.getDimension(R.dimen.food_container_margin)).toInt())
                    foodContainerList.get(i).layoutParams = paramsList.get(i)
                    foodContainerList.get(i).visibility = View.VISIBLE
                }
            }
        }
    }

    fun foodContainerInit(rootView: View) {
        foodContainerList.add(rootView.food_container1)
        foodContainerList.add(rootView.food_container2)
        foodContainerList.add(rootView.food_container3)
        foodContainerList.add(rootView.food_container4)
        for (container in foodContainerList) {
            container.visibility = View.INVISIBLE
        }
    }

    fun countSelectedFoods(view: View) {
        var savedFoods = db!!.foodDao().loadAllByChecked()
        if (savedFoods.isEmpty()) {
            view.selected_food_count.text = "0"
        } else {
            view.selected_food_count.text = savedFoods.size.toString()
        }
    }
}