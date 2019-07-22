package game;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/*
      소켓 통신용 클라이언트
 */
public class ChatGame_Client_TCP {
    GameData gameData; // 서버와 통신용 게임데이터
//    ChatGame_Client_Controller clientController;
    ArrayList<GameData> test;
    Socket socket; // 서버와 연결하는 소켓
    ObjectOutputStream out;  // 객체 전송용 스트림
    String name;
    Sound userComeIn = new Sound("userIn.mp3", false);
    Sound count3 = new Sound("count.mp3", false);
    Sound count2 = new Sound("count.mp3", false);
    Sound count = new Sound("count_last.mp3", false);
    Sound win = new Sound("win.mp3", false);
    Sound lose = new Sound("lose.mp3", false);

    public ChatGame_Client_TCP(String ServerIP, int port, String userID/*, ChatGame_Client_Controller clientController*/) {
        try {
//            this.clientController = clientController;
            // 소켓을 생성하여 연결을 요청한다.
            this.socket = new Socket(ServerIP, port); // socket은 server socket
            this.name = userID;
            System.out.println("서버에 연결되었습니다.");
            ClientSender sender = new ClientSender(socket, this.name);  // userID를 통해 맨 처음 유저 이름을 서버로 전달.
            ClientReceiver receiver = new ClientReceiver(socket);
            sender.start();
            receiver.start();
        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (Exception e) {}
    } // main


    class ClientSender extends Thread {
        Socket socket;

        ClientSender(Socket socket, String name) {
            gameData = new GameData(name); // 연결시 게임데이터 객체 생성
            this.socket = socket; // server socket
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
            } catch (Exception e) {}
        }
        public void run() {
            try {
                if(out!=null) {       // 맨 처음에 플레이어 이름 전송
                    out.writeObject(gameData.clone());
                }
            } catch (IOException e) {
            } catch (Exception e) {}
        }
    }

    class ClientReceiver extends Thread {
        Socket socket;
        ObjectInputStream in;
        int i = 0;
        int j = 0;
        ClientReceiver(Socket socket) {
            this.socket = socket;
            try {
                in = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {}
        }

        // UI 갱신 (점수, 플레이어 이름)
        public void renewUI() {
            for (String key : gameData.getScore().keySet()){
                if (i == 0) {
                    ChatGame_GameRoom.userA_info_inside.setText(key);
                    int tmp = gameData.getScore().get(key);
                    ChatGame_GameRoom.userA_Score_inside.setText("Score:" +String.valueOf(tmp));
                    i++;
                }else if(i == 1){
                    ChatGame_GameRoom.userB_info_inside.setText(key);
                    int tmp = gameData.getScore().get(key);
                    ChatGame_GameRoom.userB_Score_inside.setText("Score:" +String.valueOf(tmp));
                    i++;
                }else if(i == 2){
                    ChatGame_GameRoom.userC_info_inside.setText(key);
                    int tmp = gameData.getScore().get(key);
                    ChatGame_GameRoom.userC_Score_inside.setText("Score:" +String.valueOf(tmp));
                    i++;
                }else{
                    ChatGame_GameRoom.userD_info_inside.setText(key);
                    int tmp = gameData.getScore().get(key);
                    ChatGame_GameRoom.userD_Score_inside.setText("Score:" +String.valueOf(tmp));
                    i++;
                }
            }
            for (; i<4; ++i) {
                if(i == 1){
                    ChatGame_GameRoom.userB_info_inside.setText("");
                    ChatGame_GameRoom.userB_Score_inside.setText("0");
                    i++;
                }else if(i == 2){
                    ChatGame_GameRoom.userC_info_inside.setText("");
                    ChatGame_GameRoom.userC_Score_inside.setText("0");
                    i++;
                }else{
                    ChatGame_GameRoom.userD_info_inside.setText("");
                    ChatGame_GameRoom.userD_Score_inside.setText("0");
                    i++;
                }
            }
            i = 0;
        }

        public void run() {
            while (in!=null) {
                try {
                    gameData = (GameData)in.readObject();
                    if (gameData.getType()==GameData.USERLOGIN) {
                        ChatGame_GameRoom.serverMsg.setText(gameData.getName()+"님이 들어왔습니다.");
                        //게임 데이터가 들어올때마다 UI갱신
                        renewUI();
                        userComeIn.start();
                    } else if (gameData.getType()==GameData.GAMESTART) {
                        // 단어 입력란 활성화.
                        ChatGame_Client_Controller.chatGameGame_room.activateInputTextLine();
                        ChatGame_Client_Controller.chatGameGame_room.updateQLabel(gameData.getMsg());
                    } else if (gameData.getType()==GameData.GAMEND) {
                        if (gameData.getName().equals(name)) {
                            ChatGame_Client_Controller.lobbyBGM.close();
                            win.start();
                            win = new Sound("win.mp3", false);
                        } else {
                            ChatGame_Client_Controller.lobbyBGM.close();
                            lose.start();
                            lose = new Sound("lose.mp3", false);
                        }
                        ChatGame_Client_Controller.lobbyBGM.close();
                        ChatGame_Client_Controller.chatGameGame_room.updateQLabel(gameData.getMsg());
                        ChatGame_Client_Controller.chatGameGame_room.deActivateInputTestLine();
                        ChatGame_Client_Controller.chatGameGame_room.activateReadyButton();
                        ChatGame_Client_Controller.chatGameGame_room.updateAnsUsr(gameData.getName() + "님이 승리하였습니다!"); // 이긴사람=맞춘사람 표시
                        renewUI();
                        count3 = new Sound("count.mp3", false);
                        count2 = new Sound("count.mp3", false);
                        count = new Sound("count_last.mp3", false);
                        j = 0;
                    } else if (gameData.getType()==GameData.RIGHTANS) { // 정답을 맞추면 단어와 점수 갱신.
                        System.out.println(gameData.getMsg());
                        //게임 데이터가 들어올때마다 UI갱신
                        renewUI();
                        ChatGame_Client_Controller.chatGameGame_room.updateQLabel(gameData.getMsg());
                        ChatGame_Client_Controller.chatGameGame_room.updateAnsUsr(gameData.getName() + "님이 정답을 맞췄습니다!"); // 맞춘사람 표시
                    } else if (gameData.getType()==GameData.COUNTER) {
                        if(j == 0){
                            count3.start();
                            j++;
                        }else if (j == 1){
                            count2.start();
                            j++;
                        }else {
                            count.start();
                        }
                    } else if (gameData.getType()==GameData.INPUTWORD) {
                        ChatGame_Client_Controller.chatGameGame_room.updateAnsUsr(gameData.getName() + "님이 틀렸습니다!"); // 틀린사람 표시
                    } else if (gameData.getType()==GameData.USEREXIT) {
                        renewUI();
                        ChatGame_Client_Controller.chatGameGame_room.updateAnsUsr(gameData.getName() + "님이 게임에서 나가셨습니다!"); // 나간사람 표시
                    }
//                    gameData.setType(GameData.INPUTWORD);
//                    System.out.println("데이터 날아옴");
                    System.out.println(gameData.getMsg() + "score : " + gameData.score.keySet() + "   val : " + gameData.score.values());
//                    int score = hello.getScore();
//                    ChatGame_GameRoom.userA_Score_inside.setText(String.valueOf(score));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } // run
    } // ClientReceiver
} // class
