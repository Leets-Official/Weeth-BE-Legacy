package leets.weeth.domain.post.repository;

import leets.weeth.domain.post.entity.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface PostRepository extends CrudRepository<Post, Long> {
    @Override
    ArrayList<Post> findAll();
}
