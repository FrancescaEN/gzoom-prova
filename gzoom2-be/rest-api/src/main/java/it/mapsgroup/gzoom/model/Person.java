package it.mapsgroup.gzoom.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

public class Person {

    private String firstName;
    private String lastName;
    private String parentRoleCode;


    private String email;
    private Instant fromDate;
    private Instant endDate;
    private String statusDescription;
    private String emplPositionTypeDescription;
    private BigDecimal employmentAmount;

    public String getEmail() {
        return email;
    }

    public Instant getFromDate() {
        return fromDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public String getEmplPositionTypeDescription() {
        return emplPositionTypeDescription;
    }

    public BigDecimal getEmploymentAmount() {
        return employmentAmount;
    }

    public String getParentRoleCode() {

        return parentRoleCode;
    }

    public void setParentRoleCode(String parentRoleCode) {

        this.parentRoleCode = parentRoleCode;
    }

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    public String getLastName() {

        return lastName;
    }

    public void setLastName(String lastName) {

        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFromDate(Instant fromDate) {
        this.fromDate = fromDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public void setEmplPositionTypeDescription(String emplPositionTypeDescription) {
        this.emplPositionTypeDescription = emplPositionTypeDescription;
    }

    public void setEmploymentAmount(BigDecimal employmentAmount) {
        this.employmentAmount = employmentAmount;
    }

}
