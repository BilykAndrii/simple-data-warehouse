package ab.demo.simpledatawarehouse.controller;

import ab.demo.simpledatawarehouse.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import static java.util.Objects.nonNull;

@RestController
public class FileController {

    private static final String TEXT_CSV = "text/csv";
    private static final String RESPONSE = "%s lines uploaded";

    @Autowired
    private FileService fileService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam(value = "header", required = false) boolean header,
                             @RequestParam(value = "dateFormat", required = false) String dateFormat) {

        if (!isValid(file)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is not valid CSV!");
        }

        int lines = fileService.saveData(file, header, dateFormat);
        return String.format(RESPONSE, lines);
    }

    private boolean isValid(MultipartFile file) {
        return nonNull(file) && TEXT_CSV.equals(file.getContentType());
    }

}
