package com.mountain96.random.ui.combination

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mountain96.random.R
import com.mountain96.random.model.AppDatabase
import com.mountain96.random.model.Food
import kotlinx.android.synthetic.main.fragment_combination.view.*
import kotlin.math.absoluteValue

class CombinationFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var notificationsViewModel: CombinationViewModel
    var db: AppDatabase? = null
    var spinner: Spinner? = null
    var foodContainerList: ArrayList<LinearLayout> = arrayListOf()
    var touched = false
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
        var btnThread : RandomButtonThread

        spinner!!.onItemSelectedListener = this
        rootView.button_combine.setOnTouchListener { view, motionEvent ->

            when(motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    rootView.randrom_progressbar.visibility = View.VISIBLE
                    touched = true
                    btnThread = RandomButtonThread(motionEvent.downTime, rootView.randrom_progressbar)
                    btnThread.start()
                }
                MotionEvent.ACTION_UP -> {
                    touched = false
                    rootView.randrom_progressbar.visibility = View.INVISIBLE
                    view.performClick()
                }
            }
            return@setOnTouchListener true
        }
        rootView.button_combine.setOnClickListener {
            pickFood(rootView.spinner_combination_count.selectedItem.toString().toInt())
        }
    return rootView
    }

    fun pickFood(targetCount: Int) {
        val selectedList : ArrayList<Food> = arrayListOf()
        selectedList.addAll(db!!.foodDao().loadAllByChecked())
        if (selectedList.isEmpty()) {
            Toast.makeText(requireContext(), "선택한 음식이 없습니다.", Toast.LENGTH_LONG).show()
            return
        } else if (selectedList.size < targetCount) {
            Toast.makeText(requireContext(), "선택한 음식의 숫자가 충분하지 않습니다.", Toast.LENGTH_LONG).show()
            return
        }
        for(i in 0 until (targetCount)) {
            var randomNum = 0
            while(randomNum == 0 || randomNum == selectedList.size+1) {
                randomNum = (Math.random() * (selectedList.size+1)).toInt()
            }
            val food = selectedList.get(randomNum-1)
            loadFoodToView(food, i)
            selectedList.remove(food)
        }
    }

    fun loadFoodToView(food: Food, position: Int) {
        when(position) {
            0 -> {
                Glide.with(requireContext()).load(food.image).apply(RequestOptions().circleCrop()).into(foodContainerList.get(position).imageview_food1)
                foodContainerList.get(position).textview_food1.text = food.name
            }
            1 -> {
                Glide.with(requireContext()).load(food.image).apply(RequestOptions().circleCrop()).into(foodContainerList.get(position).imageview_food2)
                foodContainerList.get(position).textview_food2.text = food.name
            }
            2 -> {
                Glide.with(requireContext()).load(food.image).apply(RequestOptions().circleCrop()).into(foodContainerList.get(position).imageview_food3)
                foodContainerList.get(position).textview_food3.text = food.name
            }
            3 -> {
                Glide.with(requireContext()).load(food.image).apply(RequestOptions().circleCrop()).into(foodContainerList.get(position).imageview_food4)
                foodContainerList.get(position).textview_food4.text = food.name
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

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

    inner class RandomButtonThread(val pressedTime: Long, val progressBar: ProgressBar) : Thread() {

        override fun run() {
            while(touched) {
                progressBar.progress = ((((System.currentTimeMillis() - pressedTime) % 2001) - 1000) / 10).toInt().absoluteValue
            }
        }
    }
}