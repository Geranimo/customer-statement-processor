package com.rabo.customer.statementprocessor;

import com.rabo.customer.statementprocessor.fileParsers.StatementFileParser;
import com.rabo.customer.statementprocessor.fileParsers.SupportedFileTypes;
import com.rabo.customer.statementprocessor.model.CustomerStatement;
import com.rabo.customer.statementprocessor.model.FailedRecord;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;

public class CustomerStatementProcessor {

    private ValidationService validationService;
    private List<StatementFileParser> statementFileParsers;

    public CustomerStatementProcessor(List<StatementFileParser> statementFileParsers,
        ValidationService validationService) {
        this.validationService = validationService;
        this.statementFileParsers = statementFileParsers;
    }

    public Set<FailedRecord> getInvalidStatements(File file) throws IOException {

        Optional<SupportedFileTypes> supportedFileType = Arrays
            .stream(SupportedFileTypes.values())
            .filter(supportedFileTypes -> supportedFileTypes.
                name().equalsIgnoreCase(FilenameUtils.getExtension(file.getName())))
            .findFirst();

        SupportedFileTypes fileType = supportedFileType.orElseThrow(() ->
            new IllegalArgumentException("File format is not supported"));

        StatementFileParser processor = getStatementTypeParser(fileType);
        final List<CustomerStatement> customerStatements = processor.parseFile(file);

        final List<CustomerStatement> invalidEndBalanceInStatements = this.validationService
            .getInvalidEndBalanceInStatements(
                customerStatements);
        final List<CustomerStatement> nonUniqueReferenceInStatements = this.validationService
            .getAllNonUniqueReferenceInStatements(
                customerStatements);

        final List<CustomerStatement> allInvalidStatements = new ArrayList<>();
        allInvalidStatements.addAll(invalidEndBalanceInStatements);
        allInvalidStatements.addAll(nonUniqueReferenceInStatements);

        return createInvalidStatements(allInvalidStatements);
    }

    private StatementFileParser getStatementTypeParser(SupportedFileTypes fileType) {
        return this.statementFileParsers.stream()
            .filter(parser -> parser.getSupportedFileType() == fileType).findFirst()
            .orElseThrow(() ->
                new IllegalArgumentException("No parser available for file type"));
    }

    public Set<FailedRecord> createInvalidStatements(
        List<CustomerStatement> customerStatements) {
        return customerStatements.stream()
            .map(customerStatement -> new FailedRecord(customerStatement.getReference(),
                customerStatement.getDescription())).collect(Collectors.toSet());
    }
}
