package com.example.dbdiary_tugasakhir

import Diary
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.icu.text.CaseMap.Title

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "Diary", null, 1) {

    // Membuat Tabel
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("CREATE TABLE Diary (Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Title TEXT, " +
                "Content TEXT," +
                "Date TIME)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS Diary")
        onCreate(p0)

    }

    // CREATE
    fun insertDiary(title: String, content: String, date: String): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put("Title", title)
        values.put("Content", content)
        values.put("Date", date)
        return db.insert("Diary", null, values)
    }

    // READ
    fun getAllDiary(search: String = ""): MutableList<Diary> {
        val list = mutableListOf<Diary>()
        val db = readableDatabase

        // kalau search kosong → ambil semua
        // kalau ada isi → cari berdasarkan Title
        val cursor = if (search.isEmpty()) {
            db.rawQuery("SELECT * FROM Diary ORDER BY Id DESC", null)
        } else {
            db.rawQuery("SELECT * FROM Diary WHERE Title LIKE ? ORDER BY Id DESC", arrayOf("%$search%"))
        }

        if (cursor.moveToFirst()) {
            do {
                val diary = Diary(
                    cursor.getInt(cursor.getColumnIndexOrThrow("Id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Title")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Content")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Date"))
                )
                list.add(diary)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }


    // UPDATE
    fun updateDiary(id: Int, title: String, content: String, date: String): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put("Title", title)
        values.put("Content", content)
        values.put("Date", date)
        return db.update("Diary", values, "Id=?", arrayOf(id.toString()))
    }

    // DELETE
    fun deleteDiary(id: Int): Int {
        val db = writableDatabase
        return db.delete("Diary", "Id=?", arrayOf(id.toString()))
    }
}