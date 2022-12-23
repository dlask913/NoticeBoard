package com.example.noticeboardservice.entity;

import com.example.noticeboardservice.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Getter @Setter @ToString
@Entity
@Table(name = "member")
public class Member extends BaseEntity{
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String email;

    private String userName;

    // 길이제한 했다가 encoder 값이 길어서 <org.springframework.dao.DataIntegrityViolationException> 에러남
    private String password;

    private String info;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setUserName(memberFormDto.getUserName());
        member.setEmail(memberFormDto.getEmail());
        String pwd = passwordEncoder.encode(memberFormDto.getPassword());
        member.setInfo(memberFormDto.getInfo());
        member.setPassword(pwd);
        member.setRole(Role.USER);
        return member;
    }

    public void updateMember(MemberFormDto memberFormDto) {
        this.setInfo(memberFormDto.getInfo());
    }
}

