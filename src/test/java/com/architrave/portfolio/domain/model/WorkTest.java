//package com.architrave.portfolio.domain.model;
//
//import com.architrave.portfolio.domain.model.builder.MemberBuilder;
//import com.architrave.portfolio.domain.model.enumType.RoleType;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class WorkTest {
//
//    private Work work;
//    private Member member;
//    private Size size;
//
//    @BeforeEach
//    void setUp() {
//        member = new MemberBuilder()
//                .email("test@example.com")
//                .password("password")
//                .username("test")
//                .role(RoleType.USER)
//                .description("description")
//                .build();
//        Size size = new Size();
//        size.setDepth(100);
//        size.setHeight(100);
//        size.setWidth(100);
//        work = Work.builder()
//                .id(1L)
//                .member(member)
//                .title("Test Title")
//                .description("Test Description")
//                .size(size)
//                .material("Test Material")
//                .prodYear(2023)
//                .build();
//    }
//
//    @Test
//    void Work_수정_테스트() {
//        String newTitle = "Updated Title";
//        String newDescription = "Updated Description";
//        Size updatedSize = new Size();
//        updatedSize.setDepth(200);
//        updatedSize.setHeight(200);
//        updatedSize.setWidth(200);
//        String newMaterial = "Updated Material";
//        Integer newProdYear = 2024;
//
//        work.update(newTitle, newDescription, updatedSize, newMaterial, newProdYear);
//
//        assertEquals(newTitle, work.getTitle());
//        assertEquals(newDescription, work.getDescription());
//        assertEquals(updatedSize.getWidth(), work.getSize().getWidth());
//        assertEquals(updatedSize.getHeight(), work.getSize().getHeight());
//        assertEquals(updatedSize.getDepth(), work.getSize().getDepth());
//        assertEquals(newMaterial, work.getMaterial());
//        assertEquals(newProdYear, work.getProdYear());
//    }
//}