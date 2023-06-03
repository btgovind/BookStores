package com.example.bookstores.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import java.util.ArrayList
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstores.R
import com.example.bookstores.activity.DescriptionActivity
import com.example.bookstores.model.Book
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(val context: Context, val itemList:ArrayList<Book>): RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {

    class HomeViewHolder(view:View): RecyclerView.ViewHolder(view){
        val bookname:TextView=view.findViewById(R.id.txtBookName)
        val authorName:TextView=view.findViewById(R.id.txtAuthorName)
        val bookPrice:TextView=view.findViewById(R.id.txtbookPrice)
        val bookRating:TextView=view.findViewById(R.id.txtBookRating)
        val bookImage:ImageView=view.findViewById(R.id.imgbook)
        val llContent:LinearLayout=view.findViewById(R.id.llContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val book=itemList[position]
        holder.bookname.text=book.bookName
        holder.authorName.text=book.bookAuthor
        holder.bookPrice.text=book.bookPrice
        holder.bookRating.text=book.bookRating
        //holder.bookImage.setImageResource(book.bookImage)
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.bookImage)

        holder.llContent.setOnClickListener {
            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("book_id", book.BookId)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}