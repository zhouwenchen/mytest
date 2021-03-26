package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *  这个可以实现的
 */
public class Picture1 {

//    private  static String path = "/data/datacenter/pic/";
    private  static String path = "c:\\test\\";
    public static void main(String[] args) throws MalformedURLException, IOException, URISyntaxException, AWTException {
        //此方法仅用于JDK1.6及以上版本
        String url = "https://www.csdn.net/";
        if(args == null){
            url = args[0];
        }
        Desktop.getDesktop().browse(new URI(url));
        Robot robot = new Robot();
        robot.delay(1000);

        Dimension d = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
        int width = (int) d.getWidth();
        int height = (int) d.getHeight();
        //最大化浏览器
//        robot.keyPress(KeyEvent.VK_F11);
        robot.delay(2000);
        Image image = robot.createScreenCapture(new Rectangle(0, 0, width, height));
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics q = bi.createGraphics();
        q.drawImage(image, 0, 0, width, height, null);
        String FileName = System.currentTimeMillis()+".jpg";
        ImageIO.write(bi, "jpg", new java.io.File(path + FileName));
        System.out.println(path + FileName);
    }
}
