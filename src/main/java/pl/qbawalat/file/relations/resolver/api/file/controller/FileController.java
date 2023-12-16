package pl.qbawalat.file.relations.resolver.api.file.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.qbawalat.file.relations.resolver.api.file.constant.SupportedParamName;
import pl.qbawalat.file.relations.resolver.api.file.constraint.FileTypeConstraint;
import pl.qbawalat.file.relations.resolver.api.file.dto.FileDto;
import pl.qbawalat.file.relations.resolver.api.file.enums.ConsumeableFileType;
import pl.qbawalat.file.relations.resolver.api.file.enums.ProduceableFileType;
import pl.qbawalat.file.relations.resolver.api.file.enums.SanitizationStrategy;
import pl.qbawalat.file.relations.resolver.api.file.service.FileProducerService;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Validated
public class FileController {
    private final FileProducerService fileProducer;

    @PostMapping("/conversion")
    @ResponseBody
    public ResponseEntity<FileDto> convertFiles(
            @RequestParam(name = SupportedParamName.FILE) List<@FileTypeConstraint MultipartFile> files,
            @RequestParam(name = SupportedParamName.PRODUCES) ProduceableFileType _produceableFileType,
            @RequestParam(name = SupportedParamName.CONSUMES) ConsumeableFileType _consumeableFileType,
            @RequestParam(name = SupportedParamName.SORT, required = false) Sort.Direction _sortDirection,
            @RequestParam(name = SupportedParamName.SANITIZE, required = false)
                    SanitizationStrategy _sanitizationStrategy) {
        FileDto fileDto = fileProducer.processFiles(files);
        return new ResponseEntity<>(fileDto, HttpStatus.OK);
    }
}
