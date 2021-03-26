package org.example;

import com.sun.glass.ui.Application;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by zhoudw
 * 2020-03-18 16:39.
 */
@Component
@Slf4j
@SpringBootApplication
public class StartRunner implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(StartRunner.class, args);
    }

    @Override
    public void run(String... args) throws Exception{
        //驱动地址
        System.setProperty("webdriver.chrome.driver","/data/datacenter/chromedriver");
//        System.setProperty("webdriver.chrome.driver","C:\\test\\chromedriver.exe");

        WebDriver webDriver = null;
        String url = "https://blog.csdn.net/qq_20667511/article/details/115201711?spm=1001.2014.3001.5502";
        ChromeOptions options=new ChromeOptions();
        //设置 chrome 的无头模式
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--start-maximized");
        //因为报表页面必须滚动才能全部展示，这里直接给个很大的高度
        options.addArguments("--window-size=1280,4300");
        //启动一个 chrome 实例
        webDriver = new ChromeDriver(options);

        //页面最大化
//        webDriver.manage().window().maximize();
//        Thread.sleep(2000);
        //全局等等
//        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //访问网址
//        webDriver.get(url);
//        Thread.sleep(2000);

//        WebDriverWait webDriverWait=new WebDriverWait(webDriver,5);

        //执行javascript 可以直接设置localstroage,cookie等方式。不过拼字符串实在太麻烦
//        String token = "localStorage.setItem('cloud.token','{\"val\":\"5d31f455-8ed5-4ebe-80d1-760665df452c\",\"expires\":1584531900299}')";
//        ((JavascriptExecutor)webDriver).executeScript(token);

        //账号密码，按钮  ，直接模拟登录操作就简单了
//        webDriver.findElement(By.id("userAccount")).sendKeys("admin");
//        webDriver.findElement(By.id("userPwd")).sendKeys("admin");
//        webDriver.findElement(By.id("login")).click();



        Thread.sleep(2000);
        //报表页面
        webDriver.get(url);
        Thread.sleep(8000);


        //定位section元素
        WebElement element =  webDriver.findElement(By.tagName("body"));
        Point p = element.getLocation();
        int width = element.getSize().getWidth();
        int height = element.getSize().getHeight();
        Rectangle rec = new Rectangle(p.getX(),p.getY(),height,width);

        //截取全屏
        File scrFile  = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE);
        //在全屏图片下裁剪
        BufferedImage img = ImageIO.read(scrFile);
        BufferedImage dest = img.getSubimage(p.getX(), p.getY(),rec.getWidth(),rec.getHeight());
        ImageIO.write(dest, "png", scrFile);
        //拷贝文件
        FileUtils.copyFile(scrFile , new File("a.png"));
        log.info("截图完成");
        //退出
        webDriver.quit();
    }
}
