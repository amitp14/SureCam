package com.example.surecam

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    companion object {
        private const val PREF_NAME = "MyAppPrefs"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USER_ID = "userId"
        private const val KEY_USER_EMAIL = "userEmail"
        // Add more keys as needed for user data
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun isLoggedIn(): Boolean {

        if(getUserEmail() != null && getUserEmail() != ""){
            return true
        }else{
            return false
        }

    }

    fun loggOut() {

        setUserEmail("")
    }

    fun setUserId(userId: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USER_ID, userId)
        editor.apply()
    }


    private fun getUserId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, null)
    }

    fun setUserEmail(userEmail: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USER_EMAIL, userEmail)
        editor.apply()
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString(KEY_USER_EMAIL, null)
    }

    // Add more functions for handling other session-related data as needed
}