package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.service.FileService;
import ru.job4j.dreamjob.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileControllerTest {

    private FileService fileService;

    private FileController fileController;

    @BeforeEach
    public void initServices() {
        fileService = mock(FileService.class);
        fileController = new FileController(fileService);
    }

    @Test
    void whenFileExistsThenReturnFileContentWithOkStatus() {
        var fileId = 1;
        var fileContent = "file content".getBytes();
        var fileDto = new FileDto("test.txt", fileContent);
        when(fileService.getFileById(fileId)).thenReturn(Optional.of(fileDto));

        var response = fileController.getById(fileId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(fileContent);
    }

    @Test
    void whenFileDoesNotExistThenReturnNotFoundStatus() {
        var fileId = 999;
        when(fileService.getFileById(fileId)).thenReturn(Optional.empty());

        var response = fileController.getById(fileId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }
}