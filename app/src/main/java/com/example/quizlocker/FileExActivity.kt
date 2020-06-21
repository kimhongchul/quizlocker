package com.example.quizlocker

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_file_ex.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.jar.Manifest

class FileExActivity : AppCompatActivity() {

    var granted = false
    val filename = "data.txt"
    val MY_PERMISSION_REQUEST = 999

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_ex)

        checkPermission()

        saveButton.setOnClickListener {
            val text = textField.text.toString()
            when {
                TextUtils.isEmpty(text) -> {
                    Toast.makeText(applicationContext, "텍스트가 비었습니다.", Toast.LENGTH_LONG).show()
                }
                !isExternalStorageWritable() -> {
                    Toast.makeText(applicationContext, "외부 저장장치가 없습니다.", Toast.LENGTH_LONG).show()
                }
                else -> {
                    //saveToInner
                    // Storage(text, filename)
                    //saveToExternalStorage(text, filename)
                    saveToExternalCustomDirectory(text)
                }
            }
        }

        loadButton.setOnClickListener {
            try {
                //textField.setText(loadFromInnerStorage(filename))
                //textField.setText(loadFromExternalStorage(filename))
                textField.setText(loadFromExternalCustomDirectory())
            } catch (e:FileNotFoundException) {
                Toast.makeText(applicationContext, "저장된 텍스트가 없습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun saveToInnerStorage(text:String, filename:String) {
        val fileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE)

        fileOutputStream.write(text.toByteArray())

        fileOutputStream.close()
    }

    fun loadFromInnerStorage(filename:String):String {
        val fileInputStream = openFileInput(filename)

        return fileInputStream.reader().readText()
    }

    fun isExternalStorageWritable():Boolean {
        when {
            Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED -> return true
            else -> return false
        }
    }

    fun getAppDataFileFromExternalStorage(filename:String): File {
        val dir = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        } else {
            File(Environment.getExternalStorageDirectory().absolutePath + "/Document")
        }
        dir?.mkdirs()
        return File("${dir?.absolutePath}${File.separator}${filename}")
    }

    fun saveToExternalStorage(text:String, filename:String) {
        val fileOutputStream = FileOutputStream(getAppDataFileFromExternalStorage(filename))
        fileOutputStream.write(text.toByteArray())
        fileOutputStream.close()
    }

    fun loadFromExternalStorage(filename:String):String {
        return FileInputStream(getAppDataFileFromExternalStorage(filename)).reader().readText()
    }

    fun checkPermission() {
        val permissionCheck = ContextCompat.checkSelfPermission(this@FileExActivity,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        when {
            permissionCheck != PackageManager.PERMISSION_GRANTED -> {
                ActivityCompat.requestPermissions(this@FileExActivity,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSION_REQUEST)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            MY_PERMISSION_REQUEST -> {
                when {
                    grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                        granted = true
                    }
                    else -> {
                        granted = false
                    }
                }
            }
        }
    }

    fun saveToExternalCustomDirectory(text:String, filepath:String="/sdcard/Download/data.txt") {
        when {
            granted -> {
                val fileOutputStream = FileOutputStream(File(filepath))
                fileOutputStream.write(text.toByteArray())
                fileOutputStream.close()
            }
            else -> {
                Toast.makeText(applicationContext, "권한이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun loadFromExternalCustomDirectory(filepath:String="/sdcard/Download/data.txt"):String {
        when {
            granted -> {
                return FileInputStream(File(filepath)).reader().readText()
            }
            else -> {
                Toast.makeText(applicationContext, "권한이 없습니다.", Toast.LENGTH_SHORT).show()
                return ""
            }
        }
    }
}