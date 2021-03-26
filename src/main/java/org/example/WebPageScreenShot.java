package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 网页截图
 */
public class WebPageScreenShot {
    /**
     * fireFox驱动路径
     */
//    String geckoDriver = "C:\\Program Files\\Python37\\geckodriver.exe";
    String geckoDriver = "C:\\test\\geckodriver-v0.29.0-win64\\geckodriver.exe";
    String type="png";

    public void loadPage(String logonUrl,String actionUrl,String resultPath) throws IOException, InterruptedException {
        System.setProperty("webdriver.gecko.driver", geckoDriver);//chromedriver服务地址
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("-headless");
        //firefoxOptions.addArguments("--start-maximized");
        //firefoxOptions.addArguments("--start-fullscreen");
        firefoxOptions.setHeadless(true);
        FirefoxDriver driver = new FirefoxDriver(firefoxOptions); //新建一个WebDriver 的对象，但是new 的是FirefoxDriver的驱动
//        driver.get(logonUrl);//打开指定的网站
//        driver.findElement(By.id("J_username")).sendKeys(new String[]{"***"});
//        driver.findElement(By.id("J_password")).sendKeys(new String[]{"***"});
//        driver.findElement(By.id("loginBtn")).click();//登录
//        (new WebDriverWait(driver, 20)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".index-welcome-info")));//检查到登录成功
//        driver.get(actionUrl);//打开指定的网站
        driver.manage().window().setSize(new Dimension(1900, 800));
        //driver.manage().window().maximize();
        String js1 = "return document.body.clientHeight.toString()";
        String js1_result = driver.executeScript(js1) + "";
        int height = Integer.parseInt(js1_result);
        List<String> files = new ArrayList<String>();
        int last_t = 0;
        for (int i = 0; i < 20; ) {
            int currentHeight = (i * height);
            String js = "window.scrollTo(0," + currentHeight + ");";
            driver.executeScript(js);
            js1 = "return document.body.scrollHeight.toString()+','+document.body.scrollTop.toString()";
            js1_result = driver.executeScript(js1) + "";
            /**
             *    real_scroll_h, real_top = js1_result.split(',')[0], js1_result.split(',')[1]
             *  22             #real_scroll_h, real_top 是当前滚动条长度和当前滚动条的top，作为是否继续执行的依据，由于存在滚动条向下拉动后会加载新内容的情况，所以需要以下的判断
             *  23             #如果这次设置的top成功，则继续滚屏
             */
            int real_scroll_h = Integer.parseInt(js1_result.split(",")[0]);
            int real_top = Integer.parseInt(js1_result.split(",")[1]);
            //#real_scroll_h, real_top 是当前滚动条长度和当前滚动条的top，作为是否继续执行的依据，由于存在滚动条向下拉动后会加载新内容的情况，所以需要以下的判断
            if (real_top == currentHeight) {
                //  #如果这次设置的top成功，则继续滚屏
                i++;
                files.add(screenshot(driver).getAbsolutePath());
                last_t = real_top;
            } else {
                // #如果本次设置失败，看这次的top和上一次记录的top值是否相等，相等则说明没有新加载内容，且已到页面底，跳出循环
                if (real_top != last_t) {
                    last_t = real_top;
                } else {
                    files.add(screenshot(driver).getAbsolutePath());
                    break;
                }
            }
        }
        driver.quit();//退出浏览器
        merge(files.toArray(new String[]{}), type, resultPath);
    }


    private File screenshot(WebDriver driver) throws InterruptedException, IOException {
        try {
            /**
             * WebDriver自带了一个智能等待的方法。
             dr.manage().timeouts().implicitlyWait(arg0, arg1）；
             Arg0：等待的时间长度，int 类型 ；
             Arg1：等待时间的单位 TimeUnit.SECONDS 一般用秒作为单位。
             */
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread.sleep(10000);//等等页面加载完成
        /**
         * dr.quit()和dr.close()都可以退出浏览器,简单的说一下两者的区别：第一个close，
         * 如果打开了多个页面是关不干净的，它只关闭当前的一个页面。第二个quit，
         * 是退出了所有Webdriver所有的窗口，退的非常干净，所以推荐使用quit最为一个case退出的方法。
         */
        byte[] imageBytes = (byte[]) ((FirefoxDriver) driver).getScreenshotAs(new OutputType<Object>() {
            public Object convertFromBase64Png(String s) {
                try {
                    return (new BASE64Decoder()).decodeBuffer(s);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            public Object convertFromPngBytes(byte[] bytes) {
                return bytes;
            }
        });
        ByteArrayInputStream bytes = new ByteArrayInputStream(imageBytes);
        BufferedImage image = ImageIO.read(bytes);
        File file = File.createTempFile((new Random()).nextInt()+"",type);
        ImageIO.write(image, "png", file);
        return file;
    }

    /**
     * Java拼接多张图片
     *
     * @param pics
     * @param type
     * @param dst_pic
     * @return
     */
    public static boolean merge(String[] pics, String type, String dst_pic) {

        int len = pics.length;
        if (len < 1) {
            System.out.println("pics len < 1");
            return false;
        }
        File[] src = new File[len];
        BufferedImage[] images = new BufferedImage[len];
        int[][] ImageArrays = new int[len][];
        for (int i = 0; i < len; i++) {
            try {
                src[i] = new File(pics[i]);
                images[i] = ImageIO.read(src[i]);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            int width = images[i].getWidth();
            int height = images[i].getHeight();
            ImageArrays[i] = new int[width * height];// 从图片中读取RGB
            ImageArrays[i] = images[i].getRGB(0, 0, width, height,
                    ImageArrays[i], 0, width);
        }

        int dst_height = 0;
        int dst_width = images[0].getWidth();
        for (int i = 0; i < images.length; i++) {
            dst_width = dst_width > images[i].getWidth() ? dst_width
                    : images[i].getWidth();

            dst_height += images[i].getHeight();
        }
        if (dst_height < 1) {
            System.out.println("dst_height < 1");
            return false;
        }

        // 生成新图片
        try {
            // dst_width = images[0].getWidth();
            BufferedImage ImageNew = new BufferedImage(dst_width, dst_height,
                    BufferedImage.TYPE_INT_RGB);
            int height_i = 0;
            for (int i = 0; i < images.length; i++) {
                ImageNew.setRGB(0, height_i, dst_width, images[i].getHeight(),
                        ImageArrays[i], 0, dst_width);
                height_i += images[i].getHeight();
            }

            File outFile = new File(dst_pic);
            ImageIO.write(ImageNew, type, outFile);// 写图片

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            /*
               删除临时文件
             */
            for(String tempFile:pics){
                File t=new File(tempFile);
                t.delete();
            }
        }

        return true;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        WebPageScreenShot screenShot=new WebPageScreenShot();
        screenShot.loadPage("******","*******","D:\\TEST.png");
    }

}