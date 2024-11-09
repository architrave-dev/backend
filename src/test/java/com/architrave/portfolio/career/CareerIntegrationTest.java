package com.architrave.portfolio.career;

import com.architrave.portfolio.api.service.CareerService;
import com.architrave.portfolio.domain.model.Career;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.enumType.CareerType;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import com.architrave.portfolio.domain.repository.CareerRepository;
import com.architrave.portfolio.domain.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CareerIntegrationTest {


    @Autowired
    private CareerService careerService;

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
    @DisplayName("Create, Update, Delete Career")
    void careerLifecycle(){
        //Create
        Career created = careerService.createCareer(
                testMember,
                CareerType.G_EXH,
                "test career",
                2020
        );
        // verify
        Career found = careerService.findCareerById(created.getId());
        assertThat(found.getContent()).isEqualTo("test career");

        //Update
        Career updated = careerService.updateCareer(
                found.getId(),
                "updated career",
                2021
        );
        // verify
        assertThat(updated.getContent()).isEqualTo("updated career");
        assertThat(updated.getYearFrom()).isEqualTo(2021);

        //Delete
        careerService.removeCareerById(updated.getId());
        // verify
        List<Career> remainingDetails = careerService.findCareerByMember(testMember);
        assertThat(remainingDetails).isEmpty();
    }

    @Test
    @DisplayName("multiple Career")
    void multipleCareer(){
        Career created_1 = careerService.createCareer(
                testMember,
                CareerType.G_EXH,
                "test career",
                2020
        );
        Career created_2 = careerService.createCareer(
                testMember,
                CareerType.EDU,
                "test career2",
                2021
        );

        List<Career> careerList = careerService.findCareerByMember(testMember);
        assertThat(careerList).hasSize(2);
    }
}
