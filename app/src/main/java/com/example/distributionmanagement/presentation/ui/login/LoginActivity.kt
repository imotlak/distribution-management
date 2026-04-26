package com.example.distributionmanagement.presentation.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.distributionmanagement.R
import com.example.distributionmanagement.data.model.UserRole
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

/**
 * Login Activity - Handles user authentication
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var usernameInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var loginButton: MaterialButton

    // Demo users for testing
    private val demoUsers = mapOf(
        "admin" to Pair("admin", UserRole.MANAGER),
        "manager" to Pair("manager", UserRole.MANAGER),
        "dataentry" to Pair("dataentry", UserRole.DATA_ENTRY),
        "distribution" to Pair("distribution", UserRole.DISTRIBUTION_MANAGER)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupUI()
        setupClickListeners()
    }

    private fun setupUI() {
        usernameInput = findViewById(R.id.username)
        passwordInput = findViewById(R.id.password)
        loginButton = findViewById(R.id.login_button)
    }

    private fun setupClickListeners() {
        loginButton.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val username = usernameInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "الرجاء إدخال اسم المستخدم وكلمة المرور", Toast.LENGTH_SHORT).show()
            return
        }

        val user = demoUsers[username]
        if (user != null && user.first == password) {
            // Login successful
            val userRole = user.second
            navigateToDashboard(userRole)
        } else {
            Toast.makeText(this, "اسم المستخدم أو كلمة المرور غير صحيحة", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToDashboard(userRole: UserRole) {
        when (userRole) {
            UserRole.MANAGER -> {
                startActivity(Intent(this, ManagerDashboardActivity::class.java))
            }
            UserRole.DATA_ENTRY -> {
                startActivity(Intent(this, DataEntryDashboardActivity::class.java))
            }
            UserRole.DISTRIBUTION_MANAGER -> {
                startActivity(Intent(this, DistributionManagerActivity::class.java))
            }
            else -> {
                Toast.makeText(this, "دور المستخدم غير معروف", Toast.LENGTH_SHORT).show()
            }
        }
        finish()
    }
}

// Import statement needed
import android.content.Intent
import com.example.distributionmanagement.presentation.ui.manager.ManagerDashboardActivity
import com.example.distributionmanagement.presentation.ui.dataentry.DataEntryDashboardActivity
import com.example.distributionmanagement.presentation.ui.distribution.DistributionManagerActivity
