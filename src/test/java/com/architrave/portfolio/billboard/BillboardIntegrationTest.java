package com.architrave.portfolio.billboard;

import com.architrave.portfolio.api.service.BillboardService;
import com.architrave.portfolio.domain.model.Billboard;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import com.architrave.portfolio.domain.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class BillboardIntegrationTest {

    @Autowired
    private BillboardService billboardService;

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
    @DisplayName("find(Create) Update  Billboard")
    void careerLifecycle(){
        //find( =Create)
        Billboard found = billboardService.findByMember(testMember);

        // verify
        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("Billboard");
        assertThat(found.getDescription()).isEqualTo("Brief description of your content");

        //Update
        Billboard updated = billboardService.updateLb(found.getId(),
                null,
                null,
                "updated Billboard",
                null,
                null
        );
        // verify
        assertThat(updated.getTitle()).isEqualTo("updated Billboard");
    }
}
