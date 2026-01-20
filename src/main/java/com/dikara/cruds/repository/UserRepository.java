package com.dikara.cruds.repository;

import com.dikara.cruds.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT b FROM User b WHERE b.userStatus = 'ACTIVE' ORDER BY b.createdDate DESC")
    Page<User> findByIsDeletedFalseOrderByCreatedDateDesc(Pageable pageable);
}
