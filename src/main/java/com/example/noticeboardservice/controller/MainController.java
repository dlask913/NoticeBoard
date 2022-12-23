package com.example.noticeboardservice.controller;

import com.example.noticeboardservice.api.ApiExamDatalabTrendShopping;
import com.example.noticeboardservice.dto.MemberFormDto;
import com.example.noticeboardservice.entity.Member;
import com.example.noticeboardservice.entity.Notice;
import com.example.noticeboardservice.service.MemberService;
import com.example.noticeboardservice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import net.minidev.json.parser.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ApiExamDatalabTrendShopping apiExamDatalabTrendShopping;
    private final MemberService memberService;
    private final NoticeService noticeService;
    @GetMapping(value = "/")
    public String main(Principal principal,Model model){
        try {
            Member member = memberService.findByEmail(principal.getName());
            List<Notice> notices = noticeService.findAll();
            List<Notice> res = new ArrayList<>();
            for (Notice notice :
                    notices) {
                if (notice.getMember().getEmail().equals(principal.getName())){
                    res.add(notice);
                }
            }
            MemberFormDto memberFormDto = memberService.getMemberDtl(member.getId());

            model.addAttribute("notice",res);
            model.addAttribute("member", memberFormDto);
            return "members/memberPage";
        } catch (NullPointerException e) {
            return "members/memberLoginForm";
        }
    }

    @RequestMapping(value = "/shop")
    @ResponseBody
    public String getShoppingApi() throws JSONException, ParseException {
        String res = apiExamDatalabTrendShopping.getShoppingApi();

        System.out.println(">>>>>result");
        System.out.println(res);
        return res;
    }
}
