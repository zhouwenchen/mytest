package org.example;

import java.io.File;

import java.io.IOException;

import org.junit.After;

import org.junit.Before;

import org.junit.Test;

import org.openqa.selenium.OutputType;

import org.openqa.selenium.TakesScreenshot;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.openqa.selenium.support.ui.WebDriverWait;

import com.sun.jna.platform.FileUtils;

public class TestTakesScreenshot {
public static void main(String[] args) {
//System.setProperty("webdriver.firefox.bin", "D:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
System.setProperty("webdriver.chrome.bin", "C:\\test\\chromedriver.exe");

WebDriver driver = new ChromeDriver();

driver.get("http://www.baidu.com");

File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE); //讲截取的图片以文件的形式返回

try {
org.apache.commons.io.FileUtils.copyFile(srcFile, new File("c:\\test\\screenshot.png")); //使用copyFile()方法保存获取到的截图文件

} catch (IOException e) {
// TODO Auto-generated catch block

e.printStackTrace();

}

driver.quit();

}

}
