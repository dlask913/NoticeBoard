package com.example.noticeboardservice.service;

import com.example.noticeboardservice.dto.CommentDto;
import com.example.noticeboardservice.entity.Comment;
import com.example.noticeboardservice.entity.Member;
import com.example.noticeboardservice.entity.Notice;
import com.example.noticeboardservice.repository.CommentRepository;
import com.example.noticeboardservice.repository.MemberRepository;
import com.example.noticeboardservice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service @Transactional
@RequiredArgsConstructor
public class CommentService{
    private final CommentRepository commentRepository;
    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    public void saveComment(CommentDto commentDto, Long noticeId, Long memberId){
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(EntityNotFoundException::new);;
        Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);;
        Comment comment = new Comment();
        comment.setCommentContent(commentDto.getCommentContent());
        comment.setMember(member);
        comment.setNotice(notice);
        commentRepository.save(comment);
    }

    public List<Comment> findAll(){
        return commentRepository.findAll();
    }

    public Comment findById(Long id){
        Comment comment = commentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return comment;
    }

    public void removeContent(Long id){
        Comment comment = commentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        commentRepository.delete(comment);
    }

    public void updateContent(Long id, Comment updateComment) {
        Comment comment = commentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        comment.setCommentContent(updateComment.getCommentContent());
    }
}
