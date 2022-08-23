package com.gudangsimulasi.ayamgulingbandaaceh.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.contentValuesOf

class Session (c: Context) {
    private var sharedPreferences: SharedPreferences = c.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
    private val sharedEdit: SharedPreferences.Editor = sharedPreferences.edit()

    fun setLogin(isLogin: Boolean){
        sharedEdit.putBoolean(IS_LOGIN, isLogin)
        sharedEdit.apply()
    }
    fun isLogin(): Boolean{
        return sharedPreferences.getBoolean(IS_LOGIN, false)
    }

    companion object {
        const val SHARED_NAME = "session"
        const val IS_LOGIN = "is_login"
    }
}