package com.rabo.customer.statementprocessor.fileParsers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.rabo.customer.statementprocessor.model.CustomerStatement;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class XMLFormatStatementFileParser implements StatementFileParser {

    private XmlMapper mapper;

    public XMLFormatStatementFileParser() {
        this.mapper = new XmlMapper();
    }

    @Override
    public List<CustomerStatement> parseFile(File file) throws IOException {
        FileReader fileReader = new FileReader(
            file);
        return this.mapper.readValue(fileReader, new TypeReference<List<CustomerStatement>>() {
        });
    }

    @Override
    public SupportedFileTypes getSupportedFileType() {
        return SupportedFileTypes.XML;
    }
}
