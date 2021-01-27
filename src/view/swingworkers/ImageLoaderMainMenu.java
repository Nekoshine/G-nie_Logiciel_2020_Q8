package view.swingworkers;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import launcher.Main;
import view.MainMenu;

/**
 * Chargement de l'image de fond pour l'interface le menu principale
 */
public final class ImageLoaderMainMenu extends SwingWorker<Image,Image> {

    private MainMenu panel;
    private Dimension dimension;

    public ImageLoaderMainMenu(MainMenu panel,Dimension dimension){
        this.panel = panel;
        this.dimension = dimension;

    }

    @Override
    protected Image doInBackground() {

        InputStream stream = Main.class.getResourceAsStream("/image/FondPrincipal.png");
        ImageIcon icon= null;

        try {
            ImageIcon imageIcon = new ImageIcon(ImageIO.read(stream));
            icon = new ImageIcon(imageIcon.getImage().getScaledInstance((int)dimension.getWidth(),(int)dimension.getHeight()-30, Image.SCALE_DEFAULT));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return icon.getImage();

    }

    @Override
    protected void done() {
        try {
            Image image = this.get();
            this.panel.imageLoaded(image);

        } catch (InterruptedException e) {
            System.out.println("ImageLoaderDefeat InterruptedException"+ e.getMessage());
        } catch (ExecutionException e) {
            System.out.println("ImageLoaderDefeat ExecutionException"+ e.getMessage());
        }

    }

}