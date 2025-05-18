package com.example.pulsepay12.service

import android.content.Context

object AuthUtils {
    fun getJwtToken(context: Context): String? {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return prefs.getString("access_token", null)
    }
}