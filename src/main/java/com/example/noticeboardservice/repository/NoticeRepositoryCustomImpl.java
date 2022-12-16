package com.example.noticeboardservice.repository;

import com.example.noticeboardservice.dto.NoticeSearchDto;
import com.example.noticeboardservice.entity.Notice;
import com.example.noticeboardservice.entity.QNotice;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom{
    private JPAQueryFactory queryFactory;

    public NoticeRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

//    private BooleanExpression regDtsAfter(String searchDateType){
//
//    }

    @Override
    public Page<Notice> getAdminItemPage(NoticeSearchDto noticeSearchDto, Pageable pageable) {
        QueryResults<Notice> results = queryFactory
                .selectFrom(QNotice.notice)
                .orderBy(QNotice.notice.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        List<Notice> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
}
