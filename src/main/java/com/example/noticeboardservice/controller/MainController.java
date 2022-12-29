package com.example.noticeboardservice.controller;

import com.example.noticeboardservice.dto.MemberFormDto;
import com.example.noticeboardservice.entity.Comment;
import com.example.noticeboardservice.entity.Member;
import com.example.noticeboardservice.entity.Notice;
import com.example.noticeboardservice.service.CommentService;
import com.example.noticeboardservice.service.MemberService;
import com.example.noticeboardservice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;
    private final NoticeService noticeService;
    private final CommentService commentService;
    @GetMapping(value = "/")
    public String main(Principal principal,Model model){
        try {
            List<Notice> notices = noticeService.findAll();
            List<Notice> resNotice = new ArrayList<>();
            List<Comment> comments = commentService.findAll();
            List<Comment> resComment = new ArrayList<>();

            for (Notice notice : notices) {
                if (notice.getMember().getEmail().equals(principal.getName())) {
                    resNotice.add(notice);
                }
            }

            for (Comment comment : comments) {
                if (comment.getMember().getEmail().equals(principal.getName())) {
                    resComment.add(comment);
                }
            }

            Member member = memberService.findByEmail(principal.getName());
            MemberFormDto memberFormDto = memberService.getMemberDtl(member.getId());

            model.addAttribute("notice", resNotice);
            model.addAttribute("comment", resComment);
            model.addAttribute("member", memberFormDto);

            return "members/memberPage";
        } catch (NullPointerException e) {
            return "members/memberLoginForm";
        }
    }
}
