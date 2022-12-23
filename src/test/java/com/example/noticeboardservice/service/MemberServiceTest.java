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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;

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

    /** 프로필 이미지 추가한 후 **/
    MultipartFile createMultipartFiles() throws Exception {
        String path = "C:/spring_study/image/profile/";
        String imageName = "image.jpg";
        MockMultipartFile multipartFile = new MockMultipartFile(path, imageName,
                "image/jpg", new byte[]{1, 2, 3, 4});
        return multipartFile;
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest() throws Exception{
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test1234@test.com");
        memberFormDto.setUserName("홍길동");
        memberFormDto.setPassword("1234");
        memberFormDto.setInfo("information");
        MultipartFile multipartFile = createMultipartFiles();
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        Long memberId = memberService.saveMember(member, multipartFile);

        Member savedMember = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        System.out.println("savedMember: "+savedMember);
        System.out.println("member: "+member);
        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getUserName(), savedMember.getUserName());
        assertEquals(member.getPassword(), savedMember.getPassword());
        assertEquals(member.getInfo(), savedMember.getInfo());
    }
    // ddl-auto: validation 으로 해서 missing table 에러 발생

    /** 프로필 이미지 추가하기 전 **/
//    @Test
//    @DisplayName("회원가입 테스트")
//    public void saveMemberTest() throws Exception{
//        Member member = createMember();
//        Member savedMember = memberService.saveMember(member);
//
//        assertEquals(member.getEmail(),savedMember.getEmail());
//        assertEquals(member.getUserName(),savedMember.getUserName());
//        assertEquals(member.getPassword(),savedMember.getPassword());
//        assertEquals(member.getRole(),savedMember.getRole());
//    }

//    @Test
//    @DisplayName("중복가입 테스트")
//    public void saveDuplicateMemberTest(){
//        Member member1 = createMember();
//        Member member2 = createMember();
//        memberService.saveMember(member1);
//        Throwable e = assertThrows(IllegalStateException.class, () -> {
//            memberService.saveMember(member2);});
//
//        assertEquals("이미 가입된 회원입니다",e.getMessage());
//    }

}