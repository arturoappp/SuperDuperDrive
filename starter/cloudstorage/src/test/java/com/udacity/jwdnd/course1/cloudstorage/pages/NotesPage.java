package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class NotesPage {
    @FindBy(id = "nav-notes-tab")
    private WebElement navNotesTab;

    @FindBy(id = "addNewNoteButton")
    private WebElement addNewNoteButton;

    @FindBy(id = "note-title")
    private WebElement noteTitleText;

    @FindBy(id = "note-description")
    private WebElement noteDescriptionText;

    @FindBy(id = "saveChangesButton")
    private WebElement saveChangesButton;

    @FindBy(className = "btn-danger")
    private WebElement deleteNoteButton;

    @FindBy(className = "btn-success")
    private WebElement editNoteButton;

    private final WebDriver webDriver;

    public NotesPage(WebDriver webDriver){
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    private void driveWait(String elementId) {
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 3);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(elementId)));
    }

    public void openEmptyNoteModal() {
        driveWait("addNewNoteButton");
        addNewNoteButton.click();
    }

    public void enterNewNote(String noteTitle, String noteDescription) {
        driveWait("note-title");

        noteTitleText.click();
        noteTitleText.sendKeys(noteTitle);

        noteDescriptionText.click();
        noteDescriptionText.sendKeys(noteDescription);
    }

    public void clearNote() {
        driveWait("note-title");

        noteTitleText.click();
        noteTitleText.clear();

        noteDescriptionText.click();
        noteDescriptionText.clear();
    }

    public void submitModal() {
        saveChangesButton.click();
    }

    public void clickEditButton(int row){
        driveWait("noteTable");
        WebElement baseTable = webDriver.findElement(By.id("noteTable"));
        List<WebElement> tableRows = baseTable.findElements(By.id("table-button-edit-note-row"));
        tableRows.get(row).click();
    }

    public void clickDeleteButton(int row){
        driveWait("noteTable");
        WebElement baseTable = webDriver.findElement(By.id("noteTable"));
        List<WebElement> tableRows = baseTable.findElements(By.id("table-button-delete-note-row"));
        tableRows.get(row).click();
    }

    public boolean checkTileIsInList(String value){
        driveWait("noteTable");
        WebElement baseTable = webDriver.findElement(By.id("noteTable"));
        List<WebElement> tableRows = baseTable.findElements(By.id("table-note-title-row"));
        return tableRows.stream().anyMatch(
                webElement -> webElement.getText().equals(value)
        );
    }

    public boolean checkDescIsInList(String value){
        driveWait("noteTable");
        WebElement baseTable = webDriver.findElement(By.id("noteTable"));
        List<WebElement> tableRows = baseTable.findElements(By.id("table-note-desc-row"));
        return tableRows.stream().anyMatch(
                webElement -> webElement.getText().equals(value)
        );
    }

    public HomePage navigateNotes(WebDriver driver) {
        navNotesTab.click();
        return new HomePage(driver);
    }
}
