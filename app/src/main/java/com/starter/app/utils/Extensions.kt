package com.starter.app.utils

import android.content.Intent
import android.os.Build
import android.os.Bundle
import java.io.Serializable

inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
        val result = getSerializable(key, T::class.java)
        requireNotNull(result) { "Failed to retrieve serialized object with key: $key" }
        result
    }

    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}

inline fun <reified T : Serializable> Intent.serializable(key: String): T = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
        val result = getSerializableExtra(key, T::class.java)
        requireNotNull(result) { "Failed to retrieve serialized object with key: $key" }
        result
    }

    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as T
}