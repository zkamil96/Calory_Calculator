package com.example.calory_calculator.MODELS

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId;

open class calory_value(
        @PrimaryKey var _id: ObjectId? = null,

        var age: Long? = null,

        var destination: String? = null,

        var gender: String? = null,

        var growth: Long? = null,

        var owner_id: String? = null,

        var physical_activity: String? = null,

        var weight: Long? = null
): RealmObject() {}
