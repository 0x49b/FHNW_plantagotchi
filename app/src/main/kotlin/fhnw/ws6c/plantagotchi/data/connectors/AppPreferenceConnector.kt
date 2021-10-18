package fhnw.ws6c.plantagotchi.data.connectors

import android.content.Context
import android.content.SharedPreferences
import java.time.ZonedDateTime

enum class Prefs(key: String) {
    PLANTNAME("plantname"),
    FIRSTRUN("firstrun"),
    LAST_STOP("last_stop"),
}

object AppPreferences {
    private const val NAME = "PlantagotchiPreferences"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    fun contains(needle: String): Boolean {
        return preferences.contains(needle)
    }

    fun remove(key: Prefs) {
        val editor = preferences.edit()
        editor.remove(key.toString())
        editor.apply()
    }

    var last_stop: ZonedDateTime
        get() = ZonedDateTime.parse(preferences.getString(Prefs.LAST_STOP.toString(), ""))
        set(value) = preferences.edit { it.putString(Prefs.LAST_STOP.toString(), value.toString()) }

    var plantname: String
        get() = preferences.getString(Prefs.PLANTNAME.toString(), "").toString()
        set(value) = preferences.edit { it.putString(Prefs.PLANTNAME.toString(), value) }

    var firstrun: Boolean
        get() = preferences.getString(Prefs.FIRSTRUN.toString(), "true").toBoolean()
        set(value) = preferences.edit { it.putString(Prefs.FIRSTRUN.toString(), value.toString()) }

}