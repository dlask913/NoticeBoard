package com.example.noticeboardservice.service;

import com.example.noticeboardservice.dto.MemberFormDto;
import com.example.noticeboardservice.dto.MemberImgDto;
import com.example.noticeboardservice.entity.Member;
import com.example.noticeboardservice.entity.MemberImg;
import com.example.noticeboardservice.entity.Notice;
import com.example.noticeboardservice.repository.MemberImgRepository;
import com.example.noticeboardservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final MemberImgService memberImgService;
    private final MemberImgRepository memberImgRepository;

    public Member findByEmail(String email) {
        Member findMember = memberRepository.findByEmail(email);
        return findMember;
    }

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    public Long saveMember(Member member, MultipartFile memberImgFile) throws Exception {
        validateDuplicateMember(member);
        memberRepository.save(member);

        MemberImg memberImg = new MemberImg();
        memberImg.setMember(member);
        memberImgService.saveMemberImg(memberImg,memberImgFile);
        return member.getId();
    }

    @Transactional(readOnly = true)
    public MemberFormDto getMemberDtl(Long memberId){
        MemberImg memberImg = memberImgRepository.findByMemberId(memberId);
        MemberImgDto memberImgDto = MemberImgDto.of(memberImg);

        Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        MemberFormDto memberFormDto = MemberFormDto.of(member);
        memberFormDto.setMemberImgDto(memberImgDto);
        return memberFormDto;
    }

    public Long updateMember(Long id, MemberFormDto memberFormDto, MultipartFile memberImgFile) throws Exception {
        Member member = memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        member.updateMember(memberFormDto);

        Long memberImgId = memberFormDto.getMemberImgId();
        memberImgService.updateMemberImg(memberImgId,memberImgFile);
        return member.getId();
    }

    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }

}
