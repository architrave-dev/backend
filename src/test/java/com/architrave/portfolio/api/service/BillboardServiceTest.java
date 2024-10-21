package com.architrave.portfolio.api.service;

import com.architrave.portfolio.domain.model.Billboard;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.builder.BillboardBuilder;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.enumType.RoleType;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BillboardServiceTest {

    @Autowired
    private BillboardService billboardService;

    @Autowired
    private MemberService memberService;

    private final String TEST_MEMBER_EMAIL = "lee@gmail.com";
    private final String TEST_MEMBER_PASSWORD = "12345";
    private final String TEST_MEMBER_USERNAME = "이중섭";
    private final RoleType ROLE_USER = RoleType.USER;

    private final String TEST_LB_IMG_URL = "http://s3.aws.origin.png";
    private final String TEST_LB_THUMBNAIL_URL = "http://s3.aws.thumbnail.png";
    private final String TEST_LB_TITLE = "Landing box.";
    private final String TEST_LB_DISCRIPTION = "Landing box description.";


    @Test
    public void createBillboard(){
        //given
        Member member = createMemberInTest();
        addContext(member);
        Billboard billboard = createBillboardInTest(member, TEST_LB_IMG_URL, TEST_LB_THUMBNAIL_URL);

        //when
        Billboard createdLb = billboardService.createLb(billboard);

        //then
        Billboard findLb = billboardService.findLbById(createdLb.getId());
        assertNotNull(findLb);
        assertNotNull(findLb.getUploadFile().getThumbnailUrl());
        assertEquals(findLb.getTitle(), TEST_LB_TITLE);
        assertEquals(findLb.getDescription(), TEST_LB_DISCRIPTION);
        assertTrue(findLb.getIsVisible());
    }

    @Test
    public void IllegalArgumentExceptionWithEmptyImgWhenCreateBillboard(){
        //given
        Member member = createMemberInTest();
        addContext(member);
        //when
        assertThrows(IllegalArgumentException.class, () ->
            createBillboardInTest(member, null, null)
        );
    }

    @Test
    public void updateTitleDescriptionUploadFileInBillboard(){
        //given
        Member member = createMemberInTest();
        addContext(member);
        Billboard billboard = createBillboardInTest(member, TEST_LB_IMG_URL, TEST_LB_THUMBNAIL_URL);
        Billboard createdLb = billboardService.createLb(billboard);

        //when
        billboardService.updateLb(createdLb.getId(),
                TEST_LB_THUMBNAIL_URL, TEST_LB_IMG_URL,
                TEST_LB_DISCRIPTION,
                TEST_LB_TITLE,
                null
        );

        //then
        Billboard findLb2 = billboardService.findLbById(createdLb.getId());
        assertNotNull(findLb2);
        assertEquals(findLb2.getUploadFile().getOriginUrl(), TEST_LB_THUMBNAIL_URL);
        assertEquals(findLb2.getUploadFile().getThumbnailUrl(), TEST_LB_IMG_URL);
        assertEquals(findLb2.getTitle(), TEST_LB_DISCRIPTION);
        assertEquals(findLb2.getDescription(), TEST_LB_TITLE);
        assertTrue(findLb2.getIsVisible());
    }
    @Test
    public void updateBillboardImgToEmpty(){ //이래도 되나??? 고민!!
        //given
        Member member = createMemberInTest();
        addContext(member);
        Billboard billboard = createBillboardInTest(member, TEST_LB_IMG_URL, TEST_LB_THUMBNAIL_URL);
        Billboard createdLb = billboardService.createLb(billboard);

        //when
        billboardService.updateLb(createdLb.getId(),
                null, null,
                null,
                null,
                false
        );

        //then
        Billboard findLb2 = billboardService.findLbById(createdLb.getId());
        assertEquals(findLb2.getTitle(), TEST_LB_TITLE);
        assertEquals(findLb2.getUploadFile().getOriginUrl(), TEST_LB_IMG_URL);
        assertEquals(findLb2.getUploadFile().getThumbnailUrl(), TEST_LB_THUMBNAIL_URL);
        assertFalse(findLb2.getIsVisible());
    }
    @Test
    public void deleteBillboard(){
        //Billboard를 테이블에서 제거하는 로직은 존재하지 않는다.
        //UploadFile의 이미지 url을 null처리 하는 로직으로 대체한다.
        //UploadFile이 null 처리 후 isVisible를 false로 변경한다.
    }

    @Test
    public void EmptyImgWhenUpdateBillboard(){
        //given
        Member member = createMemberInTest();
        addContext(member);
        Billboard billboard = createBillboardInTest(member, TEST_LB_IMG_URL, TEST_LB_THUMBNAIL_URL);
        Billboard createdLb = billboardService.createLb(billboard);
        billboardService.updateLb(createdLb.getId(),
                null, null,
                null,
                null,
                true
        );

        //when
        billboardService.updateLb(createdLb.getId(),
                TEST_LB_THUMBNAIL_URL, TEST_LB_IMG_URL,
                null,
                null,
                false
        );
        //then
        Billboard findLb2 = billboardService.findLbById(createdLb.getId());
        assertNotNull(findLb2.getUploadFile().getOriginUrl());
        assertNotNull(findLb2.getUploadFile().getThumbnailUrl());
        assertFalse(findLb2.getIsVisible());
    }

    @Test
    public void WrongMemberWhenUpdateBillboard(){
        //context가 없거나 다른 Member인데 update 요청을 한다?
        //controller에서 짜름.
    }

    @Test
    public void removeBillboard(){
        //given
        Member member = createMemberInTest();
        addContext(member);
        Billboard billboard = createBillboardInTest(member, TEST_LB_IMG_URL, TEST_LB_THUMBNAIL_URL);
        Billboard createdLb = billboardService.createLb(billboard);

        //when
        Billboard findLb = billboardService.findLbById(createdLb.getId());
        findLb.setIsVisible(true);
        billboardService.updateLb(createdLb.getId(),
                null, null,
                null,
                null,
                false
        );

        //then
        Billboard findLb2 = billboardService.findLbById(createdLb.getId());
        assertFalse(findLb2.getIsVisible());

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

    private Billboard createBillboardInTest(Member member, String imgUrl, String thumbnailUrl){
        return new BillboardBuilder()
                .member(member)
                .originUrl(imgUrl)
                .thumbnailUrl(thumbnailUrl)
                .title(TEST_LB_TITLE)
                .description(TEST_LB_DISCRIPTION)
                .build();
    }
}
