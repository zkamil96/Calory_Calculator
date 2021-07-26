package com.example.calory_calculator.MODELS

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId;
open class favorite_list_value(
        @PrimaryKey var _id: ObjectId? = null,
        var amount: Double? = null,
        var name: String? = null,
        var owner_id: String? = null,
        var product_id: Long? = null
): RealmObject() {}