package com.kei.newsapp.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kei.newsapp.R
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    val date = getCurrentDateTime()

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
    fun Date.toString(format : String, locale: Locale = Locale.getDefault()):String{
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }
    companion object {
        fun getLaunchService (from: Context) = Intent(from, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        ib_profile_main.setOnClickListener(this)
        tv_date_main.text = date.toString("dd/MM/yyyy")
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.ib_profile_main -> startActivity(Intent(ProfileActivity.getLaunchService(this)))
        }
    }
}