package com.example.noticeboardservice.entity;

import com.example.noticeboardservice.dto.MemberDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter @Setter @ToString
@Entity
@Table(name = "member")
public class Member {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(length = 20)
    private String userName;

    // 길이제한 했다가 encoder 값이 길어서 <org.springframework.dao.DataIntegrityViolationException> 에러남
    @Column(nullable = false)
    private String pw;

//    @Enumerated(EnumType.STRING)
//    private Role role;

    public static Member createMember(MemberDto memberDto){
        Member member = new Member();
        member.setUserName(memberDto.getUserName());
        member.setEmail(memberDto.getEmail());
        member.setPw(memberDto.getPw());
        return member;
    }
}

