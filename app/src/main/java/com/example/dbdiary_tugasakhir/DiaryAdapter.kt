package com.example.dbdiary_tugasakhir

import Diary
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class DiaryAdapter(
    private var list: MutableList<Diary>,
    private val context: Context,
    private val onSelectionModeChange: (Boolean) -> Unit
) : RecyclerView.Adapter<DiaryAdapter.ViewHolder>() {

    // Simpan daftar posisi item yang dipilih
    val selectedItems = mutableSetOf<Int>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvJudul: TextView = view.findViewById(R.id.Tvjudul)
        val tvTanggal: TextView = view.findViewById(R.id.Waktu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvJudul.text = item.title
        holder.tvTanggal.text = item.date

        // Warna background jika item dipilih
        holder.itemView.setBackgroundColor(
            if (selectedItems.contains(position))
                ContextCompat.getColor(context, R.color.Purple)
            else
                ContextCompat.getColor(context, android.R.color.transparent)
        )

        // Klik untuk pilih / batal pilih
        holder.itemView.setOnClickListener {
            if (selectedItems.isNotEmpty()) {
                toggleSelection(position)
            } else {
                // Kalau belum mode seleksi, buka halaman edit
                val intent = Intent(context, AddDiary::class.java)
                intent.putExtra("id", item.id)
                intent.putExtra("Title", item.title)
                intent.putExtra("Content", item.content)
                intent.putExtra("Date", item.date)
                context.startActivity(intent)
            }
        }

        // Tekan lama buat masuk mode seleksi
        holder.itemView.setOnLongClickListener {
            toggleSelection(position)
            true
        }
    }

    override fun getItemCount(): Int = list.size

    // Fungsi toggle pilihan
    private fun toggleSelection(position: Int) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(position)
        } else {
            selectedItems.add(position)
        }
        notifyItemChanged(position)
        onSelectionModeChange(selectedItems.isNotEmpty())
    }

    // Ambil daftar Diary yang dipilih
    fun getSelectedItems(): List<Diary> {
        return selectedItems.map { list[it] }
    }

    // Hapus item terpilih dari database dan list
    fun removeSelectedItems(db: DatabaseHelper) {
        val toDelete = getSelectedItems()
        toDelete.forEach {
            db.deleteDiary(it.id)
            list.remove(it)
        }
        selectedItems.clear()
        notifyDataSetChanged()
        onSelectionModeChange(false)
    }

    fun updateData(newList: MutableList<Diary>) {
        list = newList
        notifyDataSetChanged()
    }
}
