package com.rabo.customer.statementprocessor;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import com.rabo.customer.statementprocessor.fileParsers.CSVFormatStatementFileParser;
import com.rabo.customer.statementprocessor.fileParsers.StatementFileParser;
import com.rabo.customer.statementprocessor.fileParsers.SupportedFileTypes;
import com.rabo.customer.statementprocessor.fileParsers.XMLFormatStatementFileParser;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

public class CustomerStatementProcessorTest {

  private CustomerStatementProcessor customerStatementProcessor;

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void exceptionIsThrownForUnsupportedFiles() throws IOException {
    exceptionRule.expect(IllegalArgumentException.class);
    exceptionRule.expectMessage("File format is not supported");

    ValidationService validationService = Mockito.mock(ValidationService.class);
    List<StatementFileParser> statementFileParsers = Arrays
        .asList(Mockito.mock(CSVFormatStatementFileParser.class), Mockito.mock(
            XMLFormatStatementFileParser.class));

    customerStatementProcessor = new CustomerStatementProcessor(statementFileParsers,
        validationService);
    customerStatementProcessor
        .getInvalidStatements(new File(this.getClass().getResource("/records.json").getFile()));
  }

  @Test
  public void getInvalidStatementsInvocationsForFileTypeCsv() throws IOException {

    ValidationService validationService = Mockito.mock(ValidationService.class);

    CSVFormatStatementFileParser csvFormatStatementFileParser = Mockito
        .mock(CSVFormatStatementFileParser.class);
    XMLFormatStatementFileParser xmlFormatStatementFileParser = Mockito.mock(
        XMLFormatStatementFileParser.class);

    when(csvFormatStatementFileParser.getSupportedFileType()).thenReturn(SupportedFileTypes.CSV);
    when(xmlFormatStatementFileParser.getSupportedFileType()).thenReturn(SupportedFileTypes.XML);

    List customerStatements = Collections.EMPTY_LIST;
    when(csvFormatStatementFileParser.parseFile(any(File.class))).thenReturn(customerStatements);

    List<StatementFileParser> statementFileParsers = Arrays
        .asList(csvFormatStatementFileParser, xmlFormatStatementFileParser);

    customerStatementProcessor = new CustomerStatementProcessor(statementFileParsers,
        validationService);

    customerStatementProcessor
        .getInvalidStatements(new File(this.getClass().getResource("/records.csv").getFile()));

    Mockito.verify(validationService).getInvalidEndBalanceInStatements(eq(customerStatements));
    Mockito.verify(validationService).getAllNonUniqueReferenceInStatements(eq(customerStatements));
  }

  @Test
  public void getInvalidStatementsInvocationsForFileTypeXml() throws IOException {

    ValidationService validationService = Mockito.mock(ValidationService.class);

    CSVFormatStatementFileParser csvFormatStatementFileParser = Mockito
        .mock(CSVFormatStatementFileParser.class);
    XMLFormatStatementFileParser xmlFormatStatementFileParser = Mockito.mock(
        XMLFormatStatementFileParser.class);

    when(csvFormatStatementFileParser.getSupportedFileType()).thenReturn(SupportedFileTypes.CSV);
    when(xmlFormatStatementFileParser.getSupportedFileType()).thenReturn(SupportedFileTypes.XML);

    List customerStatements = Collections.EMPTY_LIST;
    when(csvFormatStatementFileParser.parseFile(any(File.class))).thenReturn(customerStatements);

    List<StatementFileParser> statementFileParsers = Arrays
        .asList(csvFormatStatementFileParser, xmlFormatStatementFileParser);

    customerStatementProcessor = new CustomerStatementProcessor(statementFileParsers,
        validationService);

    customerStatementProcessor
        .getInvalidStatements(new File(this.getClass().getResource("/records.xml").getFile()));

    Mockito.verify(validationService).getInvalidEndBalanceInStatements(eq(customerStatements));
    Mockito.verify(validationService).getAllNonUniqueReferenceInStatements(eq(customerStatements));
  }

}
