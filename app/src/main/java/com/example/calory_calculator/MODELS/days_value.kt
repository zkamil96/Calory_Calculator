package com.example.calory_calculator.MODELS

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey
import java.util.Date;
import org.bson.types.ObjectId;
open class days_value(
        @PrimaryKey var _id: ObjectId? = null,
        var actual_calory: Long? = null,
        var actual_carbohydrates: Long? = null,
        var actual_fats: Long? = null,
        var actual_proteins: Long? = null,
        var breakfast: RealmList<days_value_breakfast> = RealmList(),
        var cups_of_water: Long? = null,
        var date: Date? = null,
        var dinner: RealmList<days_value_dinner> = RealmList(),
        var lunchtime: RealmList<days_value_lunchtime> = RealmList(),
        var owner_id: String? = null,
        var snacks: RealmList<days_value_snacks> = RealmList()
): RealmObject() {}