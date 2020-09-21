package com.mountain96.random.ui.foods

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
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
    var adapter: FoodsRecyclerviewAdapter? = null
    var db: AppDatabase? = null
    val TAG: String = "FoodsFragment"

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProviders.of(this).get(FoodsViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_foods, container, false)
        adapter = FoodsRecyclerviewAdapter()
        view.foods_recyclerview.adapter = adapter
        view.foods_recyclerview.layoutManager = LinearLayoutManager(activity)
        setUpCheckbox(view)
        return view
    }

    override fun onDestroyView() {
        updateDB()
        super.onDestroyView()
    }

    fun updateDB() {
        for(food in adapter!!.foodList) {
            db!!.foodDao().updateFood(food)
        }
    }

    fun setUpCheckbox(view: View) {
        val koreanCheckBox = view.koreanfood_checkbox
        val chineseCheckBox = view.chinesefood_checkbox
        val japaneseCheckBox = view.japanesefood_checkbox
        val westernCheckBox = view.westernfood_checkbox
        val othersCheckBox = view.otherfood_checkbox
        val allCheckBox = view.allfood_checkbox

        allCheckBox.isChecked = true
        allCheckBox.setOnClickListener {
            if (allCheckBox.isChecked){
                uncheckAllCheckbox(view)
                allCheckBox.isChecked = true
                adapter!!.selectAll()
            } else {
                allCheckBox.isChecked = true
            }
        }


        koreanCheckBox.setOnClickListener {
            if (koreanCheckBox.isChecked){
                uncheckAllCheckbox(view)
                koreanCheckBox.isChecked = true
                adapter!!.selectByCategory(FoodCategory.KOREAN)
            } else {
                allCheckBox.isChecked = true
                adapter!!.selectAll()
            }
        }
        chineseCheckBox.setOnClickListener {
            if (chineseCheckBox.isChecked){
                uncheckAllCheckbox(view)
                chineseCheckBox.isChecked = true
                adapter!!.selectByCategory(FoodCategory.CHINESE)
            } else {
                allCheckBox.isChecked = true
                adapter!!.selectAll()
            }
        }
        japaneseCheckBox.setOnClickListener {
            if (japaneseCheckBox.isChecked){
                uncheckAllCheckbox(view)
                japaneseCheckBox.isChecked = true
                adapter!!.selectByCategory(FoodCategory.JAPANESE)
            } else {
                allCheckBox.isChecked = true
                adapter!!.selectAll()
            }
        }
        westernCheckBox.setOnClickListener {
            if (westernCheckBox.isChecked){
                uncheckAllCheckbox(view)
                westernCheckBox.isChecked = true
                adapter!!.selectByCategory(FoodCategory.WESTERN)
            } else {
                allCheckBox.isChecked = true
                adapter!!.selectAll()
            }
        }
        othersCheckBox.setOnClickListener {
            if (othersCheckBox.isChecked){
                uncheckAllCheckbox(view)
                othersCheckBox.isChecked = true
                adapter!!.selectByCategory(FoodCategory.OTHERS)
            } else {
                allCheckBox.isChecked = true
                adapter!!.selectAll()
            }
        }
    }

    fun uncheckAllCheckbox(view: View) {
        view.allfood_checkbox.isChecked = false
        view.koreanfood_checkbox.isChecked = false
        view.chinesefood_checkbox.isChecked = false
        view.japanesefood_checkbox.isChecked = false
        view.westernfood_checkbox.isChecked = false
        view.otherfood_checkbox.isChecked = false
    }

    inner class FoodsRecyclerviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var foodList : ArrayList<Food> = arrayListOf()
        //var db: AppDatabase? = null

        init {
            db = AppDatabase.getInstance(context!!)
            val savedFoods = db!!.foodDao().getALL()
            if (savedFoods.isNotEmpty()) {
                //배포시 내용 삭제하고 주석 없애기
                foodList.addAll(savedFoods)
                //db?.clearAllTables()
                //InitSettings.initFoods(db, foodList)
            } else {
                InitSettings.initFoods(db, foodList)
            }
        }

        fun selectByCategory(category: FoodCategory) {
            foodList.clear()
            val savedFoods = db!!.foodDao().loadAllByCategory(category)
            foodList.addAll(savedFoods)
            this.notifyDataSetChanged()
        }

        fun removeByCategory(category: FoodCategory) {
            for(food in foodList) {
                if (food.category == category)
                    foodList.remove(food)
            }
            this.notifyDataSetChanged()
        }

        fun selectAll() {
            foodList.clear()
            val savedFoods = db!!.foodDao().getALL()
            foodList.addAll(savedFoods)
            this.notifyDataSetChanged()
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
            var view = holder.itemView
            var food = foodList.get(position)

            view.linearlayout_select_area.setOnClickListener(View.OnClickListener {
                if(view.checkbox_itemfood.isChecked) {
                    food.isChecked = false
                    view.checkbox_itemfood.isChecked = false
                } else {
                    food.isChecked = true
                    view.checkbox_itemfood.isChecked = true
                }
                foodList.set(position, food)
                db!!.foodDao().updateFood(food)
            })

            view.icon_favorite_mark.setOnClickListener {
                if (food.isFavorite) {
                    food.isFavorite = false
                    view.icon_favorite_mark.setImageDrawable(resources.getDrawable(R.drawable.icon_favorite_mark_bold))
                    //빈 그림으로 대체
                } else {
                    food.isFavorite = true
                    view.icon_favorite_mark.setImageDrawable(resources.getDrawable(R.drawable.icon_favorite_mark_fill))
                    //채워진 그림으로 대체
                }
                foodList.set(position, food)
                db!!.foodDao().updateFood(food)
            }
            view.textview_foodname.text = foodList.get(position).name
            Glide.with(requireActivity()).load(foodList.get(position).image).into(view.imageview_food)

            if (food.isFavorite) {
                view.icon_favorite_mark.setImageDrawable(resources.getDrawable(R.drawable.icon_favorite_mark_fill))
            } else {
                view.icon_favorite_mark.setImageDrawable(resources.getDrawable(R.drawable.icon_favorite_mark_bold))
            }

            if (food.isChecked) {
                view.checkbox_itemfood.isChecked = true
            } else {
                view.checkbox_itemfood.isChecked = false
            }
        }
    }
}