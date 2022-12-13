package com.example.noticeboardservice.controller;

import com.example.noticeboardservice.dto.NoticeDto;
import com.example.noticeboardservice.entity.Member;
import com.example.noticeboardservice.entity.Notice;
import com.example.noticeboardservice.service.MemberService;
import com.example.noticeboardservice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(value = "/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final MemberService memberService;

    @GetMapping(value = "/{noticeId}/details")
    public String noticeDetails(@PathVariable("noticeId") Long noticeId, Model model) {
        Notice notice = noticeService.findByNoticeId(noticeId);
        model.addAttribute("notice", notice);
        return "notices/noticeDetails";
    }

    @GetMapping(value = "/all")
    public String noticesList(Model model) {
        List<Notice> noticeList = noticeService.findAll();
        model.addAttribute("noticeList", noticeList);
        return "notices/noticeList";
    }

    @GetMapping(value = "/new")
    public String noticeForm(Model model) {
        model.addAttribute("noticeDto", new NoticeDto());
        return "notices/noticeForm";
    }

    @PostMapping(value = "/new")
    public String noticeCreate(NoticeDto noticeDto, Principal principal) {
        System.out.println(principal.getName());
        Member member = memberService.findByEmail(principal.getName());
        Notice notice = Notice.createNotice(noticeDto,member);
        noticeService.saveNotice(notice);
        return "redirect:/notices/all";
    }

    @GetMapping(value = "/remove/{id}")
    public String noticeDelete(@PathVariable("id") Long id) {
        System.out.println("GET>>"+id);
        noticeService.deleteNotice(id);
        return "redirect:/notices/all";
    }

    @GetMapping(value = "/{id}")
    public String noticeUpdate(@PathVariable("id") Long id, Model model) {
        Notice notice = noticeService.findByNoticeId(id);
        model.addAttribute("noticeDto", notice);
        return "notices/noticeForm";
    }

    @PostMapping(value = "/{id}")
    public String noticeUpdate(@PathVariable("id") Long id, Notice notice) {
        noticeService.updateNotice(id,notice);
        return "redirect:/notices/all";
    }
}
