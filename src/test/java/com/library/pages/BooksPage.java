package com.library.pages;

import com.library.utilities.BrowserUtils;
import com.library.utilities.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class BooksPage extends TopNavigations {
    public BooksPage() {
        PageFactory.initElements(Driver.get(),this);
    }
    @FindBy(xpath = "//input[@type='search']")
    public WebElement searchBox;
    @FindBy(xpath = "(//a[.=' Edit Book'])[1]")
    public WebElement editBookLink;


    public WebElement editBook(String book) {
        searchBox.sendKeys(book+ Keys.ENTER);
        BrowserUtils.waitFor(1);
        String xpath = "//td[3][.='" + book + "']/../td/a";
        return Driver.get().findElement(By.xpath(xpath));
    }
    public String getBookDataByAttribute(String nameAttribute) {
        String xpath = "//form[@id='edit_book_form']//*[@name='" + nameAttribute + "']";//form[@id='edit_book_form']//*[@name='author']
        return Driver.get().findElement(By.xpath(xpath)).getAttribute("value");
    }

//    public Map<String,String> getBooksDataAsMap(){
//
//        Map<String,String>boobInfo=new LinkedHashMap<>();
//         boobInfo.put("name",getBookDataByAttribute("name"));
//         boobInfo.put("isbn",getBookDataByAttribute("isbn"));
//         boobInfo.put("year",getBookDataByAttribute("year"));
//         boobInfo.put("author",getBookDataByAttribute("author"));
//         return boobInfo;
//
//    }



}
