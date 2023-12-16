package pl.qbawalat.file.relations.resolver.api.file.controller;

import static pl.qbawalat.file.relations.resolver.api.util.MultipartFileMocker.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SelectPackages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * FileControllerE2eTests itself contains simple conversion cases to test the whole api flow - from retrieving a request  to returning it.
 * Also, it scans for more detailed e2e tests inside e2e package (sorting, sanitizing, validations etc.).
 */
@SpringBootTest
@AutoConfigureMockMvc
@SelectPackages({"pl.qbawalat.file.relations.resolver.api.features.file.controller.e2e"})
class FileControllerE2eTests {
    @Autowired
    private MockMvc mvc;

    @Test
    void givenOneCsvFile_thenConvertToJson() throws Exception {
        MockMultipartFile file = createMultipartFileMock(
                "animal.csv",
                "text/csv",
                createCsvContent(createRecord("code", "type"), createRecord("dog", "canine")));
        MockMultipartHttpServletRequestBuilder multipart = createRequestMock(List.of(file), true);
        mvc.perform(multipart)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
						[{"code":"dog","type":"canine"}]
						"""));
    }

    @Test
    void givenTwoCsvFiles_whenOneMainAndOneSupplyingFile_thenConvertToJson() throws Exception {
        MockMultipartFile mainFile = createMultipartFileMock(
                "animals.csv",
                "text/csv",
                createCsvContent(createRecord("code", "type"), createRecord("dog", "canine")));
        MockMultipartFile supplierFile = createMultipartFileMock(
                "types.csv",
                "text/csv",
                createCsvContent(createRecord("code", "description"), createRecord("canine", "woof woof")));
        MockMultipartHttpServletRequestBuilder multipart = createRequestMock(List.of(mainFile, supplierFile), true);
        mvc.perform(multipart)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.content()
                                .json(
                                        """
								[{"code":"dog","type":{"code":"canine","description":"woof woof"}}]
								"""));
    }

    @Test
    void givenThreeCsvFiles_whenOneMainAndTwoSupplyingFiles_thenConvertToJson() throws Exception {
        MockMultipartFile mainFile = createMultipartFileMock(
                "animals.csv",
                "text/csv",
                createCsvContent(createRecord("code", "type", "diet"), createRecord("dog", "canine", "carnivore")));
        MockMultipartFile typesSupplierFile = createMultipartFileMock(
                "types.csv",
                "text/csv",
                createCsvContent(createRecord("code", "description"), createRecord("canine", "woof woof")));
        MockMultipartFile dietsSupplierFile = createMultipartFileMock(
                "diets.csv",
                "text/csv",
                createCsvContent(createRecord("code", "food"), createRecord("carnivore", "meat")));
        MockMultipartHttpServletRequestBuilder multipart =
                createRequestMock(List.of(mainFile, typesSupplierFile, dietsSupplierFile), true);
        mvc.perform(multipart)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.content()
                                .json(
                                        """
								[{"code":"dog","type":{"code":"canine","description":"woof woof"}}]
								"""));
    }
}
