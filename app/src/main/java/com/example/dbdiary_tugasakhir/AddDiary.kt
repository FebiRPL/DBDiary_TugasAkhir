package com.example.dbdiary_tugasakhir

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AddDiary : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_diary)

        db = DatabaseHelper(this)

        val EtTitle = findViewById<EditText>(R.id.ETjudul)
        val ETContent = findViewById<EditText>(R.id.ETisi)
        val btnSave = findViewById<ImageView>(R.id.IVSave)
        val btnBack = findViewById<ImageView>(R.id.IVBack)


        id = intent.getIntExtra("id", -1)
        if (id != -1) {
            EtTitle.setText(intent.getStringExtra("Title"))
            ETContent.setText(intent.getStringExtra("Content"))
        }

        btnSave.setOnClickListener {
            val Title = EtTitle.text.toString()
            val Content = ETContent.text.toString()
            val Date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())

            if (id == -1) {
                db.insertDiary(Title, Content, Date)
            } else {
                db.updateDiary(id!!, Title, Content, Date)
            }
            finish()
        }

        btnBack.setOnClickListener{
            finish()
        }
    }
}
