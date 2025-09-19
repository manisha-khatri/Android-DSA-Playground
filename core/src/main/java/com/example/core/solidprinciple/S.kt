package com.example.core.solidprinciple

// Single Responsibility Principle

class EmailPasswordValidatorUseCase {
    val EMAIL_REGEX = "^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    val PASSWORD_REGEX = "[a-zA-Z0-9!@#\$%^&*]{8,20}"

    fun isEmailValid(email: String) {
        // TODO: Check EMAIL_REGEX
    }

    fun isPasswordValid(password: String) {
        // TODO: Check PASSWORD_REGEX
    }
}

interface Validator {
    fun validate(value: String): Boolean
}

class MyEmailValidator : Validator {
    val EMAIL_REGEX = "^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun validate(value: String): Boolean {
        TODO(reason = "Not yet implemented")
    }
}

class MyPasswordValidator : Validator {
    val PASSWORD_REGEX = "[a-zA-Z0-9!@#\$%^&*]{8,20}"

    override fun validate(value: String): Boolean {
        TODO(reason = "Not yet implemented")
    }
}


