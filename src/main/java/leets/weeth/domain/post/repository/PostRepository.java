package leets.weeth.domain.post.repository;

import leets.weeth.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.ArrayList;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    ArrayList<Post> findAll();
    List<Post> findByUserId(Long userId, Sort sort);

    @Query("SELECT MAX(p.id) FROM Post p")
    Long findMaxPostId();

    @Query("SELECT p FROM Post p WHERE p.id < :maxPostId ORDER BY p.id DESC")
    List<Post> findRecentPosts(@Param("maxPostId") Long maxPostId, Pageable pageable);

}
