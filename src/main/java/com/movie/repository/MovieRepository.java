package com.movie.repository;

import com.movie.model.Movie;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM MOVIE WHERE UPPER(NAME) = UPPER (?1) ")
    List<Movie> findMovieDetails(String name);

    @Query(nativeQuery = true, value = "SELECT * FROM MOVIE WHERE release_date > NOW(); ")
    List<Movie> findUpcomingMovies();

    @Query(nativeQuery = true, value = "SELECT * FROM MOVIE WHERE release_date <= NOW(); ")
    default List<Movie> findReleasedMovies() {
        return null;
    }

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO MOVIE VALUES (DEFAULT,?1,?2,?3,?4)")
    void addNewMovies(String moviename, String description, int rating, Date date1);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM MOVIE WHERE UPPER(NAME)=UPPER(?1) ")
    void deleteMoviesByName(String moviename);
}
