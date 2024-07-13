package com.architrave.portfolio.api.service;

import com.architrave.portfolio.domain.model.LandingBox;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.builder.LandingBoxBuilder;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import jakarta.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class LandingBoxServiceTest {

    private final LandingBoxService landingBoxService;
    private final MemberService memberService;
    private final EntityManager em;
    @Autowired
    public LandingBoxServiceTest(
            LandingBoxService landingBoxService,
            MemberService memberService,
            EntityManager em
    ) {
        this.landingBoxService = landingBoxService;
        this.memberService = memberService;
        this.em = em;
    }
    private final String TEST_MEMBER_EMAIL = "lee@gmail.com";
    private final String TEST_MEMBER_PASSWORD = "12345";
    private final String TEST_MEMBER_USERNAME = "이중섭";
    private final RoleType ROLE_USER = RoleType.USER;

    private final String TEST_LB_IMG_URL = "http://s3.aws.origin.png";
    private final String TEST_LB_THUMBNAIL_URL = "http://s3.aws.thumbnail.png";
    private final String TEST_LB_TITLE = "Landing box.";
    private final String TEST_LB_DISCRIPTION = "Landing box description.";


    @Test
    @Transactional
    public void createLandingBox(){
        //given
        Member member = createMemberInTest();
        addContext(member);
        LandingBox landingBox = createLandingBoxInTest(member, TEST_LB_IMG_URL, TEST_LB_THUMBNAIL_URL);

        //when
        LandingBox createdLb = landingBoxService.createLb(landingBox);

        em.flush();
        em.clear();

        //then
        LandingBox findLb = landingBoxService.findLbById(createdLb.getId());
        assertNotNull(findLb);
        assertNotNull(findLb.getUploadFile().getThumbnailUrl());
        assertEquals(findLb.getTitle(), TEST_LB_TITLE);
        assertEquals(findLb.getDescription(), TEST_LB_DISCRIPTION);
        assertFalse(findLb.getIsDeleted());
    }

    @Test
    @Transactional
    public void IllegalArgumentExceptionWithEmptyImgWhenCreateLandingBox(){
        //given
        Member member = createMemberInTest();
        addContext(member);
        //when
        assertThrows(IllegalArgumentException.class, () ->
            createLandingBoxInTest(member, null, null)
        );
    }

    @Test
    @Transactional
    public void updateTitleAndDescriptionLandingBox(){
        //given
        Member member = createMemberInTest();
        addContext(member);
        LandingBox landingBox = createLandingBoxInTest(member, TEST_LB_IMG_URL, TEST_LB_THUMBNAIL_URL);
        LandingBox createdLb = landingBoxService.createLb(landingBox);
        em.flush();
        em.clear();

        //when
        LandingBox findLb = landingBoxService.findLbById(createdLb.getId());
        findLb.setTitle(TEST_LB_DISCRIPTION);
        findLb.setDescription(TEST_LB_TITLE);
        em.flush();
        em.clear();

        //then
        LandingBox findLb2 = landingBoxService.findLbById(createdLb.getId());
        assertNotNull(findLb2);
        assertNotNull(findLb2.getUploadFile().getThumbnailUrl());
        assertEquals(findLb2.getTitle(), TEST_LB_DISCRIPTION);
        assertEquals(findLb2.getDescription(), TEST_LB_TITLE);
        assertFalse(findLb2.getIsDeleted());
    }
    @Test
    @Transactional
    public void updateLandingBoxImgToAnother(){
        //given
        Member member = createMemberInTest();
        addContext(member);
        LandingBox landingBox = createLandingBoxInTest(member, TEST_LB_IMG_URL, TEST_LB_THUMBNAIL_URL);
        LandingBox createdLb = landingBoxService.createLb(landingBox);

        em.flush();
        em.clear();

        //when
        LandingBox findLb = landingBoxService.findLbById(createdLb.getId());
        findLb.setUploadFileUrl(TEST_LB_THUMBNAIL_URL, TEST_LB_IMG_URL);
        em.flush();
        em.clear();

        //then
        LandingBox findLb2 = landingBoxService.findLbById(createdLb.getId());
        assertEquals(findLb2.getTitle(), TEST_LB_TITLE);
        assertEquals(findLb2.getUploadFile().getOriginUrl(), TEST_LB_THUMBNAIL_URL);
        assertEquals(findLb2.getUploadFile().getThumbnailUrl(), TEST_LB_IMG_URL);
        assertFalse(findLb2.getIsDeleted());
    }
    @Test
    @Transactional
    public void updateLandingBoxImgToEmpty(){ //이래도 되나??? 고민!!
        //given
        Member member = createMemberInTest();
        addContext(member);
        LandingBox landingBox = createLandingBoxInTest(member, TEST_LB_IMG_URL, TEST_LB_THUMBNAIL_URL);
        LandingBox createdLb = landingBoxService.createLb(landingBox);

        em.flush();
        em.clear();

        //when
        LandingBox findLb = landingBoxService.findLbById(createdLb.getId());

        findLb.removeUploadFile();
        em.flush();
        em.clear();

        //then
        LandingBox findLb2 = landingBoxService.findLbById(createdLb.getId());
        assertEquals(findLb2.getTitle(), TEST_LB_TITLE);
        assertEquals(findLb2.getUploadFile().getOriginUrl(), null);
        assertEquals(findLb2.getUploadFile().getThumbnailUrl(), null);
        assertTrue(findLb2.getIsDeleted());
    }
    @Test
    public void deleteLandingBox(){
        //LandingBox를 테이블에서 제거하는 로직은 존재하지 않는다.
        //UploadFile의 이미지 url을 null처리 하는 로직으로 대체한다.
        //UploadFile이 null 처리 후 isDeleted를 true로 변경한다.
    }

    @Test
    @Transactional
    public void EmptyImgWhenUpdateLandingBox(){
        //given
        Member member = createMemberInTest();
        addContext(member);
        LandingBox landingBox = createLandingBoxInTest(member, TEST_LB_IMG_URL, TEST_LB_THUMBNAIL_URL);
        LandingBox createdLb = landingBoxService.createLb(landingBox);
        createdLb.removeUploadFile();

        em.flush();
        em.clear();

        //when
        LandingBox findLb = landingBoxService.findLbById(createdLb.getId());
        findLb.setUploadFileUrl(TEST_LB_THUMBNAIL_URL, TEST_LB_IMG_URL);
        em.flush();
        em.clear();

        //then
        LandingBox findLb2 = landingBoxService.findLbById(createdLb.getId());
        assertNotNull(findLb2.getUploadFile().getOriginUrl());
        assertNotNull(findLb2.getUploadFile().getThumbnailUrl());
        assertFalse(findLb2.getIsDeleted());
    }

    @Test
    public void WrongMemberWhenUpdateLandingBox(){
        //context가 없거나 다른 Member인데 update 요청을 한다?
        //controller에서 짜름.
    }

    @Test
    @Transactional
    public void removeLandingBox(){
        //given
        Member member = createMemberInTest();
        addContext(member);
        LandingBox landingBox = createLandingBoxInTest(member, TEST_LB_IMG_URL, TEST_LB_THUMBNAIL_URL);
        LandingBox createdLb = landingBoxService.createLb(landingBox);
        em.flush();
        em.clear();

        //when
        LandingBox findLb = landingBoxService.findLbById(createdLb.getId());
        findLb.setIsDeleted(true);
        em.flush();
        em.clear();

        //then
        LandingBox findLb2 = landingBoxService.findLbById(createdLb.getId());
        assertTrue(findLb2.getIsDeleted());

    }

    private void addContext(Member member){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                member,
                null,
                member.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
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

    private LandingBox createLandingBoxInTest(Member member, String imgUrl, String thumbnailUrl){
        return new LandingBoxBuilder()
                .member(member)
                .originImgUrl(imgUrl)
                .thumbnailUrl(thumbnailUrl)
                .title(TEST_LB_TITLE)
                .description(TEST_LB_DISCRIPTION)
                .build();
    }
}
