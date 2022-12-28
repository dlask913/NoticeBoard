package com.example.noticeboardservice.service;

import com.example.noticeboardservice.entity.MemberImg;
import com.example.noticeboardservice.repository.MemberImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service @Transactional
@RequiredArgsConstructor
public class MemberImgService {

    @Value("${memberImgLocation}")
    private String memberImgLocation;
    private final MemberImgRepository memberImgRepository;
    private final FileService fileService;

    public void saveMemberImg(MemberImg memberImg, MultipartFile memberImgFile) throws Exception {
        String oriImgName = memberImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl ="";

        //파일 업로드
        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(memberImgLocation, oriImgName, memberImgFile.getBytes());
            imgUrl = "/images/profile/"+imgName;
        }

        // 이미지 정보 저장
        memberImg.updateMemberImg(oriImgName,imgName,imgUrl);
        memberImgRepository.save(memberImg);
        memberImg.getMember().setMemberImg(memberImg);
    }

    public void updateMemberImg(Long memberImgId, MultipartFile memberImgFile) throws Exception {
        if (!memberImgFile.isEmpty()) {
            MemberImg savedMemberImg = memberImgRepository.findById(memberImgId)
                    .orElseThrow(EntityNotFoundException::new);
            if (!StringUtils.isEmpty(savedMemberImg.getImgName())) {
                fileService.deleteFile(memberImgLocation+"/"+savedMemberImg.getImgName());
            }
            String oriImgName = memberImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(memberImgLocation, oriImgName, memberImgFile.getBytes());
            String imgUrl = "/images/profile/"+imgName;
            savedMemberImg.updateMemberImg(oriImgName,imgName,imgUrl);
        }
    }
}
