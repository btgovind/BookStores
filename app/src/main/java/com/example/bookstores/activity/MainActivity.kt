package com.example.bookstores.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bookstores.R
import com.example.bookstores.fragment.FaqsFragment
import com.example.bookstores.fragment.FavouriteFragment
import com.example.bookstores.fragment.HomeFragment
import com.example.bookstores.fragment.MyProfileFragment
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var navigationView: NavigationView
    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var frameLayout: FrameLayout
    private var previousMenuItem: MenuItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout=findViewById(R.id.drawer_layout)
        navigationView=findViewById(R.id.navigation_view)
        coordinatorLayout=findViewById(R.id.coordinator)
        toolbar=findViewById(R.id.toolbar)
        frameLayout=findViewById(R.id.frame)
        setUpToolbar()
        displayHome()

        val actionBarDrawerToggle= ActionBarDrawerToggle(this@MainActivity,drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }

            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when(it.itemId){
                R.id.home ->{
                    displayHome()
                    drawerLayout.closeDrawers()
                }
                R.id.myProfile ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        MyProfileFragment()
                    ).commit()
                    supportActionBar?.title="My Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.favRes ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        FavouriteFragment()
                    ).commit()
                    supportActionBar?.title="Favourite"
                    drawerLayout.closeDrawers()
                }
                R.id.faqs ->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame, FaqsFragment()).commit()
                    supportActionBar?.title="FAQs"
                    drawerLayout.closeDrawers()
                }
                R.id.logout ->{
                    Toast.makeText(this@MainActivity,"Logout", Toast.LENGTH_SHORT).show()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }
    private  fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id=item.itemId
        if(id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun displayHome() {
        val fragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
        supportActionBar?.title = "Home"
        navigationView.setCheckedItem(R.id.home)
    }

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.frame)
        when(frag){
            !is HomeFragment -> displayHome()
            else -> super.onBackPressed()
        }
    }
}