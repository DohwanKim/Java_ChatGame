package com.game;// 컴파일 : javac com\socket\ChatGame_Server.ChatGame_Server.java -encoding UTF-8

import java.net.*;
import java.io.*;
import java.util.*;


public class ChatGame_Server {
    HashMap clients;
    static ArrayList<GameData> gameDataArrayList = new ArrayList<GameData>();
    WordData wordData = new WordData();
    public GameData gameData; // 유저가 보내는 이름과 메세지
    String tmp;

    ChatGame_Server() {
        clients = new HashMap();
//        gameData = new GameData();
        /*
        * 왜 동기화를 하는가?
        * 동기화를 지원하는 Collection의 경우 순회하다가 새로운 값이 추가되는 경우 이슈가 발생하지 않는다.
        * 이는 멀티쓰레딩에서 유용한데, 이는 즉 OS에서 배웠던 Critical Section문제를 해결하기 좋기 때문이다. Critical Section Lock을 해주는것.
        * 쓰레드 동기화 : 한 쓰레드가 진행중인 작업을 다른 쓰레드가 간섭하지 못하도록 막는것.
        * 아래의 Collections.synchronizedMap()은 동기화 기능을 가진 Map을 만들어 데이터의 무결성을 보장해준다.
        * http://ooz.co.kr/71
        *  https://madplay.github.io/post/java-collection-synchronize
        * */
        Collections.synchronizedMap(clients);
    }

    public void start() {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(8080);  // 서버 소켓 포트 할당, 서버 오픈
            System.out.println("서버시작");

            while (true) {  // 클라이언트와 연결후, 클라이언트에 대한 데이터 입출력 쓰레드 생성, 시작
                socket = serverSocket.accept();  // 연결 기다림.
                System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "에서 접속하였습니다.");
                ServerReceiver thread = new ServerReceiver(socket);  // 클라이언트로부터 입력을 받고 전송하는 쓰레드 작동. socket = 클라이언트
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } // start()

    void sendToALL(Object msg) {
        Iterator it = clients.keySet().iterator();

        while (it.hasNext()) {
            try {
                ObjectOutputStream out = (ObjectOutputStream) clients.get(it.next());
                out.writeObject(msg);
                out.flush();
            } catch (IOException e) {}
        } // while
    } // sendToAll

    class ServerReceiver extends Thread {
        Socket socket;
        ObjectInputStream in; // 스트링 입력을 받음.
        ObjectOutputStream out; // 객체를 전송.
        String name = "";

        ServerReceiver(Socket socket) {
            this.socket = socket; // client socket
            try {
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {}
        }

        public void run() {

            try {
                if (clients.size() > 0) { // 2번째 이상으로 접속한 client에 대한 작업
                    GameData gameDataTmp = (GameData)in.readObject();  // 클라이언트로부터 시작 데이터 입력받음
                    name = gameDataTmp.getName();
                    gameData = new GameData(name, "#"+name+"님이 들어오셨습니다.", GameData.USERLOGIN, gameData.getScore());
                    gameData.setScore(name, gameDataTmp.getScore(name));
                    gameDataTmp = null;
                } else { // 처음 접속한 client에 대한 작업
                    tmp = wordData.getWord();
                    gameData = (GameData)in.readObject();
                    name = gameData.getName();
                    gameData.setMsg("#"+name+"님이 들어오셨습니다.");
                    gameData.setType(GameData.USERLOGIN);
                }
                System.out.println("데이터 날아옴");
                System.out.println(name+" : "+gameData.getScore(name));
//                System.out.println(gameData.getMsg());
                clients.put(name, out);
                sendToALL(gameData.clone());
                System.out.println("현재 서버접속자 수는 "+clients.size()+"입니다.");

                gameDataArrayList.add(gameData);
                while (in!=null) {
                    gameData = (GameData)in.readObject();
//                    System.out.println("message type" + gameData.getType());
                    System.out.println("현재 제시된 문제: "+ tmp);
//                    gameData.ms
                    // 메세지의 타입을 확인
                    // 1.메세지가 INPUTWORD 메세지, 제시된 데이터와 메시지를 비교, true면 수행
                    System.out.println("보내온 메세지 : " + gameData.getMsg() + "제시된 단어 : " + tmp);
                    if (gameData.getType() == GameData.INPUTWORD && gameData.getMsg().equals(tmp)) {
                        gameData.setScore(gameData.getName(),gameData.getScore(gameData.getName())+WordData.wordMap.get(tmp));  // 해당 플레이어의 점수 증가.
                        System.out.println("해당 문제 점수: "+WordData.wordMap.get(tmp));
                        if (gameData.getScore(gameData.getName()) >= 2000) {  // 승리조건 2000점이상
                            gameData.setMsg(gameData.getName()+"님이 2000점을 달성하여 승리하였습니다.");
                            gameData.setType(GameData.GAMEND); // 단어 입력란을 비활성, 시작대기를 활성화
                            tmp = "";
                            gameData.setMsg(tmp);  // 단어를 갱신하여 보여줌.
                            gameData.setNumOfReadyPlayer(0);
                            for (String keyName : gameData.getScore().keySet()) {
                                gameData.setScore(keyName, 0);
                            }
                        } else {
                            tmp = wordData.getWord(); //새로운 문제를 가져옴
                            System.out.println("정답!!! " + tmp);
                            gameData.setMsg(tmp);  // 단어를 갱신하여 보여줌.
                            gameData.setType(GameData.RIGHTANS);
                        }
                        System.out.println("정답을 맞춰서 변경된 단어 : " + tmp);
                    }
                    // 2.메세지가 READY 메세지이면, 전부 레디를 했는지 체크
                    else if (gameData.getType() == GameData.READY && gameData.getNumOfReadyPlayer() == clients.size()) {
                        // 단어 입력란을 활성화시키라는 신호
                        // 3초의 카운터 다운 후 시작
                        for (int i=0; i<3; i++){
                            Thread.sleep(1000);
                            gameData.setType(gameData.COUNTER);
                            sendToALL(gameData.clone());
                        }
                        tmp = wordData.getWord();
                        gameData.setMsg(tmp);  // 단어를 갱신하여 보여줌.
                        gameData.setType(GameData.GAMESTART);
                    }
//                    System.out.println("sendALL 직전 서버에서" + name + ":" + gameData.getMsg());
                    sendToALL(gameData.clone());
//                    System.out.println("서버에서 쏴주는것 : "+gameData.getInputWord());
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    System.out.println("#"+name+"님이 나가셨습니다.");
                    gameData = new GameData(name, "#"+name+"님이 나가셨습니다.", GameData.USEREXIT, gameData.getScore());
                    gameData.getScore().remove(name);    // gameData.score에서 나간 유저 정보 삭제
//                    gameData.printGameData();
                    clients.remove(name);
                    sendToALL(gameData.clone());
                    System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "에서 접속을 종료하였습니다.");
                    System.out.println("현재 서버접속자 수는 " + clients.size() + "입니다.");
                } catch (Exception e) {}
            } // try
        } // run
    } // ReceiverThread

    public static void main(String[] args) {
        new ChatGame_Server().start();
    } // 서버 객체 생성후 시작.
}
