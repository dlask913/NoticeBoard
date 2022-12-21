package com.example.noticeboardservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberFormDto {
    private String id;
    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    private String email;
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String userName;
    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
    @NotEmpty(message = "기본 정보를 입력해주세요.")
    private String info;
}
