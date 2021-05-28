package com.example.calory_calculator.MODELS

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey
import java.util.Date;
import org.bson.types.ObjectId;

open class history_value(
        @PrimaryKey var _id: ObjectId? = null,
        var amount: Double? = null,
        var date: Date? = null,
        var name: String? = null,
        var owner_id: String? = null,
        var product_id: Long? = null
): RealmObject() {}
