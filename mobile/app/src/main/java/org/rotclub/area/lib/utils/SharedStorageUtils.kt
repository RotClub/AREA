package org.rotclub.area.lib.utils

import android.content.Context
import android.content.SharedPreferences

class SharedStorageUtils(localContext: Context) {

    var sharedPreferences: SharedPreferences = localContext
        .getSharedPreferences("Area", Context.MODE_PRIVATE)

    fun getToken(): String? {
        return sharedPreferences.getString("token", "")
    }

    fun clearToken() {
        sharedPreferences.edit().remove("token").apply()
    }

}