package com.my.bob;

import org.junit.jupiter.api.Test;

import java.util.*;

public class BaseBallTest {

//    @Test
    public void baseball() {
        int length = 4;
        Scanner sc = new Scanner(System.in);

        // 정답 직접 입력
//        System.out.println("Input Answer first (ex. 1234)");
//        String answerStr = sc.nextLine();
//        int[] answer = convertArray(answerStr);
//        System.out.println("Answer is [" + printArray(answer) + "]");

        // 랜덤 생성
        int[] answer = createRandomArray(length);
        System.out.println("Answer is [" + printArray(answer) + "]");

        System.out.println("- Start game, input " + length + " length number");
        while(true) {
            String read = sc.nextLine().trim();
            int[] input = convertArray(read);
            String check = checkBaseball(answer, input);
            System.out.println(check);
            if(check.equals("answer")) break;
        }
        System.out.println("- End game");
        sc.close();
    }


    private String checkBaseball(int[] answer, int[] input) {
        int[] answerNumList = new int[10];
        Arrays.stream(answer).forEach(integer -> answerNumList[integer] = 1);

        int[] ballArray = new int[10];
        int[] strikeArray = new int[10];
        for (int i = 0; i < answer.length; i++) {
            int in = input[i];
            if(answerNumList[in] == 1) {
                if(in == answer[i]) {
                    // If it has been treated with balls in advance, take it out.
                    if(ballArray[in] == 1) {
                        ballArray[in] = 0;
                    }
                    strikeArray[in] = 1;
                } else if(ballArray[in] != 1 && strikeArray[in] != 1) {
                    ballArray[in] = 1;
                }
            }
        }

        int strike = Arrays.stream(strikeArray).sum();
        int ball = Arrays.stream(ballArray).sum();
        if(strike == 0 && ball == 0) {
            return "out";
        } else if(strike == answer.length) {
            return "answer";
        } else {
            return strike +"s " + ball +"b";
        }
    }

    private int[] convertArray(String inputStr) {
        if(inputStr.contains(",")) {
            inputStr = inputStr.replaceAll(",", "");
        }
        if(inputStr.contains(" ")){
            inputStr = inputStr.replaceAll(" ", "");
        }

        int[] input = new int[inputStr.length()];
        for (int i = 0; i < inputStr.length(); i++) {
            int in = inputStr.charAt(i) - '0';
            input[i] = in;
        }
        return input;
    }

    private String printArray(int[] array){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            builder.append(array[i]);
            if(i != array.length -1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    private int[] createRandomArray(int length) {
        Random random = new Random();
        List<Integer> ballArray = new ArrayList<>();
        while(ballArray.size() != length) {
            // 0 ~ 9 까지의 무작위 int 값 리턴
            int i = random.nextInt(10);
            if(!ballArray.contains(i)) {
                ballArray.add(i);
            }
        }
        return ballArray.stream().mapToInt(Integer::intValue).toArray();
    }
}

