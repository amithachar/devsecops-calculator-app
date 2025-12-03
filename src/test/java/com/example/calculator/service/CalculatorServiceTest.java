package com.example.calculator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.calculator.service.CalculatorService;

public class CalculatorServiceTest {

    private CalculatorService calculatorService;

    @BeforeEach
    public void setUp() {
        calculatorService = new CalculatorService();
    }

    @Test
    public void testAdd() {
        assertEquals(5.0, calculatorService.add(2.0, 3.0));
        assertEquals(-1.0, calculatorService.add(-2.0, 1.0));
    }

    @Test
    public void testSubtract() {
        assertEquals(1.0, calculatorService.subtract(3.0, 2.0));
        assertEquals(-3.0, calculatorService.subtract(-2.0, 1.0));
    }

    @Test
    public void testMultiply() {
        assertEquals(6.0, calculatorService.multiply(2.0, 3.0));
        assertEquals(-2.0, calculatorService.multiply(-2.0, 1.0));
    }

    @Test
    public void testDivide() {
        assertEquals(2.0, calculatorService.divide(6.0, 3.0));
        assertEquals(-2.0, calculatorService.divide(-6.0, 3.0));
    }

    @Test
    public void testDivideByZero() {
        assertThrows(IllegalArgumentException.class, () -> calculatorService.divide(1.0, 0.0));
    }
}