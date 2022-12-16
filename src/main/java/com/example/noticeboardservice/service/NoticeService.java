package com.example.noticeboardservice.service;

import com.example.noticeboardservice.dto.NoticeSearchDto;
import com.example.noticeboardservice.entity.Notice;
import com.example.noticeboardservice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public Notice saveNotice(Notice notice){
        return noticeRepository.save(notice);
    }

    public List<Notice> findAll() {
        return noticeRepository.findAll();
    }

    public Notice findByNoticeId(Long userId){
        List<Notice> noticeList = noticeRepository.findAll();
        for (Notice notice:noticeList) {
            if (notice.getId() == userId) {
                return notice;
            }
        }
        return null;
    }

    public void deleteNotice(Long id){
        List<Notice> noticeList = noticeRepository.findAll();
        for (Notice notice:noticeList) {
            if (notice.getId().equals(id)) {
                noticeRepository.delete(notice);
            }
        }
    }

    public void updateNotice(Long id, Notice updateNotice) {
        Notice notice = findByNoticeId(id);
        notice.setTitle(updateNotice.getTitle());
        notice.setContent(updateNotice.getContent());
    }

    @Transactional(readOnly = true)
    public Page<Notice> getAdminItemPage(NoticeSearchDto noticeSearchDto, Pageable pageable) {
        return noticeRepository.getAdminItemPage(noticeSearchDto, pageable);
    }
}
