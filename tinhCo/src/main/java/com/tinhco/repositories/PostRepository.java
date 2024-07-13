package com.tinhco.repositories;

import com.tinhco.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
    boolean existsByTitleAndSlug(String title, String slug);
}
