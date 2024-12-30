package com.library.pages;

import com.library.utilities.ConfigurationReader;
import com.library.utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignInPage {
    public SignInPage() {
        PageFactory.initElements(Driver.get(), this);
    }

    @FindBy(xpath = "//input[@id='inputEmail']")
    public WebElement usernameField;

    @FindBy(xpath = "//input[@id='inputPassword']")
    public WebElement passwordField;

    @FindBy(xpath = "//button[@type='submit']")
    public WebElement loginButton;

    public void login() {

        String username = ConfigurationReader.getProperty("username");
        //String username = System.getenv("USERNAME");
        String password = ConfigurationReader.getProperty("password");
        //String password = System.getenv("PASSWORD");
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();

    }
}
