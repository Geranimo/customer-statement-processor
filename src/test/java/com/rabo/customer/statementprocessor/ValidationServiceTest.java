package com.rabo.customer.statementprocessor;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.rabo.customer.statementprocessor.model.CustomerStatement;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class ValidationServiceTest {

  private ValidationService validationService;

  @Test
  public void testInvalidEndBalancesFromStatements() {
    validationService = new ValidationService();

    double invalidEndBalance = 130.00;
    double startBalance = 100.00;
    double mutation = 20.00;
    CustomerStatement inValidStatement = CustomerStatement.builder()
        .reference(1)
        .accountNumber("NL32RABO1234567890")
        .description("Test transaction with invalid end balance")
        .startBalance(BigDecimal.valueOf(startBalance))
        .mutation(BigDecimal.valueOf(mutation))
        .endBalance(BigDecimal.valueOf(invalidEndBalance)).build();

    CustomerStatement validStatement = CustomerStatement.builder()
        .reference(2)
        .accountNumber("NL32RABO1234567890")
        .description("Test transaction with invalid end balance")
        .startBalance(BigDecimal.valueOf(100.00))
        .mutation(BigDecimal.valueOf(20.00))
        .endBalance(BigDecimal.valueOf(120.00)).build();

    List<CustomerStatement> invalidEndBalanceInStatements = validationService
        .getInvalidEndBalanceInStatements(Arrays.asList(inValidStatement, validStatement));

    assertThat(invalidEndBalanceInStatements.size(), is(1));
    assertThat(invalidEndBalanceInStatements.contains(inValidStatement), is(true));
    assertThat(invalidEndBalanceInStatements.contains(validStatement), is(false));
  }

  @Test
  public void testNonUniqueReferenceFromStatements() {
    validationService = new ValidationService();

    CustomerStatement firstUniqueReference = CustomerStatement.builder()
        .reference(1)
        .accountNumber("NL32RABO1234567890")
        .description("Test transaction with first unique reference")
        .startBalance(BigDecimal.valueOf(10.00))
        .mutation(BigDecimal.valueOf(1.00))
        .endBalance(BigDecimal.valueOf(11.00)).build();

    CustomerStatement nonUniqueReference = CustomerStatement.builder()
        .reference(1)
        .accountNumber("NL32RABO0987654321")
        .description("Test transaction with duplicated reference")
        .startBalance(BigDecimal.valueOf(100.00))
        .mutation(BigDecimal.valueOf(20.00))
        .endBalance(BigDecimal.valueOf(120.00)).build();

    List<CustomerStatement> nonUniqueReferencesInStatements = validationService
        .getAllNonUniqueReferenceInStatements(Arrays.asList(firstUniqueReference, nonUniqueReference));

    assertThat(nonUniqueReferencesInStatements.size(), is(1));
    assertThat(nonUniqueReferencesInStatements.contains(nonUniqueReference), is(true));
  }
}
