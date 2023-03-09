package com.fusion.shared.data.remote.justfa.models

import kotlinx.serialization.Serializable


@Serializable
data class JfaAccountDto(
    val id: String,
    val login: String,
    val type: String,
    val person: Person,
    val company: Company,
    val planningMode: PlanningMode,
)

@Serializable
data class Person (val id: String, val name: String)

@Serializable
data class Company (val id: String, val name: String)

@Serializable
enum class PlanningMode { PLANNING_ON_MY_OWN, PLANNING_ON_ADVISED }


/* DTO Example:
{
    "id": "fedf8b01-efe2-4e15-b2d4-dc253199e8a9",
    "login": "kent.black@test.com",
    "type": "Client",
    "person": {
        "id": "e41db1a3-3fd4-4fa7-b65b-40c555a92497",
        "name": "Kent Black"
    },
    "company": {
        "id": 1,
        "name": "JustFA"
    },
    "allowedActions": [
        "CONFIRM_DOCUMENT_SIGNING",
        "VIEW_DICTIONARY",
        "VIEW_COMPANY",
        "VIEW_CLIENT",
        "EDIT_CLIENT",
        "VIEW_CLIENT_ACCOUNT",
        "VIEW_ACCOUNT",
        "EDIT_ACCOUNT",
        "VIEW_FINANCIAL_REVIEW",
        "EDIT_FINANCIAL_REVIEW",
        "COMPLETE_FINANCIAL_REVIEW",
        "VIEW_COMMENT",
        "CALCULATE_INVESTMENT_PROJECTIONS",
        "CREATE_INVESTMENT_PROJECTIONS",
        "VIEW_INVESTMENT_PROJECTIONS",
        "CALCULATE_PENSION_PROJECTIONS",
        "CREATE_PENSION_PROJECTIONS",
        "VIEW_PENSION_PROJECTIONS",
        "VIEW_PROJECTIONS",
        "DELETE_PROJECTIONS",
        "VIEW_COMPANY_DOCUMENT",
        "VIEW_DOCUMENT",
        "DOWNLOAD_SOURCE_DOCUMENT",
        "UPLOAD_SIGNED_DOCUMENT",
        "DELETE_SIGNED_DOCUMENT",
        "DOWNLOAD_SIGNED_DOCUMENT",
        "VIEW_CLIENT_SERVICE",
        "REJECT_CLIENT_SERVICE",
        "VIEW_FEE_AND_SERVICE_CLIENT",
        "VIEW_TRANSACTION",
        "VIEW_RECURRING_PAYMENT",
        "DELETE_RECURRING_PAYMENT",
        "CHANGE_RECURRING_PAYMENT_STATUS",
        "VIEW_CLIENT_GOAL",
        "UPDATE_CLIENT_GOAL",
        "VIEW_NOTIFICATION",
        "CHANGE_NOTIFICATION_STATUS",
        "REQUEST_CONSULTATION",
        "VIEW_CONSULTATION",
        "CHANGE_CONSULTATION_STATUS",
        "CHANGE_TRANSACTION_STATUS"
    ],
    "grantedAuthorities": [
        {
            "authority": "CONFIRM_DOCUMENT_SIGNING",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "VIEW_DICTIONARY",
            "scope": []
        },
        {
            "authority": "VIEW_COMPANY",
            "scope": [
                "COMPANY:1"
            ]
        },
        {
            "authority": "VIEW_CLIENT",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "EDIT_CLIENT",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "VIEW_CLIENT_ACCOUNT",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "VIEW_ACCOUNT",
            "scope": [
                "ACCOUNT:fedf8b01-efe2-4e15-b2d4-dc253199e8a9"
            ]
        },
        {
            "authority": "EDIT_ACCOUNT",
            "scope": [
                "ACCOUNT:fedf8b01-efe2-4e15-b2d4-dc253199e8a9"
            ]
        },
        {
            "authority": "VIEW_FINANCIAL_REVIEW",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "EDIT_FINANCIAL_REVIEW",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "COMPLETE_FINANCIAL_REVIEW",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "VIEW_COMMENT",
            "scope": [
                "VISIBLE_TO_CLIENT:true"
            ]
        },
        {
            "authority": "CALCULATE_INVESTMENT_PROJECTIONS",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "CREATE_INVESTMENT_PROJECTIONS",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "VIEW_INVESTMENT_PROJECTIONS",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "CALCULATE_PENSION_PROJECTIONS",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "CREATE_PENSION_PROJECTIONS",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "VIEW_PENSION_PROJECTIONS",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "VIEW_PROJECTIONS",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "DELETE_PROJECTIONS",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "VIEW_COMPANY_DOCUMENT",
            "scope": []
        },
        {
            "authority": "VIEW_DOCUMENT",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "DOWNLOAD_SOURCE_DOCUMENT",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "UPLOAD_SIGNED_DOCUMENT",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "DELETE_SIGNED_DOCUMENT",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "DOWNLOAD_SIGNED_DOCUMENT",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "VIEW_CLIENT_SERVICE",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "REJECT_CLIENT_SERVICE",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "VIEW_FEE_AND_SERVICE_CLIENT",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "VIEW_TRANSACTION",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "VIEW_RECURRING_PAYMENT",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "DELETE_RECURRING_PAYMENT",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "CHANGE_RECURRING_PAYMENT_STATUS",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "VIEW_CLIENT_GOAL",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "UPDATE_CLIENT_GOAL",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "VIEW_NOTIFICATION",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "CHANGE_NOTIFICATION_STATUS",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "REQUEST_CONSULTATION",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "VIEW_CONSULTATION",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "CHANGE_CONSULTATION_STATUS",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        },
        {
            "authority": "CHANGE_TRANSACTION_STATUS",
            "scope": [
                "CLIENT:e41db1a3-3fd4-4fa7-b65b-40c555a92497"
            ]
        }
    ],
    "planningMode": "PLANNING_ON_ADVISED",
    "displayCompanyLogoDocumentId": "1dd47888-7639-43a6-8f6a-18066922333d",
    "displayCompanyName": "JustFA"
}
 */
