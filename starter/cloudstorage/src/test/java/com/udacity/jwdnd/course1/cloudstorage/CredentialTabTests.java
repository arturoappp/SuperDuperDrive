package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pages.CredentialsPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static com.udacity.jwdnd.course1.cloudstorage.UtilTest.loginAsUser;
import static com.udacity.jwdnd.course1.cloudstorage.UtilTest.randomString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CredentialTabTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private CredentialsPage credentialsPage;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        credentialsPage = new CredentialsPage(driver);
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    //Write a Selenium test that logs in an existing user, creates a credential and verifies that the credential details are visible in the credential list.

    @Test
    public void addAndVerifyCredentialTest() {
        String url = randomString("url");
        String userName = randomString("username");
        String password = randomString("password");

        loginAsUser(driver, port);
        credentialsPage.navigateCredentials(driver);
        credentialsPage.openEmptyCredentialModal();
        credentialsPage.enterNewCredential(url, userName, password);
        credentialsPage.submitModal();

        Assertions.assertTrue(credentialsPage.checkUrlIsInList(url));
        Assertions.assertTrue(credentialsPage.checkUserNameIsInList(userName));
        Assertions.assertTrue(credentialsPage.checkPasswordIsInList());
    }
    //Write a Selenium test that logs in an existing user with existing credentials, clicks the edit credential button on an existing credential, changes the credential data, saves the changes, and verifies that the changes appear in the credential list.

    @Test
    public void editAndVerifyCredentialTest() {
        String url = randomString("url");
        String userName = randomString("username");
        String password = randomString("password");

        loginAsUser(driver, port);

        //adding a note
        credentialsPage.navigateCredentials(driver);
        credentialsPage.openEmptyCredentialModal();
        credentialsPage.enterNewCredential(url, userName, password);
        credentialsPage.submitModal();

        //edit credential
        String urlEdited = randomString("url");
        String userNameEdited = randomString("username");
        String passwordEdited = randomString("password");

        credentialsPage.clickEditButton(0);
        credentialsPage.clearCredentialForm();
        credentialsPage.enterNewCredential(urlEdited, userNameEdited, passwordEdited);
        credentialsPage.submitModal();

        Assertions.assertTrue(credentialsPage.checkUrlIsInList(urlEdited));
        Assertions.assertTrue(credentialsPage.checkUserNameIsInList(userNameEdited));
        Assertions.assertTrue(credentialsPage.checkPasswordIsInList());
    }
    //Write a Selenium test that logs in an existing user with existing credentials, clicks the delete credential button on an existing credential, and verifies that the credential no longer appears in the credential list.

    @Test
    public void deleteAndVerifyCredentialTest() {
        String url = randomString("url");
        String userName = randomString("username");
        String password = randomString("password");

        loginAsUser(driver, port);

        //adding a note
        credentialsPage.navigateCredentials(driver);
        credentialsPage.openEmptyCredentialModal();
        credentialsPage.enterNewCredential(url, userName, password);
        credentialsPage.submitModal();

        //delete note
        credentialsPage.clickDeleteButton(0);

        Assertions.assertFalse(credentialsPage.checkUrlIsInList(url));
        Assertions.assertFalse(credentialsPage.checkUserNameIsInList(userName));
        Assertions.assertFalse(credentialsPage.checkPasswordIsInList());
    }


}
