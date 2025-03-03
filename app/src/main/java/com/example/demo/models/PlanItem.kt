package com.example.demo.models

data class PlanItem(
    val duration: String? = null,
    val emi: String? = null,
    val icon: String? = null,
    val subtitle: String,
    val tag: String? = null,
    val title: String
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "duration" to duration,
            "emi" to emi,
            "icon" to icon,
            "subtitle" to subtitle,
            "tag" to tag,
            "title" to title
        )
    }
}