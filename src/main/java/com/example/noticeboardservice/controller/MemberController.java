package com.example.noticeboardservice.controller;

import com.example.noticeboardservice.dto.MemberFormDto;
import com.example.noticeboardservice.dto.MemberImgDto;
import com.example.noticeboardservice.entity.Member;
import com.example.noticeboardservice.entity.Notice;
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
    private final NoticeService noticeService;
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
    public String memberUpdate(@PathVariable("email") String id, @Valid MemberFormDto memberFormDto,
                               BindingResult bindingResult,
                               @RequestParam("memberImgFile") MultipartFile memberImgFile, Model model) {
        if (bindingResult.hasErrors()) {
            return "member/memberForm";
        }
        if (memberImgFile.isEmpty() && memberFormDto.getId() == null) {
            model.addAttribute("errorMessage", "프로필 이미지를 등록해주세요.");
        }

        Member member = memberService.findByEmail(id);
        if (!passwordEncoder.matches(memberFormDto.getPassword(), member.getPassword())) {
            model.addAttribute("loginErrorMsg", "비밀번호를 확인해주세요.");
            model.addAttribute("memberFormDto", member);
            return "members/memberForm";
        }

        try {
            memberService.updateMember(member.getId(),memberFormDto,memberImgFile);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "프로필 수정 중 에러 발생");
            return "member/memberForm";
        }
        return "redirect:/members/mypage";
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
        MemberFormDto memberFormDto = memberService.getMemberDtl(member.getId());

        model.addAttribute("notice",res);
        model.addAttribute("member",memberFormDto);

        return "members/memberPage";
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
