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

class MainActivity : AppCompatActivity() {
    private val max = 255

    lateinit var seekBarRed : SeekBar
    lateinit var seekBarBlue : SeekBar
    lateinit var seekBarGreen: SeekBar
    lateinit var textViewRed: TextView
    lateinit var textViewBlue: TextView
    lateinit var textViewGreen: TextView
    lateinit var colorSquare : View
    lateinit var hexColorText : TextView
    lateinit var hsvButton : Button
    lateinit var color : Integer  //= intent.extras?.getInt("COLOR")
    lateinit var shareButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the UI components
        seekBarRed = findViewById(R.id.seekBarRed)
        seekBarGreen = findViewById(R.id.seekBarGreen)
        seekBarBlue = findViewById(R.id.seekBarBlue)
        textViewRed = findViewById(R.id.textViewRed)
        textViewGreen = findViewById(R.id.textViewGreen)
        textViewBlue = findViewById(R.id.textViewBlue)
        colorSquare = findViewById(R.id.color_square)
        hexColorText = findViewById(R.id.textViewHexColor)
        hsvButton = findViewById(R.id.hsvButton)
        shareButton = findViewById(R.id.shareButton)

        color = (intent.extras?.getInt("COLOR") ?: Color.rgb(Random.nextInt(0, 256),
            Random.nextInt(0, 256), Random.nextInt(0, 256))) as Integer

        setUpSeekbar(seekBarRed, textViewRed, resources.getString(R.string.red))
        setUpSeekbar(seekBarGreen, textViewGreen, resources.getString(R.string.green))
        setUpSeekbar(seekBarBlue, textViewBlue, resources.getString(R.string.blue))
        hsvButton.setOnClickListener(object : View.OnClickListener {
             override fun onClick(v: View) {
                hsvBtnClick()
            }
        })

        shareButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v : View) {
                var intent = Intent()
                intent.setAction(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_TEXT, "Check out this color: <"+hexColorText.text+">")
                intent.setType("text/plain")
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent)
                }
            }
        })


        // Initialize the square's color on onCreate()
        regenerateColor()

    }

    private fun initialSetUp(sb: SeekBar, tv: TextView, c: String) {
        // Set initial color to random number

        if (color != null) {
            if (c.equals("Red")) {
                sb.progress = Color.red(color.toInt())
            } else if (c.equals("Green")) {
                sb.progress = Color.green(color.toInt())
            }
            else {
                sb.progress = Color.blue(color.toInt())
            }
            tv.text = resources.getString(R.string.seekbarLabel, c, sb.progress)
        }
    }

    private fun setUpSeekbar(sb: SeekBar, tv: TextView, color : String) {
        // Set the max value of seekbar to max hexcode - 255
        sb.max = max
        initialSetUp(sb, tv, color)

        sb.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                regenerateColor()

                // Set TextView based on orientation
                updateSeekBarTextView(tv, color, p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
    }

    // Modifies text label next to SeekBar depending on device orientation
    private fun updateSeekBarTextView(tv: TextView, color: String, progress: Int) {
        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                tv.text = color
            }
            Configuration.ORIENTATION_PORTRAIT -> {
                tv.text = resources.getString(R.string.seekbarLabel, color, progress)
            }
        }
    }

    // Regenerates the color of the color square.
    private fun regenerateColor() {
        colorSquare.setBackgroundColor(
            Color.rgb(seekBarRed.progress, seekBarGreen.progress, seekBarBlue.progress)
        )

        hexColorText.text = resources.getString(
            R.string.hexString,
            Integer.toHexString(seekBarRed.progress).toUpperCase(),
            Integer.toHexString(seekBarGreen.progress).toUpperCase(),
            Integer.toHexString(seekBarBlue.progress).toUpperCase()
        )

    }

 /*   private fun setUpHsvButton(button : Button) {

    }

    override fun onClick(v: View?) {
        var hsvArr = FloatArray(3)
        Color.RGBToHSV(seekBarRed.progress, seekBarGreen.progress, seekBarBlue.progress, hsvArr)
        var intent = Intent(this, HsvActivity::class.java)
        intent.putExtra("HUE", hsvArr[0])
        intent.putExtra("SAT",hsvArr[1])
        intent.putExtra("VALUE",hsvArr[2])
        startActivity(intent)
    }*/

  fun hsvBtnClick() {
     var hsvArr = FloatArray(3)
     Color.RGBToHSV(
         seekBarRed.progress,
         seekBarGreen.progress,
         seekBarBlue.progress,
         hsvArr
     )
     var intent = Intent(this, HsvActivity::class.java)
     intent.putExtra("HUE", hsvArr[0])
     intent.putExtra("SAT", hsvArr[1])
     intent.putExtra("VALUE", hsvArr[2])
      if (intent.resolveActivity(packageManager) != null) {
          startActivity(intent)
      }

 }

}