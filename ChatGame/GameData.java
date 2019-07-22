package com.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class GameData implements Serializable, Cloneable {
    static final int INPUTWORD = 0, READY = 1, GAMESTART = 2, USERLOGIN = 3, USEREXIT = 4, GAMEND = 5, COUNTER = 6, RIGHTANS = 7;
    private int type;
    private String name; // 플레이어 이름
    private String msg; // 보내준 메시지0000000
    private int numOfReadyPlayer = 0;
    LinkedHashMap<String,Integer> score = new LinkedHashMap<String,Integer>(7); // LinkedHashMap은 데이터의 입력순서를 보장

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    // 생성자 오버로딩
    public GameData() {
        type = USERLOGIN;
    }

    public GameData(String name) {
        this.name = name;
        this.type = INPUTWORD;
        this.score.put(name, 0);
    }

    public GameData(String name, String msg, int type, int score) {
        this.name = name;
        this.msg = msg;
        this.type = type;
        this.score.put(name, score);
    }

    public GameData(String name, String msg, int type, LinkedHashMap<String,Integer> score) {
        this.name = name;
        this.msg = msg;
        this.type = type;
        this.score.putAll(score);
    }



    // 모든 프로퍼티 출력
    public void printGameData() {
        System.out.println("name은 "+name+", msg는 "+msg+", type은 "+type+", score의 keySet은 "+score.keySet()+", score의 value는 "+score.values()+" 입니다.");
    }



    // 프로퍼티 get, set
    public  void setType(int type) {
        this.type = type;
    }
    public int getType() {
        return this.type;
    }

    public void setNumOfReadyPlayer() {
        ++this.numOfReadyPlayer;
    }
    public void setNumOfReadyPlayer(int numOfReadyPlayer) {
        this.numOfReadyPlayer = numOfReadyPlayer;
    }
    public int getNumOfReadyPlayer() {
        return this.numOfReadyPlayer;
    }

    public void setScore(String name,int score){
        this.score.put(name, score);
        System.out.println("name : " + name + ", score : " + score);
        System.out.println(this.score.keySet() + " " + this.score.values());
    }
    public int getScore(String name){
        return this.score.get(name);
    }
    public LinkedHashMap<String,Integer> getScore() {
        return this.score;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() { return this.name; }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return this.msg;
    }

}
