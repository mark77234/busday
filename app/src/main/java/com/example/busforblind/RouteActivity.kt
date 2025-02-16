package com.example.busforblind

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.button.MaterialButton
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class RouteActivity : AppCompatActivity() {

    private lateinit var startInput: EditText
    private lateinit var arriveInput: EditText
    private lateinit var backToMainButton: ImageButton
    private lateinit var micButtonStart: ImageButton
    private lateinit var micButtonArrive: ImageButton
    private lateinit var gpsButton: MaterialButton
    private lateinit var rotationButton: ImageButton

    private lateinit var searchGlassImageView: ImageView
    private lateinit var searchGlassTextView: TextView


    private val REQUEST_CODE_SPEECH_INPUT = 100
    private val REQUEST_CODE_LOCATION_PERMISSION = 101

    private var currentEditText: EditText? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)

        startInput = findViewById(R.id.start_input)
        arriveInput = findViewById(R.id.arrive_input)
        backToMainButton = findViewById(R.id.backToMain)
        micButtonStart = findViewById(R.id.mic_button_start)
        micButtonArrive = findViewById(R.id.mic_button_arrive)
        gpsButton = findViewById(R.id.gps_button)
        rotationButton = findViewById(R.id.rotation_btn)

        searchGlassImageView = findViewById(R.id.search_glass)
        searchGlassTextView = findViewById(R.id.search_glass_txt)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        backToMainButton.setOnClickListener {
            finish()
        }

        micButtonStart.setOnClickListener {
            setActiveEditText(startInput)
            promptSpeechInput()
        }

        micButtonArrive.setOnClickListener {
            setActiveEditText(arriveInput)
            promptSpeechInput()
        }

        gpsButton.setOnClickListener {
            checkLocationPermissionAndFetch()
        }

        rotationButton.setOnClickListener {
            swapTextValues()
        }

        startInput.addTextChangedListener { updateSearchGlassVisibility() }
        arriveInput.addTextChangedListener { updateSearchGlassVisibility() }
    }

    private fun setActiveEditText(editText: EditText) {
        currentEditText = editText
    }

    private fun promptSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ko-KR")
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "말씀하세요")
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
        } catch (e: Exception) {
            Toast.makeText(this, "음성 인식을 사용할 수 없습니다", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkLocationPermissionAndFetch() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION_PERMISSION)
        } else {
            fetchLocation()
        }
    }

    private fun fetchLocation() {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val addresses: List<Address> = geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    ) as List<Address>
                    if (addresses.isNotEmpty()) {
                        val address: Address = addresses[0]
                        startInput.setText(address.getAddressLine(0))
                    } else {
                        Toast.makeText(this, "주소를 찾을 수 없습니다", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "위치를 가져올 수 없습니다", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchLocation()
        } else {
            Toast.makeText(this, "위치 권한이 필요합니다", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (result != null && result.isNotEmpty()) {
                val spokenText = result[0]
                currentEditText?.setText(spokenText)
            }
        }
    }

    private fun swapTextValues() {
        val startText = startInput.text.toString()
        val arriveText = arriveInput.text.toString()

        startInput.setText(arriveText)
        arriveInput.setText(startText)
    }

    private fun updateSearchGlassVisibility() {
        val startText = startInput.text.toString()
        val arriveText = arriveInput.text.toString()
        val gpsButton = findViewById<MaterialButton>(R.id.gps_button)

        val busContainer1 = findViewById<FrameLayout>(R.id.bus_timetable_container1)
        val busContainer2 = findViewById<FrameLayout>(R.id.bus_timetable_container2)
        val busContainer3 = findViewById<FrameLayout>(R.id.bus_timetable_container3)

        if (startText.isEmpty() && arriveText.isEmpty()) {
            searchGlassImageView.setImageResource(R.drawable.search_glass) // 기본 이미지
            searchGlassTextView.text = "찾으시려는 경로를 \n입력해 주세요"
            searchGlassImageView.visibility = View.VISIBLE
            searchGlassTextView.visibility = View.VISIBLE
            busContainer1.visibility = View.INVISIBLE
            busContainer2.visibility = View.INVISIBLE
            busContainer3.visibility = View.INVISIBLE
        } else if (startText.isNotEmpty() && arriveText.isNotEmpty()) {
            if(startText == "대한민국 경상남도 밀양시 삼랑진읍 삼랑진로 1268-50" && arriveText == "밀양역"){
                searchGlassImageView.visibility = View.INVISIBLE
                searchGlassTextView.visibility = View.INVISIBLE
                gpsButton.visibility = View.INVISIBLE
                busContainer1.visibility = View.VISIBLE
                busContainer2.visibility = View.VISIBLE
                busContainer3.visibility = View.VISIBLE
            }
            else{
                searchGlassImageView.setImageResource(R.drawable.error) // 값이 있을 때 이미지
                searchGlassTextView.text = "검색 결과가 없습니다 \n\n 입력한 정보를 한번 더 확인해 주세요"
                searchGlassImageView.visibility = View.VISIBLE
                searchGlassTextView.visibility = View.VISIBLE
                gpsButton.visibility = View.VISIBLE
                busContainer1.visibility = View.INVISIBLE
                busContainer2.visibility = View.INVISIBLE
                busContainer3.visibility = View.INVISIBLE
            }

        } else {
            searchGlassImageView.setImageResource(R.drawable.search_glass)
            searchGlassTextView.text = "경로를 찾으려면 \n출발지와 목적지를 모두 입력해 주세요."
            searchGlassImageView.visibility = View.VISIBLE
            searchGlassTextView.visibility = View.VISIBLE
            gpsButton.visibility = View.VISIBLE
            busContainer1.visibility = View.INVISIBLE
            busContainer2.visibility = View.INVISIBLE
            busContainer3.visibility = View.INVISIBLE
        }
    }


}
