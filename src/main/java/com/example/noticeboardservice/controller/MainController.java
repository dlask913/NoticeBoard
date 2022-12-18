package com.example.noticeboardservice.controller;

import com.example.noticeboardservice.api.ApiExamDatalabTrendShopping;
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

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ApiExamDatalabTrendShopping apiExamDatalabTrendShopping;
//    @GetMapping(value = "/")
//    public String main(){
//        return "main";
//    }

    @GetMapping(value = "/")
    String getShoppingApi(Model model) throws JSONException, ParseException {
        JSONArray res = apiExamDatalabTrendShopping.getShoppingApi();
        List<JSONObject> result = new ArrayList<>();
        for (int i = 0; i < res.length(); i++) {
            JSONObject jsonObject = (JSONObject) res.get(i);
        }
        model.addAttribute("shopApi",res);
        System.out.println(">>>>>");
        System.out.println(result);
        return "main";
    }
}
