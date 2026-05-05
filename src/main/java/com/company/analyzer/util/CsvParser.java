package com.company.analyzer.util;

import com.company.analyzer.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class CsvParser {

    public static List<Employee> parseCsv(String csvData) {
        List<Employee> employees = new ArrayList<>();
        String[] lines = csvData.split("\\r?\\n");

        // Start at 1 to skip header: Id,firstName,lastName,salary,managerId
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] values = line.split(",");
            if (values.length >= 4) {
                Employee employee = new Employee();
                employee.setId(Integer.parseInt(values[0].trim()));
                employee.setFirstName(values[1].trim());
                employee.setLastName(values[2].trim());
                employee.setSalary(Double.parseDouble(values[3].trim()));
                if (values.length > 4 && !values[4].trim().isEmpty()) {
                    employee.setManagerId(Integer.parseInt(values[4].trim()));
                } else {
                    employee.setManagerId(null);
                }
                employees.add(employee);
            }
        }

        return employees;
    }
}
