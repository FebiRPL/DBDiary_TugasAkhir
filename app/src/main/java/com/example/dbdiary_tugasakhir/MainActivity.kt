package com.example.dbdiary_tugasakhir

import Diary
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.ImageView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var adapter: DiaryAdapter
    private lateinit var list: ArrayList<Diary>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi komponen
        db = DatabaseHelper(this)
        val recyclerView = findViewById<RecyclerView>(R.id.RCYDiary)
        val btnAdd = findViewById<ImageView>(R.id.IVAdd)
        val searchView = findViewById<SearchView>(R.id.Search)

        // Set up RecyclerView
        list = ArrayList()
        adapter = DiaryAdapter(list, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Load semua data pertama kali
        loadData("")

        // Tombol tambah diary
        btnAdd.setOnClickListener {
            startActivity(Intent(this, AddDiary::class.java))
        }

        // Fitur search diary berdasarkan judul
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                loadData(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                loadData(newText ?: "")
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadData("") // refresh data setelah tambah/edit
    }

    // Fungsi untuk menampilkan data dari database
    private fun loadData(search: String) {
        list.clear()
        list.addAll(db.getAllDiary(search)) // langsung ambil dari DatabaseHelper
        adapter.notifyDataSetChanged()
    }
}
