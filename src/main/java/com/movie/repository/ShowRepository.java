package com.movie.repository;

import com.movie.model.Show;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Component
@Repository
public interface ShowRepository extends CrudRepository<Show, Integer> {

    @Query(nativeQuery = true, value = "SELECT  * FROM SHOW WHERE UPPER(CITY) = UPPER (?1) ")
    List<Show> findAllShowsByCity(String city1);

    @Query(nativeQuery = true, value = "SELECT DISTINCT CITY FROM SHOW ")
    List<String> findAllCity();

    @Query(nativeQuery = true, value = "SELECT  MOVIENAME FROM SHOW WHERE SHOWID=?1 ")
    String findMovieNameViaShow(int showId);

    @Query(nativeQuery = true, value = "SELECT  * FROM SHOW WHERE SHOWID=?1 AND AVAILABILITY >= ?2 AND date>=NOW()  ")
    String findTicketAvailability(int showId, int quantity);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE SHOW SET AVAILABILITY=(AVAILABILITY-?2) WHERE SHOWID=?1")
    void findBooking(int showId, int quantity);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO SHOW VALUES (DEFAULT,?1,?2,?3,?4,?5)")
    void addNewShows(int availability, String city, String Language, Date date1, int movieid);
}
