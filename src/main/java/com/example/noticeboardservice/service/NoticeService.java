package com.example.noticeboardservice.service;

import com.example.noticeboardservice.entity.Notice;
import com.example.noticeboardservice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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

}
