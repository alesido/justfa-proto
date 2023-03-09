package com.fusion.shared.data.remote.justfa.repositories.mappers

import com.fusion.shared.data.remote.justfa.models.JfaAccountDto
import com.fusion.shared.domain.models.Account
import com.fusion.shared.domain.models.Company
import com.fusion.shared.domain.models.Person
import com.fusion.shared.domain.models.PlanningMode

fun JfaAccountDto.toDomain() : Account = Account(
    id = id,
    person = Person(person.id, person.name),
    company = Company(company.id, company.name),
    planningMode = PlanningMode.valueOf(planningMode.toString())
)

