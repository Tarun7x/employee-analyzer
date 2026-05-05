package com.company.analyzer;

import com.company.analyzer.model.Employee;
import com.company.analyzer.service.AnalysisService;
import com.company.analyzer.service.CsvReaderService;

import java.util.List;

public class App {
    public static void main(String[] args) {
        CsvReaderService csvReaderService = new CsvReaderService();
        List<Employee> employees = csvReaderService.readEmployees();

        AnalysisService analysisService = new AnalysisService(employees);

        printUnderpaidManagers(analysisService.findUnderpaidManagers());
        printOverpaidManagers(analysisService.findOverpaidManagers());
        printLongReportingLines(analysisService.findTooLongReportingLines());
    }

    private static void printUnderpaidManagers(List<AnalysisService.SalaryViolation> violations) {
        System.out.println("Managers earning less than they should:");
        if (violations.isEmpty()) {
            System.out.println("None");
            return;
        }

        for (AnalysisService.SalaryViolation violation : violations) {
            Employee manager = violation.getManager();
            System.out.printf("%s %s (id=%d): short by %.2f%n",
                    manager.getFirstName(), manager.getLastName(), manager.getId(), violation.getAmount());
        }
    }

    private static void printOverpaidManagers(List<AnalysisService.SalaryViolation> violations) {
        System.out.println("Managers earning more than they should:");
        if (violations.isEmpty()) {
            System.out.println("None");
            return;
        }

        for (AnalysisService.SalaryViolation violation : violations) {
            Employee manager = violation.getManager();
            System.out.printf("%s %s (id=%d): over by %.2f%n",
                    manager.getFirstName(), manager.getLastName(), manager.getId(), violation.getAmount());
        }
    }

    private static void printLongReportingLines(List<AnalysisService.ReportingLineViolation> violations) {
        System.out.println("Employees with reporting line too long:");
        if (violations.isEmpty()) {
            System.out.println("None");
            return;
        }

        for (AnalysisService.ReportingLineViolation violation : violations) {
            Employee employee = violation.getEmployee();
            System.out.printf("%s %s (id=%d): too long by %d%n",
                    employee.getFirstName(), employee.getLastName(), employee.getId(), violation.getLevelsTooLong());
        }
    }
}
