package com.example.noticeboardservice.controller;

import com.example.noticeboardservice.dto.NoticeDto;
import com.example.noticeboardservice.entity.Notice;
import com.example.noticeboardservice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;


    @GetMapping(value = "/details")
    public String NoticeDetails(Model model) {
        NoticeDto noticeDto = new NoticeDto();
        noticeDto.setContent("게시글 내용");
        noticeDto.setUserName("작성자");
        noticeDto.setTitle("게시글 제목");
        noticeDto.setPostDate(new Date());

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
