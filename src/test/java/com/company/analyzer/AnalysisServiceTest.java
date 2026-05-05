package com.company.analyzer;

import com.company.analyzer.model.Employee;
import com.company.analyzer.service.AnalysisService;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnalysisServiceTest {

    @Test
    public void testFindUnderpaidManagers() {
        List<Employee> employees = Arrays.asList(
                new Employee(1, "CEO", "One", 100000, null),
                new Employee(2, "Manager", "Two", 70000, 1),
                new Employee(3, "Emp", "A", 70000, 2),
                new Employee(4, "Emp", "B", 60000, 2)
        );

        AnalysisService analysisService = new AnalysisService(employees);
        List<AnalysisService.SalaryViolation> underpaid = analysisService.findUnderpaidManagers();

        assertEquals(1, underpaid.size());
        assertEquals(2, underpaid.get(0).getManager().getId());
        assertEquals(8000.0, underpaid.get(0).getAmount(), 0.0001);
    }

    @Test
    public void testFindOverpaidManagers() {
        List<Employee> employees = Arrays.asList(
                new Employee(1, "CEO", "One", 100000, null),
                new Employee(2, "Manager", "Two", 120000, 1),
                new Employee(3, "Emp", "A", 70000, 2),
                new Employee(4, "Emp", "B", 60000, 2)
        );

        AnalysisService analysisService = new AnalysisService(employees);
        List<AnalysisService.SalaryViolation> overpaid = analysisService.findOverpaidManagers();

        assertEquals(1, overpaid.size());
        assertEquals(2, overpaid.get(0).getManager().getId());
        assertEquals(22500.0, overpaid.get(0).getAmount(), 0.0001);
    }

    @Test
    public void testFindTooLongReportingLines() {
        List<Employee> employees = Arrays.asList(
                new Employee(1, "CEO", "One", 200000, null),
                new Employee(2, "M1", "", 150000, 1),
                new Employee(3, "M2", "", 130000, 2),
                new Employee(4, "M3", "", 110000, 3),
                new Employee(5, "M4", "", 90000, 4),
                new Employee(6, "M5", "", 80000, 5),
                new Employee(7, "Employee", "Deep", 50000, 6)
        );

        AnalysisService analysisService = new AnalysisService(employees);
        List<AnalysisService.ReportingLineViolation> longLines = analysisService.findTooLongReportingLines();

        assertEquals(1, longLines.size());
        assertEquals(7, longLines.get(0).getEmployee().getId());
        assertEquals(1, longLines.get(0).getLevelsTooLong());
    }
}
