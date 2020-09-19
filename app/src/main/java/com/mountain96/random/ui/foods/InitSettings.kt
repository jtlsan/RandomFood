package com.mountain96.random.ui.foods

import com.mountain96.random.model.AppDatabase
import com.mountain96.random.model.Food
import com.mountain96.random.model.FoodCategory

class InitSettings {
    companion object {
        public fun initFoods(db: AppDatabase?, foodList: ArrayList<Food>) {
            //foodList.add(Food(0, "냉면", FoodCategory.KOREAN, "", false, false))
            foodList.add(Food(0, "후라이드치킨", FoodCategory.KOREAN, "https://cdn.pixabay.com/photo/2017/03/20/09/08/food-2158543_960_720.jpg", false, false))
            foodList.add(Food(1, "양념치킨", FoodCategory.KOREAN, "https://cdn.pixabay.com/photo/2015/02/19/08/26/chicken-641881_960_720.jpg", false, false))
            foodList.add(Food(2, "족발", FoodCategory.KOREAN, "https://cdn.pixabay.com/photo/2015/10/22/14/13/food-1001256_960_720.jpg", false, false))
            foodList.add(Food(3, "보쌈", FoodCategory.KOREAN, "https://cdn.pixabay.com/photo/2015/05/02/00/56/bossam-749357_960_720.jpg", false, false))
            foodList.add(Food(4, "냉면", FoodCategory.KOREAN, "https://cdn.pixabay.com/photo/2018/09/17/05/14/water-noodle-3683050_960_720.jpg", false, false))
            foodList.add(Food(5, "짜장면", FoodCategory.CHINESE, "https://cdn.pixabay.com/photo/2017/07/27/16/45/i-2545938_960_720.jpg", false, false))
            foodList.add(Food(6, "초밥", FoodCategory.JAPANESE, "https://cdn.pixabay.com/photo/2017/01/06/16/46/sushi-1958247_960_720.jpg", false, false))
            foodList.add(Food(7, "피자", FoodCategory.WESTERN, "https://cdn.pixabay.com/photo/2017/01/22/19/20/pizza-2000615_960_720.jpg", false, false))
            foodList.add(Food(8, "쌀국수", FoodCategory.OTHERS, "https://cdn.pixabay.com/photo/2016/02/18/06/42/vietnam-1206576_960_720.jpg", false, false))

            for(food in foodList) {
                db?.foodDao()?.insertAll(food)
            }
        }
    }
}