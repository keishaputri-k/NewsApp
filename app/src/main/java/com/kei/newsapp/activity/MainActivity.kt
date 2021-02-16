package com.kei.newsapp.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kei.newsapp.NewsAdapter
import com.kei.newsapp.R
import com.kei.newsapp.model.ResponseNews
import com.kei.newsapp.service.RetrofitConfig
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        getNews()
    }

    private fun getNews() {
        var country = "id"
        var apiKey = "8dddfa41ffd2420b960f4ff66b17b0b8"

        var loading = ProgressDialog.show(this, "Request Data", "Loading...")
        RetrofitConfig.getInstance().getNewsData(country, apiKey).enqueue(
            object : Callback<ResponseNews>{
                override fun onResponse(
                    call: Call<ResponseNews>,
                    response: Response<ResponseNews>
                ){
                    Log.d("Response", "Success" + response.body()?.articles)
                    loading.dismiss()
                    if (response.isSuccessful){
                        val status = response.body()?.status
                        if (status.equals("ok")){
                            Toast.makeText(this@MainActivity, "Data Success !", Toast.LENGTH_SHORT).show()
                            val newsData = response.body()?.articles
                            val newsAdapter = NewsAdapter(this@MainActivity, newsData)
                            rv_main.adapter = newsAdapter
                            rv_main.layoutManager = LinearLayoutManager(this@MainActivity)
                        }else{
                            Toast.makeText(this@MainActivity, "Data Failed !", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: Call<ResponseNews>, t: Throwable) {
                    Log.d("Response","Failed : " + t.localizedMessage)
                    loading.dismiss()
                }
            }
        )
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.ib_profile_main -> startActivity(Intent(ProfileActivity.getLaunchService(this)))
        }
    }
}