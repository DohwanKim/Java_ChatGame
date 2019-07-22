package game;


import javax.swing.*;

public class ChatGame_Client {
    static ChatGame_Client_Controller panelCon;

    public void UIset(){
        panelCon.setTitle("ChatGame/ChatGame");
        panelCon.chatGameGame_room = new ChatGame_GameRoom(panelCon);
        panelCon.chatGameLogin = new ChatGame_Login(panelCon);
        panelCon.add(panelCon.chatGameLogin);
        panelCon.setSize(800, 620);
        panelCon.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panelCon.setResizable(false); //임의(마우스)로 창 크기 변경 금지
        panelCon.setVisible(true);
    }
    public static void main(String[] args){
        panelCon = new ChatGame_Client_Controller(args);
        ChatGame_Client FirstSet = new ChatGame_Client();
        FirstSet.UIset();
    }
}
