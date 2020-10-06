package com.mountain96.random.ui.foods

import com.mountain96.random.model.AppDatabase
import com.mountain96.random.model.Food
import com.mountain96.random.model.FoodCategory
import com.mountain96.random.model.ModelType

class InitSettings {
    //categoryId : 0 = 추가버튼, 1 = 모두, 2 .. 그외
    companion object {
        public fun initFoods(db: AppDatabase?, foodList: ArrayList<Food>) {
            foodList.add(Food(0, "", 0, "", false, false, ModelType.TYPE_BUTTON))
            foodList.add(Food(1, "후라이드치킨", 2, "https://cdn.pixabay.com/photo/2017/03/20/09/08/food-2158543_960_720.jpg", false, false, ModelType.TYPE_ITEM))
            foodList.add(Food(2, "양념치킨", 2, "https://cdn.pixabay.com/photo/2015/02/19/08/26/chicken-641881_960_720.jpg", false, false, ModelType.TYPE_ITEM))
            foodList.add(Food(3, "족발", 2, "https://cdn.pixabay.com/photo/2015/10/22/14/13/food-1001256_960_720.jpg", false, false, ModelType.TYPE_ITEM))
            foodList.add(Food(4, "보쌈", 2, "https://cdn.pixabay.com/photo/2015/05/02/00/56/bossam-749357_960_720.jpg", false, false, ModelType.TYPE_ITEM))
            foodList.add(Food(5, "냉면", 2, "https://cdn.pixabay.com/photo/2018/09/17/05/14/water-noodle-3683050_960_720.jpg", false, false, ModelType.TYPE_ITEM))
            foodList.add(Food(6, "짜장면", 3, "https://cdn.pixabay.com/photo/2017/07/27/16/45/i-2545938_960_720.jpg", false, false, ModelType.TYPE_ITEM))
            foodList.add(Food(7, "초밥", 4, "https://cdn.pixabay.com/photo/2017/01/06/16/46/sushi-1958247_960_720.jpg", false, false, ModelType.TYPE_ITEM))
            foodList.add(Food(8, "피자", 5, "https://cdn.pixabay.com/photo/2017/01/22/19/20/pizza-2000615_960_720.jpg", false, false, ModelType.TYPE_ITEM))
            foodList.add(Food(9, "쌀국수", 6, "https://cdn.pixabay.com/photo/2016/02/18/06/42/vietnam-1206576_960_720.jpg", false, false, ModelType.TYPE_ITEM))



            for(food in foodList) {
                db?.foodDao()?.insertAll(food)
            }
        }

        public fun initCategory(db: AppDatabase, categoryList: ArrayList<FoodCategory>) {
            categoryList.add(FoodCategory(0, "", false, ModelType.TYPE_BUTTON))
            categoryList.add(FoodCategory(1, "모두", true, ModelType.TYPE_ITEM))
            categoryList.add(FoodCategory(2, "한식", false, ModelType.TYPE_ITEM))
            categoryList.add(FoodCategory(3, "중식", false, ModelType.TYPE_ITEM))
            categoryList.add(FoodCategory(4, "일식", false, ModelType.TYPE_ITEM))
            categoryList.add(FoodCategory(5, "양식", false, ModelType.TYPE_ITEM))
            categoryList.add(FoodCategory(6, "그외", false, ModelType.TYPE_ITEM))

            for(category in categoryList) {
                db?.foodCategoryDao().insertAll(category)
            }
        }
    }
}