package com.example.myapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil

class SimulationActivity : AppCompatActivity() {

    private lateinit var interpreter: Interpreter

    private lateinit var inputDay: EditText
    private lateinit var inputCar: EditText
    private lateinit var inputBike: EditText
    private lateinit var inputBus: EditText
    private lateinit var inputTruck: EditText
    private lateinit var btnRun: Button
    private lateinit var tvPrediction: TextView
    private lateinit var tvResult: TextView
    private lateinit var resultCard: CardView

    private val labels = arrayOf("Macet", "Padat", "Normal", "Lancar")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulation)

        inputDay = findViewById(R.id.input_day)
        inputCar = findViewById(R.id.input_car)
        inputBike = findViewById(R.id.input_bike)
        inputBus = findViewById(R.id.input_bus)
        inputTruck = findViewById(R.id.input_truck)
        btnRun = findViewById(R.id.btn_run_simulation)
        tvPrediction = findViewById(R.id.tv_prediction)
        tvResult = findViewById(R.id.tv_result)
        resultCard = findViewById(R.id.result_card)

        try {
            val model = FileUtil.loadMappedFile(this, "traffic_model.tflite")
            interpreter = Interpreter(model)
        } catch (e: Exception) {
            Toast.makeText(this, "Gagal memuat model: ${e.message}", Toast.LENGTH_LONG).show()
            return
        }

        btnRun.setOnClickListener {
            runSimulation()
        }
    }

    private fun runSimulation() {
        try {
            val dayText = inputDay.text.toString().trim().lowercase()
            val car = inputCar.text.toString().toIntOrNull()
            val bike = inputBike.text.toString().toIntOrNull()
            val bus = inputBus.text.toString().toIntOrNull()
            val truck = inputTruck.text.toString().toIntOrNull()

            if (dayText.isEmpty() || car == null || bike == null || bus == null || truck == null) {
                Toast.makeText(this, "Isi semua kolom dengan angka bulat!", Toast.LENGTH_SHORT).show()
                return
            }

            val dayMap = mapOf(
                "senin" to 1f, "selasa" to 2f, "rabu" to 3f,
                "kamis" to 4f, "jumat" to 5f, "sabtu" to 6f, "minggu" to 7f
            )

            val dayValue = dayMap[dayText] ?: run {
                Toast.makeText(this, "Hari tidak valid! Gunakan nama hari (Senin - Minggu)", Toast.LENGTH_SHORT).show()
                return
            }

            val total = car + bike + bus + truck

            // Normalisasi input
            val inputArray = arrayOf(
                floatArrayOf(
                    dayValue,
                    car / 500f,
                    bike / 500f,
                    bus / 500f,
                    truck / 500f,
                    total / 500f
                )
            )

            val outputArray = Array(1) { FloatArray(4) }
            interpreter.run(inputArray, outputArray)

            val confidences = outputArray[0]
            var predictedIndex = confidences.indices.maxByOrNull { confidences[it] } ?: -1
            var predictedLabel = labels.getOrElse(predictedIndex) { "Unknown" }

            // 🔒 Koreksi prediksi jika kendaraan terlalu sedikit
            if (total < 15) {
                predictedLabel = "Lancar"
                predictedIndex = labels.indexOf("Lancar")
                for (i in confidences.indices) {
                    confidences[i] = if (i == predictedIndex) 1f else 0f
                }
            }

            val confidenceText = confidences.mapIndexed { i, v ->
                val percent = (v * 100).toInt()
                val mark = if (i == predictedIndex) " ✅" else ""
                "- ${labels[i]}: $percent%$mark"
            }.joinToString("\n")

            tvPrediction.text = "Prediksi Situasi Lalu Lintas: $predictedLabel"
            tvResult.text = "Confidence (%):\n$confidenceText\n\nTotal Kendaraan: $total"
            resultCard.visibility = CardView.VISIBLE

        } catch (e: Exception) {
            Toast.makeText(this, "Terjadi error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }





    override fun onDestroy() {
        interpreter.close()
        super.onDestroy()
    }
}
