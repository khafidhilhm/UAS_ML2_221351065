package com.example.myapp

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil

class SimulationActivity : AppCompatActivity() {
    private lateinit var interpreter: Interpreter
    private lateinit var tvResult: TextView
    private lateinit var tvPrediction: TextView
    private lateinit var inputField: EditText
    private lateinit var resultCard: CardView

    private val labels = arrayOf("Macet", "Padat", "Normal", "Lancar")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulation)

        tvResult = findViewById(R.id.tv_result)
        tvPrediction = findViewById(R.id.tv_prediction)
        inputField = findViewById(R.id.input_value)
        resultCard = findViewById(R.id.result_card)

        val btnRun = findViewById<Button>(R.id.btn_run_simulation)

        try {
            val model = FileUtil.loadMappedFile(this, "traffic_model.tflite")
            interpreter = Interpreter(model)
        } catch (e: Exception) {
            Toast.makeText(this, "Gagal memuat model", Toast.LENGTH_LONG).show()
            return
        }

        btnRun.setOnClickListener {
            runSimulation()
        }
    }

    private fun runSimulation() {
        val inputText = inputField.text.toString()
        val inputValue = inputText.toFloatOrNull()

        if (inputValue == null) {
            Toast.makeText(this, "Masukkan angka yang valid!", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val inputShape = interpreter.getInputTensor(0).shape() // [1, 1]
            val outputShape = interpreter.getOutputTensor(0).shape() // [1, 4]

            val input = Array(inputShape[0]) { FloatArray(inputShape[1]) { inputValue } }
            val output = Array(outputShape[0]) { FloatArray(outputShape[1]) }

            interpreter.run(input, output)

            val confidences = output[0]
            val maxIndex = confidences.indices.maxByOrNull { confidences[it] } ?: -1
            val predictedLabel = labels.getOrElse(maxIndex) { "Unknown" }

            val confidenceText = confidences.mapIndexed { i, v ->
                val mark = if (i == maxIndex) " ✅" else ""
                "- ${labels[i]}: %.2f$mark".format(v)
            }.joinToString("\n")

            tvPrediction.text = "Prediksi: $predictedLabel"
            tvResult.text = "Confidence:\n$confidenceText"
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
