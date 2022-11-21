package com.example.noticeboardservice.controller;

import com.example.noticeboardservice.dto.MemberDto;
import com.example.noticeboardservice.entity.Member;
import com.example.noticeboardservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String memberForm(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "members/memberForm";
    }

    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberDto memberDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "members/memberForm";
        }
        try{
            Member member = Member.createMember(memberDto,passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "members/memberForm";
        }
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
