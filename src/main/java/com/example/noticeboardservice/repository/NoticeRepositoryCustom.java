package com.example.noticeboardservice.repository;

import com.example.noticeboardservice.dto.NoticeSearchDto;
import com.example.noticeboardservice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {
    Page<Notice> getAdminItemPage(NoticeSearchDto noticeSearchDto, Pageable pageable);
}
