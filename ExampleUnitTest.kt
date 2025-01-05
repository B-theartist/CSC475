package com.example.unitconverter

import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testTemperatureConversions() {
        assertEquals("32.00000", performConversion("0", "Temperature", "Celsius", "Fahrenheit"))
        assertEquals("0.00000", performConversion("32", "Temperature", "Fahrenheit", "Celsius"))
        assertEquals("273.15000", performConversion("0", "Temperature", "Celsius", "Kelvin"))
        assertEquals("-273.15000", performConversion("0", "Temperature", "Kelvin", "Celsius"))
    }

    @Test
    fun testLengthConversions() {
        assertEquals("1.09361", performConversion("1", "Length", "Meters", "Yards"))
        assertEquals("0.91440", performConversion("1", "Length", "Yards", "Meters"))
        assertEquals("0.62137", performConversion("1", "Length", "Kilometers", "Miles")) // Kilometers to Miles
        assertEquals("1.60934", performConversion("1", "Length", "Miles", "Kilometers"))
        assertEquals("3.00000", performConversion("1", "Length", "Yards", "Feet"))
    }

    @Test
    fun testWeightConversions() {
        assertEquals("2.20462", performConversion("1", "Weight", "Kilograms", "Pounds"))
        assertEquals("0.45359", performConversion("1", "Weight", "Pounds", "Kilograms"))
        assertEquals("35.27400", performConversion("1", "Weight", "Kilograms", "Ounces")) // Correct input
        assertEquals("0.02835", performConversion("1", "Weight", "Ounces", "Kilograms"))
        assertEquals("453.59200", performConversion("1", "Weight", "Pounds", "Grams"))
    }

    @Test
    fun testInvalidConversions() {
        assertEquals("Invalid Conversion", performConversion("1", "Length", "Meters", "Celsius"))
        assertEquals("Invalid Conversion", performConversion("1", "Weight", "Grams", "Fahrenheit"))
        assertEquals("Invalid Input", performConversion("", "Temperature", "Celsius", "Fahrenheit"))
        assertEquals("Invalid Input", performConversion("abc", "Weight", "Kilograms", "Pounds"))
    }
}