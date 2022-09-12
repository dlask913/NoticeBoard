package com.example.noticeboardservice.controller;

import com.example.noticeboardservice.dto.MemberDto;
import com.example.noticeboardservice.entity.Member;
import com.example.noticeboardservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping(value = "/new")
    public String memberForm(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "members/memberForm";
    }

    @PostMapping(value = "/new")
    public String memberForm(MemberDto memberDto){
        Member member = Member.createMember(memberDto);
        memberService.saveMember(member);
        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "members/memberLoginForm";
    }

    @PostMapping(value = "/login")
    public String loginMember(MemberDto memberDto,Model model) {
        Member member = memberService.findByEmail(memberDto.getEmail());
        if (member == null){
            model.addAttribute("message", "없는 회원입니다.");
            return "members/memberLoginError";
        } else if (!member.getPw().equals(memberDto.getPw())) {
            model.addAttribute("message", "잘못된 비밀번호입니다.");
            return "members/memberLoginError";
        }
        return "redirect:/";
    }

}
