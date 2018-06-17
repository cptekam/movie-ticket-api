package com.movie.repository;

import com.movie.model.Cart;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface CartRepository extends CrudRepository<Cart, Integer> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO Cart values (DEFAULT,?1,?2,?3) ")
    void addCartDetails(int userId, int showId, int quantity);

    @Query(nativeQuery = true, value = "SELECT  * FROM Cart WHERE USERID = ?1 ")
    String findUseridExist(String user1);

    @Query(nativeQuery = true, value = "SELECT * FROM Cart WHERE USERID=?1 ")
    List<Cart> findCartDetailsViaUserId(int userId);

    @Query(nativeQuery = true, value = "SELECT numberoftickets FROM Cart WHERE USERID=?1 ")
    int findQuantityViaUserId(int userId);

    @Query(nativeQuery = true, value = "SELECT showid FROM Cart WHERE USERID=?1 ")
    int findShowIdViaUserId(int userId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM Cart WHERE USERID=?1 ")
    void deleteEntryviaCartId(int userId);
}
