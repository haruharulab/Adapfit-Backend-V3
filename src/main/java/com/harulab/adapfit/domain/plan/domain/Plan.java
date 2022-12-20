package com.harulab.adapfit.domain.plan.domain;

import com.harulab.adapfit.domain.category.domain.Category;
import com.harulab.adapfit.domain.image.domain.Image;
import com.harulab.adapfit.domain.plan.exception.DontAccessOtherPlanException;
import com.harulab.adapfit.domain.plan.presentation.dto.req.PlanUpdateInfoRequestDto;
import com.harulab.adapfit.domain.plan.presentation.dto.req.PlanUpdateRequestDto;
import com.harulab.adapfit.domain.user.domain.User;
import com.harulab.adapfit.global.entity.BaseTimeEntity;
import com.harulab.adapfit.infrastructure.s3.S3FileResponseDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Plan extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 36)
    @NotNull
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    @NotNull
    private String content;

    @Column(nullable = false)
    private String thumbnailName;

    @Column(nullable = false)
    private String thumbnailUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    private List<Image> images;

    @Builder
    public Plan(String title, String content, String thumbnailName, String thumbnailUrl, Category category, User writer) {
        this.title = title;
        this.content = content;
        this.thumbnailName = thumbnailName;
        this.thumbnailUrl = thumbnailUrl;
        this.category = category;
        this.writer = writer;
    }

    // 연관관계 편의 메서드
    public void confirmWriter(User user) {
        this.writer = user;
        user.addPlan(this);
    }

    public void confirmCategory(Category category) {
        this.category = category;
        category.addPlan(this);
    }

    public void isRightWriter(User writer) {
        if (!Objects.equals(writer.getId(), writer.getId())) {
            throw DontAccessOtherPlanException.EXCEPTION;
        }
    }

    public void updateThumbnail(S3FileResponseDto res) {
        this.thumbnailName = res.getFileUrl();
        this.thumbnailUrl = res.getFileUrl();
    }

    public void updateInfo(PlanUpdateRequestDto req, Category category) {
        this.title = req.getTitle();
        this.content = req.getContent();
        this.category = category;
    }
}
