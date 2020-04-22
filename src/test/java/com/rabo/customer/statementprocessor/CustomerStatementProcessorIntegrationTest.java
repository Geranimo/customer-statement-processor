package com.rabo.customer.statementprocessor;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.rabo.customer.statementprocessor.fileParsers.CSVFormatStatementFileParser;
import com.rabo.customer.statementprocessor.fileParsers.XMLFormatStatementFileParser;
import com.rabo.customer.statementprocessor.model.FailedRecord;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;

public class CustomerStatementProcessorIntegrationTest {

  private CustomerStatementProcessor customerStatementProcessor;

  @Before
  public void setUp() {

    customerStatementProcessor = new CustomerStatementProcessor(
        Arrays.asList(new CSVFormatStatementFileParser(), new XMLFormatStatementFileParser()),
        new ValidationService());
  }

  @Test
  public void testProcessStatementsWithNoInvalidRecordsCsv() throws IOException {
    Set<FailedRecord> invalidStatements = customerStatementProcessor
        .getInvalidStatements(new File(
            this.getClass().getResource("/csv/records_with_no_invalid_records.csv").getFile()));
    assertThat(invalidStatements.size(), is(0));
  }

  @Test
  public void testProcessStatementsWithNoInvalidRecordsXml() throws IOException {
    Set<FailedRecord> invalidStatements = customerStatementProcessor
        .getInvalidStatements(new File(
            this.getClass().getResource("/xml/records_with_no_invalid_records.xml").getFile()));
    assertThat(invalidStatements.size(), is(0));
  }

  @Test
  public void testProcessStatementsWithNonUniqueReferenceIdsCsv() throws IOException {

    Set<FailedRecord> invalidStatements = customerStatementProcessor
        .getInvalidStatements(new File(
            this.getClass().getResource("/csv/records_with_non_unique_invalid_records.csv")
                .getFile()));

    assertThat(invalidStatements.size(), is(1));
    Optional<FailedRecord> recordWithNonUniqueReferenceID = getRecordWithReferenceID(
        invalidStatements,
        132843);
    assertTrue(recordWithNonUniqueReferenceID.get().getDescription()
        .equals("record with non unique reference id 2"));
  }

  @Test
  public void testProcessStatementsWithNonUniqueReferenceIdsXml() throws IOException {

    Set<FailedRecord> invalidStatements = customerStatementProcessor
        .getInvalidStatements(new File(
            this.getClass().getResource("/xml/records_with_non_unique_invalid_records.xml")
                .getFile()));

    assertThat(invalidStatements.size(), is(1));
    Optional<FailedRecord> recordWithNonUniqueReferenceID = getRecordWithReferenceID(
        invalidStatements,
        108366);
    assertTrue(recordWithNonUniqueReferenceID.get().getDescription()
        .equals("record with non unique reference id"));
  }

  @Test
  public void testProcessStatementsWithInvalidEndBalancesCsv() throws IOException {

    Set<FailedRecord> invalidStatements = customerStatementProcessor
        .getInvalidStatements(new File(
            this.getClass().getResource("/csv/records_containing_invalid_end_balances.csv")
                .getFile()));
    assertThat(invalidStatements.size(), is(2));

    Optional<FailedRecord> invalidBalanceRecord1 = getRecordWithReferenceID(invalidStatements,
        132844);
    assertThat(invalidBalanceRecord1.get().getDescription(), is("Invalid balance record 1"));
    Optional<FailedRecord> invalidBalanceRecord2 = getRecordWithReferenceID(invalidStatements,
        132895);
    assertThat(invalidBalanceRecord2.get().getDescription(), is("Invalid balance record 2"));
  }

  @Test
  public void testProcessStatementsWithInvalidEndBalancesXml() throws IOException {

    Set<FailedRecord> invalidStatements = customerStatementProcessor
        .getInvalidStatements(new File(
            this.getClass().getResource("/xml/records_containing_invalid_end_balances.xml")
                .getFile()));
    assertThat(invalidStatements.size(), is(2));

    Optional<FailedRecord> invalidBalanceRecord1 = getRecordWithReferenceID(invalidStatements,
        108366);
    assertThat(invalidBalanceRecord1.get().getDescription(),
        is("record with invalid end balance 1"));
    Optional<FailedRecord> invalidBalanceRecord2 = getRecordWithReferenceID(invalidStatements,
        108399);
    assertThat(invalidBalanceRecord2.get().getDescription(),
        is("record with invalid end balance 2"));
  }

  private Optional<FailedRecord> getRecordWithReferenceID(Set<FailedRecord> invalidStatements,
      Integer referenceId) {
    return invalidStatements.stream()
        .filter(failedRecord -> failedRecord.getReferenceId().intValue() == referenceId)
        .findFirst();
  }

  @Test
  public void testProcessStatementsWithNonUniqueIdsAndInvalidEndBalancesCsv() throws IOException {

    Set<FailedRecord> invalidStatements = customerStatementProcessor
        .getInvalidStatements(new File(
            this.getClass()
                .getResource("/csv/records_containing_non_unique_id_and_invalid_balances.csv")
                .getFile()));
    assertThat(invalidStatements.size(), is(3));

    List<FailedRecord> failedRecordsWithNonUniqueReferenceId = invalidStatements.stream()
        .filter(failedRecord -> failedRecord.getReferenceId().intValue() == 132843).collect(
            Collectors.toList());
    assertThat(failedRecordsWithNonUniqueReferenceId.size(), is(2));

    FailedRecord nonUniqueRecord1 = failedRecordsWithNonUniqueReferenceId.get(0);
    assertThat(nonUniqueRecord1.getDescription(),
        is("invalid balance and non unique reference id"));

    FailedRecord nonUniqueRecord2 = failedRecordsWithNonUniqueReferenceId.get(1);
    assertThat(nonUniqueRecord2.getDescription(), is("valid balance and non unique reference id"));

    Optional<FailedRecord> invalidEndBalanceRecord2 = getRecordWithReferenceID(invalidStatements,
        132895);

    assertThat(invalidEndBalanceRecord2.get().getDescription(),
        is("invalid balance and unique reference id"));
  }

}
