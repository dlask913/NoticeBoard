package com.example.noticeboardservice.controller;

import com.example.noticeboardservice.dto.CommentDto;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final MemberService memberService;
    private final NoticeService noticeService;

    @GetMapping("/{noticeId}/remove/{id}")
    public String removeContent(@PathVariable("id") Long commentId){
        commentService.removeContent(commentId);
        return "redirect:/notices/{noticeId}/details";
    }

    @GetMapping("/{noticeId}/{id}")
    public String updateContent(@PathVariable("id") Long commentId, @PathVariable("noticeId") Long noticeId,
                                Model model) {
        Comment comment = commentService.findById(commentId);
        Notice notice = noticeService.findByNoticeId(noticeId);
        Member member = notice.getMember();
        MemberFormDto memberFormDto = memberService.getMemberDtl(member.getId());
        String userId = notice.getMember().getEmail();
        model.addAttribute("notice", notice);
        model.addAttribute("member", memberFormDto);
        model.addAttribute("userId", userId);

        List<Comment> commentList = commentService.findAll();
        model.addAttribute("commentList", commentList);
        model.addAttribute("commentDto", new CommentDto());
        model.addAttribute("updateDto",comment);
        return "notices/noticeDetails";
    }

    @PostMapping("/{noticeId}/{id}")
    public String noticeUpdate(@PathVariable("id") Long id, Comment comment) {
        commentService.updateContent(id,comment);
        return "redirect:/notices/{noticeId}/details";
    }

}
