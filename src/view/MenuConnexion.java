//Codé par alan

package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;


public class MenuConnexion extends JPanel implements ActionListener {

    private JPanel login;
    private JPanel mdp;
    private JPanel conteneurinscription;

    private JButton connexion;

    private JTextField saisieidentifiant;
    private JTextField saisiemotdepasse;

    private JLabel identifiant;
    private JLabel motdepasse;
    private JLabel inscription;

    public MenuConnexion() {

        //creation de la partie login


        login = new JPanel();
        login.setLayout(new FlowLayout(FlowLayout.CENTER,30,0));

        identifiant = new JLabel("Identifiant :");
        saisieidentifiant = new JTextField();
        saisieidentifiant.setColumns(30);

        login.add(identifiant);
        login.add(saisieidentifiant);

        //creation de la partie motdepasse

        mdp = new JPanel();
        mdp.setLayout(new FlowLayout(FlowLayout.CENTER,10,0));
        motdepasse = new JLabel("Mot de passe :");
        saisiemotdepasse = new JTextField();
        saisiemotdepasse.setColumns(30);

        mdp.add(motdepasse);
        mdp.add(saisiemotdepasse);

        //creation du bouton de connexion

        connexion = new JButton("Connexion");
        connexion.setBackground(Color.GREEN);

        //création du lien vers l'inscription
        conteneurinscription = new JPanel();
        conteneurinscription.setLayout(new FlowLayout());

        inscription = new JLabel("s'inscrire");
        inscription.setForeground(Color.BLUE);





        this.add(Box.createRigidArea(new Dimension(0, 150)));
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        login.setMaximumSize(new Dimension(800,50));
        this.add(login);
        mdp.setMaximumSize(new Dimension(800,30));
        this.add(mdp);
        this.add(inscription);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(connexion);
        this.setVisible(true);

    }

    public void actionPerformed(ActionEvent event) {
        
    }
}


