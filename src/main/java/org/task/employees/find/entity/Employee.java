package org.task.employees.find.entity;

import java.util.Date;

public class Employee {
    private final String empId;
    private final Date dateFrom;
    private final Date dateTo;

    public Employee(String empId, Date dateFrom, Date dateTo) {
        this.empId = empId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public String getEmpId() {
        return empId;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }
}
