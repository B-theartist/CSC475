package com.example.unitconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnitConverterApp()
        }
    }
}

@Composable
fun UnitConverterApp() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            UnitConverterScreen()
        }
    }
}

@Composable
fun UnitConverterScreen() {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Temperature") }
    var selectedFromUnit by remember { mutableStateOf("Celsius") }
    var selectedToUnit by remember { mutableStateOf("Fahrenheit") }

    // Units for each category
    val categories = listOf("Temperature", "Length", "Weight")
    val units = mapOf(
        "Temperature" to listOf("Celsius", "Fahrenheit", "Kelvin"),
        "Length" to listOf("Meters", "Yards", "Miles", "Feet", "Inches"),
        "Weight" to listOf("Kilograms", "Pounds", "Grams", "Ounces")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Top dropdown: Select category
        DropdownMenuField(
            label = "Select Category",
            options = categories,
            selectedOption = selectedCategory,
            onOptionSelected = {
                selectedCategory = it
                selectedFromUnit = units[it]?.first() ?: ""
                selectedToUnit = units[it]?.getOrNull(1) ?: ""
                input = ""
                result = ""
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown to select "From" unit
        DropdownMenuField(
            label = "Convert From",
            options = units[selectedCategory] ?: emptyList(),
            selectedOption = selectedFromUnit,
            onOptionSelected = {
                selectedFromUnit = it
                result = performConversion(input, selectedCategory, selectedFromUnit, selectedToUnit)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown to select "To" unit
        DropdownMenuField(
            label = "Convert To",
            options = units[selectedCategory] ?: emptyList(),
            selectedOption = selectedToUnit,
            onOptionSelected = {
                selectedToUnit = it
                result = performConversion(input, selectedCategory, selectedFromUnit, selectedToUnit)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input and converted value fields
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "Value (${selectedFromUnit})")
                OutlinedTextField(
                    value = input,
                    onValueChange = {
                        input = it
                        result = performConversion(input, selectedCategory, selectedFromUnit, selectedToUnit)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "Converted (${selectedToUnit})")
                OutlinedTextField(
                    value = result,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )
            }
        }
    }
}

@Composable
fun DropdownMenuField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (selectedOption.isEmpty()) label else selectedOption)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun performConversion(input: String, category: String, fromUnit: String, toUnit: String): String {
    // Handle invalid or empty input
    if (input.isEmpty() || fromUnit.isEmpty() || toUnit.isEmpty()) return "Invalid Input"
    if (fromUnit == toUnit) return String.format("%.5f", input.toDouble())

    return try {
        val value = input.toDouble()
        val result = when (category) {
            "Temperature" -> when (fromUnit to toUnit) {
                "Celsius" to "Fahrenheit" -> (value * 9 / 5 + 32)
                "Fahrenheit" to "Celsius" -> ((value - 32) * 5 / 9)
                "Celsius" to "Kelvin" -> (value + 273.15)
                "Kelvin" to "Celsius" -> (value - 273.15)
                "Kelvin" to "Fahrenheit" -> ((value - 273.15) * 9 / 5 + 32)
                "Fahrenheit" to "Kelvin" -> ((value - 32) * 5 / 9 + 273.15)
                else -> return "Invalid Conversion"
            }
            "Length" -> when (fromUnit to toUnit) {
                "Meters" to "Yards" -> (value * 1.09361)
                "Yards" to "Meters" -> (value / 1.09361)
                "Miles" to "Kilometers" -> (value * 1.60934)
                "Kilometers" to "Miles" -> (value / 1.60934)
                "Feet" to "Inches" -> (value * 12)
                "Inches" to "Feet" -> (value / 12)
                "Meters" to "Feet" -> (value * 3.28084)
                "Feet" to "Meters" -> (value / 3.28084)
                "Feet" to "Yards" -> (value / 3)
                "Yards" to "Feet" -> (value * 3)
                "Miles" to "Yards" -> (value * 1760)
                "Yards" to "Miles" -> (value / 1760)
                "Kilometers" to "Yards" -> (value * 1093.61)
                "Yards" to "Kilometers" -> (value / 1093.61)
                else -> return "Invalid Conversion"
            }
            "Weight" -> when (fromUnit to toUnit) {
                "Kilograms" to "Pounds" -> (value * 2.20462)
                "Pounds" to "Kilograms" -> (value / 2.20462)
                "Grams" to "Ounces" -> (value / 28.3495) // Updated formula for Grams to Ounces
                "Ounces" to "Grams" -> (value * 28.3495)
                "Kilograms" to "Grams" -> (value * 1000)
                "Grams" to "Kilograms" -> (value / 1000)
                "Pounds" to "Ounces" -> (value * 16)
                "Ounces" to "Pounds" -> (value / 16)
                "Grams" to "Pounds" -> (value / 453.592)
                "Pounds" to "Grams" -> (value * 453.592)
                "Kilograms" to "Ounces" -> (value * 35.274)
                "Ounces" to "Kilograms" -> (value / 35.274)
                "Kilograms" to "Milligrams" -> (value * 1_000_000)
                "Milligrams" to "Kilograms" -> (value / 1_000_000)
                "Pounds" to "Milligrams" -> (value * 453_592)
                "Milligrams" to "Pounds" -> (value / 453_592)
                else -> return "Invalid Conversion"
            }
            else -> return "Invalid Conversion"
        }
        String.format("%.5f", result) // Round to 5 decimal places
    } catch (e: Exception) {
        "Invalid Input"
    }
}