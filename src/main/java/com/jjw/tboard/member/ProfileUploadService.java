package com.jjw.tboard.member;

/*
* User 관련 비즈니스 로직
* - 사용자의 프로필 이미지를 파일로 직접 생성하는 코드를 작성해 보자
* - 역할 : 실제 파일을 시스템에 저장하고 삭제하는 작업만 처리할 예정 
* - 주의 : 데이터베이스 업데이트는 UserService 에서 처리할 예정
* */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class ProfileUploadService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    /*프로필 이미지 파일을 서버에 업로드 하는 메서드*/
    public String uploadProfileImage(MultipartFile multipartFile) throws IOException{
        // 1. 단계 : 업로드할 디렉토리(폴더)가 존재하지 않으면 생성
        createUploadDirectory();
        // 2. 단계 : 업로드된 파일의 원본 이름 추출
        // DB <-- 실제 저장되는 경로, 사용자가 올린 파일명도 관리할 수 있다.
        String originFilename = multipartFile.getOriginalFilename();
        // 3. 단계 : 파일 확장자를 추출
        String extension = getFileExtension(originFilename);
        
        // 4. 단계 : 중복을 방지하기 위해 고유한 파일명 생성
        // 현재 날짜 및 시간_파일이름.jpg 형식
        String uniqueFileName = generateUniqueFileName(extension);

        // 5. 단계  최종 저장할 파일의 전체 경로를 생성
        // 예 : C:/uploads/profiles/20212955_123123.jpg
        Path filePath = Paths.get(uploadDir,uniqueFileName);

        // 6단계 : 실제로 파일을 임시 저장소에서 최종  우리가 정한 위치로 이동/복사
        multipartFile.transferTo(filePath);

        // 7단계 : 실제 바이트 단위로 받은 데이터를 서버 컴퓨터에 new File()
        return "/uploads/profiles/" + uniqueFileName;
    }

    private String generateUniqueFileName(String extension) {
        // 1. 현재 날짜와 시간을 "YYYYMMDD HHMMSS" 형태로 포맷팅
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYYMMDD_HHmmss"));
        // 2. UUID(범용 고유 식별자를 만들때 사용)
        String uuid = UUID.randomUUID().toString().substring(0,8);
        return timestamp +  "_" + uuid + extension;
    }

    // 파일 확장자만 추출 해주는 메서드
    private String getFileExtension(String originFilename) {
        if(originFilename == null || originFilename.lastIndexOf(".") == -1){
            return ""; // 확장자가 없으면 빈 문자열을 반환
        }
        // 마지막 점(.) 문자 이후 문자열을 확장자로 변환
        // profile.jpg --> latIndexOf(".") --> 7을 반환(확장자전 까지)
        return originFilename.substring(originFilename.lastIndexOf("."));
    }

    // 폴더를 생성하는 메서드
    private void createUploadDirectory() throws IOException{
        Path uploadPath = Paths.get(uploadDir);
        System.out.println("업로드 디렉토리 경로: " + uploadPath.toAbsolutePath());

        // 디렉토리가 존재하지 않으면 생성
        // C:/uploads/profiles/ 경로에 파일이 없으면
        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
    }

    public void deleteProfileImage(String imagePath){
        if(imagePath != null && imagePath.isEmpty() == false){
            try{
                // uploads/profiles/123123123.jpg
                // 1.단계 전체 경로에서 파일명만 추출
                String fileName = imagePath.substring(imagePath.lastIndexOf("/")+1);
                
                // 2.단계 실제 파일 시스템 경로 생성 
                Path filePath = Paths.get(uploadDir,fileName);
                
                // 3.단계 : 파일이 존재하면 삭제 , 없으면 아무것도 안함
                Files.deleteIfExists(filePath);
                
            }catch (IOException e){
                throw new RuntimeException("프로필 이미지를 삭제하지 못했습니다");
            }
        }
    }
}
