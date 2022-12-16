package com.example.noticeboardservice.controller;

import com.example.noticeboardservice.dto.NoticeDto;
import com.example.noticeboardservice.dto.NoticeSearchDto;
import com.example.noticeboardservice.entity.Member;
import com.example.noticeboardservice.entity.Notice;
import com.example.noticeboardservice.service.MemberService;
import com.example.noticeboardservice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
        String userId = notice.getMember().getEmail();
        model.addAttribute("userId", userId);
        return "notices/noticeDetails";
    }

    @GetMapping(value = {"/all","/all/{page}"})
    public String noticesList(Model model, @PathVariable("page")Optional<Integer> page, NoticeSearchDto noticeSearchDto) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,3);
        Page<Notice> notices = noticeService.getAdminItemPage(noticeSearchDto, pageable);

//        List<Notice> noticeList = noticeService.findAll();
        model.addAttribute("noticeList", notices);
        model.addAttribute("noticeSearchDto", noticeSearchDto);
        model.addAttribute("maxPage", 5);
        return "notices/noticeList";
    }

    @GetMapping(value = "/new")
    public String noticeForm(Model model) {
        model.addAttribute("noticeDto", new NoticeDto());
        return "notices/noticeForm";
    }


    // BindingResult나 Errors는 바인딩 받는 객체 바로 다음에 선언
    @PostMapping(value = "/new")
    public String noticeCreate(@Valid NoticeDto noticeDto,BindingResult bindingResult,Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "notices/noticeForm";
        }
        try{
            Member member = memberService.findByEmail(principal.getName());
            Notice notice = Notice.createNotice(noticeDto,member);
            notice.setUserName(member.getUserName());
            noticeService.saveNotice(notice);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "notices/noticeForm";
        }
        return "redirect:/notices/all";
    }

    @GetMapping(value = "/remove/{id}")
    public String noticeDelete(@PathVariable("id") Long id) {
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
