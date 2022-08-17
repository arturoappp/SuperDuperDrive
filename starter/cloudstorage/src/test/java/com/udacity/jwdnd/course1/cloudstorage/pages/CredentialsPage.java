package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CredentialsPage {
    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(id = "addNewCredentialButton")
    private WebElement addNewCredentialButton;

    @FindBy(id = "credential-url")
    private WebElement credentialUrl;

    @FindBy(id = "credential-username")
    private WebElement credentialUsername;

    @FindBy(id = "credential-password")
    private WebElement credentialPassword;

    @FindBy(className = "table-button-delete-credential-row")
    private WebElement deleteCredentialButton;

    @FindBy(className = "table-button-edit-credential-row")
    private WebElement editCredentialButton;

    @FindBy(id = "saveCredentialChanges")
    private WebElement saveCredentialChanges;

    private final WebDriver webDriver;

    public CredentialsPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    private void driveWait(String elementId) {
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 2);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(elementId)));
    }

    private void driveWaitForTable() {
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 2);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
    }

    public void openEmptyCredentialModal() {
        driveWait("addNewCredentialButton");
        addNewCredentialButton.click();
    }


    public void enterNewCredential(String url, String userName, String password) {
        driveWait("credential-url");

        credentialUrl.click();
        credentialUrl.sendKeys(url);

        credentialUsername.click();
        credentialUsername.sendKeys(userName);

        credentialPassword.click();
        credentialPassword.sendKeys(password);
    }

    public void clearCredentialForm() {
        driveWait("credential-url");

        credentialUrl.click();
        credentialUrl.clear();

        credentialUsername.click();
        credentialUsername.clear();

        credentialPassword.click();
        credentialPassword.clear();
    }

    public void submitModal() {
        saveCredentialChanges.click();
    }

    public void clickEditButton(int row){
        driveWaitForTable();
        WebElement baseTable = webDriver.findElement(By.id("credentialTable"));
        List<WebElement> tableRows = baseTable.findElements(By.id("table-button-edit-credential-row"));
        tableRows.get(row).click();
    }

    public void clickDeleteButton(int row){
        driveWaitForTable();
        WebElement baseTable = webDriver.findElement(By.id("credentialTable"));
        List<WebElement> tableRows = baseTable.findElements(By.id("table-button-delete-credential-row"));
        tableRows.get(row).click();
    }

    public boolean checkUrlIsInList(String value){
        driveWaitForTable();
        WebElement baseTable = webDriver.findElement(By.id("credentialTable"));
        List<WebElement> tableRows = baseTable.findElements(By.id("table-credential-url-row"));
        return tableRows.stream().anyMatch(
                webElement -> webElement.getText().equals(value)
        );
    }

    public boolean checkUserNameIsInList(String value){
        driveWaitForTable();
        WebElement baseTable = webDriver.findElement(By.id("credentialTable"));
        List<WebElement> tableRows = baseTable.findElements(By.id("table-credential-username-row"));
        return tableRows.stream().anyMatch(
                webElement -> webElement.getText().equals(value)
        );
    }

    public boolean checkPasswordIsInList(){
        driveWaitForTable();
        WebElement baseTable = webDriver.findElement(By.id("credentialTable"));
        List<WebElement> tableRows = baseTable.findElements(By.id("table-credential-password-row"));
        return tableRows.stream().anyMatch(
                webElement -> !webElement.getText().isEmpty()
        );
    }

    public HomePage navigateCredentials(WebDriver driver) {
        credentialsTab.click();
        return new HomePage(driver);
    }
}
