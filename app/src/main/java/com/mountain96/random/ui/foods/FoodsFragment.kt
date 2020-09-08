package com.mountain96.random.ui.foods

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.mountain96.random.R
import com.mountain96.random.model.FoodDTO
import kotlinx.android.synthetic.main.fragment_foods.*

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
        return view
    }

    inner class FoodsRecyclerviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var foodList : ArrayList<FoodDTO> = arrayListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view : View) : RecyclerView.ViewHolder(view)

        override fun getItemCount(): Int {
            return foodList.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            
        }
    }
}