package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pages.NotesPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static com.udacity.jwdnd.course1.cloudstorage.UtilTest.loginAsUser;
import static com.udacity.jwdnd.course1.cloudstorage.UtilTest.randomString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NoteTabTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private NotesPage notesPage;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        notesPage = new NotesPage(driver);
        loginAsUser(driver, port);
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    //Write a Selenium test that logs in an existing user, creates a note and verifies that the note details are visible in the note list.
    @Test
    public void addAndVerifyNoteTest() {
        String noteTile = randomString("noteTitle");
        String noteDescription = randomString("noteDesc");

        loginAsUser(driver, port);

        notesPage.navigateNotes(driver);
        notesPage.openEmptyNoteModal();
        notesPage.enterNewNote(noteTile, noteDescription);
        notesPage.submitModal();

        Assertions.assertTrue(notesPage.checkTileIsInList(noteTile));
        Assertions.assertTrue(notesPage.checkDescIsInList(noteDescription));
    }

    //Write a Selenium test that logs in an existing user with existing notes, clicks the edit note button on an existing note, changes the note data, saves the changes, and verifies that the changes appear in the note list.
    @Test
    public void editAndVerifyNoteTest() {
        String noteTile = randomString("noteTitle");
        String noteDescription = randomString("noteDesc");

        loginAsUser(driver, port);

        //adding a note
        notesPage.navigateNotes(driver);
        notesPage.openEmptyNoteModal();
        notesPage.enterNewNote(noteTile, noteDescription);
        notesPage.submitModal();

        //edit note
        String noteTileEdited = randomString("noteTitle");
        String noteDescriptionEdited = randomString("noteDesc");

        notesPage.clickEditButton(0);
        notesPage.clearNote();
        notesPage.enterNewNote(noteTileEdited, noteDescriptionEdited);
        notesPage.submitModal();

        Assertions.assertTrue(notesPage.checkTileIsInList(noteTileEdited));
        Assertions.assertTrue(notesPage.checkDescIsInList(noteDescriptionEdited));
    }

    //Write a Selenium test that logs in an existing user with existing notes, clicks the delete note button on an existing note, and verifies that the note no longer appears in the note list.

    @Test
    public void deleteAndVerifyNoteTest() {
        String noteTile = randomString("noteTitle");
        String noteDescription = randomString("noteDesc");

        loginAsUser(driver, port);

        //adding a note
        notesPage.navigateNotes(driver);
        notesPage.openEmptyNoteModal();
        notesPage.enterNewNote(noteTile, noteDescription);
        notesPage.submitModal();

        //delete note
        notesPage.clickDeleteButton(0);

        Assertions.assertFalse(notesPage.checkTileIsInList(noteTile));
        Assertions.assertFalse(notesPage.checkDescIsInList(noteDescription));
    }

}
