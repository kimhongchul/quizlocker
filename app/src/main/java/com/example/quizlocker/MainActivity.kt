package com.example.quizlocker

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.MultiSelectListPreference
import android.preference.PreferenceFragment
import android.preference.SwitchPreference
import kotlinx.android.synthetic.main.activity_main.*
import java.util.HashSet
import kotlin.contracts.contract

class MainActivity : AppCompatActivity() {

    val fragment = MyPreferenceFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentManager.beginTransaction().replace(R.id.preferenceContent, fragment).commit()

        initButton.setOnClickListener {
            initAnswer()
        }
    }

    fun initAnswer() {
        val correctAnswerPref = getSharedPreferences("correctAnswer", Context.MODE_PRIVATE)
        val wrongAnswerPref = getSharedPreferences("wrongAnswer", Context.MODE_PRIVATE)
        correctAnswerPref.edit().clear().apply()
        wrongAnswerPref.edit().clear().apply()
    }

    class MyPreferenceFragment:PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            addPreferencesFromResource(R.xml.pref)

            val categoryPref = findPreference("category") as MultiSelectListPreference
            categoryPref.summary = categoryPref.values.joinToString(", ")

            categoryPref.setOnPreferenceChangeListener { preference, newValue ->
                val newValueSet =
                    newValue as? HashSet<*> ?: return@setOnPreferenceChangeListener true
                categoryPref.summary = newValue.joinToString(", ")

                true
            }


            val useLockScreenPref = findPreference("useLockScreen") as SwitchPreference
            useLockScreenPref.setOnPreferenceClickListener {
                when {
                    useLockScreenPref.isChecked -> {
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            activity.startForegroundService(Intent(activity, LockScreenService::class.java))
                        } else {
                            activity.startService(Intent(activity, LockScreenService::class.java))
                        }
                    }
                    else -> {
                        activity.stopService(Intent(activity, LockScreenService::class.java))
                    }
                }
                true
            }

            if(useLockScreenPref.isChecked) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    activity.startForegroundService(Intent(activity, LockScreenService::class.java))
                } else {
                    activity.startService(Intent(activity, LockScreenService::class.java))
                }
            }
        }
    }
}