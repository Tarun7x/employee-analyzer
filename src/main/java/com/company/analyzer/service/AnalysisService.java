package com.company.analyzer.service;

import com.company.analyzer.model.Employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnalysisService {

    // Assignment rules:
    // manager salary must be between 120% and 150% of direct reports' average salary.
    private static final double MIN_MANAGER_RATIO = 1.20;
    private static final double MAX_MANAGER_RATIO = 1.50;
    private static final int MAX_MANAGERS_BETWEEN_EMPLOYEE_AND_CEO = 4;

    private final List<Employee> employees;
    private final Map<Integer, Employee> employeesById;
    private final Map<Integer, List<Employee>> subordinatesByManagerId;

    public AnalysisService(List<Employee> employees) {
        this.employees = employees;
        // Fast lookup by employee id (used while traversing manager chains).
        this.employeesById = employees.stream().collect(Collectors.toMap(Employee::getId, e -> e));
        // Group direct reports by manager id for salary checks.
        this.subordinatesByManagerId = employees.stream()
                .filter(e -> e.getManagerId() != null)
                .collect(Collectors.groupingBy(Employee::getManagerId));
    }

    public List<SalaryViolation> findUnderpaidManagers() {
        return collectSalaryViolations(true);
    }

    public List<SalaryViolation> findOverpaidManagers() {
        return collectSalaryViolations(false);
    }

    private List<SalaryViolation> collectSalaryViolations(boolean underpaidCheck) {
        List<SalaryViolation> violations = new ArrayList<>();

        // We only evaluate employees who actually manage at least one direct subordinate.
        for (Employee manager : employees) {
            List<Employee> subordinates = getDirectSubordinates(manager.getId());
            if (subordinates.isEmpty()) {
                continue;
            }

            double avgSubordinateSalary = averageSalary(subordinates);
            double minAllowed = avgSubordinateSalary * MIN_MANAGER_RATIO;
            double maxAllowed = avgSubordinateSalary * MAX_MANAGER_RATIO;

            // Same loop handles both checks; the flag decides which boundary to enforce.
            if (underpaidCheck && manager.getSalary() < minAllowed) {
                violations.add(new SalaryViolation(manager, minAllowed - manager.getSalary()));
            } else if (!underpaidCheck && manager.getSalary() > maxAllowed) {
                violations.add(new SalaryViolation(manager, manager.getSalary() - maxAllowed));
            }
        }

        return violations;
    }

    public List<ReportingLineViolation> findTooLongReportingLines() {
        List<ReportingLineViolation> violations = new ArrayList<>();

        for (Employee employee : employees) {
            // "Managers between employee and CEO" excludes both employee and CEO.
            int managersBetween = managersBetweenEmployeeAndCeo(employee);
            if (managersBetween > MAX_MANAGERS_BETWEEN_EMPLOYEE_AND_CEO) {
                violations.add(new ReportingLineViolation(
                        employee,
                        managersBetween - MAX_MANAGERS_BETWEEN_EMPLOYEE_AND_CEO
                ));
            }
        }

        return violations;
    }

    private double averageSalary(List<Employee> employees) {
        return employees.stream().mapToDouble(Employee::getSalary).average().orElse(0.0);
    }

    private List<Employee> getDirectSubordinates(int managerId) {
        return subordinatesByManagerId.getOrDefault(managerId, Collections.emptyList());
    }

    private int managersBetweenEmployeeAndCeo(Employee employee) {
        int edgesToCeo = 0;
        Integer managerId = employee.getManagerId();
        Set<Integer> visited = new HashSet<>();

        // Walk up the org chart until we reach CEO (managerId == null).
        while (managerId != null) {
            // Invalid hierarchy guard: break loops such as A -> B -> A.
            if (!visited.add(managerId)) {
                return -1;
            }

            Employee manager = employeesById.get(managerId);
            if (manager == null) {
                return -1;
            }

            edgesToCeo++;
            managerId = manager.getManagerId();
        }

        return Math.max(0, edgesToCeo - 1);
    }

    public static class SalaryViolation {
        private final Employee manager;
        private final double amount;

        public SalaryViolation(Employee manager, double amount) {
            this.manager = manager;
            this.amount = amount;
        }

        public Employee getManager() {
            return manager;
        }

        public double getAmount() {
            return amount;
        }
    }

    public static class ReportingLineViolation {
        private final Employee employee;
        private final int levelsTooLong;

        public ReportingLineViolation(Employee employee, int levelsTooLong) {
            this.employee = employee;
            this.levelsTooLong = levelsTooLong;
        }

        public Employee getEmployee() {
            return employee;
        }

        public int getLevelsTooLong() {
            return levelsTooLong;
        }
    }
}
