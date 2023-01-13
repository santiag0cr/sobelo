package com.ultimate.ultimatesophos.presentation.view.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View.VISIBLE
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.projects.sophosapp.R
import com.projects.sophosapp.databinding.ActivityLoginBinding
import com.ultimate.ultimatesophos.data.models.request.UserRequestEntity
import com.ultimate.ultimatesophos.domain.definitions.Constants.DEFAULT_BOOLEAN
import com.ultimate.ultimatesophos.domain.definitions.Constants.DEFAULT_STRING
import com.ultimate.ultimatesophos.domain.definitions.Constants.USER_INFO
import com.ultimate.ultimatesophos.domain.models.UserResponseDto
import com.ultimate.ultimatesophos.presentation.viewmodel.LoginViewModel
import java.util.concurrent.Executor


class LoginActivity :
    AppCompatActivity(),
    BiometricManager.Authenticators {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private var userInfo = UserResponseDto()
    private val loginViewModel: LoginViewModel by viewModels()
    private val sharedPreferences: SharedPreferences by lazy {
        this.getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)
    }

    private val editTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val enabled = !binding.emailEditText.text.isNullOrEmpty() && !binding.passwordEditText.text.isNullOrEmpty()
            manageLoginButtonVisuals(enabled)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialize()
    }

    override fun onStart() {
        super.onStart()
        if (applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            verifyBiometric()
            binding.loginWithFingerprintButton.setOnClickListener {
                biometricPrompt.authenticate(promptInfo)
            }
        }
        onBackPressedDispatcher.addCallback(this) {
            finishAffinity()
        }
    }

    private fun initialize() {
        retrieveSharedPreferences()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        subscribeToViewModel()
        binding.loginWithCredentialsButton.setOnClickListener {
            getUserByCredentials()
        }
        binding.emailEditText.addTextChangedListener(editTextWatcher)
        binding.passwordEditText.addTextChangedListener(editTextWatcher)
    }

    private fun retrieveSharedPreferences() {
        with(sharedPreferences) {
            userInfo.apply {
                id = getString(getString(R.string.preferences_id_key), DEFAULT_STRING)
                name = getString(getString(R.string.preferences_username_key), DEFAULT_STRING)
                lastName = getString(getString(R.string.preferences_last_name_key), DEFAULT_STRING)
                access = getBoolean(getString(R.string.preferences_access_key), DEFAULT_BOOLEAN)
                admin = getBoolean(getString(R.string.preferences_admin_key), DEFAULT_BOOLEAN)
                email = getString(getString(R.string.preferences_email_key), DEFAULT_STRING)
            }
        }
    }

    private fun saveSharedPreferences() {
        userInfo.email = binding.emailEditText.text.toString()
        with(sharedPreferences.edit()) {
            putString(getString(R.string.preferences_id_key), userInfo.id)
            putString(getString(R.string.preferences_username_key), userInfo.name)
            putString(getString(R.string.preferences_last_name_key), userInfo.lastName)
            putBoolean(getString(R.string.preferences_access_key), userInfo.access)
            putBoolean(getString(R.string.preferences_admin_key), userInfo.admin ?: false)
            putString(getString(R.string.preferences_email_key), userInfo.email)
            apply()
        }
    }

    private fun subscribeToViewModel() {
        loginViewModel.userModel.observe(
            this,
            Observer { result ->
                userInfo = result
                validateUserAccess()
            }
        )
        loginViewModel.isLoading.observe(
            this
        ) {
            binding.loaderProgressBar.isVisible = it
        }
    }

    private fun manageLoginButtonVisuals(enabled: Boolean) {
        binding.loginWithCredentialsButton.isEnabled = enabled
        if (enabled) {
            binding.loginWithCredentialsButton.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(this, R.color.main_color))
        } else {
            binding.loginWithCredentialsButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.button_disabled))
        }
    }

    private fun getUserByCredentials() {
        val params = UserRequestEntity(
            binding.emailEditText.text.toString(),
            binding.passwordEditText.text.toString()
        )
        loginViewModel.getUserByCredentials(params)
    }

    private fun validateUserAccess() {
        if (userInfo.access) {
            saveSharedPreferences()
            goToMenu()
        } else {
            showDialogMessage(
                title = getString(R.string.incorrect_credentials_title),
                message = getString(R.string.incorrect_credentials_body),
                acceptButtonMessage = getString(R.string.accept_message),
                acceptAction = ::resetForm
            )
        }
    }

    private fun showDialogMessage(
        title: String,
        message: String,
        acceptButtonMessage: String = "",
        rejectButtonMessage: String = "",
        acceptAction: () -> Unit = {},
        rejectAction: () -> Unit = {},
        cancelable: Boolean = true
    ) {
        AlertDialog
            .Builder(this, R.style.Theme_SophosApp_AlertDialogTheme)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                acceptButtonMessage
            ) { _, _ ->
                acceptAction()
            }
            .setNegativeButton(
                rejectButtonMessage
            ) { _, _ ->
                rejectAction()
            }
            .setCancelable(cancelable)
            .create()
            .show()
    }

    private fun resetForm() {
        binding.emailEditText.setText("")
        binding.passwordEditText.setText("")
    }

    private fun goToMenu() {
        startActivity(
            Intent(this, WelcomeActivity::class.java).apply {
                putExtra(USER_INFO, userInfo)
            }
        )
    }

    private fun verifyBiometric() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                if (!userInfo.id.isNullOrEmpty()) {
                    setBiometricParameters()
                    binding.loginWithFingerprintButton.visibility = VISIBLE
                }
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to cls that your app accepts.
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG
                    )
                }
                startActivityForResult(enrollIntent, BIOMETRIC_STRONG)
            }
        }
    }

    private fun setBiometricParameters() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(
            this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult,
                ) {
                    super.onAuthenticationSucceeded(result)
                    goToMenu()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    biometricPrompt.authenticate(promptInfo)
                }
            }
        )

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.biometrics_title))
            .setSubtitle(getString(R.string.biometrics_subtitle))
            .setNegativeButtonText(getString(R.string.biometrics_button))
            .build()
    }
}
