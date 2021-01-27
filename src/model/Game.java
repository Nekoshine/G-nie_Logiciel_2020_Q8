package model;

import java.io.Serializable;

/**
* Représentation d'un jeu
*/
public final class Game implements Serializable {
    private int id;
    private String titre;
    private int score;
    private int idUser;
    private int timer;
    private Boolean ready;
    private String endMessage;

    public Game(int id,String titre,int score,int idUser,int timer,Boolean ready,String endMessage){
        this.id = id;
        this.titre = titre;
        this.score = score;
        this.idUser = idUser;
        this.timer = timer;
        this.ready=ready;
        this.endMessage=endMessage;
    }

    public int getId(){
    return this.id;
    }

    public String getTitre(){
    return this.titre;
    }

    public int getScore(){
    return this.score;
    }

    public int getTimer(){
    return this.timer;
    }

    public Boolean getReady(){
    return this.ready;
    }

    public String getEndMessage() {
    return this.endMessage;
    }

    public void setTitre(String titre) {
    this.titre = titre;
    }

    public void setScore(int score) {
    this.score = score;
    }

    public int getIdUser() {
    return this.idUser;
    }
}
