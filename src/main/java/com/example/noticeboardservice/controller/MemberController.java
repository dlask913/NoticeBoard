package com.example.noticeboardservice.controller;

import com.example.noticeboardservice.dto.MemberFormDto;
import com.example.noticeboardservice.dto.MemberImgDto;
import com.example.noticeboardservice.entity.Comment;
import com.example.noticeboardservice.entity.Member;
import com.example.noticeboardservice.entity.Notice;
import com.example.noticeboardservice.service.CommentService;
import com.example.noticeboardservice.service.MemberService;
import com.example.noticeboardservice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model,
                            @RequestParam("memberImgFile") MultipartFile memberImgFile){
        if (bindingResult.hasErrors()) {
            return "members/memberForm";
        }
        if (memberImgFile.isEmpty() && memberFormDto.getId() == null) {
            model.addAttribute("errorMessage", "이미지를 입력해주세요");
            return "members/memberForm";
        }

        try{
            Member member = Member.createMember(memberFormDto,passwordEncoder);
            memberService.saveMember(member,memberImgFile);
        } catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "members/memberForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember() {
        return "members/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "members/memberLoginForm";
    }

    @GetMapping(value = "/{email}")
    public String memberUpdate(@PathVariable("email") String id, Model model) {
        Member member = memberService.findByEmail(id);
        MemberFormDto memberFormDto = memberService.getMemberDtl(member.getId());
        model.addAttribute("memberFormDto", memberFormDto);
        return "members/memberForm";
    }

    @PostMapping(value = "/{email}")
    public String memberUpdate(@Valid MemberFormDto memberFormDto, BindingResult bindingResult,
                               @PathVariable("email") String id,
                               @RequestParam("memberImgFile") MultipartFile memberImgFile, Model model) {

        Member member = memberService.findByEmail(id);
        if (bindingResult.hasErrors() || !passwordEncoder.matches(memberFormDto.getPassword(), member.getPassword())) {
            memberFormDto = memberService.getMemberDtl(member.getId());
            model.addAttribute("loginErrorMsg", "비밀번호를 확인해주세요.");
            model.addAttribute("memberFormDto", memberFormDto);
            return "members/memberForm";
        }

        if (memberImgFile.isEmpty() && memberFormDto.getId() == null) {
            model.addAttribute("errorMessage", "프로필 이미지를 등록해주세요.");
        }

        try {
            memberService.updateMember(member.getId(),memberFormDto,memberImgFile);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "프로필 수정 중 에러 발생");
            return "member/memberForm";
        }
        return "redirect:/";
    }

    @PostMapping(value = "/remove/{email}")
    public String memberUpdate(@PathVariable("email") String id, @Valid MemberFormDto memberFormDto,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/memberForm";
        }
        Member member = memberService.findByEmail(id);
        memberService.deleteMember(member);
        return "redirect:/";
    }
}
