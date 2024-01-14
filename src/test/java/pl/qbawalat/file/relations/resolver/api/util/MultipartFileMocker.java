package pl.qbawalat.file.relations.resolver.api.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ResourceUtils;

public class MultipartFileMocker {

    private MultipartFileMocker() {}

    public static MockMultipartFile createMockMultipartFileFromCsv(String fileName) throws IOException {
        String filePath = "classpath:" + fileName;
        File file = ResourceUtils.getFile(filePath);
        byte[] content = Files.readAllBytes(file.toPath());
        String contentType = "text/csv";

        return createMockMultipartFile(fileName.substring(0, fileName.indexOf(".")), fileName, contentType, content);
    }

    public static MockMultipartFile createMockMultipartFile(
            String name, String originalFilename, String contentType, byte[] content) {
        return new MockMultipartFile(name, originalFilename, contentType, content);
    }

    public static String createCsvContent(String columns, String... records) {
        String newLine = "\n";
        return columns + newLine + String.join(newLine, records);
    }

    public static String createRecord(String... values) {
        return String.join(";", values);
    }

    public static MockMultipartFile createMultipartFileMock(
            String fileNameWithExtension, String contentType, String content) {
        return new MockMultipartFile("file", fileNameWithExtension, contentType, content.getBytes());
    }

    public static MockMultipartHttpServletRequestBuilder createRequestMock(
            List<MockMultipartFile> files, boolean shouldAddRequiredParams, IdValue... params) {
        MockMultipartHttpServletRequestBuilder multipart = MockMvcRequestBuilders.multipart("/file/conversion");
        if (shouldAddRequiredParams) {
            multipart.param("produces", "json");
            multipart.param("consumes", "csv");
        }
        files.forEach(multipart::file);
        Arrays.stream(params).forEach(param -> multipart.param(param.id(), param.value()));
        return multipart;
    }
}
