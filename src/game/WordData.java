package game;

import java.util.HashMap;
import java.util.Random;

public class WordData {
    static HashMap<String, Integer> wordMap;

    public WordData() {
        wordMap = new HashMap<String, Integer>();
        //단어 데이터
        //단어, 그 단어의 점수
        //단어는 5단계가 있음
        //1 - 50, 2 - 100, 3 - 200, 4 - 300, 5 - 500
        wordMap.put("안녕", 50);
        wordMap.put("가끔미치도록네가안고싶어질때가있어", 500);
        wordMap.put("노후를대비해이세계에서금화8만개를모읍니다", 500);
        wordMap.put("어두운책상", 100);
        wordMap.put("해가뜨는데나는못잔다", 200);
        wordMap.put("너무피곤한데잠을잘수가없어", 300);
    }
    public String getWord(){
        // Get a random entry from the HashMap.
        Object[] wordRandomMap = wordMap.keySet().toArray();
        Object key = wordRandomMap[new Random().nextInt(wordRandomMap.length)];

        //key변수에 에 해쉬맵에서 랜덤하게 키값을 담아온다.
        System.out.println("************ Random Value ************ \n" + key + " :: " + wordMap.get(key));

        return String.valueOf(key);
    }
}
