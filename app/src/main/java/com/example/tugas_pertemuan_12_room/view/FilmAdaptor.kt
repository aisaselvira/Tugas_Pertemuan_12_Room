package com.example.tugas_pertemuan_12_room.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas_pertemuan_12_room.database.Film
import com.example.tugas_pertemuan_12_room.databinding.ItemFilmBinding

class ListFilmAdapter(private var listFilm: List<Film>) :
    RecyclerView.Adapter<ListFilmAdapter.ItemFilmViewHolder>() {

    inner class ItemFilmViewHolder(private val binding: ItemFilmBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnHapus.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteClickListener?.onDeleteClick(listFilm[position])
                }
            }

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.onItemClick(listFilm[position])
                }
            }
        }

        fun bind(film: Film) {
            with(binding) {
                titleFilm.text = film.title
                descriptionFilm.text = film.description
                dateFilm.text = film.date
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newFilms: List<Film>) {
        listFilm = newFilms
        notifyDataSetChanged()
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(film: Film)
    }

    interface OnItemClickListener {
        fun onItemClick(film: Film)
    }

    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var onItemClickListener: OnItemClickListener? = null

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        onDeleteClickListener = listener
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFilmViewHolder {
        val binding = ItemFilmBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ItemFilmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemFilmViewHolder, position: Int) {
        holder.bind(listFilm[position])
    }

    override fun getItemCount(): Int = listFilm.size
}
