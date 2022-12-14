package com.example.noticeboardservice.entity;

import com.example.noticeboardservice.dto.NoticeDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter @Setter @ToString
@Table(name = "notice")
@Entity
public class Notice extends BaseEntity{

    @Id
    @Column(name = "notice_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 글 id

    @Column(nullable = false, length = 10)
    private String userName; // 작성자

    @Column(nullable = false, length = 20)
    private String title; // 제목

    @Lob
    @Column(nullable = false)
    private String content; // 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL)
    private List<Comment> noticeComments = new ArrayList<>();


    public static Notice createNotice(NoticeDto noticeDto, Member member) {
        Notice notice = new Notice();
        notice.setTitle(noticeDto.getTitle());
        notice.setContent(noticeDto.getContent());
        notice.setMember(member);
        member.getMemberNotices().add(notice);
        return notice;
    }
}
