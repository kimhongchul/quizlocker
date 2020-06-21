package com.example.quizlocker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class ScreenOffReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when {
            intent?.action == Intent.ACTION_SCREEN_OFF -> {

                val intent = Intent(context, QuizLockerActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context?.startActivity(intent)
//                Log.d("ScreenOffReceiver","퀴즈잠금: 화면이 꺼졌습니다.")
//                Toast.makeText(context, "퀴즈잠금: 화면이 꺼졌습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

}