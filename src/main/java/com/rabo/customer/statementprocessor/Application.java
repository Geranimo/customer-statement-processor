package com.rabo.customer.statementprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabo.customer.statementprocessor.fileParsers.CSVFormatStatementFileParser;
import com.rabo.customer.statementprocessor.fileParsers.XMLFormatStatementFileParser;
import com.rabo.customer.statementprocessor.model.FailedRecord;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        LOG.info("file with path to process");

        // path for the file to process
        String filePath = scanner.next();
        scanner.close();

        CustomerStatementProcessor customerStatementProcessor = new CustomerStatementProcessor(
            Arrays.asList(new CSVFormatStatementFileParser(), new XMLFormatStatementFileParser()),
            new ValidationService());

        Set<FailedRecord> invalidStatements = customerStatementProcessor
            .getInvalidStatements(new File(filePath));

        ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(new File("InvalidRecords.json"), invalidStatements);
    }
}
