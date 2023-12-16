package pl.qbawalat.file.relations.resolver.api.file.controller.e2e;

import static pl.qbawalat.file.relations.resolver.api.util.MultipartFileMocker.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.qbawalat.file.relations.resolver.api.file.constant.SupportedParamName;

@SpringBootTest
@AutoConfigureMockMvc
class FileControllerSortingIntegrationTest {
    private static final String VALID_CSV_CONTENT = createCsvContent(
            createRecord("code", "type", "sort_order"),
            createRecord("dog", "canine", "10"),
            createRecord("wolf", "canine", "1"),
            createRecord("coyote", "canine", "5"));
    private static final String INVALID_CSV_CONTENT_MISSING_SORT_COLUMN = createCsvContent(
            createRecord("code", "type"),
            createRecord("dog", "canine"),
            createRecord("wolf", "canine"),
            createRecord("coyote", "canine"));
    private static final String INVALID_CSV_CONTENT_EMPTY_COLUMNS = createCsvContent(
            createRecord("code", "type", "sort_order"),
            createRecord("dog", "canine", ""),
            createRecord("wolf", "canine", "1"),
            createRecord("coyote", "canine", ""));

    @Autowired
    private MockMvc mvc;

    @Test
    void sortsJsonAscendingForAscSortParam() throws Exception {
        MockMultipartHttpServletRequestBuilder multipart = createValidRequest();
        multipart.param(SupportedParamName.SORT, Sort.Direction.ASC.name());

        mvc.perform(multipart)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.content()
                                .json(
                                        """
												[
													{"code":"wolf","type":{"code":"canine","description":"woof woof"},"sort_order":"1"},
													{"code":"coyote","type":{"code":"canine","description":"woof woof"},"sort_order":"5"},
													{"code":"dog","type":{"code":"canine","description":"woof woof"},"sort_order":"10"}
												]
												"""));
    }

    @Test
    void sortsJsonDescendingForDescSortParam() throws Exception {
        MockMultipartHttpServletRequestBuilder multipart = createValidRequest();
        multipart.param(SupportedParamName.SORT, Sort.Direction.DESC.name());

        mvc.perform(multipart)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.content()
                                .json(
                                        """
												[
													{"code":"dog","type":{"code":"canine","description":"woof woof"},"sort_order":"10"},
													{"code":"coyote","type":{"code":"canine","description":"woof woof"},"sort_order":"5"},
													{"code":"wolf","type":{"code":"canine","description":"woof woof"},"sort_order":"1"}
												]
												"""));
    }

    @Test
    void skipsSortingWhenMissingSortOrderColumn() throws Exception {
        MockMultipartHttpServletRequestBuilder multipart = createInvalidRequestMissingSortColumn();

        mvc.perform(multipart)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.content()
                                .json(
                                        """
												[
													{"code":"dog","type":{"code":"canine","description":"woof woof"}},
													{"code":"wolf","type":{"code":"canine","description":"woof woof"}},
													{"code":"coyote","type":{"code":"canine","description":"woof woof"}}
												]
												"""));
    }

    @Test
    void skipsSortingWhenSortParamMissing() throws Exception {
        MockMultipartHttpServletRequestBuilder multipart = createInvalidRequestEmptySortColumnValue();

        mvc.perform(multipart)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.content()
                                .json(
                                        """
												[
													{"code":"dog","type":{"code":"canine","description":"woof woof"},"sort_order":""},
													{"code":"wolf","type":{"code":"canine","description":"woof woof"},"sort_order":"1"},
													{"code":"coyote","type":{"code":"canine","description":"woof woof"},"sort_order":""}
												]
												"""));
    }

    @Test
    void unprocessableEntityWhenMissingSortOrderColumnHeader() throws Exception {
        MockMultipartHttpServletRequestBuilder multipart = createInvalidRequestMissingSortColumn();
        multipart.param(SupportedParamName.SORT, Sort.Direction.ASC.name());

        mvc.perform(multipart).andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    void unprocessableEntityWhenEmptySortOrderCell() throws Exception {
        MockMultipartHttpServletRequestBuilder multipart = createInvalidRequestEmptySortColumnValue();
        multipart.param(SupportedParamName.SORT, Sort.Direction.ASC.name());

        mvc.perform(multipart).andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    void unprocessableEntityWhenNonNumericSortOrder() throws Exception {
        MockMultipartHttpServletRequestBuilder multipart = createInvalidRequestNonNumericSortColumnValue();
        multipart.param(SupportedParamName.SORT, Sort.Direction.ASC.name());
        mvc.perform(multipart).andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    private MockMultipartHttpServletRequestBuilder createValidRequest() {

        return createRequest(VALID_CSV_CONTENT);
    }

    private MockMultipartHttpServletRequestBuilder createInvalidRequestMissingSortColumn() {

        return createRequest(INVALID_CSV_CONTENT_MISSING_SORT_COLUMN);
    }

    private MockMultipartHttpServletRequestBuilder createInvalidRequestEmptySortColumnValue() {

        return createRequest(INVALID_CSV_CONTENT_EMPTY_COLUMNS);
    }

    private MockMultipartHttpServletRequestBuilder createInvalidRequestNonNumericSortColumnValue() {
        return createRequest(createCsvContent(
                createRecord("code", "type", "sort_order"),
                createRecord("dog", "canine", "abracadabra"),
                createRecord("wolf", "canine", "1"),
                createRecord("coyote", "canine", "")));
    }

    private MockMultipartHttpServletRequestBuilder createRequest(String csvContent) {
        MockMultipartFile mainFile = createMultipartFileMock("animals.csv", "text/csv", csvContent);
        MockMultipartFile supplierFile = createMultipartFileMock(
                "types.csv",
                "text/csv",
                createCsvContent(createRecord("code", "description"), createRecord("canine", "woof woof")));
        return createRequestMock(List.of(mainFile, supplierFile), true);
    }
}
