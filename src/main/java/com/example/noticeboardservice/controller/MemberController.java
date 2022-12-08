package com.example.noticeboardservice.controller;

import com.example.noticeboardservice.dto.MemberFormDto;
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
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "members/memberForm";
    }

    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "members/memberForm";
        }
        try{
            Member member = Member.createMember(memberFormDto,passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "members/memberForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember(Model model) {
//        model.addAttribute("memberFormDto", new MemberFormDto());
        return "members/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "members/memberLoginForm";
    }

//    @PostMapping(value = "/login")
//    public String loginMember(MemberFormDto memberFormDto, Model model) {
//        Member member = memberService.findByEmail(memberFormDto.getEmail());
//        if (member == null){
//            model.addAttribute("message", "없는 회원입니다.");
//            return "members/memberLoginError";
//        } else if (!member.getPw().equals(memberFormDto.getPw())) {
//            model.addAttribute("message", "잘못된 비밀번호입니다.");
//            return "members/memberLoginError";
//        }
//        return "redirect:/";
//    }

}
