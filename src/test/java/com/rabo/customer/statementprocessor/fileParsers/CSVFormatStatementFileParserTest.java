package com.rabo.customer.statementprocessor.fileParsers;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.rabo.customer.statementprocessor.model.CustomerStatement;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.Test;

public class CSVFormatStatementFileParserTest {

  private CSVFormatStatementFileParser csvFormatStatementFileParser;

  @Test
  public void testFieldsAreParsedExpected() throws IOException {
    csvFormatStatementFileParser = new CSVFormatStatementFileParser();
    List<CustomerStatement> customerStatements = csvFormatStatementFileParser
        .parseFile(new File(this.getClass().getResource("/csv/records_test.csv").getFile()));
    assertThat(customerStatements.size(), is(1));
    CustomerStatement customerStatement = customerStatements.get(0);
    assertThat(customerStatement.getReference(), is(132843));
    assertThat(customerStatement.getAccountNumber(), is("NL56RABO0149876948"));
    assertThat(customerStatement.getDescription(), is("Flowers for Erik Bakker"));
    assertThat(customerStatement.getStartBalance().toString(), is("90.68"));
    assertThat(customerStatement.getMutation().toString(), is("-45.33"));
    assertThat(customerStatement.getEndBalance().toString(), is("45.35"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOnInvalidReferenceExceptionIsThrown() throws IOException {
    csvFormatStatementFileParser = new CSVFormatStatementFileParser();
    csvFormatStatementFileParser
        .parseFile(
            new File(this.getClass().getResource("/csv/records_invalid_reference.csv").getFile()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOnInvalidBalanceExceptionIsThrown() throws IOException {
    csvFormatStatementFileParser = new CSVFormatStatementFileParser();
    csvFormatStatementFileParser
        .parseFile(
            new File(this.getClass().getResource("/csv/records_invalid_balance.csv").getFile()));
  }

  @Test
  public void testGetSupportedFileType() {
    csvFormatStatementFileParser = new CSVFormatStatementFileParser();
    assertThat(csvFormatStatementFileParser.getSupportedFileType(), is(SupportedFileTypes.CSV));
  }

}
