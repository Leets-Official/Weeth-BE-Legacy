package leets.weeth.domain.post.repository;

import leets.weeth.domain.post.entity.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    ArrayList<Post> findAll();
    List<Post> findByUserId(Long userId, Sort sort);

    @Query("SELECT MAX(p.id) FROM Post p")
    Long findMaxPostId();

    @Query("SELECT COUNT(p) FROM Post p")
    int countTotalPosts();
}
