package com.mountain96.random.ui.foods

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bumptech.glide.Glide
import com.mountain96.random.R
import com.mountain96.random.model.AppDatabase
import com.mountain96.random.model.Food
import com.mountain96.random.model.FoodCategory
import kotlinx.android.synthetic.main.fragment_foods.*
import kotlinx.android.synthetic.main.fragment_foods.view.*
import kotlinx.android.synthetic.main.item_food.view.*

class FoodsFragment : Fragment() {

    private lateinit var dashboardViewModel: FoodsViewModel


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProviders.of(this).get(FoodsViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_foods, container, false)
        view.foods_recyclerview.adapter =FoodsRecyclerviewAdapter()
        view.foods_recyclerview.layoutManager = LinearLayoutManager(activity)
        return view
    }

    inner class FoodsRecyclerviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var foodList : ArrayList<Food> = arrayListOf()
        var db: AppDatabase? = null

        init {
            db = AppDatabase.getInstance(context!!)
            val savedFoods = db!!.foodDao().getALL()
            if (savedFoods.isNotEmpty()) {
                //배포시 내용 삭제하고 주석 없애기
                //foodList.addAll(savedFoods)
                db?.clearAllTables()
                InitSettings.initFoods(db, foodList)
            } else {
                InitSettings.initFoods(db, foodList)
            }
        }

        fun initFoods() {
            val food = Food(0, "후라이드치킨", FoodCategory.KOREAN, "https://cdn.pixabay.com/photo/2017/03/20/09/08/food-2158543_960_720.jpg"
            , false, false)
            db?.foodDao()?.insertAll(food)
            foodList.add(food)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view : View) : RecyclerView.ViewHolder(view)

        override fun getItemCount(): Int {
            return foodList.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val db = Room.databaseBuilder(context!!, AppDatabase::class.java, "foods.db")
            var view = holder.itemView

            view.textview_foodname.text = foodList.get(position).name
            Glide.with(requireActivity()).load(foodList.get(position).image).into(view.imageview_food)
        }
    }
}