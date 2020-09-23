package com.mountain96.random.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mountain96.random.R
import com.mountain96.random.model.AppDatabase
import com.mountain96.random.model.Food
import com.mountain96.random.model.FoodCategory
import com.mountain96.random.ui.foods.FoodsFragment
import com.mountain96.random.ui.foods.InitSettings
import kotlinx.android.synthetic.main.fragment_foods.view.*
import kotlinx.android.synthetic.main.item_food.view.*

class FavoriteFragment : Fragment(){

    var adapter: FavoriteFragment.FavoriteFoodsRecyclerviewAdapter? = null
    var db: AppDatabase? = null
    var recyclerview: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_foods, container, false)
        adapter = FavoriteFoodsRecyclerviewAdapter()
        recyclerview = view.foods_recyclerview
        recyclerview!!.adapter = adapter
        recyclerview!!.layoutManager = LinearLayoutManager(activity)
        setUpCheckbox(view)
        return view
    }

    fun updateFavorite(position: Int) {
        adapter!!.foodList.removeAt(position)
        recyclerview!!.removeViewAt(position)
        adapter!!.notifyItemRemoved(position);
        adapter!!.notifyItemRangeChanged(position, adapter!!.foodList.size)
        adapter!!.notifyDataSetChanged()
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


    inner class FavoriteFoodsRecyclerviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var foodList : ArrayList<Food> = arrayListOf()
        //var db: AppDatabase? = null

        init {
            db = AppDatabase.getInstance(context!!)
            val savedFoods = db!!.foodDao().getAllFavorites()
            foodList.addAll(savedFoods)
        }

        fun selectByCategory(category: FoodCategory) {
            foodList.clear()
            val savedFoods = db!!.foodDao().loadAllFavoritesByCategory(category)
            foodList.addAll(savedFoods)
            this.notifyDataSetChanged()
        }

        fun selectAll() {
            foodList.clear()
            val savedFoods = db!!.foodDao().getAllFavorites()
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
                } else {
                    food.isFavorite = true
                    view.icon_favorite_mark.setImageDrawable(resources.getDrawable(R.drawable.icon_favorite_mark_fill))
                }
                foodList.set(position, food)
                db!!.foodDao().updateFood(food)
                updateFavorite(position)

            }
            view.textview_foodname.text = foodList.get(position).name
            Glide.with(requireActivity()).load(foodList.get(position).image).apply(RequestOptions().circleCrop()).into(view.imageview_food)

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