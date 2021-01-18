import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class KolejePane extends JLayeredPane {
    /*private BufferedImage[] loadImages() {
        BufferedImage[] images = new BufferedImage[fileNames.length];
        for (int i = 0; i < images.length; i++)
            try {
                URL url = getClass().getResource("img/" + fileNames[i] + ".jpg");
                images[i] = ImageIO.read(url);
            } catch (MalformedURLException mue) {
                System.out.println("Bad URL: " + mue.getMessage());
            } catch (IOException ioe) {
                System.out.println("Read trouble: " + ioe.getMessage());
            }
        return images;
    }*/
}
