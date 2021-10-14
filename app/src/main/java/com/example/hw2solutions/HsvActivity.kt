package com.example.hw2solutions

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import java.util.jar.Manifest
import kotlin.math.roundToInt
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
    lateinit var locButton : Button
    lateinit var locationClient : FusedLocationProviderClient
    var hsvArr = FloatArray(3)
    var initHue = Random.nextInt(0, 361).toFloat()
    var initSat = Random.nextFloat()
    var initValue = Random.nextFloat()

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
        locButton = findViewById(R.id.locButton)

        initHue = getIntent().extras?.getFloat("HUE") ?: Random.nextInt(0, 361).toFloat()
        initSat = getIntent().extras?.getFloat("SAT") ?: Random.nextInt(0, 361).toFloat()
        initValue = getIntent().extras?.getFloat("VALUE") ?: Random.nextInt(0, 361).toFloat()

        hsvArr[0] = initHue
        hsvArr[1] = initSat
        hsvArr[2] = initValue

        setUpSeekBar(seekBarHue, textViewHue, "Hue")
        setUpSeekBar(seekBarSat, textViewSat, "Sat")
        setUpSeekBar(seekBarValue, textViewValue, "Value")

        rgbButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                rgbBtnClick()
            }
        })

        locButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                locBtnClick()
            }
        })

        regenerateColor()
    }

    private fun initialSetUp(sb: SeekBar, tv:TextView, hsvVal: String){
        if (hsvVal.equals("Hue")) {
            sb.progress = initHue.toInt()
            tv.text = resources.getString(R.string.seekbarLabel, "Hue", initHue.toInt())
        }
        else if (hsvVal.equals("Sat")) {
            sb.progress = (initSat * 100).toInt()
            tv.text = resources.getString(R.string.hsvSeekbarLabel, "Saturation", initSat)
        }
        else {
            sb.progress = (initValue * 100).toInt()
            tv.text = resources.getString(R.string.hsvSeekbarLabel, "Value", initValue)
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
                regenerateColor()
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

    fun rgbBtnClick() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("COLOR", Color.HSVToColor(hsvArr))
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    fun locBtnClick() {
        locationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationClient.lastLocation.addOnSuccessListener {
                    location -> Log.d("TAG", location.latitude.toString() ?: "#043900")
                    Color.colorToHSV(Color.parseColor(getColorString(location.latitude)), hsvArr)
                    regenerateColor()
                    textViewHue.text = resources.getString(R.string.hsvSeekbarLabel, "Hue", hsvArr[0])
                    textViewSat.text = resources.getString(R.string.hsvSeekbarLabel, "Saturation", hsvArr[1])
                    textViewValue.text = resources.getString(R.string.hsvSeekbarLabel, "Value", hsvArr[2])
                    seekBarHue.progress = hsvArr[0].toInt()
                    seekBarSat.progress = (hsvArr[1] * 100).toInt()
                    seekBarValue.progress = (hsvArr[2] * 100).toInt()

            }
        } else {
            val snackBar = Snackbar.make(locButton, "Permission Denied", Snackbar.LENGTH_LONG)
            snackBar.show()
            snackBar.setAction("RETRY") {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }
    }

    // enter location.latitude as a parameter
    private fun getColorString(latitude : Double) : String {
        return resources.getString(
            R.string.locationString,
            ((latitude % 1) * 100000).roundToInt().toString().padStart(6, '0')
        )
    }
}