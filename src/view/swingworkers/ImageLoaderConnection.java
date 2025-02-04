package view.swingworkers;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import launcher.Main;
import view.ConnectionMenu;

/**
* Chargement de l'image de fond pour l'interface de connection
*/
public final class ImageLoaderConnection extends SwingWorker<BufferedImage,BufferedImage> {

    private ConnectionMenu panel;

    public ImageLoaderConnection(ConnectionMenu panel){
        this.panel = panel;
    }

    @Override
    protected BufferedImage doInBackground() {
        InputStream is  = Main.class.getResourceAsStream("/image/FondConnection2.png");
        BufferedImage backgroundConnexion = null;
        try {
            backgroundConnexion = ImageIO.read(is);
            backgroundConnexion.getScaledInstance(720, 450, Image.SCALE_FAST);
            backgroundConnexion.setAccelerationPriority(1);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return backgroundConnexion;
    }

    @Override
    protected void done() {
        try{
            BufferedImage image = this.get();
            this.panel.imageLoaded(image);
        } catch (ExecutionException e) {
            System.out.println("ImageLoaderConnection ExecutionException"+ e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("ImageLoaderConnection InterruptedException"+ e.getMessage());
        }
    }

}
