package leets.weeth.domain.post.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PostFileUploadDTO {
    private List<MultipartFile> files;
}
