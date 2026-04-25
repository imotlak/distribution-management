package com.example.distributionmanagement.domain.service

import com.example.distributionmanagement.data.model.DistributionItem
import com.example.distributionmanagement.data.model.DistributionRequest

/**
 * Service to validate distribution requests and items
 */
class ValidationService {

    /**
     * Validate if a distribution request has all required items
     */
    fun validateRequestHasItems(items: List<DistributionItem>): Boolean {
        return items.isNotEmpty()
    }

    /**
     * Validate if item quantity is valid
     */
    fun validateItemQuantity(quantity: Double): Boolean {
        return quantity > 0
    }

    /**
     * Validate if camp name is not empty
     */
    fun validateCampName(name: String): Boolean {
        return name.trim().isNotEmpty() && name.length >= 3
    }

    /**
     * Validate if user credentials are valid
     */
    fun validateUserCredentials(username: String, password: String): Boolean {
        return username.trim().isNotEmpty() &&
                password.length >= 6 &&
                username.length >= 3
    }

    /**
     * Validate distribution request notes (optional but should have max length)
     */
    fun validateNotes(notes: String): Boolean {
        return notes.length <= 1000
    }
}
