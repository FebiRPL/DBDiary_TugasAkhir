package com.example.dbdiary_tugasakhir

import Diary
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
        val Delete = findViewById<ImageView>(R.id.IVDelete)

        // Tombol delete disembunyikan dulu
        Delete.visibility = View.GONE  // 👇 tambahan

        // Set up RecyclerView
        list = ArrayList()
        adapter = DiaryAdapter(list, this) { isSelecting ->
            // Kalau user lagi tahan item (mode pilih aktif), tampilkan tombol delete
            Delete.visibility = if (isSelecting) View.VISIBLE else View.GONE  // 👇 tambahan
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Load semua data pertama kali
        loadData("")

        // Tombol tambah diary
        btnAdd.setOnClickListener {
            startActivity(Intent(this, AddDiary::class.java))
        }

        // Tombol hapus diary
        Delete.setOnClickListener {
            val selected = adapter.getSelectedItems()
            if (selected.isEmpty()) {
                Toast.makeText(this, "Pilih Diary yang akan dihapus", Toast.LENGTH_SHORT).show()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Hapus Diary")
                    .setMessage("Yakin mau hapus diary? ${selected.size}")
                    .setPositiveButton("Ya") { _, _ ->
                        adapter.removeSelectedItems(db)
                        Toast.makeText(this, "Diary berhasil dihapus", Toast.LENGTH_SHORT).show()
                        Delete.visibility = View.GONE  // 👇 tambahan
                    }
                    .setNegativeButton("Tidak", null)
                    .show()
            }
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