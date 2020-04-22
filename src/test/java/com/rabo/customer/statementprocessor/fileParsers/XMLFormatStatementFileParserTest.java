package com.rabo.customer.statementprocessor.fileParsers;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.rabo.customer.statementprocessor.model.CustomerStatement;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.Test;

public class XMLFormatStatementFileParserTest {

  private XMLFormatStatementFileParser xmlFormatStatementFileParser;

  @Test
  public void testRecordsAreParsedAsExpected() throws IOException {
    xmlFormatStatementFileParser = new XMLFormatStatementFileParser();
    List<CustomerStatement> customerStatements = xmlFormatStatementFileParser
        .parseFile(new File(this.getClass().getResource("/xml/records_test.xml").getFile()));
    assertThat(customerStatements.size(), is(1));
    CustomerStatement customerStatement = customerStatements.get(0);
    assertThat(customerStatement.getReference(), is(108366));
    assertThat(customerStatement.getAccountNumber(), is("NL27SNSB0917829871"));
    assertThat(customerStatement.getDescription(), is("Candy from Willem de Vries"));
    assertThat(customerStatement.getStartBalance().toString(), is("30.77"));
    assertThat(customerStatement.getMutation().toString(), is("22.38"));
    assertThat(customerStatement.getEndBalance().toString(), is("53.15"));
  }

  @Test
  public void testGetSupportedFileType() {
    xmlFormatStatementFileParser = new XMLFormatStatementFileParser();
    assertThat(xmlFormatStatementFileParser.getSupportedFileType(), is(SupportedFileTypes.XML));
  }
}
