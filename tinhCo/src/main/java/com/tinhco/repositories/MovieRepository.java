package com.tinhco.repositories;

import com.tinhco.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository  extends JpaRepository<Movie, Integer> {
}
