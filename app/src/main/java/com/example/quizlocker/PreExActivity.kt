package com.example.quizlocker

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_pre_ex.*

class PreExActivity : AppCompatActivity() {

    val nameFieldKey = "nameField"
    val pushCheckBoxKey = "pushCheckBox"
    val preference by lazy {getSharedPreferences("PrefExActivity", Context.MODE_PRIVATE)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_ex)

        saveButton.setOnClickListener {
            preference.edit().putString(nameFieldKey, nameField.text.toString()).apply()

            preference.edit().putBoolean(pushCheckBoxKey, pushCheckBox.isChecked).apply()
        }

        loadButton.setOnClickListener {
            nameField.setText(preference.getString(nameFieldKey, ""))
            pushCheckBox.isChecked = preference.getBoolean(pushCheckBoxKey, false)
        }
    }
}