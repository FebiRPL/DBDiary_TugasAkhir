package com.example.dbdiary_tugasakhir

import Diary
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DiaryAdapter(private var list: MutableList<Diary>, val context: Context) :
    RecyclerView.Adapter<DiaryAdapter.ViewHolder>() {

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

        holder.itemView.setOnClickListener {
            val intent = Intent(context, AddDiary::class.java)
            intent.putExtra("id", item.id)
            intent.putExtra("Title", item.title)
            intent.putExtra("Content", item.content)
            intent.putExtra("Date", item.date)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: MutableList<Diary>) {
        list = newList
        notifyDataSetChanged()
    }
}
