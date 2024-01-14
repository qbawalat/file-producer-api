package pl.qbawalat.file.relations.resolver.api.file.controller.e2e;

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
class FileControllerValidationsIntegrationTests {
    @Autowired
    private MockMvc mvc;

    @Test
    void unprocessableEntityWhenEmptyCellHeader() throws Exception {
        MockMultipartFile file = createMultipartFileMock(
                "animals.csv", "text/csv", createCsvContent(createRecord("", "type"), createRecord("dog", "canine")));
        MockMultipartHttpServletRequestBuilder multipart = createRequestMock(List.of(file), true);
        mvc.perform(multipart).andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }
}
