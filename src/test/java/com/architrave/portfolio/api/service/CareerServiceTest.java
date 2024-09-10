package com.architrave.portfolio.api.service;

import static org.junit.jupiter.api.Assertions.*;

import com.architrave.portfolio.domain.model.Career;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.builder.CareerBuilder;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.enumType.CareerType;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import com.architrave.portfolio.global.exception.custom.RequiredValueEmptyException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.NoSuchElementException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CareerServiceTest {

    @Autowired
    private CareerService careerService;
    @Autowired
    private MemberService memberService;

    private final String TEST_MEMBER_EMAIL = "lee@gmail.com";
    private final String TEST_MEMBER_PASSWORD = "12345";
    private final String TEST_MEMBER_USERNAME = "이중섭";
    private final RoleType ROLE_USER = RoleType.USER;

    private final String TEST_CAREER_CONTENT_1 = "서울대학교";
    private final String TEST_CAREER_CONTENT_2 = "서울대학원";
    private final Integer TEST_CAREER_YEARFROM = 2023;
    private final Integer TEST_CAREER_YEARTO = 2024;

    @Test
    public void getCareerListByMemberEmpty(){
        //given
        Member member = createMemberInTest();

        //when
        List<Career> careerList = careerService.findCareerByMember(member);

        //then
        assertEquals(careerList.size() , 0);
    }

    @Test
    public void getCareerListByMember(){
        //given
        Member member = createMemberInTest();
        //when
        createCareerInTest(member, TEST_CAREER_CONTENT_1);
        createCareerInTest(member, TEST_CAREER_CONTENT_2);

        //when
        List<Career> careerList = careerService.findCareerByMember(member);

        //then
        assertEquals(careerList.size() , 2);
    }

    @Test
    public void createCareer(){
        //given
        Member member = createMemberInTest();
        //when
        Career career = createCareerInTest(member, TEST_CAREER_CONTENT_1);

        //then
        Career careerById = careerService.findCareerById(career.getId());
        assertNotNull(careerById);
        assertEquals(career.getCareerType(), careerById.getCareerType());
        assertEquals(career.getMember().getId(), careerById.getMember().getId());
    }

    @Test
    public void createCareerWithoutMember(){
        //then
        assertThrows(RequiredValueEmptyException.class, () -> {
            //given when
            Career career = new CareerBuilder()
                    .careerType(CareerType.EDU)
                    .content(TEST_CAREER_CONTENT_1)
                    .yearFrom(TEST_CAREER_YEARFROM)
                    .yearTo(TEST_CAREER_YEARTO)
                    .build();
        });
    }


    @Test
    public void updateCareer(){
        //given
        Member member = createMemberInTest();
        Career career = createCareerInTest(member, TEST_CAREER_CONTENT_1);

        //when
        Career updateCareer = careerService.updateCareer(
                career.getId(),
                TEST_CAREER_CONTENT_2,
                null,
                null);

        //then
        assertEquals(career.getId(), updateCareer.getId());
        assertNotEquals(career.getContent(), updateCareer.getContent());
    }

    @Test
    public void updateCareerNotValid(){
        //given
        Member member = createMemberInTest();
        Career career = createCareerInTest(member, TEST_CAREER_CONTENT_1);

        //when then
        assertThrows(RequiredValueEmptyException.class, () -> {
            Career updateCareer = careerService.updateCareer(
                    career.getId(),
                    TEST_CAREER_CONTENT_2,
                    null,
                    TEST_CAREER_YEARTO);
        });
    }

    @Test
    public void removeCareer(){
        //given
        Member member = createMemberInTest();
        //when
        Career career1 = createCareerInTest(member, TEST_CAREER_CONTENT_1);
        Career career2 = createCareerInTest(member, TEST_CAREER_CONTENT_2);

        //when
        careerService.removeCareerById(career1.getId());

        //then
        List<Career> careerList = careerService.findCareerByMember(member);
        assertEquals(careerList.size() , 1);
        Career career = careerList.get(0);
        assertEquals(career2.getId(), career.getId());
    }
    @Test
    public void NoSuchElementExceptionWhenRemoveEmptyCareer(){
        //given
        //when
        //then
        assertThrows(NoSuchElementException.class, () -> {
            careerService.removeCareerById(10L);
        });
    }

    private Career createCareerInTest(Member member, String content){
        Career career = new CareerBuilder()
                .member(member)
                .careerType(CareerType.EDU)
                .content(content)
                .build();
        return careerService.createCareer(career);
    }
    private Member createMemberInTest(){
        Member member = new MemberBuilder()
                .email(TEST_MEMBER_EMAIL)
                .password(TEST_MEMBER_PASSWORD)
                .username(TEST_MEMBER_USERNAME)
                .role(ROLE_USER)
                .build();
        return memberService.createMember(member);
    }
}
