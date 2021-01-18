package model;

public class Room {

    private int id;
    private Game game;
    private int userInside;
    private boolean competitive;

    public Room(int id, Game Game, boolean competitive, int userInside){
        this.id = id;
        this.game = Game;
        this.competitive = competitive;
        this.userInside=userInside;
    }

    public Room(int id, int idGame,String titre,int score,int idUser,int timer,Boolean ready,String endMessage, boolean competitive, int userInside){
        this.id = id;
        this.game = new Game(idGame,titre,score,idUser,timer,ready,endMessage);
        this.competitive = competitive;
        this.userInside=userInside;
    }

    public int getId(){
        return this.id;
    }

    public Game getGame(){
        return this.game;
    }

    public boolean getCompetitive(){
        return this.competitive;
    }

    public void setGame(model.Game game) {
        this.game = game;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCompetitive(boolean competitive){
        this.competitive = competitive;
    }

    public int getUserInside() {
        return userInside;
    }

    public void setUserInside(int userInside) {
        this.userInside = userInside;
    }
}
