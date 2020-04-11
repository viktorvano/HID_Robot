package com.viktor.vano.hid.robot;

import com.sun.istack.internal.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class HidRobot {
    private static Robot robot = null;

    public static void main(String[] args)
    {
        System.out.println("Starting HID Robot...");

        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
            System.out.println("Failed to create a Robot...");
            System.exit(-10);
        }

        beepCountdown(3);
        BufferedImage image = null;

        //Open Chrome and verify reCAPTCHA
        moveCursorTo(155, 1054);
        click();
        moveCursorTo(232, 65);
        click();
        typeString("https://patrickhlauke.github.io/recaptcha/");
        keyStrokeEnter();
        //wait until web page loads
        boolean reCaptcha = false;
        int color;
        //this loop waits until the page is loaded and looks for a pixel of reCaptcha
        while(!reCaptcha)
        {
            try {
                image = captureScreen("screenshot", false);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            color = getColor(image, 350, 260);
            System.out.println("Color: " + "R=" + getColorRed(color) + " G=" + getColorGreen(color) + " B=" + getColorBlue(color));
            if(getColorRed(color) == 28 && getColorGreen(color) == 61 && getColorBlue(color) == 172)
                reCaptcha = true;
            sleep(200);
        }
        //click on the check box
        moveCursorTo(42, 274);
        click();
        sleep(4000);
        moveCursorTo(289, 20);
        click();

        //Open Jar file on Desktop
        moveCursorTo(994, 285);
        doubleClick();
        sleep(4000);
        closeAppAltF4();

        //Run MS Paint
        windowsStart();
        sleep(800);
        typeString("mspaint");
        sleep(800);
        keyStrokeEnter();

        System.out.println("Drawing a square...");
        int x = 600, y = 300;
        moveCursorTo(x, y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        sleep(50);
        moveCursorTo(x+100, y);
        moveCursorTo(x+100, y+100);
        moveCursorTo(x, y+100);
        moveCursorTo(x, y);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        sleep(4000);
        windowsDesktop();

        System.exit(0);
    }

    public static void sleep(int milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void moveCursorTo(int x, int y)
    {
        final int targetX = x, targetY = y;
        Point mousePointer = MouseInfo.getPointerInfo().getLocation();
        x = mousePointer.x;
        y = mousePointer.y;
        while(x != targetX || y != targetY)
        {
            if(x < targetX)
                x++;
            else if(x > targetX)
                x--;

            if(y < targetY)
                y++;
            else if(y > targetY)
                y--;

            robot.mouseMove(x, y);
            sleep(10);

            //update cursor location
            mousePointer = MouseInfo.getPointerInfo().getLocation();
            x = mousePointer.x;
            y = mousePointer.y;
            System.out.println("Cursor currently at: " + x + ", " + y);
            System.out.println("Cursor target at: " + targetX + ", " + targetY);
        }
    }

    public static BufferedImage captureScreen(String fileName, boolean saveImage) throws Exception
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(screenSize);
        Robot robotForImage = new Robot();
        BufferedImage image = robotForImage.createScreenCapture(screenRectangle);
        if(saveImage)
            ImageIO.write(image, "png", new File(fileName));
        return image;
    }

    public static void beepCountdown(int seconds)
    {
        System.out.println("Countdown set to: " + seconds + " seconds.");
        for(int i=0; i<seconds; i++)
        {
            sleep(1000);
            Toolkit.getDefaultToolkit().beep();
            System.out.println("Countdown: " + (seconds-i-1) + " seconds left.");
        }
    }

    public static int getColor(@NotNull BufferedImage image, int x, int y)
    {
        return image.getRGB(x, y) & 0x00FFFFFF;
    }

    public static int getColorRed(int color)
    {
        return (color & 0x00FF0000) >> 16;
    }

    public static int getColorGreen(int color)
    {
        return (color & 0x0000FF00) >> 8;
    }

    public static int getColorBlue(int color)
    {
        return color & 0x000000FF;
    }

    public static void click()
    {
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        sleep(800);
    }

    public static void doubleClick()
    {
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        sleep(40);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        sleep(800);
    }

    public static void keyStrokeEnter()
    {
        keyStroke(KeyEvent.VK_ENTER);
    }

    public static void windowsDesktop()
    {
        keyStroke(KeyEvent.VK_WINDOWS, KeyEvent.VK_D);
    }

    public static void windowsStart()
    {
        keyStroke(KeyEvent.VK_WINDOWS);
    }

    public static void closeAppAltF4()
    {
        keyStroke(KeyEvent.VK_ALT, KeyEvent.VK_F4);
    }

    public static void typeString(String text)
    {
        for(int i=0; i<text.length(); i++)
        {
            charKeyStroke(text.charAt(i));
            sleep(20);
        }
    }

    private static void keyStroke(int key)
    {
        robot.keyPress(key);
        robot.keyRelease(key);
        sleep(10);
    }

    private static void keyStroke(int key1, int key2)
    {
        robot.keyPress(key1);
        robot.keyPress(key2);
        sleep(10);
        robot.keyRelease(key1);
        robot.keyRelease(key2);
        sleep(10);
    }

    //Works with ENG US Keyboard Layout
    public static void charKeyStroke(char character)
    {
        switch (character) {
            case 'a': keyStroke(KeyEvent.VK_A); break;
            case 'b': keyStroke(KeyEvent.VK_B); break;
            case 'c': keyStroke(KeyEvent.VK_C); break;
            case 'd': keyStroke(KeyEvent.VK_D); break;
            case 'e': keyStroke(KeyEvent.VK_E); break;
            case 'f': keyStroke(KeyEvent.VK_F); break;
            case 'g': keyStroke(KeyEvent.VK_G); break;
            case 'h': keyStroke(KeyEvent.VK_H); break;
            case 'i': keyStroke(KeyEvent.VK_I); break;
            case 'j': keyStroke(KeyEvent.VK_J); break;
            case 'k': keyStroke(KeyEvent.VK_K); break;
            case 'l': keyStroke(KeyEvent.VK_L); break;
            case 'm': keyStroke(KeyEvent.VK_M); break;
            case 'n': keyStroke(KeyEvent.VK_N); break;
            case 'o': keyStroke(KeyEvent.VK_O); break;
            case 'p': keyStroke(KeyEvent.VK_P); break;
            case 'q': keyStroke(KeyEvent.VK_Q); break;
            case 'r': keyStroke(KeyEvent.VK_R); break;
            case 's': keyStroke(KeyEvent.VK_S); break;
            case 't': keyStroke(KeyEvent.VK_T); break;
            case 'u': keyStroke(KeyEvent.VK_U); break;
            case 'v': keyStroke(KeyEvent.VK_V); break;
            case 'w': keyStroke(KeyEvent.VK_W); break;
            case 'x': keyStroke(KeyEvent.VK_X); break;
            case 'y': keyStroke(KeyEvent.VK_Y); break;
            case 'z': keyStroke(KeyEvent.VK_Z); break;
            case 'A': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_A); break;
            case 'B': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_B); break;
            case 'C': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_C); break;
            case 'D': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_D); break;
            case 'E': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_E); break;
            case 'F': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_F); break;
            case 'G': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_G); break;
            case 'H': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_H); break;
            case 'I': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_I); break;
            case 'J': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_J); break;
            case 'K': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_K); break;
            case 'L': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_L); break;
            case 'M': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_M); break;
            case 'N': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_N); break;
            case 'O': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_O); break;
            case 'P': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_P); break;
            case 'Q': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_Q); break;
            case 'R': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_R); break;
            case 'S': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_S); break;
            case 'T': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_T); break;
            case 'U': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_U); break;
            case 'V': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_V); break;
            case 'W': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_W); break;
            case 'X': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_X); break;
            case 'Y': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_Y); break;
            case 'Z': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_Z); break;
            case '`': keyStroke(KeyEvent.VK_BACK_QUOTE); break;
            case '0': keyStroke(KeyEvent.VK_0); break;
            case '1': keyStroke(KeyEvent.VK_1); break;
            case '2': keyStroke(KeyEvent.VK_2); break;
            case '3': keyStroke(KeyEvent.VK_3); break;
            case '4': keyStroke(KeyEvent.VK_4); break;
            case '5': keyStroke(KeyEvent.VK_5); break;
            case '6': keyStroke(KeyEvent.VK_6); break;
            case '7': keyStroke(KeyEvent.VK_7); break;
            case '8': keyStroke(KeyEvent.VK_8); break;
            case '9': keyStroke(KeyEvent.VK_9); break;
            case '-': keyStroke(KeyEvent.VK_MINUS); break;
            case '=': keyStroke(KeyEvent.VK_EQUALS); break;
            case '~': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_QUOTE); break;
            case '!': keyStroke(KeyEvent.VK_EXCLAMATION_MARK); break;
            case '@': keyStroke(KeyEvent.VK_AT); break;
            case '#': keyStroke(KeyEvent.VK_NUMBER_SIGN); break;
            case '$': keyStroke(KeyEvent.VK_DOLLAR); break;
            case '%': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_5); break;
            case '^': keyStroke(KeyEvent.VK_CIRCUMFLEX); break;
            case '&': keyStroke(KeyEvent.VK_AMPERSAND); break;
            case '*': keyStroke(KeyEvent.VK_ASTERISK); break;
            case '(': keyStroke(KeyEvent.VK_LEFT_PARENTHESIS); break;
            case ')': keyStroke(KeyEvent.VK_RIGHT_PARENTHESIS); break;
            case '_': keyStroke(KeyEvent.VK_UNDERSCORE); break;
            case '+': keyStroke(KeyEvent.VK_PLUS); break;
            case '\t': keyStroke(KeyEvent.VK_TAB); break;
            case '\n': keyStroke(KeyEvent.VK_ENTER); break;
            case '[': keyStroke(KeyEvent.VK_OPEN_BRACKET); break;
            case ']': keyStroke(KeyEvent.VK_CLOSE_BRACKET); break;
            case '\\': keyStroke(KeyEvent.VK_BACK_SLASH); break;
            case '{': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_OPEN_BRACKET); break;
            case '}': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_CLOSE_BRACKET); break;
            case '|': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_SLASH); break;
            case ';': keyStroke(KeyEvent.VK_SEMICOLON); break;
            case ':': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_SEMICOLON); break;
            case '\'': keyStroke(KeyEvent.VK_QUOTE); break;
            case '"': keyStroke(KeyEvent.VK_QUOTEDBL); break;
            case ',': keyStroke(KeyEvent.VK_COMMA); break;
            case '<': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_COMMA); break;
            case '.': keyStroke(KeyEvent.VK_PERIOD); break;
            case '>': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_PERIOD); break;
            case '/': keyStroke(KeyEvent.VK_SLASH); break;
            case '?': keyStroke(KeyEvent.VK_SHIFT, KeyEvent.VK_SLASH); break;
            case ' ': keyStroke(KeyEvent.VK_SPACE); break;
            default:
                System.out.println("Cannot type this char: " + character + "its numeric value: " + (int)character);
        }
    }
}
