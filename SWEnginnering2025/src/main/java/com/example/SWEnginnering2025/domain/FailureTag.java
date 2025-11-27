/*Project: AttachmentController.java
        Author: 한지윤
        Date of creation: 2025.11.23
        Date of last update: 2025.11.23
                */


package com.example.SWEnginnering2025.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "failure_tag")
@Getter
public class FailureTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 기본 태그면 null, 사용자 태그면 userId에 그 사람의 ID를 저장해 누가 만든 태그인지 구분
    private Long userId;

    //태그 이름(예: 시간관리 실패), 비어있으면 안됨
    @Column(nullable = false, length = 100, unique = false)
    private String name;

    // 기본 제공 태그 여부, builtIn이 true면 앱이 제공한 공용태그, false면 유저가 만든 태그
    @Column(nullable = false)
    private boolean builtIn;

    protected FailureTag() {
    }

    @Builder
    public FailureTag(Long userId, String name, boolean builtIn) {
        this.userId = userId;
        this.name = name;
        this.builtIn = builtIn;
    }
}
