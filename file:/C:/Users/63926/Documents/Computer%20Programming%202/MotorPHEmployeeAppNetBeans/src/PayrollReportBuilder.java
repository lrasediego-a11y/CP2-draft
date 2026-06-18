/**
 * PayrollReportBuilder
 *
 * Builds formatted text reports for payroll output and summary display.
 * Separates report formatting from computation and GUI logic
 * (single-responsibility principle).
 */
public final class PayrollReportBuilder {

  private PayrollReportBuilder() {}

  /**
   * Builds the full individual payroll result report for one employee.
   *
   * @param employeeId        employee number
   * @param employeeName      full name
   * @param payCoverageStart  pay period start date
   * @param payCoverageEnd    pay period end date
   * @param basicSalary       monthly basic salary in PHP
   * @param hourlyRate        hourly rate in PHP
   * @param hoursWorked       total hours within the pay period
   * @param grossPay          computed gross pay
   * @param sssContribution   SSS deduction
   * @param philHealthContrib PhilHealth deduction
   * @param pagIbigContrib    Pag-IBIG deduction
   * @param withholdingTax    withholding tax
   * @param totalDeductions   sum of all deductions
   * @param netPay            final take-home pay
   * @param attendanceSummary pre-formatted attendance lines
   * @return formatted payroll result string for display
   */
  public static String buildIndividualPayrollReport(
      String employeeId,
      String employeeName,
      String payCoverageStart,
      String payCoverageEnd,
      double basicSalary,
      double hourlyRate,
      double hoursWorked,
      double grossPay,
      double sssContribution,
      double philHealthContrib,
      double pagIbigContrib,
      double withholdingTax,
      double totalDeductions,
      double netPay,
      String attendanceSummary) {

    return "╔══════════════════════════════════════════════════════════════╗\n"
        + "║         MOTORPH PAYROLL COMPUTATION RESULT                    ║\n"
        + "╚══════════════════════════════════════════════════════════════╝\n"
        + formatLabelValue("Employee No.",    employeeId)
        + formatLabelValue("Employee Name",   employeeName)
        + formatLabelValue("Pay Coverage",
            payCoverageStart + "  to  " + payCoverageEnd)
        + formatCurrencyRow("Basic Salary",   basicSalary)
        + formatCurrencyRow("Hourly Rate",    hourlyRate)
        + String.format("  %-16s: %.2f hours%n", "Hours Worked", hoursWorked)
        + "  ──────────────────────────────────────────────────────\n"
        + formatCurrencyRow("Gross Pay",      grossPay)
        + "  ──────────────────────────────────────────────────────\n"
        + "  Deductions (CP1 MotorPH Formula)\n"
        + formatDeductionRow("SSS",               sssContribution)
        + formatDeductionRow("PhilHealth",         philHealthContrib)
        + formatDeductionRow("Pag-IBIG",           pagIbigContrib)
        + formatDeductionRow("Withholding Tax",    withholdingTax)
        + formatDeductionRow("Total Deductions",   totalDeductions)
        + "  ══════════════════════════════════════════════════════\n"
        + formatCurrencyRow("NET PAY",        netPay)
        + "  ══════════════════════════════════════════════════════\n"
        + "\n  Attendance Detail:\n"
        + attendanceSummary;
  }

  /**
   * Builds the payroll summary report for all employees.
   *
   * @param employeeIds     array of all employee IDs
   * @param employeeNames   corresponding names
   * @param grossPayValues  per-employee gross pay
   * @param deductionValues per-employee total deductions
   * @param netPayValues    per-employee net pay
   * @param totalGrossPay   sum of all gross pays
   * @param totalDeductions sum of all deductions
   * @param totalNetPay     sum of all net pays
   * @return formatted summary report string
   */
  public static String buildPayrollSummaryReport(
      String[] employeeIds,
      String[] employeeNames,
      double[] grossPayValues,
      double[] deductionValues,
      double[] netPayValues,
      double totalGrossPay,
      double totalDeductions,
      double totalNetPay) {

    StringBuilder summaryRows = new StringBuilder();
    summaryRows.append(String.format("  %-8s %-22s %14s %14s %14s%n",
        "No.", "Name", "Gross Pay", "Deductions", "Net Pay"));
    summaryRows.append("  ").append("─".repeat(74)).append("\n");

    for (int i = 0; i < employeeIds.length; i++) {
      summaryRows.append(String.format("  %-8s %-22s %14s %14s %14s%n",
          employeeIds[i],
          employeeNames[i],
          formatPhp(grossPayValues[i]),
          formatPhp(deductionValues[i]),
          formatPhp(netPayValues[i])));
    }

    summaryRows.append("  ").append("═".repeat(74)).append("\n");
    summaryRows.append(String.format("  %-8s %-22s %14s %14s %14s%n",
        "TOTAL",
        "(" + employeeIds.length + " employees)",
        formatPhp(totalGrossPay),
        formatPhp(totalDeductions),
        formatPhp(totalNetPay)));

    return "╔══════════════════════════════════════════════════════════════╗\n"
        + "║             MOTORPH PAYROLL SUMMARY REPORT                    ║\n"
        + "╚══════════════════════════════════════════════════════════════╝\n\n"
        + summaryRows
        + "\n"
        + String.format("  Total Employees  : %d%n", employeeIds.length)
        + String.format("  Total Gross Pay  : %s%n", formatPhp(totalGrossPay))
        + String.format("  Total Deductions : %s%n", formatPhp(totalDeductions))
        + String.format("  Total Net Pay    : %s%n", formatPhp(totalNetPay));
  }

  // ── Private Format Helpers ──────────────────────────────────────────────────

  private static String formatLabelValue(String label, String value) {
    return String.format("  %-16s: %s%n", label, value);
  }

  private static String formatCurrencyRow(String label, double amount) {
    return String.format("  %-16s: PHP %,.2f%n", label, amount);
  }

  private static String formatDeductionRow(String label, double amount) {
    return String.format("    %-20s: PHP %,.2f%n", label, amount);
  }

  private static String formatPhp(double amount) {
    return String.format("PHP %,.2f", amount);
  }
}
