package com.example.springcoditstudy.d0916.trainer;


import com.example.springcoditstudy.d0916.common.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.ImagingOpException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/trainers")
public class TrainerController2 {

    private final TrainerService2 trainerService;

    public static final List<String> ALLOWED_EXTENSIONS = List.of("jpg","jpeg","png","gif");

    @Tag(name = "트레이너 등록", description = "트레이너 및 이미지 등록 API")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<TrainerResponse>> create(@Valid @ModelAttribute CreateTrainerRequest createTrainerRequest,
                                                               @RequestParam(required = false) MultipartFile multipartFile) throws IOException {
        String savedFileName = null;
        if(multipartFile != null && !multipartFile.isEmpty()){
            validateImageFile(multipartFile);
            String fileName = multipartFile.getOriginalFilename();
            String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
            savedFileName = UUID.randomUUID() + extension;
            Path savePath = Paths.get("./uploads/" + savedFileName);
            Files.createDirectories(savePath.getParent());
            multipartFile.transferTo(savePath);
        }

        TrainerResponse trainer = trainerService.createTrainer(createTrainerRequest.getName(),createTrainerRequest.getSpecialty(),savedFileName);
        return ResponseEntity.ok().body(ApiResponse.success(trainer));
    }

    @Tag(name = "트레이너 이미지 등록")
    private void validateImageFile(MultipartFile multipartFile){
        if(multipartFile.isEmpty()) {
            throw new IllegalArgumentException("파일이 비었습니다.");
        }
        String fileName = multipartFile.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
        if(!ALLOWED_EXTENSIONS.contains(extension)){
            throw new IllegalArgumentException("허용되지 않은 확장자 입니다.");
        }
        String contentType = multipartFile.getContentType();
        if(contentType == null || !contentType.startsWith("image/")){
            throw new ImagingOpException("이미지 파일만 업로드 가능합니다.");
        }
    }

    @Tag(name = "트레이너 목록",description = "트레이너 전체 및 특정 트레이너Id 검색")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TrainerResponse>>> getTrainers(@RequestParam(required = false)String specialty){
        List<TrainerResponse> trainers = trainerService.getTrainers(specialty).stream()
                .map(TrainerResponse::form)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(ApiResponse.success(trainers));
    }

    @Tag(name = "트레이너 이미지 읽기")
    @GetMapping("/{trainer-id}/profile-image")
    public ResponseEntity<Resource> getTrainerImage(@PathVariable("trainer-id") long trainerId) throws IOException {
        Trainer trainer = trainerService.getTrainer(trainerId);
        if(trainer.getProfileImageFileName() == null){
            return ResponseEntity.notFound().build();
        }
        Path filePath = Paths.get("./uploads/" + trainer.getProfileImageFileName());
        if(!Files.exists(filePath)){
            throw new FileNotFoundException("존재하지 않는 파일입니다.");
        }
        Resource resource = new InputStreamResource(Files.newInputStream(filePath));
        String contentType = Files.probeContentType(filePath);
        return ResponseEntity.ok()
                //.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"sample.txt\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);

    }

}
