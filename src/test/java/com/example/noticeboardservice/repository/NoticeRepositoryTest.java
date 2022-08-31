package com.example.noticeboardservice.repository;

import com.example.noticeboardservice.entity.Notice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class NoticeRepositoryTest {
    @Autowired
    NoticeRepository noticeRepository;
    @Test
    @DisplayName("게시글 저장 테스트")
    public void createNoticeTest() {
        Notice notice = new Notice();
        notice.setUserName("nana");
        notice.setTitle("게시글 만들기");
        notice.setContent("게시글 내용");
        notice.setPostDate(new Date());
        Notice savedNotice = noticeRepository.save(notice);
        System.out.println(savedNotice);
    }

    public void createNoticeList(){
        for (int i = 0; i < 10; i++) {
            Notice notice = new Notice();
            notice.setUserName("길동"+i);
            notice.setTitle("게시글 만들기"+i);
            notice.setContent("게시글 내용"+i);
            notice.setPostDate(new Date());
            noticeRepository.save(notice);
        }
    }

    @Test
    @DisplayName("유저명 검색 테스트")
    public void findByUserNameTest() {
        this.createNoticeList();
        List<Notice> noticeList = noticeRepository.findByUserName("길동1");
        for (Notice notice :noticeList) {
            System.out.println(notice.toString());
        }
    }
}