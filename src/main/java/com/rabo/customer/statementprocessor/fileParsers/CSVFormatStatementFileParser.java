package com.rabo.customer.statementprocessor.fileParsers;

import com.rabo.customer.statementprocessor.model.CustomerStatement;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVFormatStatementFileParser implements StatementFileParser {

    private static final String REFERENCE = "Reference";
    private static final String ACCOUNT_NUMBER = "Account Number";
    private static final String DESCRIPTION = "Description";
    private static final String START_BALANCE = "Start Balance";
    private static final String MUTATION = "Mutation";
    private static final String END_BALANCE = "End Balance";
    public static final String NEGATE_SIGN = "-";

    public CSVFormatStatementFileParser() {
    }

    @Override
    public List<CustomerStatement> parseFile(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        CSVParser parser = CSVFormat.DEFAULT
            .withHeader(REFERENCE, ACCOUNT_NUMBER, DESCRIPTION, START_BALANCE, MUTATION,
                END_BALANCE)
            .withIgnoreHeaderCase()
            .withFirstRecordAsHeader()
            .parse(fileReader);
        return this.getCustomerStatements(parser);
    }

    @Override
    public SupportedFileTypes getSupportedFileType() {
        return SupportedFileTypes.CSV;
    }

    private List<CustomerStatement> getCustomerStatements(CSVParser parser) {
        final List<CustomerStatement> customerStatements = new ArrayList<>();
        for (CSVRecord csvRecord : parser) {
            CustomerStatement customerStatement = CustomerStatement.builder()
                .reference(getId(csvRecord, REFERENCE))
                .accountNumber(csvRecord.get(ACCOUNT_NUMBER))
                .description(csvRecord.get(DESCRIPTION))
                .startBalance(parseBigDecimal(getAmount(csvRecord, START_BALANCE)))
                .mutation(parseBigDecimal(getAmount(csvRecord, MUTATION)))
                .endBalance(parseBigDecimal(getAmount(csvRecord, END_BALANCE)))
                .build();
            customerStatements.add(customerStatement);
        }
        return customerStatements;
    }

    private int getId(CSVRecord csvRecord, String reference) {
        if (!csvRecord.get(reference).matches("^\\d+$")) {
            throw new IllegalArgumentException(
                reference + " field should be a valid reference number");
        }
        return Integer.parseInt(csvRecord.get(reference));
    }

    private String getAmount(CSVRecord csvRecord, String header) {
        if (!isValidAmount(csvRecord.get(header))) {
            throw new IllegalArgumentException(header + " field should be a valid amount");
        }
        return csvRecord.get(header);
    }

    private BigDecimal parseBigDecimal(String startBalance) {
        double value = Double.parseDouble(startBalance.replaceAll("^[-+]", ""));
        if (startBalance.contains(NEGATE_SIGN)) {
            return BigDecimal.valueOf(value).negate();
        }
        return BigDecimal.valueOf(value);
    }

    private boolean isValidAmount(String amount) {
        return amount.matches("^[+-]?\\d*(\\.\\d{1,4})?$");
    }
}
