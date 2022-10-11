package com.kotklin.ckenken.cloudinteractivekennethkuo.others

import android.content.Context
import android.util.Log
import androidx.startup.Initializer

class BBBInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Log.d("ckenken", "BBBBBBBBB")
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}