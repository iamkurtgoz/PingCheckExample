package com.iamkurtgoz.pingcheckexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.stealthcopter.networktools.Ping
import com.stealthcopter.networktools.ping.PingResult
import com.stealthcopter.networktools.ping.PingStats
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private var isRemaining: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onStartPingTest(view: View){
        var count: Int? = editCount.text.toString().toIntOrNull()
        if (count == null){
            count = 5
        }
        if (!isRemaining){
            Ping.onAddress("172.217.17.110").setDelayMillis(1000).setTimes(count).doPing(object: Ping.PingListener{
                override fun onResult(result: PingResult?) {
                    Handler(Looper.getMainLooper()).post { textResult.text = result?.fullString }
                    Log.d("MyLog", "Res: ${result?.toString()}")
                }

                override fun onError(p0: Exception?) {
                    isRemaining = false
                    Handler(Looper.getMainLooper()).post { textResult.text = p0?.localizedMessage }
                    Log.d("MyLog", "Error: ${p0?.localizedMessage}")
                }

                override fun onFinished(p0: PingStats?) {
                    isRemaining = false
                    val min: Float? = p0?.minTimeTaken
                    val max: Float? = p0?.maxTimeTaken
                    Handler(Looper.getMainLooper()).post { textResult.text = "Max: $max - Min: $min" }
                    Log.d("MyLog", "Stats: ${p0?.toString()}")
                }
            })
        }
    }
}
