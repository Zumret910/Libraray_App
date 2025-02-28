package com.library.pages;

import com.library.utilities.BrowserUtils;
import com.library.utilities.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public abstract class TopNavigations {
    public TopNavigations() {
        PageFactory.initElements(Driver.get(),this);
    }
    @FindBy (xpath = "//span[.='Dashboard']")
    public WebElement dashboardLink;
    @FindBy (xpath = "//span[.='Users']")
    public WebElement usersLink;
    @FindBy (xpath = "//span[.='Books']")
    public WebElement booksLink;
    @FindBy(xpath = "//a[@id='navbarDropdown']/span")
    public WebElement userNameDashboard;
public void navigateByName(String moduleName){
    switch (moduleName.toLowerCase()){
        case "books":
            booksLink.click();
            break;
        case "users":
            usersLink.click();
            break;
        case "dashboard":
            dashboardLink.click();
            break;
    }
}
public String getUserNameText(){
    userNameDashboard.click();
    BrowserUtils.waitForPageToLoad(3);
   return userNameDashboard.getText();

}
}
