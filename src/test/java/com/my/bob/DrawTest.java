package com.my.bob;

import org.junit.jupiter.api.Test;

import java.util.*;

class DrawTest {
    /* 점심 픽업 뽑기 */
    private final Random random = new Random();

    // 육소정
    //12/02(월) : 단백질
    //12/03(화) : 단백질
    //12/04(수) : -
    //12/05(목) : -
    //12/06(금) : -
    //
    // 장정원
    //12/02(월) : 단백질
    //12/03(화) : 단백질
    //12/04(수) : -
    //12/05(목) : 샐러드(테이크아웃)
    //12/06(금) : 샐러드(테이크아웃)
    //
    //서도형
    //12/02(월) : 단백질
    //12/03(화) : 단백질
    //12/04(수) : 샐러드(테이크아웃)
    //12/05(목) : 단백질
    //12/06(금) : 단백질

    private static final boolean IS_MANY_SALAD = false;
    private String[] participants = new String[]{
            "조이"
            , "데이브"
            , "토니"
            , "아리엘"
//            , "세이디"
            , "애들린"
            , "제이든"
            , "럭키"
    };

//    @Test
    void drawParticipants() throws InterruptedException {
        int numberOfWinners = IS_MANY_SALAD ? 3 : 2;

        System.out.println("[순서 섞기]");
        shuffleArray(participants);
        for (int i = 1; i <= participants.length; i++) {
            System.out.print("[" + i + "번]" + participants[i - 1] + "   ");
        }

        System.out.println("\n\n> [뽑기]");
        Thread.sleep(1000);
        List<Integer> winners = new ArrayList<>();
        while (winners.size() != numberOfWinners) {
            int get = random.nextInt(participants.length);
            if (!winners.contains(get)) winners.add(get);
        }

        System.out.println("[오늘 가져오는 사람]");
        for (Integer winner : winners) {
            System.out.println("[" + (winner + 1) + "번] " + participants[winner]);
        }
    }

    private void shuffleArray(String[] participants) {
        List<String> participantsList = Arrays.asList(participants);
        Collections.shuffle(participantsList);
        this.participants = participantsList.toArray(String[]::new);
    }

    // 오늘의 확률
//    @Test
    void todayLucky() {
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
