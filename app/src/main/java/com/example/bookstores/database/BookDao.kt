package com.example.bookstores.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookDao {

    @Insert
    fun insertBook(bookEntities: BookEntity)

    @Delete
    fun deleteBook(bookEntities: BookEntity)

    @Query("SELECT*FROM books")
    fun getAllBooks():List<BookEntity>

    @Query("SELECT*FROM books WHERE book_id=:bookId")
    fun getBookbyId(bookId:String):BookEntity
}