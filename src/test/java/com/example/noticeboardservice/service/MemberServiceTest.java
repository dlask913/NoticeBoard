package com.example.noticeboardservice.service;

import com.example.noticeboardservice.dto.MemberFormDto;
import com.example.noticeboardservice.entity.Member;
import com.example.noticeboardservice.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MemberRepository memberRepository;

    MultipartFile createMultipartFiles() throws Exception {
        String path = "C:/spring_study/image/profile/";
        String imageName = "image.jpg";
        MockMultipartFile multipartFile = new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1, 2, 3, 4});
        return multipartFile;
    }


    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test1234@test.com");
        memberFormDto.setUserName("홍길동");
        memberFormDto.setPassword("asdf1234");
        return Member.createMember(memberFormDto,passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest() throws Exception{
        Member member = createMember();
        Member savedMember = memberService.saveMember(member);

        assertEquals(member.getEmail(),savedMember.getEmail());
        assertEquals(member.getUserName(),savedMember.getUserName());
        assertEquals(member.getPassword(),savedMember.getPassword());
        assertEquals(member.getRole(),savedMember.getRole());
    }

    @Test
    @DisplayName("중복가입 테스트")
    public void saveDuplicateMemberTest(){
        Member member1 = createMember();
        Member member2 = createMember();
        memberService.saveMember(member1);
        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member2);});

        assertEquals("이미 가입된 회원입니다",e.getMessage());
    }

}