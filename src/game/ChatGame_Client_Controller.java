package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

class ChatGame_GameRoom extends JPanel {
    private ChatGame_Client_Controller win;
    private JPanel mainPanel;
    private JLabel userInputBefore = new JLabel("당신의 마지막 입력: ");

    private static JLabel QLabel = new JLabel("문제가 나오는곳");
    private static JLabel answerUser = new JLabel("문제를 맞춘사람");
    static JLabel serverMsg = new JLabel("서버의 메시지");
    static JLabel timer;

    //데이터를 보내는 창
    static JTextField inputTextLine = new JTextField("", 30);

    //주요 버튼
    JButton btn_ready;
    JButton btn_start;
    JButton btn_exit;

    //데이터를 받아와서 출력하는 창 변수들
    //맞춘 사람 출력
    //user1(me)
    static JLabel userA_Score_inside;
    static JLabel userA_info_inside;
    //user2
    static JLabel userB_Score_inside;
    static JLabel userB_info_inside;
    //user3
    static JLabel userC_Score_inside;
    static JLabel userC_info_inside;
    //user4
    static JLabel userD_Score_inside;
    static JLabel userD_info_inside;

    public void activateInputTextLine() {
        inputTextLine.setEditable(true);
        btn_exit.setEnabled(false);
    }
    public void deActivateInputTestLine() {
        inputTextLine.setEditable(false);
    }
    public void activateReadyButton() {
        btn_ready.setEnabled(true);
        btn_exit.setEnabled(true);
    }
    public void deActivateReadyButton() {
        btn_ready.setEnabled(false);
    }
    public void updateQLabel(String text) {
        QLabel.setText(text);
    }
    public void updateAnsUsr(String name) {
        answerUser.setText(name);
    }

    public ChatGame_GameRoom(ChatGame_Client_Controller win){
        this.win = win;

        //메인 패널 시작 ----------------
        mainPanel = new JPanel();
        JPanel userPanel = new JPanel();
        JPanel QPanel = new JPanel();
        JPanel CenterPanel = new JPanel();

        //======= 메인 패널 설정 ======== (800x600)인 최상단 패널
        mainPanel.setPreferredSize(new Dimension(790, 590));
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.add(userPanel, "South");
        mainPanel.add(QPanel, "North");
        mainPanel.add(CenterPanel, "Center");

        //===== QPanel 설정 영역(상단) =====
//        QLabel = new JLabel("문제가 나오는곳");
        QLabel.setFont(new Font("Serif", Font.BOLD, 16));
        QPanel.setBackground(Color.GREEN);
        QPanel.add(QLabel);
        QPanel.setPreferredSize(new Dimension(0, 70));

        //===== CenterPanel 설정 영역(중간) =====
        JLabel effectLabel = new JLabel("게임 이펙트가 나오는곳자니");
        CenterPanel.add(effectLabel);
        CenterPanel.setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel();
        JPanel timePanel = new JPanel();
        JPanel effectPanel = new JPanel();
        JLabel inputDescription = new JLabel("입력하는곳");
        btn_ready = new JButton("시작대기");
        btn_ready.addActionListener(new ReadyListener());
        btn_start = new JButton("게임시작");
        btn_exit = new JButton("게임종료");
        btn_exit.addActionListener(new ExitActionListener());
        deActivateInputTestLine();  // 입력란 비활성화
        inputTextLine.requestFocus();
        inputTextLine.addKeyListener(new EnterListener());
        effectPanel.setBackground(Color.CYAN);
        CenterPanel.add(inputPanel, "South");
        CenterPanel.add(timePanel, "North");
        CenterPanel.add(effectPanel, "Center");
        timer = new JLabel("");
        JPanel userInput = new JPanel();
        inputPanel.add(userInput);
        timePanel.add(timer);
        //이펙트 패널에 사용자 단어 입력 이력과 서버 메세지 출력 라벨 추가
        effectPanel.setLayout(new BorderLayout());
        JPanel effectNorthPanel = new JPanel();
        JPanel effectCenterPanel = new JPanel();
        JPanel effectSouthPanel = new JPanel();
        effectPanel.add(effectNorthPanel, "North");
        effectPanel.add(effectCenterPanel, "Center");
        effectPanel.add(effectSouthPanel, "South");
        effectNorthPanel.setLayout(new FlowLayout());
        effectSouthPanel.setLayout(new FlowLayout());
        answerUser.setFont(new Font("Serif", Font.BOLD, 20));
        effectNorthPanel.add(userInputBefore);
        effectCenterPanel.add(answerUser);
        effectSouthPanel.add(serverMsg);

        //인풋패널에 버튼 추가
        userInput.setLayout(new FlowLayout(FlowLayout.RIGHT));
        inputPanel.add(inputDescription);
        inputPanel.add(inputTextLine);
//        inputPanel.add(btn_start);
        inputPanel.add(btn_ready);
        inputPanel.add(btn_exit);

        //===== userPanel 설정 영역(하단) =====
        userPanel.setBackground(Color.YELLOW);
        userPanel.setLayout(new FlowLayout());
        userPanel.setPreferredSize(new Dimension(0, 180)); //해당 영역의 높이를 원하는 만큼 조절
        //이미지 가져오기
        ImageIcon icon01  = new ImageIcon("./src/images/user_icon_01.png");
        ImageIcon icon02  = new ImageIcon("./src/images/user_icon_02.png");
        ImageIcon icon03  = new ImageIcon("./src/images/user_icon_03.png");
        ImageIcon icon04  = new ImageIcon("./src/images/user_icon_04.png");
        JLabel userA_icon  = new JLabel(icon01);
        JLabel userB_icon  = new JLabel(icon02);
        JLabel userC_icon  = new JLabel(icon03);
        JLabel userD_icon  = new JLabel(icon04);


        //=== 유저 생성 ===
        //유저1
        JPanel userA = new JPanel();
        userA.setPreferredSize(new Dimension(150, 170));

        JPanel userA_Score = new JPanel();
        JPanel userA_info = new JPanel();
        userA_Score_inside = new JLabel("0");
        userA_info_inside = new JLabel("");
        userA_info_inside.setOpaque(true);
        userA_Score.add(userA_Score_inside);
        userA_info.add(userA_info_inside);
        userA.setLayout(new BorderLayout(20, 10));
        userA.add(userA_info, "South");
        userA.add(userA_icon, "Center");
        userA.add(userA_Score, "North");

        //유저2
        JPanel userB = new JPanel();
        userB.setPreferredSize(new Dimension(150, 170));
        JPanel userB_Score = new JPanel();
        JPanel userB_info = new JPanel();
        userB_Score_inside = new JLabel("0");
        userB_info_inside = new JLabel("");
        userB_info_inside.setOpaque(true);
        userB_Score.add(userB_Score_inside);
        userB_info.add(userB_info_inside);
        userB.setLayout(new BorderLayout(20, 10));
        userB.add(userB_info, "South");
        userB.add(userB_icon, "Center");
        userB.add(userB_Score, "North");

        //유저3
        JPanel userC = new JPanel();
        userC.setPreferredSize(new Dimension(150, 170));
        JPanel userC_Score = new JPanel();
        JPanel userC_info = new JPanel();
        userC_Score_inside = new JLabel("0");
        userC_info_inside = new JLabel("");
        userC_info_inside.setOpaque(true);
        userC_Score.add(userC_Score_inside);
        userC_info.add(userC_info_inside);
        userC.setLayout(new BorderLayout(20, 10));
        userC.add(userC_info, "South");
        userC.add(userC_icon, "Center");
        userC.add(userC_Score, "North");

        //유저4
        JPanel userD = new JPanel();
        userD.setPreferredSize(new Dimension(150, 170));
        JPanel userD_Score = new JPanel();
        JPanel userD_info = new JPanel();
        userD_Score_inside = new JLabel("0");
        userD_info_inside = new JLabel("");
        userD_info_inside.setOpaque(true);
        userD_Score.add(userD_Score_inside);
        userD_info.add(userD_info_inside);
        userD.setLayout(new BorderLayout(20, 10));
        userD.add(userD_info, "South");
        userD.add(userD_icon, "Center");
        userD.add(userD_Score, "North");

        //유저 하단 패널에 붙임
        userPanel.add(userA);
        userPanel.add(userB);
        userPanel.add(userC);
        userPanel.add(userD);
        //----------------------------메인 패널 끝


        //----------------------------접속 패널 끝
        add(mainPanel);

    }

    class ExitActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    class ReadyListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            ChatGame_Client_Controller.userSocket.gameData.setType(GameData.READY);
            ChatGame_Client_Controller.userSocket.gameData.setNumOfReadyPlayer();
            try {
                ChatGame_Client_Controller.userSocket.out.writeObject(ChatGame_Client_Controller.userSocket.gameData.clone());
            } catch (Exception e1) {
                e1.printStackTrace();
                System.out.println("서버로 READY DATA 전송 실패");
            }
            deActivateReadyButton();
        }
    }

    /* 키 이벤트 처리 */
    class EnterListener implements KeyListener {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                userInputBefore.setText("당신이 입력한 글자: "+ inputTextLine.getText());  // 입력한 글자를 Label에 표시
                // ChatGame_TCP_set형 userSocket : 서버와의 연결 설정,통신 담당
                /*--------------------------데이터 전송--------------------------*/
                ChatGame_Client_Controller.userSocket.gameData.setMsg(inputTextLine.getText());
                ChatGame_Client_Controller.userSocket.gameData.setName(ChatGame_Client_Controller.userSocket.name);
                ChatGame_Client_Controller.userSocket.gameData.setType(GameData.INPUTWORD);
                try {
                    ChatGame_Client_Controller.userSocket.out.writeObject(/*(game.GameData)*/ChatGame_Client_Controller.userSocket.gameData.clone());
                    /*--------------------------데이터 전송--------------------------*/
                } catch (Exception e1) {
                    e1.printStackTrace();
                    System.out.println("서버로 전송 실패");
                }
                inputTextLine.setText("");
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }
    }
}

class ChatGame_Login extends JPanel{
    private JButton loginBtn = new JButton("접속");
    private JPanel loginMainPanel = new JPanel();
    private ChatGame_Client_Controller win;
    JTextField uesrID_text;
    JTextField serverIP_text;
    JTextField serverPort_text;
    JLabel debugLabel;

    public ChatGame_Login(ChatGame_Client_Controller win){
        this.win = win;
        //====== 최상단 패널 ======
        loginMainPanel.setLayout(new BorderLayout());
        //=== 로그인과 로고를 넣을 첫번째 자식 패널 ===
        JPanel loginCenter = new JPanel();
        loginCenter.setLayout(new BorderLayout());

        //=== 로고 ===
        ImageIcon ic  = new ImageIcon("./src/images/Logo.png");
        JLabel gameLogo  = new JLabel(ic);
        JPanel gameLogoPanel = new JPanel();
        loginCenter.add(gameLogoPanel, "North");
        gameLogoPanel.setLayout(new FlowLayout());
        gameLogoPanel.setBackground(Color.YELLOW);
        gameLogoPanel.setPreferredSize(new Dimension(500,  400));
        gameLogoPanel.add(gameLogo);

        //=== 로그인 폼 ===
        JPanel loginForm = new JPanel();
        loginCenter.add(loginForm, "South");
        loginForm.setLayout(new GridLayout(0, 2));
        JLabel userID_label = new JLabel("유저 아이디");
        uesrID_text = new JTextField("nimi",10);
        JLabel serverIP_label = new JLabel("서버 IP 주소");
        serverIP_text = new JTextField("localhost",10);
        JLabel serverPort_label = new JLabel("포트 번호");
        serverPort_text = new JTextField("8080",10);
        debugLabel = new JLabel("");
        debugLabel.setForeground(Color.red);
        JButton submitBtn = new JButton("접속하기");
        submitBtn.addActionListener(new LoginActionListener());
        JButton exitBtn = new JButton("종료");
        exitBtn.addActionListener(new ExitActionListener());

        loginForm.add(userID_label); loginForm.add(uesrID_text);
        loginForm.add(serverIP_label); loginForm.add(serverIP_text);
        loginForm.add(serverPort_label); loginForm.add(serverPort_text);
        loginForm.add(debugLabel); loginForm.add(submitBtn);
        loginForm.add(new Label("")); loginForm.add(exitBtn);

        //차례로 부모 패널에 추가
        loginMainPanel.add(loginCenter);
        add(loginMainPanel);
    }

    class LoginActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            win.change("GameStart");
        }
    }
    class ExitActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}

public class ChatGame_Client_Controller extends JFrame{
    public static ChatGame_GameRoom chatGameGame_room = null;
    public static ChatGame_Login chatGameLogin = null;
    Sound introBGM = new Sound("intro.mp3", true);
    Sound loginEff = new Sound("Login.mp3", false);
    static Sound lobbyBGM = new Sound("bgm.mp3", true);
    String[] args;
    static ChatGame_Client_TCP userSocket; // 서버와의 연결설정,통신 담당
    static String inputName;
    static String inputServerIP;
    static String inputPort;

    public ChatGame_Client_Controller(String[] args){
        introBGM.start();
        this.args = args;
    }

    public void change(String panelName){
        if(panelName.equals("GameStart")){ //게임 화면으로 전환하며 소켓을 만들어서 서버에 접속한다.
            int parsePort = 0000;
            String userID = chatGameLogin.uesrID_text.getText();
            String serverIp = chatGameLogin.serverIP_text.getText();
            String port = chatGameLogin.serverPort_text.getText();
            inputName = userID;
            inputServerIP = serverIp;
            inputPort = port;

            if(!(port.length()==0)) {
                parsePort = Integer.parseInt(port);
            }
            System.out.println(userID);
            System.out.println(serverIp);
            System.out.println(parsePort);

            //입력폼 예외구문
            if(serverIp.length() < 9){
                chatGameLogin.debugLabel.setText("ip를 제대로 입력해주세요.");
            }else if(!isStringDouble(port)){
                chatGameLogin.debugLabel.setText("포트번호는 숫자만 입력해주셔야합니다.");
            }else if(port.length() != 4){
                chatGameLogin.debugLabel.setText("포트는 4자리를 입력해주세요.");
            }else{
                //예외구문 통과시 서버 접속을 시도
                try {
                    userSocket = new ChatGame_Client_TCP(serverIp, parsePort, userID/*, this*/);
                } catch (Exception e) {
                    chatGameLogin.debugLabel.setText("서버에 접속하지 못했습니다.");
                    System.out.println("서버 접속 실패");
                    System.out.println(e);
                }
                //서버 접속 시도를 해서 서버가 켜저 있으면 화면을 전환한다.
                if(userSocket.socket != null && !userSocket.socket.isClosed()) { //이 두 조건을 만족해야 서버가 열려있는 것이다.
                    getContentPane().removeAll();
                    getContentPane().add(chatGameGame_room);
                    ChatGame_GameRoom.userA_info_inside.setText(inputName);
                    revalidate();
                    repaint();
                    introBGM.close();
                    loginEff.start();
                    lobbyBGM.start();
                }else {
                    chatGameLogin.debugLabel.setText("입력은 정상이나, 서버가 닫혔습니다.");
                }
                //접속하는 느낌과 동기화를 위해 약간의 딜레이를 준다.
                try{
                    Thread.sleep(500);
                }catch (InterruptedException e){ }
            }
        }else{ //의외 로그인 화면 표시
            System.exit(0); //게임 종료
            getContentPane().removeAll();
            getContentPane().add(chatGameLogin);
            revalidate();
            repaint();
        }
    }

    private boolean isStringDouble(String port) {
        try {
            Double.parseDouble(port);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}