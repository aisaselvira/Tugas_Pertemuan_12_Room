package com.example.tugas_pertemuan_12_room.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FilmDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(film: Film)
    @Update
    fun update(film: Film)
    @Delete
    fun delete(film: Film)
    @get:Query("SELECT * from film_table ORDER BY id ASC")
    val allFilms: LiveData<List<Film>>
}