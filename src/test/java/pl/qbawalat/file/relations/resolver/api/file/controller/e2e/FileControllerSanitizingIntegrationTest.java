package pl.qbawalat.file.relations.resolver.api.file.controller.e2e;

import static pl.qbawalat.file.relations.resolver.api.util.MultipartFileMocker.*;

import java.text.MessageFormat;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.qbawalat.file.relations.resolver.api.file.constant.SupportedParamName;
import pl.qbawalat.file.relations.resolver.api.file.enums.SanitizationStrategy;

@SpringBootTest
@AutoConfigureMockMvc
class FileControllerSanitizingIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void skipEmptyCellsStrategyReturnsJsonWithoutEmptyCells() throws Exception {
        MockMultipartHttpServletRequestBuilder multipart = createRequest(
                createCsvContent(createRecord("code", "type", "some-header"), createRecord("coyote", "canine", "")));
        multipart.param(SupportedParamName.SANITIZE, SanitizationStrategy.SKIP_EMPTY_CELLS.name());

        mvc.perform(multipart)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.content()
                                .json(
                                        """
								[
									{"code":"coyote","type":{"code":"canine","description":"woof woof"}}
								]
								"""));
    }

    @Test
    void noSanitizeStrategyReturnsJsonWithBlanks() throws Exception {
        testSanitization(
                SanitizationStrategy.NO_SANITIZATION,
                new SanitizationCell("", ""),
                MockMvcResultMatchers.status().isOk());
    }

    @Test
    void sanitizeToNullsStrategyReturnsJsonWithNulls() throws Exception {
        testSanitization(
                SanitizationStrategy.EMPTY_CELLS_TO_NULLS,
                new SanitizationCell("", null),
                MockMvcResultMatchers.status().isOk());
    }

    private void testSanitization(
            SanitizationStrategy sanitizationStrategy,
            SanitizationCell subColumnValue,
            ResultMatcher resultMatcherStatus)
            throws Exception {
        MockMultipartHttpServletRequestBuilder multipart = createRequest(createCsvContent(
                createRecord("code", "type", "some-header"),
                createRecord("coyote", "canine", subColumnValue.preSanitization)));
        multipart.param(SupportedParamName.SANITIZE, sanitizationStrategy.name());

        ResultActions resultActions = mvc.perform(multipart).andExpect(resultMatcherStatus);
        if (resultMatcherStatus.equals(MockMvcResultMatchers.status().isOk())) {
            resultActions.andExpect(MockMvcResultMatchers.content()
                    .json(MessageFormat.format(
                            """
											[
												'{'"code":"coyote","type":'{'"code":"canine","description":"woof woof"'}',"some-header":{1}'}'
											]""",
                            wrapInQuotes(subColumnValue.postSanitization))));
        }
    }

    private MockMultipartHttpServletRequestBuilder createRequest(String csvContent) {
        MockMultipartFile mainFile = createMultipartFileMock("animals.csv", "text/csv", csvContent);
        MockMultipartFile supplierFile = createMultipartFileMock(
                "types.csv",
                "text/csv",
                createCsvContent(createRecord("code", "description"), createRecord("canine", "woof woof")));
        return createRequestMock(List.of(mainFile, supplierFile), true);
    }

    public static String wrapInQuotes(String input) {
        return input != null ? "\"" + input + "\"" : null;
    }

    record SanitizationCell(String preSanitization, String postSanitization) {}
}
