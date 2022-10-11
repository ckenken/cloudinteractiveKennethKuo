package com.kotklin.ckenken.cloudinteractivekennethkuo.others

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.startup.Initializer

class AAAInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Log.d("ckenken", "context = $context, context is Application = ${context is Application}")

        AAALib().init(context)
    }
    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(
            BBBInitializer::class.java
        )
    }
}


