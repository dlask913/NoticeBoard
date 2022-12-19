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
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ApiExamDatalabTrendShopping apiExamDatalabTrendShopping;
    @GetMapping(value = "/")
    public String main(){
        return "main";
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
