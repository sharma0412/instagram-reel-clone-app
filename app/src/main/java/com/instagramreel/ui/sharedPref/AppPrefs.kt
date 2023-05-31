package com.instagramreel.ui.sharedPref

import android.content.Context
import android.content.SharedPreferences
import com.instagramreel.ui.utils.Constants.Companion.PREFS_NAME


class AppPrefs(private val ctx: Context) {

    private fun getPrefs(): SharedPreferences {
        return ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

     fun setString(key: String, value:String) {
        val edit = getPrefs().edit()
        edit.putString(key, value)
        edit.apply()
    }

    fun setInt(key: String, value: Int) {
        val edit = getPrefs().edit()
        edit.putInt(key, value)
        edit.apply()
    }

    fun getInt(key: String): Int {
        return getPrefs().getInt(key, 2)
    }

    fun getStringKey(key: String) :String?{
        return getPrefs().getString(key,"")
    }


    fun setToken(key : String,value: String){
        val edit = getPrefs().edit()
        edit.putString(key, value)
        edit.apply()
    }

    fun getToken(key: String) :String?{
        return getPrefs().getString(key,"")
    }
}