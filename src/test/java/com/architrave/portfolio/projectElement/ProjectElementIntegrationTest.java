package com.architrave.portfolio.projectElement;

import com.architrave.portfolio.api.service.ProjectElementService;
import com.architrave.portfolio.domain.model.*;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.builder.ProjectBuilder;
import com.architrave.portfolio.domain.model.builder.WorkBuilder;
import com.architrave.portfolio.domain.model.builder.projectElementBuilder.DocumentInPEBuilder;
import com.architrave.portfolio.domain.model.builder.projectElementBuilder.TextBoxInPEBuilder;
import com.architrave.portfolio.domain.model.builder.projectElementBuilder.WorkInPEBuilder;
import com.architrave.portfolio.domain.model.enumType.*;
import com.architrave.portfolio.domain.repository.*;
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
public class ProjectElementIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private WorkRepository workRepository;
    @Autowired
    private TextBoxRepository textBoxRepository;
    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ProjectElementService projectElementService;

    private Member testMember;
    private Project testProject;
    private Work testWork;
    private TextBox testTextBox;
    private Document testDocument;

    @BeforeEach
    void setUp() {
        // Initialize Work, TextBox, and Document for ProjectElement
        testMember = new MemberBuilder()
                    .email("test@example.com")
                    .password("password123")
                    .username("testArtist")
                    .role(RoleType.USER)
                    .build();
        memberRepository.save(testMember);

        testProject = new ProjectBuilder()
                .member(testMember)
                .title("Test Title")
                .originUrl("test origin")
                .description("Test Description")
                .build();
        projectRepository.save(testProject);

        testWork = new WorkBuilder()
                .member(testMember)
                .workType(WorkType.DIGITAL)
                .title("Test Artwork")
                .originUrl("work-origin.jpg")
                .build();
        workRepository.save(testWork);

        testTextBox = TextBox.createTextBox("Test TextBox Content");
        textBoxRepository.save(testTextBox);

        UploadFile uploadFile = UploadFile.builder()
                .originUrl("document-origin.jpg")
                .build();
        testDocument = Document.createDocument(uploadFile, "Test Document Description");
        documentRepository.save(testDocument);
    }

    @Test
    @DisplayName("create, update, delete ProjectElement(Work)")
    void workPeLifecycle() {
        //Create
        ProjectElement workPe = new WorkInPEBuilder()
                .project(testProject)
                .work(testWork)
                .workAlignment(DisplayAlignment.CENTER)
                .workDisplaySize(DisplaySize.BIG)
                .build();
        ProjectElement created = projectElementService.createProjectElement(workPe);

        //verify
        assertThat(created).isNotNull();
        assertThat(created.getWork()).isEqualTo(testWork);
        assertThat(created.getDisplayAlignment()).isEqualTo(DisplayAlignment.CENTER);

        //Update
        ProjectElement updated = projectElementService.updateProjectElementWork(
                testWork,
                created.getId(),
                DisplayAlignment.LEFT,
                null
        );
        //verify
        assertThat(updated).isNotNull();
        assertThat(updated.getWork()).isEqualTo(testWork);
        assertThat(updated.getDisplayAlignment()).isEqualTo(DisplayAlignment.LEFT);

        //Update2

        //Remove
        projectElementService.removeById(updated.getId());
        //verify
        List<ProjectElement> remains = projectElementService.findProjectElementByProject(testProject);
        assertThat(remains).isEmpty();
    }

    @Test
    @DisplayName("create, update, delete ProjectElement(TextBox)")
    void textboxPeLifecycle() {
        //Create
        ProjectElement textBoxPe = new TextBoxInPEBuilder()
                .project(testProject)
                .textBox(testTextBox)
                .textBoxAlignment(TextAlignment.CENTER)
                .build();
        ProjectElement created = projectElementService.createProjectElement(textBoxPe);

        //verify
        assertThat(created).isNotNull();
        assertThat(created.getWork()).isNull();
        assertThat(created.getDisplayAlignment()).isNull();
        assertThat(created.getTextBox()).isEqualTo(testTextBox);
        assertThat(created.getTextAlignment()).isEqualTo(TextAlignment.CENTER);

        //Update
        ProjectElement updated = projectElementService.updateProjectElementTextBox(
                testTextBox,
                created.getId(),
                TextAlignment.LEFT
        );
        //verify
        assertThat(updated).isNotNull();
        assertThat(updated.getTextBox()).isEqualTo(testTextBox);
        assertThat(updated.getTextAlignment()).isEqualTo(TextAlignment.LEFT);

        //Update2

        //Remove
        projectElementService.removeById(updated.getId());
        //verify
        List<ProjectElement> remains = projectElementService.findProjectElementByProject(testProject);
        assertThat(remains).isEmpty();
    }
    @Test
    @DisplayName("create, update, delete ProjectElement(Document)")
    void ducumentPeLifecycle() {
        //Create
        ProjectElement documentPe = new DocumentInPEBuilder()
                .project(testProject)
                .document(testDocument)
                .documentAlignment(DisplayAlignment.CENTER)
                .build();
        ProjectElement created = projectElementService.createProjectElement(documentPe);

        //verify
        assertThat(created).isNotNull();
        assertThat(created.getWork()).isNull();
        assertThat(created.getDisplayAlignment()).isNull();
        assertThat(created.getTextBox()).isNull();
        assertThat(created.getTextAlignment()).isNull();
        assertThat(created.getDocument()).isEqualTo(testDocument);
        assertThat(created.getDisplayAlignment()).isEqualTo(DisplayAlignment.CENTER);


        ProjectElement updated = projectElementService.updateProjectElementDocument(
                testDocument,
                created.getId(),
                DisplayAlignment.LEFT
        );
        //verify
        assertThat(updated).isNotNull();
        assertThat(updated.getTextBox()).isNull();
        assertThat(updated.getTextAlignment()).isNull();
        assertThat(updated.getDocument()).isEqualTo(testDocument);
        assertThat(updated.getDisplayAlignment()).isEqualTo(DisplayAlignment.LEFT);

        //Remove
        projectElementService.removeById(updated.getId());
        //verify
        List<ProjectElement> remains = projectElementService.findProjectElementByProject(testProject);
        assertThat(remains).isEmpty();
    }

    @Test
    @DisplayName("multiple Work, Textbox, Document ProjectElement")
    void multipleWorkTextboxDocumentPe() {
        ProjectElement textBoxPe = new TextBoxInPEBuilder()
                .project(testProject)
                .textBox(testTextBox)
                .textBoxAlignment(TextAlignment.CENTER)
                .build();
        projectElementService.createProjectElement(textBoxPe);

        ProjectElement workPe = new WorkInPEBuilder()
                .project(testProject)
                .work(testWork)
                .workAlignment(DisplayAlignment.CENTER)
                .workDisplaySize(DisplaySize.BIG)
                .build();
        projectElementService.createProjectElement(workPe);

        ProjectElement documentPe = new DocumentInPEBuilder()
                .project(testProject)
                .document(testDocument)
                .documentAlignment(DisplayAlignment.CENTER)
                .build();
        projectElementService.createProjectElement(documentPe);


        List<ProjectElement> peList = projectElementService.findProjectElementByProject(testProject);
        assertThat(peList).hasSize(3);

        projectElementService.removeProjectElementByProject(testProject);
        List<ProjectElement> remains = projectElementService.findProjectElementByProject(testProject);
        assertThat(remains).isEmpty();
    }
}
