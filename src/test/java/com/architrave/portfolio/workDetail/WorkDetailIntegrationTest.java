package com.architrave.portfolio.workDetail;

import com.architrave.portfolio.api.service.WorkDetailService;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.domain.model.WorkDetail;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.builder.WorkBuilder;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import com.architrave.portfolio.domain.model.enumType.WorkType;
import com.architrave.portfolio.domain.repository.MemberRepository;
import com.architrave.portfolio.domain.repository.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class WorkDetailIntegrationTest {
    @Autowired
    private WorkDetailService workDetailService;

    @Autowired
    private WorkRepository workRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;
    private Work testWork;

    @BeforeEach
    void setUp() {
        // Create test data without any database interaction
        testMember = new MemberBuilder()
                .email("test@example.com")
                .password("password123")
                .username("testArtist")
                .role(RoleType.USER)
                .build();
        memberRepository.save(testMember);

        testWork = new WorkBuilder()
                .member(testMember)
                .workType(WorkType.digital)
                .title("Test Artwork")
                .originUrl("work-origin.jpg")
                .thumbnailUrl("work-thumbnail.jpg")
                .build();
        workRepository.save(testWork);
    }

    @Test
    @DisplayName("Should create and retrieve work detail")
    void workDetailLifecycle() {
        // Create
        WorkDetail created = workDetailService.createWorkDetail(
                testWork,
                "detail-origin.jpg",
                "detail-thumbnail.jpg",
                "Test Detail"
        );

        // Retrieve and verify
        WorkDetail found = workDetailService.findWorkDetailById(created.getId());
        assertThat(found.getDescription()).isEqualTo("Test Detail");

        // Update
        WorkDetail updated = workDetailService.updateWorkDetail(
                found.getId(),
                "updated-origin.jpg",
                "updated-thumbnail.jpg",
                "Updated Detail"
        );
        assertThat(updated.getDescription()).isEqualTo("Updated Detail");

        // Delete and verify
        workDetailService.removeWorkDetailById(updated.getId());
        List<WorkDetail> remainingDetails = workDetailService.findWorkDetailByWork(testWork);
        assertThat(remainingDetails).isEmpty();
    }


    @Test
    @DisplayName("Should handle multiple work details for single work")
    void multipleWorkDetails() {
        // Create multiple details
        WorkDetail detail1 = workDetailService.createWorkDetail(
                testWork,
                "origin1.jpg",
                "thumb1.jpg",
                "Detail 1"
        );

        WorkDetail detail2 = workDetailService.createWorkDetail(
                testWork,
                "origin2.jpg",
                "thumb2.jpg",
                "Detail 2"
        );

        // Verify retrieval
        List<WorkDetail> details = workDetailService.findWorkDetailByWork(testWork);
        assertThat(details).hasSize(2);

        // Delete all and verify
        workDetailService.removeWorkDetailByWork(testWork);
        List<WorkDetail> remainingDetails = workDetailService.findWorkDetailByWork(testWork);
        assertThat(remainingDetails).isEmpty();
    }

}
