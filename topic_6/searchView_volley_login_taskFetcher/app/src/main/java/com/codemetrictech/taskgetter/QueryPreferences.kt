package com.codemetrictech.taskgetter

import android.app.Activity
import android.content.Context
import android.preference.PreferenceManager
import android.util.Log

open class QueryPreferences {
    companion object queryPreference{
        val TAG = "queryPreference"
        private val PREF_SEARCH_QUERY = "searchQuery"

        fun getStoredQuery(context: Context): String? {
            return PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(PREF_SEARCH_QUERY, null)
        }

        fun setStoredQuery(context: Context, query: String) {
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putString(PREF_SEARCH_QUERY, query)
                    .apply()
            Log.d(TAG,"setStoredQuery: $query")
        }
    }
}
