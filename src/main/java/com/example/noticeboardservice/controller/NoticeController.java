package com.example.noticeboardservice.controller;

import com.example.noticeboardservice.dto.CommentDto;
import com.example.noticeboardservice.dto.MemberFormDto;
import com.example.noticeboardservice.dto.NoticeDto;
import com.example.noticeboardservice.dto.NoticeSearchDto;
import com.example.noticeboardservice.entity.Comment;
import com.example.noticeboardservice.entity.Member;
import com.example.noticeboardservice.entity.Notice;
import com.example.noticeboardservice.service.CommentService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final MemberService memberService;
    private final CommentService commentService;

    @GetMapping(value = "/new")
    public String noticeForm(Model model) {
        model.addAttribute("noticeDto", new NoticeDto());
        return "notices/noticeForm";
    }


    // BindingResult나 Errors는 바인딩 받는 객체 바로 다음에 선언
    @PostMapping(value = "/new")
    public String noticeCreate(@Valid NoticeDto noticeDto,BindingResult bindingResult,Model model,
                               Principal principal) {
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

    @GetMapping(value = "/{noticeId}/details")
    public String noticeDetails(@PathVariable("noticeId") Long noticeId, Model model, Principal principal) {
        Notice notice = noticeService.findById(noticeId);
        Member member = memberService.findByEmail(principal.getName());
        MemberFormDto memberFormDto = memberService.getMemberDtl(member.getId());
        String userId = notice.getMember().getEmail();
        model.addAttribute("notice", notice);
        model.addAttribute("member", memberFormDto);
        model.addAttribute("userId", userId);
        model.addAttribute("commentDto", new CommentDto());
        model.addAttribute("updateDto", new CommentDto());

        List<Comment> commentList = commentService.findAll();
        List<Comment> res = new ArrayList<>();
        for (Comment comment :
                commentList) {
            if (comment.getNotice().getId().equals(noticeId)) {
                res.add(comment);
            }
        }
        model.addAttribute("commentList", res);
        return "notices/noticeDetails";
    }

    @GetMapping(value = "/{id}")
    public String noticeUpdate(@PathVariable("id") Long id, Model model) {
        Notice notice = noticeService.findById(id);
        model.addAttribute("noticeDto", notice);
        return "notices/noticeForm";
    }

    @PostMapping(value = "/{id}")
    public String noticeUpdate(@PathVariable("id") Long id, Notice notice) {
        noticeService.updateNotice(id,notice);
        return "redirect:/notices/all";
    }

    @GetMapping(value = "/remove/{id}")
    public String noticeDelete(@PathVariable("id") Long id) {
        noticeService.deleteNotice(id);
        return "redirect:/notices/all";
    }

    @GetMapping(value = {"/all","/all/{page}"})
    public String noticesList(Model model, @PathVariable("page")Optional<Integer> page, NoticeSearchDto noticeSearchDto) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,9);
        Page<Notice> notices = noticeService.getAdminItemPage(noticeSearchDto, pageable);

        model.addAttribute("noticeList", notices);
        model.addAttribute("noticeSearchDto", noticeSearchDto);
        model.addAttribute("maxPage", 5);
        return "notices/noticeList";
    }

    @PostMapping(value = "/{noticeId}/details")
    public String commentNew(@PathVariable("noticeId") Long noticeId, Model model,
                             CommentDto commentDto, Principal principal){

        Member member = memberService.findByEmail(principal.getName());
        commentService.saveComment(commentDto,noticeId,member.getId());

        return "redirect:/notices/{noticeId}/details";
    }
}
