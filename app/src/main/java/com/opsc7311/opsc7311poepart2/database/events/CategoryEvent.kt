package com.opsc7311.opsc7311poepart2.database.events

sealed interface CategoryEvent {
    object saveCategory: CategoryEvent
    data class setCategoryName(val categoryName: String): CategoryEvent
    data class setColor(val color: String): CategoryEvent

    data class setUserId(val UserId: Int): CategoryEvent
    object showDialog: CategoryEvent

    object hideObject: CategoryEvent


}