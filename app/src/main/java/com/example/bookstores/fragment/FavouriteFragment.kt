package com.example.bookstores.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.bookstores.R
import com.example.bookstores.adapter.FavouriteRecyclerAdapter
import com.example.bookstores.database.BookDatabase
import com.example.bookstores.database.BookEntity


@Suppress("DEPRECATION")
class FavouriteFragment : Fragment() {

    lateinit var recyclerFavourite:RecyclerView
    lateinit var proggressLayout:RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavouriteRecyclerAdapter

    var dbBookList= listOf<BookEntity>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view=inflater.inflate(R.layout.fragment_favourite,container,false)

        setHasOptionsMenu(true)

        recyclerFavourite=view.findViewById(R.id.recyclerFavourite)
        proggressLayout=view.findViewById(R.id.progress_layout)
        progressBar=view.findViewById(R.id.progress_bar)

        layoutManager=GridLayoutManager(activity as Context,2)

        dbBookList=RetriveFavourites(activity as Context).execute().get()

        if(activity!=null){
            proggressLayout.visibility=View.GONE
            recyclerAdapter=FavouriteRecyclerAdapter(activity as Context, dbBookList)
            recyclerFavourite.adapter=recyclerAdapter
            recyclerFavourite.layoutManager=layoutManager

        }


        return view
    }

    class RetriveFavourites(val context: Context):AsyncTask<Void,Void,List<BookEntity>>(){
        override fun doInBackground(vararg p0: Void?): List<BookEntity> {
            val db= Room.databaseBuilder(context,BookDatabase::class.java,"books-db",).build()
            return db.bookDao().getAllBooks()
        }

    }
}