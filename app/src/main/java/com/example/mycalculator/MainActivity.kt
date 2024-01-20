package com.example.mycalculator

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.util.Stack
import androidx.appcompat.app.AppCompatActivity



class MainActivity : AppCompatActivity() {

    private lateinit var inputEditText: EditText
    private lateinit var outputEditText:EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeViews()
    }

    private fun initializeViews() {
        inputEditText = findViewById(R.id.inputTextField)
        outputEditText = findViewById(R.id.outputTextField)
    }


    fun buttonClicked(view: View) {

        val button = view as Button
        val textButton = button.text.toString()
        val oldInputText = inputEditText.text.toString()

        if (textButton.equals("C", ignoreCase = true)) {
            val length = oldInputText.length
            if (length > 1) {
                val resultString = oldInputText.substring(0, length - 1)
                inputEditText.setText(resultString)
            } else {
                inputEditText.setText("")
            }
        } else if (textButton.equals("DELETE", ignoreCase = true)) {
            inputEditText.setText("")
            outputEditText.setText("")
        } else if (textButton.equals("X", ignoreCase = true)) {
            inputEditText.setText("$oldInputText*")
        } else if (textButton.equals("=", ignoreCase = true)) {
            if (oldInputText.trim { it <= ' ' }.equals("", ignoreCase = true)) {
                inputEditText.setText(outputEditText.text.toString())
                outputEditText.setText("")
                return
            }
            //calculate the expression and set to answer
            try {
//                val expression = Expression(oldInputText)
//                val result: EvaluationValue = expression.evaluate()
                val result = stringSolverWithoutStack(oldInputText)
                Log.d("MainActivity", result)
                 Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
                outputEditText.setText(result.toString())
                inputEditText.setText("")
            } catch (e: Exception) {
                Toast.makeText(this, "Cannot Evaluate Expression", Toast.LENGTH_SHORT).show()
            }
        } else {
            inputEditText.setText(oldInputText + textButton)
        }
    }


    fun stringSolverWithoutStack(equation: String): String {
        return dmasBracketsStringSolver(equation).toString()
    }

    fun dmasBracketsStringSolver(equation: String = "22 / 2 * 34 - 4"): Double {
        if (equation.contains("(")) {
            val v = equation.lastIndexOf("(")
            val v1 = equation.substring(0, v)
            val v2 = equation.substring(v + 1)
            val v3 = v2.indexOf(")")
            val v4 = v2.substring(0, v3)
            val v5 = v2.substring(v3 + 1)
            return dmasBracketsStringSolver("${v1.trim()}${dmasBracketsStringSolver(v4.trim())}${v5.trim()}")
        } else {
            return dmasStringSolver(equation)
        }
    }

    fun dmasStringSolver(equation: String = "22 / 2 * 34 - 4"): Double {
        when {
            equation.contains("+") -> {
                val v = equation.lastIndexOf("+")
                val v1 = equation.substring(0, v)
                val v2 = equation.substring(v + 1)
                return dmasStringSolver(v1.trim()) + dmasStringSolver(v2.trim())
            }
            equation.contains("-") -> {
                val v = equation.lastIndexOf("-")
                val v1 = equation.substring(0, v)
                val v2 = equation.substring(v + 1)

                return if (v1.trim() == "") {
                    -1 * dmasStringSolver(v2.trim())
                } else {
                    dmasStringSolver(v1.trim()) - dmasStringSolver(v2.trim())
                }
            }
            equation.contains("*") -> {
                val v = equation.lastIndexOf("*")
                val v1 = equation.substring(0, v)
                val v2 = equation.substring(v + 1)
                return dmasStringSolver(v1.trim()) * dmasStringSolver(v2.trim())
            }
            equation.contains("/") -> {
                val v = equation.lastIndexOf("/")
                val v1 = equation.substring(0, v)
                val v2 = equation.substring(v + 1)
                return dmasStringSolver(v1.trim()) / dmasStringSolver(v2.trim())
            }
            else -> {
                return equation.trim().toDouble()
            }
        }
    }





}