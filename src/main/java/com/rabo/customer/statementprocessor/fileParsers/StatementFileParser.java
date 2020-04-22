package com.rabo.customer.statementprocessor.fileParsers;

import com.rabo.customer.statementprocessor.model.CustomerStatement;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface StatementFileParser {

    List<CustomerStatement> parseFile(File file) throws IOException;

    SupportedFileTypes getSupportedFileType();
}
