package com.example.noticeboardservice.controller;

import com.example.noticeboardservice.dto.NoticeDto;
import com.example.noticeboardservice.entity.Notice;
import com.example.noticeboardservice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping(value = "/{noticeId}/details")
    public String NoticeDetails(@PathVariable("noticeId") Long noticeId, Model model) {
        Notice notice = noticeService.findByNoticeId(noticeId);

        NoticeDto noticeDto = new NoticeDto();
        noticeDto.setContent(notice.getContent());
        noticeDto.setUserName(notice.getUserName());
        noticeDto.setTitle(notice.getTitle());
        noticeDto.setPostDate(notice.getPostDate());

        model.addAttribute("noticeDto", noticeDto);
        return "notices/noticeDetails";
    }

    @GetMapping(value = "/all")
    public String NoticeList(Model model) {
        List<Notice> noticeDtoList = noticeService.findAll();
        model.addAttribute("noticeDtoList", noticeDtoList);
        return "notices/noticeList";
    }

    @GetMapping(value = "/new")
    public String noticeForm(Model model) {
        model.addAttribute("noticeDto", new NoticeDto());
        return "notices/createNoticeForm";
    }

    @PostMapping(value = "/new")
    public String noticeForm(NoticeDto noticeDto) {
        Notice notice = Notice.createNotice(noticeDto);
        noticeService.saveNotice(notice);
        return "redirect:/notices/all";
    }
}
