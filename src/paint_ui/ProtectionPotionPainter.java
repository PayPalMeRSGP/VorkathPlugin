package paint_ui;

import org.osbot.rs07.canvas.paint.Painter;
import org.osbot.rs07.script.Plugin;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ProtectionPotionPainter implements Painter {

    private static final Color TRANS_GRAY = new Color(156,156,156,127);
    private Rectangle protectionTimerBG = new Rectangle(0, 0, 170, 70);
    private long antiFireExpiration = 0, antiVenomExpiration = 0;
    private BufferedImage antiVenomImg, antiFireImg;


    ProtectionPotionPainter(Plugin p) {
        try {
            antiVenomImg = ImageIO.read(p.getScriptResourceAsStream("resources/Anti-venom+(4).png"));
            antiFireImg = ImageIO.read(p.getScriptResourceAsStream("resources/Extended_super_antifire(4).png"));
        } catch (IOException e) {
            p.warn("unable to fetch image icons!");
            e.printStackTrace();
        }
    }

    @Override
    public void onPaint(Graphics2D g) {
        drawProtectionTimers(g);
    }

    private void drawProtectionTimers(Graphics2D g) {
        long currentUnix = System.currentTimeMillis() / 1000L;
        g.setColor(TRANS_GRAY);
        g.fill(protectionTimerBG);
        long remainingAntiVenom = antiVenomExpiration - currentUnix, remainingAntiFire = antiFireExpiration - currentUnix;

        if(remainingAntiVenom < 30) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.WHITE);
        }
        g.drawImage(antiVenomImg, null, protectionTimerBG.x + 8, protectionTimerBG.y + 2);
        g.drawString(remainingAntiVenom / 60 + ":" + remainingAntiVenom % 60, protectionTimerBG.x + 50, protectionTimerBG.y + 20);

        if(remainingAntiFire < 30) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.WHITE);
        }
        g.drawImage(antiFireImg, null, protectionTimerBG.x + 50, protectionTimerBG.y + 37);
        g.drawString(remainingAntiFire / 60 + ":" + remainingAntiFire % 60, protectionTimerBG.x + 50, protectionTimerBG.y + 55);
    }

    public void startAntiFireTimer() {
        antiFireExpiration = System.currentTimeMillis() / 1000L + 300;
    }

    public void startAntiVenomTimer() {
        antiVenomExpiration = System.currentTimeMillis() / 1000L + 300;
    }

}
