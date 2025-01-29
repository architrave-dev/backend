package com.architrave.portfolio.memberInfo;

import com.architrave.portfolio.api.service.MemberInfoService;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.MemberInfo;
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
public class MemberInfoIntegrationTest {

    @Autowired
    private MemberInfoService memberInfoService;

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
    @DisplayName("find( =Create) update")
    void memberInfoLifecycle(){
        //find( =Create)
        MemberInfo found = memberInfoService.findMIByMember(testMember);

        // verify
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("");
        assertThat(found.getDescription()).isEqualTo("");

        //Update
        MemberInfo updated = memberInfoService.updateMI(found.getId(),
                "updated originUrl",
                "updated name",
                null,
                null,
                null,
                null,
                null
        );
        // verify
        assertThat(updated.getName()).isEqualTo("updated name");
    }
}
