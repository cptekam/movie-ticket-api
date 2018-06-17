package com.movie.repository;

import com.movie.model.Users;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Component
public interface UserEntityRepository extends CrudRepository<Users, Integer>  {

    @Query(nativeQuery = true,value="SELECT * FROM USERENTITY WHERE UPPER(USERNAME) = UPPER (?1) ")
    String findUserExist(String user1);

    @Query(nativeQuery = true,value="SELECT PASSWORD FROM USERENTITY WHERE UPPER(USERNAME)= UPPER(?1)")
    String findUserPassword(String user1);

    @Query(nativeQuery = true,value="SELECT username FROM USERENTITY  ")
    List<String> findAllUsers();

    @Query(nativeQuery = true,value="SELECT USERID FROM USERENTITY WHERE UPPER(USERNAME)= UPPER(?1)")
    int findUserIdByName(String user1);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value="INSERT INTO USERENTITY (USERNAME,PASSWORD) VALUES (?1,?2)")
    void addUserCredentials(String uname, String password);
}
