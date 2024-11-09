package com.architrave.portfolio.projectElement;

import com.architrave.portfolio.api.service.ProjectElementService;
import com.architrave.portfolio.domain.model.*;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.builder.ProjectBuilder;
import com.architrave.portfolio.domain.model.builder.WorkBuilder;
import com.architrave.portfolio.domain.model.builder.projectElementBuilder.DocumentInProjectBuilder;
import com.architrave.portfolio.domain.model.builder.projectElementBuilder.TextBoxInProjectBuilder;
import com.architrave.portfolio.domain.model.builder.projectElementBuilder.WorkInProjectBuilder;
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
                .thumbnailUrl("test thumbnail")
                .description("Test Description")
                .build();
        projectRepository.save(testProject);

        testWork = new WorkBuilder()
                .member(testMember)
                .workType(WorkType.digital)
                .title("Test Artwork")
                .originUrl("work-origin.jpg")
                .thumbnailUrl("work-thumbnail.jpg")
                .build();
        workRepository.save(testWork);

        testTextBox = TextBox.createTextBox("Test TextBox Content");
        textBoxRepository.save(testTextBox);

        UploadFile uploadFile = UploadFile.builder()
                .originUrl("document-origin.jpg")
                .thumbnailUrl("document-thumbnail.jpg")
                .build();
        testDocument = Document.createDocument(uploadFile, "Test Document Description");
        documentRepository.save(testDocument);
    }

    @Test
    @DisplayName("create, update, delete ProjectElement(Work)")
    void workPeLifecycle() {
        //Create
        ProjectElement workPe = new WorkInProjectBuilder()
                .project(testProject)
                .work(testWork)
                .workAlignment(WorkAlignment.CENTER)
                .workDisplaySize(WorkDisplaySize.BIG)
                .build();
        ProjectElement created = projectElementService.createProjectElement(workPe);

        //verify
        assertThat(created).isNotNull();
        assertThat(created.getWork()).isEqualTo(testWork);
        assertThat(created.getWorkAlignment()).isEqualTo(WorkAlignment.CENTER);

        //Update
        ProjectElement updated = projectElementService.updateProjectElementWork(
                testWork,
                created.getId(),
                WorkAlignment.LEFT,
                null
        );
        //verify
        assertThat(updated).isNotNull();
        assertThat(updated.getWork()).isEqualTo(testWork);
        assertThat(updated.getWorkAlignment()).isEqualTo(WorkAlignment.LEFT);

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
        ProjectElement textBoxPe = new TextBoxInProjectBuilder()
                .project(testProject)
                .textBox(testTextBox)
                .textBoxAlignment(TextBoxAlignment.CENTER)
                .build();
        ProjectElement created = projectElementService.createProjectElement(textBoxPe);

        //verify
        assertThat(created).isNotNull();
        assertThat(created.getWork()).isNull();
        assertThat(created.getWorkAlignment()).isNull();
        assertThat(created.getTextBox()).isEqualTo(testTextBox);
        assertThat(created.getTextBoxAlignment()).isEqualTo(TextBoxAlignment.CENTER);

        //Update
        ProjectElement updated = projectElementService.updateProjectElementTextBox(
                testTextBox,
                created.getId(),
                TextBoxAlignment.LEFT
        );
        //verify
        assertThat(updated).isNotNull();
        assertThat(updated.getTextBox()).isEqualTo(testTextBox);
        assertThat(updated.getTextBoxAlignment()).isEqualTo(TextBoxAlignment.LEFT);

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
        ProjectElement documentPe = new DocumentInProjectBuilder()
                .project(testProject)
                .document(testDocument)
                .documentAlignment(WorkAlignment.CENTER)
                .build();
        ProjectElement created = projectElementService.createProjectElement(documentPe);

        //verify
        assertThat(created).isNotNull();
        assertThat(created.getWork()).isNull();
        assertThat(created.getWorkAlignment()).isNull();
        assertThat(created.getTextBox()).isNull();
        assertThat(created.getTextBoxAlignment()).isNull();
        assertThat(created.getDocument()).isEqualTo(testDocument);
        assertThat(created.getDocumentAlignment()).isEqualTo(WorkAlignment.CENTER);


        ProjectElement updated = projectElementService.updateProjectElementDocument(
                testDocument,
                created.getId(),
                WorkAlignment.LEFT
        );
        //verify
        assertThat(updated).isNotNull();
        assertThat(updated.getTextBox()).isNull();
        assertThat(updated.getTextBoxAlignment()).isNull();
        assertThat(updated.getDocument()).isEqualTo(testDocument);
        assertThat(updated.getDocumentAlignment()).isEqualTo(WorkAlignment.LEFT);

        //Remove
        projectElementService.removeById(updated.getId());
        //verify
        List<ProjectElement> remains = projectElementService.findProjectElementByProject(testProject);
        assertThat(remains).isEmpty();
    }

    @Test
    @DisplayName("multiple Work, Textbox, Document ProjectElement")
    void multipleWorkTextboxDocumentPe() {
        ProjectElement textBoxPe = new TextBoxInProjectBuilder()
                .project(testProject)
                .textBox(testTextBox)
                .textBoxAlignment(TextBoxAlignment.CENTER)
                .build();
        projectElementService.createProjectElement(textBoxPe);

        ProjectElement workPe = new WorkInProjectBuilder()
                .project(testProject)
                .work(testWork)
                .workAlignment(WorkAlignment.CENTER)
                .workDisplaySize(WorkDisplaySize.BIG)
                .build();
        projectElementService.createProjectElement(workPe);

        ProjectElement documentPe = new DocumentInProjectBuilder()
                .project(testProject)
                .document(testDocument)
                .documentAlignment(WorkAlignment.CENTER)
                .build();
        projectElementService.createProjectElement(documentPe);


        List<ProjectElement> peList = projectElementService.findProjectElementByProject(testProject);
        assertThat(peList).hasSize(3);

        projectElementService.removeProjectElementByProject(testProject);
        List<ProjectElement> remains = projectElementService.findProjectElementByProject(testProject);
        assertThat(remains).isEmpty();
    }
}
