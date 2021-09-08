package com.finter.india.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.preference.PreferenceManager
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.lang.reflect.Type
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
class SharedPreferenceUtilC(@ApplicationContext val context: Context) : SharedPreferenceUtil {
    private val encryptedPreferencesName = "finter_prefs"

    private var sharedPreferences: SharedPreferences

    init {

        val nonEncryptedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sharedPreferences = nonEncryptedPreferences
            if (nonEncryptedPreferences.all.isNotEmpty()) {
                // migrate non encrypted shared preferences
                // to encrypted shared preferences and clear them once finished.
                nonEncryptedPreferences.copyTo(sharedPreferences)
                nonEncryptedPreferences.clear()
            }
        } else {
            sharedPreferences = nonEncryptedPreferences
        }
    }

    override fun putValue(key: String?, value: String?) {
        sharedPreferences.edit()?.putString(key, value)?.apply()
    }

    override fun putValue(key: String?, value: Int) {
        sharedPreferences.edit()?.putInt(key, value)?.apply()
    }

    override fun putValue(key: String?, value: Long) {
        sharedPreferences.edit()?.putLong(key, value)?.apply()
    }

    override fun putValue(key: String?, value: Boolean) {
        sharedPreferences.edit()?.putBoolean(key, value)?.apply()
    }

    override fun getString(key: String?, defValue: String?): String? {
        return sharedPreferences.getString(key, defValue)
    }

    override fun getInt(key: String?, defValue: Int): Int {
        return sharedPreferences.getInt(key, defValue) ?: 0
    }

    override fun getLong(key: String?, defValue: Long): Long {
        return sharedPreferences.getLong(key, defValue) ?: 0
    }

    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defValue) ?: false
    }

    override operator fun contains(key: String?): Boolean {
        return sharedPreferences.contains(key)
    }

    override fun remove(key: String?) {
        sharedPreferences.edit()?.remove(key)?.apply()
    }

    override fun clear() {
        sharedPreferences.clear()
    }

    override fun <T : Any> put(key: String, value: T) {
        sharedPreferences.put(key, value, value.javaClass)
    }

    override fun <T : Any> put(key: String, value: T, type: Type) {
        sharedPreferences.put(key, value, type)
    }

    override fun <T : Any> get(key: String, klass: KClass<T>, default: T): T {
        return sharedPreferences.getObject(
            key,
            klass.javaObjectType,
            default,
            moshi = Moshi.Builder().build()
        )
    }

    override fun <T : Any> getObject(
        key: String,
        type: Type,
        default: T
    ): T {
        return sharedPreferences.getObject(key, type, default, moshi = Moshi.Builder().build())
    }

}

interface SharedPreferenceUtil {
    fun putValue(key: String?, value: String?)

    fun putValue(key: String?, value: Int)

    fun putValue(key: String?, value: Long)

    fun putValue(key: String?, value: Boolean)

    fun getString(key: String?, defValue: String?): String?

    fun getInt(key: String?, defValue: Int): Int

    fun getLong(key: String?, defValue: Long): Long

    fun getBoolean(key: String?, defValue: Boolean): Boolean

    operator fun contains(key: String?): Boolean

    fun remove(key: String?)

    fun clear()

    fun <T : Any> put(key: String, value: T)
    fun <T : Any> put(key: String, value: T, type: Type)
    fun <T : Any> get(
        key: String,
        klass: KClass<T>,
        default: T
    ): T

    fun <T : Any> getObject(key: String, type: Type, default: T): T
}

//fun <T : Any> SharedPreferences.put(key: String, value: T, moshi: Moshi = Moshi.Builder().build()): Boolean = put(key, value, value.javaClass, moshi)

fun <T : Any> SharedPreferences.put(
    key: String,
    value: T,
    type: Type,
    moshi: Moshi = Moshi.Builder().build()
): Boolean {
    require(key.isNotEmpty()) { "Key must not be empty" }
    val json = moshi.adapter<T>(type).toJson(value)
    Timber.i("> putObject, storing $json with key $key")
    return edit().putString(key, json).commit()
}

//fun <T : Any> SharedPreferences.get(key: String, klass: KClass<T>, default: T, moshi: Moshi = Moshi.Builder().build()): T =
//    getObject(this, key, klass.javaObjectType, default, moshi)
//
//fun <T : Any> SharedPreferences.get(key: String, type: Type, default: T, moshi: Moshi = Moshi.Builder().build()): T =
//    getObject(this, key, type, default, moshi)

private fun <T : Any> SharedPreferences.getObject(
    key: String,
    type: Type,
    default: T,
    moshi: Moshi
): T {
    if (key.isEmpty()) {
        Timber.w("> getObject, key must not be empty, returning defaultValue")
        return default
    }
    val json = getString(key, null)
    return if (json == null) {
        Timber.w("> getObject, json is null for Key $key, returning defaultValue")
        default
    } else {
        try {
            moshi.adapter<T>(type).fromJson(json)!!
        } catch (e: JsonDataException) {
            Timber.e("> getObject, Object stored with Key $key not able to instance to $type")
            throw e
        }
    }
}