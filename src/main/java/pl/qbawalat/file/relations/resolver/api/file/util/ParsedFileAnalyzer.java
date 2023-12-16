package pl.qbawalat.file.relations.resolver.api.file.util;

import java.util.Set;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedFile;

public class ParsedFileAnalyzer {
    private ParsedFileAnalyzer() {}

    public static Set<String> getColumnsNames(ParsedFile parsedFile) {
        return parsedFile.content().records().get(0).keySet();
    }

    public static String getForeignKeyName(ParsedFile parsedFile) {
        String fileName = parsedFile.fileName();
        return toForeignKey(fileName);
    }

    public static String getForeignKeyName(String parsedFileName) {
        return toForeignKey(parsedFileName);
    }

    private static String toForeignKey(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.') - 1);
    }
}
