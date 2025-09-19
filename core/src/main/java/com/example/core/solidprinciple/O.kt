package com.example.core.solidprinciple

// Open/Closed Principle:
// The class should be open for extension but closed for modification

open class EmailValidator {
    val EMAIL_REGEX = "^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    fun isEmailValid(email: String) {
        // TODO: Check EMAIL_REGEX
    }

  /*  fun isValidGmail(gmail: String) { // New modification
        // TODO: Check PASSWORD_REGEX
    }*/
}

class GmailValidator: EmailValidator() {
    fun isValidGmail(gmail: String) { }
}


