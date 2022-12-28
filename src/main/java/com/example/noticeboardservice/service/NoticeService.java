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

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public Notice saveNotice(Notice notice){
        return noticeRepository.save(notice);
    }

    public Notice findById(Long id){
        return noticeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Notice> findAll() {
        return noticeRepository.findAll();
    }

    public void updateNotice(Long id, Notice updateNotice) {
        Notice notice = noticeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        notice.setTitle(updateNotice.getTitle());
        notice.setContent(updateNotice.getContent());
    }

    public void deleteNotice(Long id){
        Notice notice = noticeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        noticeRepository.delete(notice);
    }

    @Transactional(readOnly = true)
    public Page<Notice> getAdminItemPage(NoticeSearchDto noticeSearchDto, Pageable pageable) {
        return noticeRepository.getAdminItemPage(noticeSearchDto, pageable);
    }
}
