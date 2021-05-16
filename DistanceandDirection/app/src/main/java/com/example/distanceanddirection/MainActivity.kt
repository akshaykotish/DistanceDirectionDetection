package com.example.distanceanddirection

import android.content.Context
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.telephony.SignalStrength
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception
import kotlin.system.measureTimeMillis


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var wifiManager:WifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        var txtview:TextView = findViewById(R.id.txtview) as TextView
        var signalstrength = findViewById(R.id.signalstrength) as TextView

        var circlebg = findViewById(R.id.circlebg) as LinearLayout

        var suggestion = findViewById(R.id.suggestion) as TextView
        suggestion
        var rssi= 0

        var frequency:Double = 2400.00;
        var res:Double = 0.0

        txtview.text = ""

        var lastminres:Double = 0.00

        var minres:Double = 0.00
        var maxres:Double = 0.00

        var ticks = 1



        var ToRUN = object : Thread() {

            override fun run() {
                try {
                    while (true) {
                        rssi = wifiManager.connectionInfo.rssi

                        res = calculateDistance(rssi.toDouble(), frequency)

                        if (minres == 0.00 && maxres == 0.00) {
                            minres = res
                            maxres = res
                        } else if (res < minres) {
                            minres = res
                        } else if (res > maxres) {
                            maxres = res
                        }




                        runOnUiThread(){
                            try {
                                txtview.setText("Signal : " + rssi + " db;\nDistance : " + res.toString().subSequence(0, 4) + " meter;\nPrevious : ${lastminres.toString().subSequence(0, 4)} ")

                                if (res > 1 && lastminres > 1) {
                                    circlebg.setBackgroundResource(R.drawable.signalcircle)
                                    if (res < lastminres) {
                                        suggestion.text = "${lastminres.toString().subSequence(0, 4)} meter Go Forward"
                                    } else {
                                        suggestion.text = "Detecting......."
                                    }

                                        signalstrength.text = "$rssi"
                                     }
                                else{
                                    circlebg.setBackgroundResource(R.drawable.transmitterfound)
                                    signalstrength.text = ""
                                    suggestion.text = "Transmitter lies within 1 meter range."
                                }
                            }
                            catch (Ex:Exception){
                                txtview.text = Ex.toString()
                            }
                        }

                        if (ticks == 10) {
                            lastminres = (minres + maxres) / 2
                            minres = 0.00
                            maxres = 0.00
                            ticks = 0
                        }


                        ticks++;
                        Thread.sleep(500)
                    }
                }
                catch (Ex:Exception){
                    txtview.setText("Erro ${Ex.toString()}")
                }
            }

        }


        ToRUN.start()
    }

    fun GetVlaue()
    {
        var rssi:Double = -56.4
        var freq:Double = 340.00
        var a = calculateDistance(rssi, freq)
        System.out.println("Distance is $a meter." )
    }


    fun calculateDistance(signalLevelInDb: Double, freqInMHz: Double): Double {
        val exp = (27.55 - 20 * Math.log10(freqInMHz) + Math.abs(signalLevelInDb)) / 20.0
        return Math.pow(10.0, exp)
    }
}