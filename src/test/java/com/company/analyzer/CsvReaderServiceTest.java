package com.company.analyzer;

import com.company.analyzer.model.Employee;
import com.company.analyzer.service.CsvReaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CsvReaderServiceTest {

    private CsvReaderService csvReaderService;

    @BeforeEach
    public void setUp() {
        csvReaderService = new CsvReaderService();
    }

    @Test
    public void testReadEmployees() {
        List<Employee> employees = csvReaderService.readEmployees();

        assertEquals(5, employees.size());
        assertEquals("Joe", employees.get(0).getFirstName());
        assertEquals("Doe", employees.get(0).getLastName());
        assertEquals(60000.0, employees.get(0).getSalary());
        assertNull(employees.get(0).getManagerId());
    }
}
