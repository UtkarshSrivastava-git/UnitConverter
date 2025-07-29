package com.example.unitconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.unitconverter.ui.theme.UnitConverterTheme
import android. widget. Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UnitConverterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding->
                    Column(modifier = Modifier.padding(padding)) {
                        unitConverter()
                    }
                }
            }
        }
    }
}

@Composable
fun unitConverter() {
    var inputValue by remember {mutableStateOf("")}
    var outputValue by remember {mutableStateOf("")}
    var inputUnit by remember {mutableStateOf("Meters")}
    var outputUnit by remember {mutableStateOf("Meters")}
    var iExpanded by remember {mutableStateOf(false)}
    var oExpanded by remember {mutableStateOf(false)}
    var conversionFactor = remember {mutableStateOf(1.0)}
    var oconversionFactor = remember {mutableStateOf(1.0)}

    val customTextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 40.sp,
        color = Color.LightGray
    )


    fun convertUnits(){
        // (?:) is known as "Elvis Operator"
        val inputValDouble = inputValue.toDoubleOrNull() ?: 0.0
        val result = (inputValDouble * conversionFactor.value * 100/ oconversionFactor.value).roundToInt() / 100.0
        outputValue = result.toString()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Unit Converter" ,
            style = customTextStyle)
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(value = inputValue, onValueChange = {
            inputValue = it
            convertUnits()  },
            label = { Text("Enter Value")})
        Spacer(modifier = Modifier.height(20.dp))


        Row {
            Box {
                Button(onClick = { iExpanded = true }) {
                    Text(inputUnit)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Arrow Down")
                }
                DropdownMenu(expanded = iExpanded, onDismissRequest = { iExpanded = false }) {
                    DropdownMenuItem(text = { Text("Kilometers") }, onClick = {
                        inputUnit = "Kilometers"
                        iExpanded = false
                        conversionFactor.value = 1000.0
                        convertUnits()
                    })
                    DropdownMenuItem(text = { Text("Meters") }, onClick = {
                        inputUnit = "Meters"
                        iExpanded = false
                        conversionFactor.value = 1.0
                        convertUnits()
                    })
                    DropdownMenuItem(text = { Text("Centimeter") },
                        onClick = { inputUnit = "Centimeters"
                        iExpanded = false
                        conversionFactor.value = 0.01
                        convertUnits() })
                    DropdownMenuItem(text = { Text("Milimeters") }, onClick = {
                        inputUnit = "Milimeters"
                        iExpanded = false
                        conversionFactor.value = 0.001
                        convertUnits()
                    })
                }
            }
            Spacer(modifier = Modifier.width(16.dp)) 
            Box {
                Button(onClick = { oExpanded = true }) {
                    Text(outputUnit)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Arrow Down")
                }
                DropdownMenu(expanded = oExpanded, onDismissRequest = { oExpanded = false }) {
                    DropdownMenuItem(text = { Text("Kilometers") }, onClick = {
                        outputUnit = "Kilometers"
                        oExpanded = false
                        oconversionFactor.value = 1000.0
                        convertUnits()
                    })
                    DropdownMenuItem(text = { Text("Meters") }, onClick = {
                        outputUnit = "Meters"
                        oExpanded = false
                        oconversionFactor.value = 1.0
                        convertUnits()
                    })
                    DropdownMenuItem(text = { Text("Centimeters") }, onClick = {
                        outputUnit = "Centimeters"
                        oExpanded = false
                        oconversionFactor.value = 0.01
                        convertUnits()
                    })
                    DropdownMenuItem(text = { Text("Milimeters ") }, onClick = {
                        outputUnit = "Milimeters"
                        oExpanded = false
                        oconversionFactor.value = 0.001
                        convertUnits()
                    })
                }

            }

        }
        Spacer(modifier = Modifier.height(20.dp))
        Text("Result : $outputValue $outputUnit " ,
                style = MaterialTheme.typography.headlineMedium  )

    }
}




@Preview(showBackground = true)
@Composable
fun UnitConverterPreview(){
    UnitConverterTheme {
        unitConverter()
    }
}

