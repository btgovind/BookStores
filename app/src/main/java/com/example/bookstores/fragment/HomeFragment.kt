package com.example.bookstores.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Header
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookstores.R
import com.example.bookstores.adapter.HomeRecyclerAdapter
import com.example.bookstores.model.Book
import com.example.bookstores.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

class HomeFragment : Fragment() {

     lateinit var recyclerDashboard: RecyclerView
     lateinit var layoutManager: RecyclerView.LayoutManager
     lateinit var progressBar: ProgressBar
     lateinit var progresslayout:RelativeLayout

     val bookList= arrayListOf("P.S I Love You","The Greate Gatsby","Anna Karennia","War are Peace","Lolita",
         "MiddleMarch","The Adventures Of Huckeleberry Finn","Moby_Dick","Lord of Rings")

    lateinit var recyclerAdapter: HomeRecyclerAdapter
    val bookinfoList= arrayListOf<Book>()

    var ratingComparator=Comparator<Book> { book1, book2 ->
        if(book1.bookRating.compareTo(book2.bookRating, true) == 0){
        book1.bookName.compareTo(book2.bookName, true)
    }else{
        book1.bookRating.compareTo(book2.bookRating,true)
    }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_home, container, false)

        setHasOptionsMenu(true)

        recyclerDashboard=view.findViewById(R.id.recycler_view)
        progresslayout=view.findViewById(R.id.progress_layout)
        progressBar=view.findViewById(R.id.progressBar)
        progressBar.visibility=View.VISIBLE

        layoutManager=LinearLayoutManager(activity)


        val queue=Volley.newRequestQueue(activity as Context)
        val url="http://13.235.250.119/v1/book/fetch_books/"

        if(ConnectionManager().checkConnectivity(activity as Context)){

            val jsonObjectRequest= object :JsonObjectRequest(Request.Method.GET,url,null,Response.Listener {

                try{
                    progresslayout.visibility=View.GONE
                    val success=it.getBoolean("success")
                    if(success) {
                        val data = it.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val bookjsonobject = data.getJSONObject(i)
                            val bookObject = Book(
                                bookjsonobject.getString("book_id"),
                                bookjsonobject.getString("name"),
                                bookjsonobject.getString("author"),
                                bookjsonobject.getString("rating"),
                                bookjsonobject.getString("price"),
                                bookjsonobject.getString("image")
                            )
                            bookinfoList.add(bookObject)
                            recyclerAdapter= HomeRecyclerAdapter(activity as Context,bookinfoList)

                            recyclerDashboard.adapter=recyclerAdapter
                            recyclerDashboard.layoutManager=layoutManager

                        }
                    }else{
                        Toast.makeText(activity as Context,"Some Error Occured!",Toast.LENGTH_SHORT).show()
                    }
                }catch (e:JSONException){
                    Toast.makeText(activity as Context,"Some Unexpected Error Occured",Toast.LENGTH_SHORT).show()
                }


            },Response.ErrorListener {

                if(activity!=null) {
                    Toast.makeText(activity as Context, "Volley Error Occured", Toast.LENGTH_SHORT)
                        .show()
                }
            }) {

                override fun getHeaders(): MutableMap<String, String> {
                    val headers=HashMap<String,String>()
                    headers["Content-type"]="application/json"
                    headers["token"]="9bf534118365f1"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)
        }else{
            val dialog=AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Interne Connection Not Found")
            dialog.setPositiveButton("Open Settings"){text,listener->
                //jjffj
                val settingIntent=Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text,listener->

                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return  view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id=item?.itemId
        if(id==R.id.action_sort){
            Collections.sort(bookinfoList,ratingComparator)

            bookinfoList.reverse()
        }

        recyclerAdapter.notifyDataSetChanged()

        return super.onOptionsItemSelected(item)
    }
}