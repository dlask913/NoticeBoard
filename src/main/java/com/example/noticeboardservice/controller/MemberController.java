package com.example.noticeboardservice.controller;

import com.example.noticeboardservice.dto.MemberFormDto;
import com.example.noticeboardservice.entity.Member;
import com.example.noticeboardservice.entity.Notice;
import com.example.noticeboardservice.service.MemberService;
import com.example.noticeboardservice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final NoticeService noticeService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "members/memberForm";
    }

    @PostMapping(value = "/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
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
        return "members/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "members/memberLoginForm";
    }

    @GetMapping(value = "/mypage")
    public String myPage(Model model, Principal principal){
        List<Notice> notices = noticeService.findAll();
        List<Notice> res = new ArrayList<>();
        for (Notice notice :
                notices) {
            if (notice.getMember().getEmail().equals(principal.getName())){
                res.add(notice);
            }
        }

        Member member = memberService.findByEmail(principal.getName());

        model.addAttribute("notice",res);
        model.addAttribute("member",member);

        return "members/memberPage";
    }

    @GetMapping(value = "/{id}")
    public String memberUpdate(@PathVariable("id") String id, Model model) {
        Member member = memberService.findByEmail(id);
        model.addAttribute("memberFormDto", member);
        return "members/memberForm";
    }

    @PostMapping(value = "/{email}")
    public String memberUpdate(@PathVariable("email") String id, Member member) {
        memberService.updateMember(id,member);
        return "redirect:/members/mypage";
    }

}
