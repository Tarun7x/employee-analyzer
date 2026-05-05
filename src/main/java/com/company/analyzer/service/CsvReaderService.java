package com.company.analyzer.service;

import com.company.analyzer.model.Employee;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvReaderService {

    private static final String CSV_FILE_PATH = "src/main/resources/employees.csv";

    public List<Employee> readEmployees() {
        List<Employee> employees = new ArrayList<>();
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            // First row is the CSV header.
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Employee employee = new Employee();
                employee.setId(Integer.parseInt(values[0].trim()));
                employee.setFirstName(values[1].trim());
                employee.setLastName(values[2].trim());
                employee.setSalary(Double.parseDouble(values[3].trim()));
                employee.setManagerId(parseManagerId(values));
                employees.add(employee);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read employees from CSV: " + CSV_FILE_PATH, e);
        }

        return employees;
    }

    private Integer parseManagerId(String[] values) {
        // CEO rows can have empty managerId; treat as null.
        if (values.length < 5) {
            return null;
        }
        String rawManagerId = values[4].trim();
        return rawManagerId.isEmpty() ? null : Integer.parseInt(rawManagerId);
    }
}
