package com.example.noticeboardservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class NoticeDto {
    private Long id;
    private String userName;
    private String title;
    private String content;
    private Date postDate;
}
