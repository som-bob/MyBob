package com.my.bob;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DrawTest {
    /** 점심 픽업 뽑기 */

    private final static boolean IS_MANY_SALAD = true;
    private final String[] participants = new String[] {
            "조이"
            , "데이브"
            , "토니"
            , "럭키"
            , "아리엘"
            , "세이디"
            , "애들린"
            , "데이빗"
    };

    @Test
    public void drawParticipants(){
        int numberOfWinners = IS_MANY_SALAD ? 3 : 2;
        Random random = new Random();
        List<Integer> winner = new ArrayList<>();
        while(winner.size() != numberOfWinners) {
            int get = random.nextInt(participants.length);
            if(! winner.contains(get)) winner.add(get);
        }

        StringBuilder builder = new StringBuilder("\n> 오늘 가져오는 사람\n");
        for (int i = 0; i < numberOfWinners; i++) {
            builder.append("- ").append(participants[winner.get(i)]).append("\n");
        }
        System.out.println(builder);
    }

    // 오늘의 확률
    @Test
    public void todayLucky(){
        Random random = new Random();
        int testCase = 10000;
        int[] intTest = new int[participants.length];
        for (int i = 0; i < testCase; i++) {
            int get = random.nextInt(participants.length);
            intTest[get]++;
        }

        // print
        for (int i = 0; i < intTest.length; i++) {
            System.out.println("[" + participants[i] + "]" + intTest[i] + "/" + testCase);
        }
    }
}
