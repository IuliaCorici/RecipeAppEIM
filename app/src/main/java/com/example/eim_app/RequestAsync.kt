package com.example.eim_app

import android.app.Activity
import android.content.Context
import android.util.Log
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


fun downloadUrlAsync(context : Context, url: String, callback: (str: String) -> Unit) {
    thread {
        val conn = URL(url).openConnection() as HttpURLConnection
        conn.setRequestProperty("Accept", "application/json")

        try {
            val stream = BufferedInputStream(conn.inputStream)
            val data: String = readStream(inputStream = stream)

            (context as Activity).runOnUiThread {
                callback(data)
            }

        } catch (ex: Exception) {
            Log.d("Exception", ex.toString())
        }
    }
}

fun readStream(inputStream: BufferedInputStream): String {
    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
    val stringBuilder = StringBuilder()
    bufferedReader.forEachLine { stringBuilder.append(it) }
    return stringBuilder.toString()
}