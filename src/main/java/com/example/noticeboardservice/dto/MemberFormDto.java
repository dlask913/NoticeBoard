package com.example.noticeboardservice.dto;

import com.example.noticeboardservice.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class MemberFormDto {
    private Long id;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    private String email;
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String userName;
    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
    @NotEmpty(message = "기본 정보를 입력해주세요.")
    private String info;

    private MemberImgDto memberImgDto;
    private Long memberImgId;
    private static ModelMapper modelMapper = new ModelMapper();

    public Member createMember(){
        return modelMapper.map(this, Member.class);
    }

    public static MemberFormDto of(Member member) {
        return modelMapper.map(member, MemberFormDto.class);
    }
}
