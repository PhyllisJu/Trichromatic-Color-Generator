package com.example.hw2solutions

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class HsvActivity : AppCompatActivity() {
    lateinit var seekBarHue : SeekBar
    lateinit var seekBarSat : SeekBar
    lateinit var seekBarValue: SeekBar
    lateinit var textViewHue: TextView
    lateinit var textViewSat: TextView
    lateinit var textViewValue: TextView
    lateinit var colorSquare : View
    lateinit var hexColorText : TextView
    lateinit var rgbButton : Button
    var hsvArr = FloatArray(3)
    var initHue = intent.extras?.getFloat("HUE") ?: Random.nextInt(0, 361) as Float
    var initSat = intent.extras?.getFloat("SAT") ?: Random.nextFloat()
    var initValue = intent.extras?.getFloat("VALUE") ?: Random.nextFloat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hsv)

        // Initialize the UI components
        seekBarHue = findViewById(R.id.seekBarHue)
        seekBarSat = findViewById(R.id.seekBarSat)
        seekBarValue = findViewById(R.id.seekBarValue)
        textViewHue = findViewById(R.id.textViewHue)
        textViewSat = findViewById(R.id.textViewSat)
        textViewValue = findViewById(R.id.textViewValue)
        colorSquare = findViewById(R.id.color_square)
        hexColorText = findViewById(R.id.textViewHexColor)
        rgbButton = findViewById(R.id.rgbButton)

        hsvArr[0] = initHue
        hsvArr[1] = initSat
        hsvArr[2] = initValue

        setUpSeekBar(seekBarHue, textViewHue, "Hue")
        setUpSeekBar(seekBarSat, textViewSat, "Sat")
        setUpSeekBar(seekBarValue, textViewValue, "Value")

        rgbButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View): Unit {
                test()
            }
        });

        regenerateColor()
    }

    private fun initialSetUp(sb: SeekBar, tv:TextView, hsvVal: String){
        if (hsvVal.equals("Hue")) {
            sb.progress = initHue.toInt()
            tv.text = resources.getString(R.string.seekbarLabel, "Hue", initHue.toInt())
        }
        else if (hsvVal.equals("Sat")) {
            sb.progress = (initSat * 100).toInt()
            tv.text = resources.getString(R.string.hsvSeekbarLabel, "Saturation", initSat.toInt())
        }
        else {
            sb.progress = (initValue * 100).toInt()
            tv.text = resources.getString(R.string.hsvSeekbarLabel, "Value", initValue.toInt())
        }
    }

    private fun setUpSeekBar(sb: SeekBar, tv: TextView, hsvVal: String) {
        if (hsvVal.equals("Hue")) {
            sb.max = 360
        } else {
            sb.max = 100
        }
        initialSetUp(sb, tv, hsvVal)

        sb.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (hsvVal.equals("Hue")) {
                    hsvArr[0] = progress.toFloat()
                    tv.text = resources.getString(R.string.hsvSeekbarLabel, "Hue", hsvArr[0])
                }
                else if (hsvVal.equals("Sat")) {
                    hsvArr[1] = progress.toFloat() / 100
                    tv.text = resources.getString(R.string.hsvSeekbarLabel, "Saturation", hsvArr[1])
                }
                else {
                    hsvArr[2] = progress.toFloat() / 100
                    tv.text = resources.getString(R.string.hsvSeekbarLabel, "Value", hsvArr[2])
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })


    }

    private fun regenerateColor(){
        colorSquare.setBackgroundColor (
            Color.HSVToColor(hsvArr)
        )
    }

    /*private fun setUpRgbButton(button : Button) {
        button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("COLOR", Color.HSVToColor(hsvArr))
        startActivity(intent)
    }*/

    fun test() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("COLOR", Color.HSVToColor(hsvArr))
        startActivity(intent)
    }
}