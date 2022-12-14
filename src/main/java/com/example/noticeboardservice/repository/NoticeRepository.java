package com.example.noticeboardservice.repository;

import com.example.noticeboardservice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long>, QuerydslPredicateExecutor<Notice>, NoticeRepositoryCustom {

    List<Notice> findAll();
    List<Notice> findByUserName(String userName);
}
