# Employee Analyzer

## Overview
This Java SE + Maven console application reads employee data from a CSV file and reports:
- managers earning less than they should, and by how much
- managers earning more than they should, and by how much
- employees with reporting lines that are too long, and by how much

## Rules Implemented
For every manager with at least one direct subordinate:
- minimum allowed manager salary = `120%` of the average salary of direct subordinates
- maximum allowed manager salary = `150%` of the average salary of direct subordinates

Reporting-line rule:
- report employees with **more than 4 managers between them and the CEO**
- reported excess = `(actual managers between employee and CEO) - 4`

## CSV Format
Expected input columns:

`Id,firstName,lastName,salary,managerId`

Notes:
- CEO has empty `managerId`
- each row is one employee

Default file path used by the app:
- `src/main/resources/employees.csv`

## Build, Test, Run
From project root:

```bash
mvn clean test
```

```bash
mvn -DskipTests exec:java -Dexec.mainClass=com.company.analyzer.App
```

## Console Output
The app prints 3 sections:
- `Managers earning less than they should:`
- `Managers earning more than they should:`
- `Employees with reporting line too long:`

If a section has no violations, it prints `None`.

## Assumptions
- Employee IDs are unique.
- `managerId`, when present, refers to an existing employee ID.
- CSV rows are valid numeric values for `Id` and `salary`.
- Salary checks are performed only for employees who actually manage at least one direct subordinate.
- For reporting depth, “managers between employee and CEO” excludes the employee and excludes the CEO.
- Empty `managerId` is treated as `null` (CEO-level employee).
- If malformed hierarchy data appears (for example, cyclic manager references), those chains are ignored for depth violations.

## Tech Stack
- Java SE
- Maven
- JUnit 5
