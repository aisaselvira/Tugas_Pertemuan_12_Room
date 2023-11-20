package com.example.tugas_pertemuan_12_room.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugas_pertemuan_12_room.database.Film
import com.example.tugas_pertemuan_12_room.database.FilmRoomDatabase
import com.example.tugas_pertemuan_12_room.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var listFilmAdapter: ListFilmAdapter
    private val UPDATE_FILM_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        val db = FilmRoomDatabase.getDatabase(this)
        val filmDao = db?.filmDao()

        val allFilms: LiveData<List<Film>>? = filmDao?.allFilms

        allFilms?.observe(this) { films ->
            films?.let { listFilmAdapter.setData(it) }
        }

        listFilmAdapter.setOnDeleteClickListener(object : ListFilmAdapter.OnDeleteClickListener {
            override fun onDeleteClick(film: Film) {
                deleteFilmInBackground(film)
            }
        })

        binding.btnPlus.setOnClickListener {
            val intent = Intent(this, Form::class.java)
            startActivity(intent)
        }

        listFilmAdapter.setOnItemClickListener(object : ListFilmAdapter.OnItemClickListener {
            override fun onItemClick(film: Film) {
                val intent = Intent(this@MainActivity, Form::class.java).apply {
                    putExtra(Form.EXTRA_ID, film.id)
                    putExtra(Form.EXTRA_TITLE, film.title)
                    putExtra(Form.EXTRA_DESC, film.description)
                    putExtra(Form.EXTRA_DATE, film.date)
                }
                startActivityForResult(intent, UPDATE_FILM_REQUEST)
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UPDATE_FILM_REQUEST && resultCode == RESULT_OK) {
            // Refresh the data in MainActivity after updating in Form
            fetchDataAndUpdateList()
        }
    }

    private fun fetchDataAndUpdateList() {
        val db = FilmRoomDatabase.getDatabase(this)
        val filmDao = db?.filmDao()

        val allFilms: LiveData<List<Film>>? = filmDao?.allFilms

        allFilms?.observe(this) { films ->
            films?.let { listFilmAdapter.setData(it) }
        }
    }

    private fun deleteFilmInBackground(film: Film) {
        val filmDao = FilmRoomDatabase.getDatabase(this)?.filmDao()
        filmDao?.let {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    // Access the database in the background
                    it.delete(film)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        listFilmAdapter = ListFilmAdapter(emptyList())
        binding.rvFilm.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = listFilmAdapter
        }
    }
}
