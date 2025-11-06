package com.example.bul.service;

import com.example.bul.model.Article;
import com.example.bul.repository.ArticleRepository;
import com.example.bul.repository.MemberRepository;
import dto.ArticleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepositroy;

    private ArticleDto mapToArticleDto(Article article){
        return ArticleDto.builder().id(article.getId())
                .title(article.getTitle())
                .description(article.getDescription())
                .created(article.getCreated())
                .updated(article.getUpdated())
                .memberId(article.getMember().getId())
                .name(article.getMember().getName())
                .email(article.getMember().getEmail()).build();
    }
}
