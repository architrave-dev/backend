package com.architrave.portfolio.work;

import com.architrave.portfolio.api.service.WorkService;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import com.architrave.portfolio.domain.model.enumType.WorkType;
import com.architrave.portfolio.domain.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class WorkIntegrationTest {

    @Autowired
    private WorkService workService;

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;

    @BeforeEach
    void setUp(){
        testMember = new MemberBuilder()
                .email("test@example.com")
                .password("password123")
                .username("testArtist")
                .role(RoleType.USER)
                .build();
        memberRepository.save(testMember);
    }

    @Test
    @DisplayName("Create, Update, Delete Work")
    void workLifecycle(){
        //Create
        Work created = workService.createWork(
                testMember,
                WorkType.PAINTING_WATER,
                "work-origin.jpg",
                "Test Artwork",
                null,
                null,
                null,
                null,
                null,
                null
        );
        // verify
        Work found = workService.findWorkById(created.getId());
        assertThat(found.getTitle()).isEqualTo("Test Artwork");

        //Update
        Work updated = workService.updateWork(
                found.getId(),
                WorkType.PAINTING_WATER,
                "updated-origin.jpg",
                "updated Artwork",
                null,
                null,
                null,
                null,
                null,
                null
        );

        // verify
        assertThat(updated.getTitle()).isEqualTo("updated Artwork");

        //Delete
        workService.removeWorkById(updated.getId());
        // verify
        List<Work> remainingDetails = workService.findWorkByMember(testMember);
        assertThat(remainingDetails).isEmpty();
    }

    @Test
    @DisplayName("")
    void multipleWorks(){
        Work work1 = workService.createWork(
                testMember,
                WorkType.PAINTING_WATER,
                "work-origin.jpg",
                "Test Artwork1",
                null,
                null,
                null,
                null,
                null,
                null
        );
        Work work2 = workService.createWork(
                testMember,
                WorkType.PAINTING_OIL,
                "work-origin.jpg",
                "Test Artwork2",
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<Work> workList = workService.findWorkByMember(testMember);
        assertThat(workList).hasSize(2);
    }
}
