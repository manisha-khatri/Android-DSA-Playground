package com.example.study2025.unittests

class Utils {

    fun isPalindrome(input: String): Boolean {
        var j = input.length-1
        var i = 0
        var result = true

        while(i<j) {
            if(input[i] != input[j]) {
                result = false
                break;
            }
            i++
            j--
        }
        return result
    }

    fun reverse(input: String): String {
        var n = input.length-1
        var result = ""

        for(i in n downTo 0) {
            result += input[i]
        }
        return result
    }

    fun isValid(password: String?) = when {
        password.isNullOrEmpty() -> {
            false
        }
        (password.length<6 || password.length>15) -> {
            false
        }

        else -> {
            true
        }
    }
}