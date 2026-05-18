package com.pao.laboratory11.exercise1;

public enum ChannelScore {
    WEB(15, true),
    APP(10, true),
    CRYPTO(30, true),
    POS(5, false),
    ATM(0, false);

    private final int score;
    private final boolean suspicious;
    ChannelScore(int score, boolean suspicious){
        this.score = score;
        this.suspicious = suspicious;
    }

    public int getScore(){
        return score;
    }
    public boolean getSuspicious(){
        return  suspicious;
    }

    public static boolean getSuspiciousChannel(String channel){
        try {
            return ChannelScore.valueOf(channel).getSuspicious();
        }
        catch(IllegalArgumentException e){
            return false;
        }
    }

    public static int getScoreChannel(String channel){
        try{
            return ChannelScore.valueOf(channel).getScore();
        }
        catch (IllegalArgumentException e){
            return 0;
        }
    }
}
