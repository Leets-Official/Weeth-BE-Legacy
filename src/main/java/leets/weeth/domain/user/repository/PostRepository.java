package leets.weeth.domain.user.repository;

import leets.weeth.domain.user.entity.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface PostRepository extends CrudRepository<Post, Long> {
    @Override
    ArrayList<Post> findAll();
}
