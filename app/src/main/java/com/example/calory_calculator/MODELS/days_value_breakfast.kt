package com.example.calory_calculator.MODELS

import io.realm.RealmObject;
import io.realm.annotations.RealmClass
import java.io.Serializable

@RealmClass(embedded = true)
open class days_value_breakfast (
    var amount: Long? = null,
    var calories: Double? = null,
    var carbohydrates: Double? = null,
    var fats: Double? = null,
    var id: Long? = null,
    var name: String? = null,
    var proteins: Double? = null
): RealmObject()
