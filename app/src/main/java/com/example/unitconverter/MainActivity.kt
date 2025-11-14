// MainActivity.kt
package com.example.unitconverter

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnitConverterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UnitConverterApp()
                }
            }
        }
    }
}

@Composable
fun UnitConverterTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF6200EE),
            secondary = Color(0xFF03DAC6),
            background = Color(0xFFF5F5F5)
        ),
        content = content
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitConverterApp() {
    var selectedCategory by remember { mutableStateOf("Temperature") }
    val categories = listOf("Temperature", "Length", "Weight", "Age")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    "Unit Converter",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF6200EE),
                titleContentColor = Color.White
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Category Selection
            Text(
                "Select Category",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Converter Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    when (selectedCategory) {
                        "Temperature" -> TemperatureConverter()
                        "Length" -> LengthConverter()
                        "Weight" -> WeightConverter()
                        "Age" -> DobCalculator()
                    }
                }
            }
        }
    }
}

@Composable
fun TemperatureConverter() {
    var inputValue by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf("Celsius") }
    var toUnit by remember { mutableStateOf("Fahrenheit") }
    val units = listOf("Celsius", "Fahrenheit", "Kelvin")

    ConverterLayout(
        title = "Temperature Converter",
        inputValue = inputValue,
        onInputChange = { inputValue = it },
        fromUnit = fromUnit,
        toUnit = toUnit,
        units = units,
        onFromUnitChange = { fromUnit = it },
        onToUnitChange = { toUnit = it },
        result = convertTemperature(inputValue, fromUnit, toUnit)
    )
}

@Composable
fun LengthConverter() {
    var inputValue by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf("Meters") }
    var toUnit by remember { mutableStateOf("Feet") }
    val units = listOf("Meters", "Kilometers", "Miles", "Feet", "Inches", "Centimeters")

    ConverterLayout(
        title = "Length Converter",
        inputValue = inputValue,
        onInputChange = { inputValue = it },
        fromUnit = fromUnit,
        toUnit = toUnit,
        units = units,
        onFromUnitChange = { fromUnit = it },
        onToUnitChange = { toUnit = it },
        result = convertLength(inputValue, fromUnit, toUnit)
    )
}

@Composable
fun WeightConverter() {
    var inputValue by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf("Kilograms") }
    var toUnit by remember { mutableStateOf("Pounds") }
    val units = listOf("Kilograms", "Grams", "Pounds", "Ounces", "Tons")

    ConverterLayout(
        title = "Weight Converter",
        inputValue = inputValue,
        onInputChange = { inputValue = it },
        fromUnit = fromUnit,
        toUnit = toUnit,
        units = units,
        onFromUnitChange = { fromUnit = it },
        onToUnitChange = { toUnit = it },
        result = convertWeight(inputValue, fromUnit, toUnit)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DobCalculator() {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var age by remember { mutableStateOf<Period?>(null) }
    val showDatePicker = remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = { showDatePicker.value = true }) {
            Text("Select Date of Birth")
        }

        if (showDatePicker.value) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { showDatePicker.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val selectedMillis = datePickerState.selectedDateMillis
                            if (selectedMillis != null) {
                                selectedDate = Date(selectedMillis).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                                age = Period.between(selectedDate, LocalDate.now())
                            }
                            showDatePicker.value = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker.value = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        selectedDate?.let {
            Text(
                "Selected DOB: ${it.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        age?.let {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
            ) {
                Text(
                    text = "You are ${it.years} years, ${it.months} months, and ${it.days} days old.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterLayout(
    title: String,
    inputValue: String,
    onInputChange: (String) -> Unit,
    fromUnit: String,
    toUnit: String,
    units: List<String>,
    onFromUnitChange: (String) -> Unit,
    onToUnitChange: (String) -> Unit,
    result: String
) {
    var fromExpanded by remember { mutableStateOf(false) }
    var toExpanded by remember { mutableStateOf(false) }

    Column {
        Text(
            title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Input Field
        OutlinedTextField(
            value = inputValue,
            onValueChange = onInputChange,
            label = { Text("Enter value") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true
        )

        // From Unit
        Text("From:", fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))
        ExposedDropdownMenuBox(
            expanded = fromExpanded,
            onExpandedChange = { fromExpanded = !fromExpanded },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            OutlinedTextField(
                value = fromUnit,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = fromExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = fromExpanded,
                onDismissRequest = { fromExpanded = false }
            ) {
                units.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(unit) },
                        onClick = {
                            onFromUnitChange(unit)
                            fromExpanded = false
                        }
                    )
                }
            }
        }

        // To Unit
        Text("To:", fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))
        ExposedDropdownMenuBox(
            expanded = toExpanded,
            onExpandedChange = { toExpanded = !toExpanded },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            OutlinedTextField(
                value = toUnit,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = toExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = toExpanded,
                onDismissRequest = { toExpanded = false }
            ) {
                units.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(unit) },
                        onClick = {
                            onToUnitChange(unit)
                            toExpanded = false
                        }
                    )
                }
            }
        }

        // Result
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
        ) {
            Text(
                text = result,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

// Conversion Functions
fun convertTemperature(value: String, from: String, to: String): String {
    val input = value.toDoubleOrNull() ?: return "Enter a valid number"

    val celsius = when (from) {
        "Celsius" -> input
        "Fahrenheit" -> (input - 32) * 5 / 9
        "Kelvin" -> input - 273.15
        else -> return "Error"
    }

    val result = when (to) {
        "Celsius" -> celsius
        "Fahrenheit" -> celsius * 9 / 5 + 32
        "Kelvin" -> celsius + 273.15
        else -> return "Error"
    }

    return "%.2f $to".format(result)
}

fun convertLength(value: String, from: String, to: String): String {
    val input = value.toDoubleOrNull() ?: return "Enter a valid number"

    val meters = when (from) {
        "Meters" -> input
        "Kilometers" -> input * 1000
        "Miles" -> input * 1609.34
        "Feet" -> input * 0.3048
        "Inches" -> input * 0.0254
        "Centimeters" -> input * 0.01
        else -> return "Error"
    }

    val result = when (to) {
        "Meters" -> meters
        "Kilometers" -> meters / 1000
        "Miles" -> meters / 1609.34
        "Feet" -> meters / 0.3048
        "Inches" -> meters / 0.0254
        "Centimeters" -> meters / 0.01
        else -> return "Error"
    }

    return "%.4f $to".format(result)
}

fun convertWeight(value: String, from: String, to: String): String {
    val input = value.toDoubleOrNull() ?: return "Enter a valid number"

    val kilograms = when (from) {
        "Kilograms" -> input
        "Grams" -> input / 1000
        "Pounds" -> input * 0.453592
        "Ounces" -> input * 0.0283495
        "Tons" -> input * 1000
        else -> return "Error"
    }

    val result = when (to) {
        "Kilograms" -> kilograms
        "Grams" -> kilograms * 1000
        "Pounds" -> kilograms / 0.453592
        "Ounces" -> kilograms / 0.0283495
        "Tons" -> kilograms / 1000
        else -> return "Error"
    }

    return "%.4f $to".format(result)
}
