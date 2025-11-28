/*Project: AttachmentController.java
        Author: 한지윤
        Date of creation: 2025.11.23
        Date of last update: 2025.11.23
                */
package com.example.SWEnginnering2025.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "failure_tag")
@Getter
@NoArgsConstructor
public class FailureTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // null → built-in tag

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FailureCategory category;

    @Column(nullable = false)
    private boolean builtIn;

    public FailureTag(Long userId, String name, FailureCategory category, boolean builtIn) {
        this.userId = userId;
        this.name = name;
        this.category = category;
        this.builtIn = builtIn;
    }
}

