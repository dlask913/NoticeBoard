package com.example.noticeboardservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class MemberDto {
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String userName;
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String pw;
}
