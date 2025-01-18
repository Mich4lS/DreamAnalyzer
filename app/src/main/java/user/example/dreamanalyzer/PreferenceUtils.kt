package user.example.dreamanalyzer

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun saveDreamsToPreferences(context: Context, dreams: List<Dream>) {
    val sharedPreferences = context.getSharedPreferences("DreamPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    val gson = Gson()
    val json = gson.toJson(dreams)
    editor.putString("dreamList", json)
    editor.apply()
}

fun loadDreamsFromPreferences(context: Context): MutableList<Dream> {
    val sharedPreferences = context.getSharedPreferences("DreamPrefs", Context.MODE_PRIVATE)
    val json = sharedPreferences.getString("dreamList", null)

    return if (json != null) {
        val gson = Gson()
        val type = object : TypeToken<MutableList<Dream>>() {}.type
        gson.fromJson(json, type)
    } else {
        mutableListOf()
    }
}
