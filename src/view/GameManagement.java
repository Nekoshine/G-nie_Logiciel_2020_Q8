package view;

import sockets.Admin;
import database.DBEnigma;
import database.DBGame;
import database.DBRoom;
import launcher.Main;
import model.EnigmaList;
import model.Game;
import model.GameList;
import view.style.ColorPerso;
import view.style.FontPerso;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;


public class GameManagement extends JPanel implements ActionListener, MouseListener {

    private GameList ListGame;


    /* Panel */
    private final JPanel listPanel;
    private final JPanel newButtonPanel;
    private final JPanel gamePanel;

    /* Boutons */
    private final JButton returnButton;
    private JButton newButton;

    private GlobalFrame frame;



    private static volatile GameManagement INSTANCE = new GameManagement(Main.frame,-1);

    /**
     * Interface de gestion des Jeux
     * @param frame fenêtre d'affichage
     * @param roomNumber n° de la salle dont on veut changer le jeu
     */
    private GameManagement(GlobalFrame frame, int roomNumber){

        this.frame = frame;
        /* Provenance */
        frame.roomNumber = roomNumber;

        /* Recuperation des jeux du User */
        this.ListGame= DBGame.getGames(Main.idAdmin);

        /* Déclaration JPanel - JScrollPane */
        listPanel = new JPanelImage(Main.class.getResourceAsStream("/image/fondGestion.jpg"),frame.windowSize);
        gamePanel = new JPanelImage(Main.class.getResourceAsStream("/image/fondGestion.jpg"),frame.windowSize);
        JPanel titlePanel = new JPanel();
        JPanel returnPanel = new JPanel();
        newButtonPanel = new JPanel();
        newButtonPanel.setBackground(ColorPerso.grisClair);
        newButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        newButtonPanel.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(gamePanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);

        /* Déclaration Layouts */
        BorderLayout mainLayout = new BorderLayout(10, 10);
        BorderLayout centerLayout = new BorderLayout(4, 4);
        GridBagLayout listLayout = new GridBagLayout();
        gamePanel.setLayout(listLayout);
        gamePanel.setBackground(ColorPerso.grisClair);

        /* Contraintes GridBag */
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7,15,7,30);

        /* Déclaration Boutons */
        returnButton = new JButton("Retour");
        returnButton.setBackground(ColorPerso.rouge);
        returnButton.setForeground(Color.white);
        returnButton.setPreferredSize(new Dimension(100,30));
        returnButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        if(frame.roomNumber==-1) {
            newButton = new JButton("Créer un nouveau jeu");
            newButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            newButton.addActionListener(this);
            newButton.addMouseListener(this);
            newButton.setBackground(ColorPerso.grisFonce);
            newButton.setForeground(Color.white);
            newButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            newButton.setPreferredSize(new Dimension(210,45));
        }

        this.createList();

        /* Setup Marges */
        Border mainPadding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Border listPadding = BorderFactory.createEmptyBorder(20, 10, 10, 10);
        this.setBorder(mainPadding);
        listPanel.setBorder(listPadding);

        /* Setup listPanel */
        listPanel.setLayout(centerLayout);
        listPanel.add(scrollPane,BorderLayout.CENTER);
        listPanel.add(newButtonPanel, BorderLayout.PAGE_END);
        listPanel.setBackground(ColorPerso.grisClair);
        listPanel.setBorder(BorderFactory.createLineBorder(Color.black,2));

        if(frame.roomNumber==-1) {
            newButtonPanel.add(newButton);
        }
        /* Setup Titre */
        JLabel titre = new JLabel("MJ - Gestion des Jeux");
        titre.setFont(FontPerso.ArialBold);
        titlePanel.setBorder(BorderFactory.createLineBorder(Color.black,2));
        titlePanel.add(titre);

        /* Setup bouton retour */
        returnPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        returnPanel.setBackground(ColorPerso.darkGray);
        returnPanel.add(returnButton);

        returnButton.addActionListener(this);
        returnButton.addMouseListener(this);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                GlobalFrame.windowSize = Main.frame.getSize();
                INSTANCE.revalidate();
                INSTANCE.repaint();
            }
        });

        this.setLayout(mainLayout);
        this.setBackground(ColorPerso.darkGray);
        this.add(listPanel, BorderLayout.CENTER);
        this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        this.add(titlePanel, BorderLayout.PAGE_START);
        this.add(returnPanel, BorderLayout.PAGE_END);
        this.setVisible(true);


    }

    /**
     * Récupération de l'instance de GameManagement
     * @param frame fenêtre d'affichage
     * @param roomNumber n° de la salle dont on veut changer le jeu
     * @return Retourne l'instance de GameManagement
     */

    public static GameManagement getInstance(GlobalFrame frame, int roomNumber) {
        //Le "Double-Checked Singleton"/"Singleton doublement vérifié" permet
        //d'éviter un appel coûteux à synchronized,
        //une fois que l'instanciation est faite.
        if (INSTANCE == null) {
            // Le mot-clé synchronized sur ce bloc empêche toute instanciation
            // multiple même par différents "threads".
            // Il est TRES important.
            synchronized(INSTANCE) {
                if (INSTANCE == null) {
                    INSTANCE = new GameManagement(frame, roomNumber);
                }
            }
        }
        else {
            INSTANCE.frame=frame;
            INSTANCE.frame.roomNumber=roomNumber;
            INSTANCE.ListGame= DBGame.getGames(Main.idAdmin);
            INSTANCE.createList();
            if(roomNumber==-1){
                INSTANCE.newButton.setVisible(true);
            }
            else{
                INSTANCE.newButton.setVisible(false);
            }
            INSTANCE.returnButton.setBackground(ColorPerso.rouge);
            INSTANCE.returnButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            INSTANCE.newButton.setBackground(ColorPerso.grisFonce);
            INSTANCE.newButton.setForeground(Color.white);
            INSTANCE.newButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        }
        return INSTANCE;
    }

    /**
     * Ajout d'un jeu à l'affichage
     * @param jeu Jeu associé
     * @param gbc Contraintes d'affichage
     * @param i N° du jeu dans la liste
     * @return Retourne le Panel associé au nouveau
     */
    private JPanel ajoutJeu(Game jeu, GridBagConstraints gbc, int i) {

        /* Contraintes GridBag */
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = jeu.getId() - 1;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        /* Ajout Panel */
        JPanel panelJeu = new JPanel();
        JPanel panelDelete = new JPanel();
        JPanel panelModify = new JPanel();
        JPanel panelChoose = new JPanel();

        GridLayout grille = new GridLayout(1,4,20,0);


        JLabel numJeu = new JLabel("Jeu  " + i + " :");
        numJeu.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel nomJeu = new JLabel(jeu.getTitre());
        nomJeu.setHorizontalAlignment(SwingConstants.CENTER);

        panelJeu.add(numJeu);
        panelJeu.add(nomJeu);
        if(frame.roomNumber==-1) {
            panelModify.setLayout(new BorderLayout());
            JButton buttonModify = new JButton("Modifier");
            buttonModify.setBackground(ColorPerso.jaune);
            buttonModify.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            panelModify.add(buttonModify,BorderLayout.CENTER);
            panelModify.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
            buttonModify.setPreferredSize(new Dimension( 200, 50));
            buttonModify.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Game jeuChoisi = ListGame.findByID(jeu.getId());
                    Main.ListEnigma= DBEnigma.getEnigmas(jeuChoisi.getId());
                    frame.gameCreationDisplay(frame,jeuChoisi);
                }
            });
            buttonModify.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {

                    buttonModify.setBackground(ColorPerso.jauneHoover);
                    buttonModify.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    buttonModify.setBackground(ColorPerso.jaune);
                    buttonModify.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                }
            });


            panelDelete.setLayout(new BorderLayout());
            JButton buttonDelete = new JButton("Supprimer");
            buttonDelete.setBackground(ColorPerso.rouge);
            buttonDelete.setForeground(Color.white);
            buttonDelete.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            buttonDelete.setPreferredSize(new Dimension( 200, 50));
            panelDelete.add(buttonDelete,BorderLayout.CENTER);
            panelDelete.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
            buttonDelete.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Game jeuChoisi = ListGame.findByID(jeu.getId());
                    String[] options = {"Oui", "Non"};
                    int reponse = JOptionPane.showOptionDialog
                            (null, "Voulez vous supprimer le jeu : \""+jeuChoisi.getTitre()+"\"",
                                    "Nouveau Joueur",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null, // pas d'icone
                                    options, // titres des boutons
                                    null); // désactiver la touche ENTER
                    if (reponse == JOptionPane.YES_OPTION) {
                        if(!DBGame.deleteGame(jeuChoisi.getId())){
                            Toolkit.getDefaultToolkit().beep();
                            JOptionPane.showMessageDialog(frame, "Le jeu ne peut pas être supprimé car il est utilisé par une salle", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    Main.frame.setContentPane(getInstance(frame,frame.roomNumber));
                }
            });
            buttonDelete.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    buttonDelete.setBackground(ColorPerso.rougeHoover);
                    buttonDelete.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    buttonDelete.setBackground(ColorPerso.rouge);
                    buttonDelete.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                }
            });

            panelJeu.add(panelModify);
            panelJeu.add(panelDelete);
        }
        else {

            BorderLayout checkLayout = new BorderLayout();
            JButton boutonChoix = new JButton("Choisir");
            boutonChoix.setPreferredSize(new Dimension( 200, 50));
            boutonChoix.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            JPanel checkPanel = new JPanel();
            checkPanel.setLayout(new BorderLayout());
            JCheckBox competitionCheck = new JCheckBox("  Mode Compétitif");
            ImageIcon selected = null;
            ImageIcon unselected = null;
            try {
                selected = new ImageIcon(new ImageIcon(ImageIO.read(Main.class.getResourceAsStream("/image/checkboxON.png"))).getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
                unselected = new ImageIcon(new ImageIcon(ImageIO.read(Main.class.getResourceAsStream("/image/checkboxOFF.png"))).getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageIcon finalSelected = selected;
            ImageIcon finalUnselected = unselected;
            competitionCheck.setIcon(finalUnselected);
            competitionCheck.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (competitionCheck.isSelected()) {
                        competitionCheck.setIcon(finalSelected);
                    }
                    else {
                        competitionCheck.setIcon(finalUnselected);
                    }
                }
            });
            competitionCheck.setHorizontalAlignment(SwingConstants.CENTER);
            competitionCheck.setVerticalAlignment(SwingConstants.CENTER);
            checkPanel.add(competitionCheck, BorderLayout.CENTER);
            panelChoose.setLayout(checkLayout);
            panelChoose.add(boutonChoix,BorderLayout.CENTER);
            panelChoose.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));

            boutonChoix.setBackground(ColorPerso.vert);
            boutonChoix.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println(competitionCheck.isSelected());
                    Game oldGame = Main.ListRoom.findByID(frame.roomNumber).getGame();
                    int idOldGame=0;
                    if(oldGame!=null) {
                        //recuperation de l'ancien jeu si on ne créé pas une salle
                        idOldGame = oldGame.getId();
                    }

                    //mise a jour dans la liste
                    Main.ListRoom.findByID(frame.roomNumber).setGame(ListGame.findByID(jeu.getId()));

                    int idRoom = Main.ListRoom.findByID(frame.roomNumber).getId();

                    if(DBRoom.isInDB(idRoom,idOldGame)) {
                        DBRoom.majRoom(Main.ListRoom.findByID(frame.roomNumber).getId(),jeu.getId(),competitionCheck.isSelected(),-1);
                    }
                    else{
                        DBRoom.insertRoom(Main.ListRoom.findByID(frame.roomNumber).getId(), jeu.getId(),competitionCheck.isSelected(),-1);
                    }
                    Admin.refreshRoomAccess(Main.idAdmin);
                    frame.roomManagementDisplay(frame);

                }
            });
            boutonChoix.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    boutonChoix.setBackground(ColorPerso.vertHoover);
                    boutonChoix.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    boutonChoix.setBackground(ColorPerso.vert);
                    boutonChoix.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                }
            });
            panelJeu.add(panelChoose);
            panelJeu.add(checkPanel);

        }
        panelJeu.setLayout(grille);

        /* Configuration panelSalle */
        panelJeu.setBorder(BorderFactory.createLineBorder(Color.BLACK,3));

        return panelJeu;

    }

    /**
     * Charge les jeux dans la fenêtre
     */

    private void createList() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7,15,7,30);
        int nbGames = ListGame.getSize();
        gamePanel.removeAll();

        for(int i = 0; i<nbGames; i++){
            listPanel.remove(newButtonPanel);
            JPanel panelGame = this.ajoutJeu(ListGame.getGame(i), gbc,i+1);
            panelGame.setPreferredSize(new Dimension((int) (listPanel.getWidth() - 85), 100));
            gamePanel.add(panelGame, gbc);
            listPanel.add(newButtonPanel, BorderLayout.PAGE_END);
            listPanel.revalidate();
            listPanel.repaint();
        }

        listPanel.revalidate();
        listPanel.repaint();
        frame.repaint();

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == returnButton){
            if (frame.roomNumber==-1){
                frame.mainMenuDisplay(frame);
            }
            else {
                frame.roomManagementDisplay(frame);
            }
        }
        else if (e.getSource() == newButton){
            Main.ListEnigma= new EnigmaList();
            frame.gameCreationDisplay(frame,null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource()==returnButton) {
            returnButton.setBackground(ColorPerso.rougeHoover);
            returnButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }
        else if(e.getSource()==newButton){
            newButton.setBackground(Color.BLACK);
            newButton.setOpaque(true);
            newButton.setForeground(Color.white);
            newButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource()==returnButton) {
            returnButton.setBackground(ColorPerso.rouge);
            returnButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        }
        else if(e.getSource()==newButton){
            newButton.setOpaque(true);
            newButton.setBackground(ColorPerso.grisFonce);
            newButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        }
    }
}
