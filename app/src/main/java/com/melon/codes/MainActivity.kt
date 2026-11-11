package com.melon.codes

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private var generated: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.generateBtn).setOnClickListener { generate() }
        findViewById<Button>(R.id.copyBtn).setOnClickListener {
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.setPrimaryClip(ClipData.newPlainText("watermelon link", generated))
            Toast.makeText(this, "Link copy ho gaya", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generate() {
        val name = findViewById<EditText>(R.id.nameInput).text.toString().trim()
        var baseUrl = findViewById<EditText>(R.id.baseUrlInput).text.toString().trim()
        val searchPath = findViewById<EditText>(R.id.searchPathInput).text.toString().trim()

        if (name.isEmpty() || baseUrl.isEmpty() || searchPath.isEmpty()) {
            Toast.makeText(this, "Sab fields bharein", Toast.LENGTH_SHORT).show()
            return
        }
        if (!baseUrl.startsWith("http://") && !baseUrl.startsWith("https://")) {
            baseUrl = "https://$baseUrl"
        }
        if (!searchPath.contains("{q}")) {
            Toast.makeText(this, "Search path me {q} zaroor hona chahiye", Toast.LENGTH_LONG).show()
            return
        }

        val json = JSONObject()
            .put("name", name)
            .put("baseUrl", baseUrl)
            .put("searchPath", searchPath)
            .toString()

        val cfg = Base64.encodeToString(
            json.toByteArray(Charsets.UTF_8),
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
        )

        generated = "wm://add?cfg=$cfg"
        findViewById<TextView>(R.id.outputText).text = generated
        findViewById<Button>(R.id.copyBtn).visibility = View.VISIBLE
    }
}
