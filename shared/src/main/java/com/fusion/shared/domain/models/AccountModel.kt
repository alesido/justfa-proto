package com.fusion.shared.domain.models


data class Account(
    val id: String,
    val person: Person,
    val company: Company,
    val planningMode: PlanningMode,
)

data class Person (val id: String, val name: String)
data class Company (val id: String, val name: String)
enum class PlanningMode { PLANNING_ON_MY_OWN, PLANNING_ON_ADVISED }


