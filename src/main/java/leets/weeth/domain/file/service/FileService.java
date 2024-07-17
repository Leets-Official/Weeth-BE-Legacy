package leets.weeth.domain.file.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import leets.weeth.domain.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import leets.weeth.domain.file.entity.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3 s3Client;

    public String uploadFile(MultipartFile file) {

        if(file == null || file.isEmpty())
            return "";
        java.io.File fileObj = convertMultiPartFileToFile(file);
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String fileName = UUID.randomUUID() + "." + extension;

        log.info("uploadFile fileName: {}", fileName);
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        fileObj.delete();
        return s3Client.getUrl(bucketName, fileName).toString();
    }

    public List<File> uploadFiles(List<MultipartFile> files) {
        // 다중 업로드 && 리스트 ","을 기준으로 하나의 문자열 반환
        // files 갯수 0 이면 반환 ""
        if(files == null || files.isEmpty())
            return List.of();

        List<File> results = new ArrayList<>();

        for (MultipartFile file : files) {
            java.io.File fileObj = convertMultiPartFileToFile(file);
            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String fileName = UUID.randomUUID() + "." + extension;

            log.info("uploadFile fileName: {}", fileName);
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
            fileObj.delete();
            File newFile = new File(s3Client.getUrl(bucketName, fileName).toString());


            fileRepository.save(newFile);
            results.add(newFile);
        }
        return results;
    }

    private java.io.File convertMultiPartFileToFile(MultipartFile file) {
        java.io.File convertedFile = new java.io.File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }

    private static String getFileExtension(String originalFileName) {
        return originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
    }

}