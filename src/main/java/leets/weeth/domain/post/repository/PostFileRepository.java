package leets.weeth.domain.post.repository;

import leets.weeth.domain.post.entity.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostFileRepository extends JpaRepository<PostFile, Long> {
}
