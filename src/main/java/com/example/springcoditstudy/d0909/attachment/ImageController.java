package com.example.springcoditstudy.d0909.attachment;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class ImageController {

    public static final List<String> ALLOWED_EXTENSIONS = List.of("jpg","jpeg","png","gif");

    private void validateImageFile(MultipartFile multipartFile){
        // 파일이 빈 파일이 아닌지 확인
        if(multipartFile.isEmpty()){
            throw new IllegalArgumentException("파일이 비었습니다.");
        }
        // 확장자 확인
        String fileName = multipartFile.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
        if(!ALLOWED_EXTENSIONS.contains(extension)){
            throw new IllegalArgumentException("허용되지 않은 확장자 입니다.");
        }
        // 확장자 조작인지 확인
        String contentType = multipartFile.getContentType();
        if(contentType == null || !contentType.startsWith("image/")){
            throw new ImageUploadException("이미지 파일만 업로드 가능합니다.","error");
        }
    }

    @ExceptionHandler(ImageUploadException.class)
    public String HandleEception(ImageUploadException e, Model model){
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(),e.getMessage() );
        model.addAttribute("e",errorResponse);
        return "test";    }



    @PostMapping("/v1/upload")
    public String uploadPokemonImage(@RequestParam("pokemonName") String pokemonName,
                                     @RequestParam("images")MultipartFile multipartFile, Model model) throws IOException {
        // 1. 검증
        validateImageFile(multipartFile);
        // 2. 이름 저장
        String fileName = multipartFile.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        String savedFileName = UUID.randomUUID() + extension;
        // 3. 실제 저장
        Path savePath = Paths.get("./uploads/" + savedFileName);
        Files.createDirectories(savePath.getParent());
        multipartFile.transferTo(savePath);
        model.addAttribute("pokemonName",pokemonName);
        model.addAttribute("fileName",savedFileName);
        return "test";
    }

    @PostMapping("/v1/upload/multiple")
    public String uploadMultiple(@RequestParam("pokemonName") String pokemonName,
                                 @RequestParam("images") List<MultipartFile> multipartFile, Model model) throws IOException     {
        List<String> fileNames = new ArrayList<>();
        for(MultipartFile file : multipartFile){
            validateImageFile(file);

            String fileName = file.getOriginalFilename();
            String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
            String savedFileName = UUID.randomUUID() + extension;
            // 3. 실제 저장
            Path savePath = Paths.get("./uploads/" + savedFileName);
            Files.createDirectories(savePath.getParent());
            file.transferTo(savePath);
            fileNames.add(savedFileName);
        }
        model.addAttribute("pokemonName",pokemonName);
        model.addAttribute("fileNames",fileNames);
        return "test";
    }

    @GetMapping("/v1/images/{fileName}")
    public ResponseEntity<Resource> getPokemonImages(@PathVariable String fileName) throws IOException {
        Path filePath = Paths.get("./uploads/" + fileName);
        if(!Files.exists(filePath)){
            throw new FileNotFoundException("존재하지 않는 파일입니다.");
        }
        Resource resource = new InputStreamResource(Files.newInputStream(filePath));
        String contentType = Files.probeContentType(filePath);
        return ResponseEntity.ok()
                // 아래 한줄로 바로 다운로드 가능
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"sample.txt\"") // 파일 다운로드 ( 없을시 화면 띄우기 )
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @DeleteMapping("/v1/images/{fileName}")
    public ResponseEntity<Void> deletePokemonImages(@PathVariable String fileName) throws IOException {
        Path filePath = Paths.get("./uploads/" + fileName);
        Files.deleteIfExists(filePath);
        return ResponseEntity.noContent().build();
    }

}

