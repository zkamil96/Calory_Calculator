package com.example.calory_calculator.MODELS

data class Nutrition(
    val caloricBreakdown: CaloricBreakdown,
    val flavanoids: List<Flavanoid>,
    val nutrients: List<Nutrient>,
    val properties: List<Property>,
    val weightPerServing: WeightPerServing
)