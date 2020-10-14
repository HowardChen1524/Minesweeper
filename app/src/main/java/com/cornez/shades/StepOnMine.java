package com.cornez.shades;

import java.util.Random;

public class StepOnMine {

    public int boom;
    public int boom1;
    public int state[] = new int[25];//default 0
    public String sign[] = new String[25];// default null

    public StepOnMine() {
        for(int i = 0; i < 25; i++)
            state[i] = 0;
        for(int i = 0; i < 25; i++)
            sign[i] = " ";
        Random randNum = new Random();
        boom = randNum.nextInt(25);
        boom1 = randNum.nextInt(25);
        while (boom == boom1)
            boom1 = randNum.nextInt(25);
        state[boom] = 3;//space 1,num,2,boom 3
        state[boom1] = 3;

        for(int i = 0; i < 5; i++)
            for(int j = i*5; j < 5+(i*5); j++){
                if(state[j] == 3){
                    sign[j] = "*";
                    if(j-5-1 >= (i-1)*5 && i > 0)
                        if (state[j-5-1] == 0)
                            state[j-5-1] = 2;
                    if(i > 0)
                        if(state[j-5] == 0)
                            state[j-5] = 2;
                    if(j-5+1 < i*5 && i > 0)
                        if(state[j-5+1] == 0)
                            state[j-5+1] = 2;
                        //up
                    if(j-1 >= i*5)
                        if(state[j-1] == 0)
                            state[j-1] = 2;
                    if(j+1 < (i+1)*5)
                        if(state[j+1] == 0)
                            state[j+1] = 2;
                        //mid
                    if(j+5-1 >= (i+1)*5 && i < 4)
                        if(state[j+5-1] == 0)
                            state[j+5-1] = 2;
                    if(i < 4)
                        if(state[j+5] == 0)
                            state[j+5] = 2;
                    if(j+5+1 < (i+2)*5 && i < 4)
                        if(state[j+5+1] == 0)
                            state[j+5+1] = 2;
                        //bottom
                }
            }
        for(int i = 0; i < 25; i++)
            if(state[i] == 0)
                state[i] = 1;
        countBoom();
    }
    public void open(int pos){ state[pos] = 0; }
    public void stop(){ for(int i = 0; i < 25; i++) state[i] = 0;}
    public int getState(int pos){ return state[pos]; }
    public String getSign(int pos){ return sign[pos]; }
    public void countBoom(){
        int num;
        for(int i = 0; i < 5; i++)
            for(int j = i*5; j < 5+(i*5); j++){
                num = 0;
                if(state[j] != 3) {
                    if (j - 5 - 1 >= (i - 1) * 5 && i > 0)
                        if (sign[j - 5 - 1] == "*")
                            num += 1;
                    if (i > 0)
                        if (sign[j - 5] == "*")
                            num += 1;
                    if (j - 5 + 1 < i * 5 && i > 0)
                        if (sign[j - 5 + 1] == "*")
                            num += 1;
                    if (j - 1 >= i * 5)
                        if (sign[j - 1] == "*")
                            num += 1;
                    if (j + 1 < (i + 1) * 5)
                        if (sign[j + 1] == "*")
                            num += 1;
                    if (j + 5 - 1 >= (i + 1) * 5 && i < 4)
                        if (sign[j + 5 - 1] == "*")
                            num += 1;
                    if (i < 4)
                        if (sign[j + 5] == "*")
                            num += 1;
                    if (j + 5 + 1 < (i + 2) * 5 && i < 4)
                        if (sign[j + 5 + 1] == "*")
                            num += 1;
                    if (num > 0)
                        sign[j] = Integer.toString(num);
                }
            }
    }

}
