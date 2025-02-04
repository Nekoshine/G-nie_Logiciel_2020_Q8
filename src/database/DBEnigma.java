package database;

import model.Enigma;
import model.EnigmaList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Tout les fonctions liée à la table Enigma dans la BDD
 */
public class DBEnigma extends DBConnexion{

      /**
      * Connexion à la BDD ou récupération si elle existe déjà
      */
      private DBEnigma(){
            super.getConnexion();
      }

      /**
       * Recuperer les enigmes d'un jeu dans la BDD
       * @param idGame l'identifiant du jeu
       * @return la liste des enigmes du jeu
       */
      public static EnigmaList getEnigmas(int idGame){
            EnigmaList enigmaList = new EnigmaList();

            String clue2;
            int timer2;
            String clue3;
            int timer3;

            try{
                  PreparedStatement requete = DBConnexion.getConnexion().prepareStatement("Select * from Enigma,Game WHERE Enigma.idGame = Game.id AND Enigma.idGame = ? ORDER BY Enigma.id ASC");
                  requete.setString(1, String.valueOf(idGame));
                  ResultSet resultat = requete.executeQuery();
                  while (resultat.next()) { // On itère chaque résultat

                        // si les indices sont null ont met "" dans les indices et -1 dans les timers
                        clue2= resultat.getString("clue2");
                        if(resultat.wasNull()){
                              clue2="";
                        }
                        timer2= resultat.getInt("timer2");
                        if(resultat.wasNull()){
                              timer2= -1;
                        }
                        clue3= resultat.getString("clue3");
                        if(resultat.wasNull()){
                              clue3="";
                        }
                        timer3= resultat.getInt("timer3");
                        if(resultat.wasNull()){
                              timer3=-1;
                        }

                        //on ajoute l'enigme a la liste
                        enigmaList.addEnigma(
                        new Enigma(resultat.getInt("id"),resultat.getString("text"),resultat.getString("answer"),
                        resultat.getString("clue1"),resultat.getInt("timer1"),clue2,timer2,
                        clue3,timer3));
                  }
                  requete.close();
                  resultat.close();
            } catch(SQLException e ){
                  System.err.println("Erreur requete getEnigmas: " + e.getMessage());
            }
            return enigmaList;
      }

      /**
      * Détermine si une énigme est dans la BDD
      * @param id l'identifiant de l'énigme recherché
      * @return true si l'énigme est dans la BDD
      */
      public static boolean isInDB(int id){
            boolean isHere = false;
            try {
                  PreparedStatement requetePresence = DBConnexion.getConnexion().prepareStatement("SELECT * FROM Enigma WHERE id=?");
                  requetePresence.setString(1, String.valueOf(id));
                  ResultSet resultatPresence = requetePresence.executeQuery();

                  //si on a un resultat, l'enigme est dans la BDD
                  if(resultatPresence.next()){
                        isHere=true;
                  }
                  requetePresence.close();
                  resultatPresence.close();
            }
            catch(SQLException e ){
            System.err.println("Erreur requete isInDB: " + e.getMessage());
            }
            return isHere;
      }

      /**
      * Fonction qui ajoute une énigme à la BDD
      * @param idGame l'identifiant du jeu auquel appartient l'énigme
      * @param text la question de l'énigme
      * @param answer la réponse à la question
      * @param clue1 le 1er indice (ne peut pas être null)
      * @param timer1 la durée en seconde au bout de laquel la 1er indice est débloqué (ne peut pas être null)
      * @param clue2 le 1er indice
      * @param timer2 la durée en seconde au bout de laquel la 1er indice est débloqué
      * @param clue3 le 1er indice
      * @param timer3 la durée en seconde au bout de laquel la 1er indice est débloqué
      * @return true si l'ajout a fonctionné
      */
      public static boolean insertEnigma(int idGame,String text,String answer, String clue1, int timer1,String clue2, int timer2,String clue3, int timer3){
            boolean inserted = false;
            try{
                  PreparedStatement requete = DBConnexion.getConnexion().prepareStatement("Insert into Enigma VALUES (default,?,?,?,?,?,?,?,?,?)");
                  requete.setInt(1,idGame);
                  requete.setString(2,text);
                  requete.setString(3,answer);
                  requete.setString(4,clue1);
                  requete.setInt(5,timer1);

                  // si les indices sont null ou vide on mets null dans la BDD
                  //si les timers valent -1 on mets null dans la BDD
                  if(clue2==null || clue2.isEmpty()){
                        requete.setNull(6, Types.NULL);
                  }
                  else {
                        requete.setString(6,clue2);
                  }

                  if(timer2==-1){
                        requete.setNull(7, Types.INTEGER);
                  }
                  else {
                        requete.setInt(7,timer2);
                  }

                  if(clue3==null || clue3.isEmpty()){
                        requete.setNull(8, Types.NULL);
                  }
                  else {
                        requete.setString(8,clue3);
                  }

                  if(timer3==-1){
                        requete.setNull(9, Types.INTEGER);
                  }
                  else {
                        requete.setInt(9,timer3);
                  }

                  requete.executeUpdate();
                  requete.close();

                  //verification de l'insertion
                  PreparedStatement requeteVerif = DBConnexion.getConnexion().prepareStatement("Select * from Enigma where text=? and idGame=?");  //c'est pas bien fait
                  requeteVerif.setString(1,text);
                  requeteVerif.setInt(2,idGame);
                  ResultSet resultatVerif = requeteVerif.executeQuery();
                  if(resultatVerif.next()){ // Si il a été inséré
                        inserted=true; // Alors on valide l insertion
                  }
                  resultatVerif.close();
                  requeteVerif.close();

            } catch(SQLException e ){
                  System.err.println("Erreur requete insertEnigma: " + e.getMessage());
            }
            return inserted;
      }

      /**
       * Fonction qui met à jour une énigme dans la BDD
       * @param id l'identificant de l'énigme à modifier
       * @param text la question de l'énigme
       * @param answer la réponse à la question
       * @param clue1 le 1er indice (ne peut pas être null)
       * @param timer1 la durée en seconde au bout de laquel la 1er indice est débloqué (ne peut pas être null)
       * @param clue2 le 1er indice
       * @param timer2 la durée en seconde au bout de laquel la 1er indice est débloqué
       * @param clue3 le 1er indice
       * @param timer3 la durée en seconde au bout de laquel la 1er indice est débloqué
       * @return true si la mise à jour a fonctionné (la vérification de la maj des timers n'est pas faites)
       */
      public static boolean majEnigma(int id, String text,String answer, String clue1, int timer1,String clue2, int timer2,String clue3, int timer3){
            boolean inserted = false;
            try{
                  PreparedStatement requete = DBConnexion.getConnexion().prepareStatement("UPDATE Enigma SET text = ?, answer = ?, clue1 = ?, timer1 = ?, clue2 = ? , timer2 = ?, clue3 = ?, timer3 = ? WHERE id=?");
                  requete.setString(1, text);
                  requete.setString(2, answer);
                  requete.setString(3, clue1);
                  requete.setInt(4, timer1);

                  if(clue2==null || clue2.isEmpty()){
                        requete.setNull(5, Types.NULL);
                  }
                  else {
                        requete.setString(5,clue2);
                  }

                  if(timer2==-1){
                        requete.setNull(6, Types.INTEGER);
                  }
                  else {
                        requete.setInt(6,timer2);
                  }

                  if(clue3==null || clue3.isEmpty()){
                        requete.setNull(7, Types.NULL);
                  }
                  else {
                        requete.setString(7,clue3);
                  }

                  if(timer3==-1){
                        requete.setNull(8, Types.INTEGER);
                  }
                  else {
                        requete.setInt(8,timer3);
                  }

                  requete.setInt(9, id);

                  requete.executeUpdate();
                  requete.close();

                  inserted = true; //normal il faut faire une verification pour mettre a true

            } catch(SQLException e ){
                  System.err.println("Erreur requete majEnigma: " + e.getMessage());
            }
            return inserted;
      }

      /**
      * Fonction qui va supprimer une enigme dans la BDD
      * @param id l'identifiant de l'enigme a supprimer
      * @return true si la suppression à reussi
      */
      public static boolean deleteEnigma(int id){
            boolean boolDelete=false;
            try{
                  //suppresion avec l'id
                  PreparedStatement requete = DBConnexion.getConnexion().prepareStatement("Delete from Enigma WHERE id=? ");
                  requete.setInt(1, id);
                  requete.executeUpdate();
                  requete.close();

                  //verification
                  PreparedStatement requeteVerif = DBConnexion.getConnexion().prepareStatement("Select * from Game where id=?");
                  requeteVerif.setInt(1, id);
                  ResultSet resultatVerif = requeteVerif.executeQuery();
                  if(!resultatVerif.next()){ // Si il a été supprimé
                        boolDelete=true; // Alors on valide la suppression
                  }
                  resultatVerif.close();
                  requeteVerif.close();
            } catch(SQLException e ){
                  System.err.println("Erreur requete deleteEnigma: " + e.getMessage());
            }
            return boolDelete;
      }
}
