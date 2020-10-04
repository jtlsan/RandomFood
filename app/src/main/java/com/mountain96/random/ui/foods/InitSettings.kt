package com.mountain96.random.ui.foods

import com.mountain96.random.model.AppDatabase
import com.mountain96.random.model.Food
import com.mountain96.random.model.FoodCategory

class InitSettings {
    companion object {
        public fun initFoods(db: AppDatabase?, foodList: ArrayList<Food>) {
            //foodList.add(Food(0, "냉면", FoodCategory.KOREAN, "", false, false))

            foodList.add(Food(0, "후라이드치킨", 1, "https://cdn.pixabay.com/photo/2017/03/20/09/08/food-2158543_960_720.jpg", false, false))
            foodList.add(Food(1, "양념치킨", 1, "https://cdn.pixabay.com/photo/2015/02/19/08/26/chicken-641881_960_720.jpg", false, false))
            foodList.add(Food(2, "족발", 1, "https://cdn.pixabay.com/photo/2015/10/22/14/13/food-1001256_960_720.jpg", false, false))
            foodList.add(Food(3, "보쌈", 1, "https://cdn.pixabay.com/photo/2015/05/02/00/56/bossam-749357_960_720.jpg", false, false))
            foodList.add(Food(4, "냉면", 1, "https://cdn.pixabay.com/photo/2018/09/17/05/14/water-noodle-3683050_960_720.jpg", false, false))
            foodList.add(Food(5, "짜장면", 2, "https://cdn.pixabay.com/photo/2017/07/27/16/45/i-2545938_960_720.jpg", false, false))
            foodList.add(Food(6, "초밥", 3, "https://cdn.pixabay.com/photo/2017/01/06/16/46/sushi-1958247_960_720.jpg", false, false))
            foodList.add(Food(7, "피자", 4, "https://cdn.pixabay.com/photo/2017/01/22/19/20/pizza-2000615_960_720.jpg", false, false))
            foodList.add(Food(8, "쌀국수", 5, "https://cdn.pixabay.com/photo/2016/02/18/06/42/vietnam-1206576_960_720.jpg", false, false))



            for(food in foodList) {
                db?.foodDao()?.insertAll(food)
            }
        }

        public fun initCategory(db: AppDatabase, categoryList: ArrayList<FoodCategory>) {
            categoryList.add(FoodCategory(0, "모두", true))
            categoryList.add(FoodCategory(1, "한식", false))
            categoryList.add(FoodCategory(2, "중식", false))
            categoryList.add(FoodCategory(3, "일식", false))
            categoryList.add(FoodCategory(4, "양식", false))
            categoryList.add(FoodCategory(5, "그외", false))

            for(category in categoryList) {
                db?.foodCategoryDao().insertAll(category)
            }
        }
    }
}