package ie.ul.cs4084project;

public class RunMilestone {
    public static final int SECOND = 1000;
    public static final int MINUTE = 60000;
    public static final int HOUR = 3600000;
    private double km;
    private long totalTime, lapTimeLong;
    private int lapTimeHours, lapTimeMinutes, lapTimeSeconds;
    private boolean fastest, slowest;

    public RunMilestone(double km, long lapTime, long totalTime){
        this.km = km;
        lapTimeLong = lapTime;
        long timeLeft = lapTime;
        lapTimeHours = 0;
        if (timeLeft > HOUR){
            lapTimeHours = (int) timeLeft / HOUR;
            timeLeft %= HOUR;
        }
        lapTimeMinutes =  (int) timeLeft / MINUTE;
        timeLeft %= MINUTE;
        lapTimeSeconds =  (int) timeLeft / SECOND;
        this.totalTime = totalTime;
        fastest=false;
        slowest = false;
    }

    public double getKm(){
        return km;
    }

    public String getLapTime(){
        String lapTime = "";
        if (lapTimeHours>0){
            lapTime+=getLapTimeUnit(lapTimeHours);
            lapTime+=":";
        }
        lapTime+=getLapTimeUnit(lapTimeMinutes)+":"+getLapTimeUnit(lapTimeSeconds);
        return lapTime;
    }

    private String getLapTimeUnit(int unit){
        String lapTime = "";


            if(unit<10){
                lapTime+= "0";
            }
            lapTime+=unit;
        return lapTime;
    }

    public long getTotalTime(){
        return totalTime;
    }

    public long getLapTimeLong(){
        return lapTimeLong;
    }

    public boolean getFastest() {
        return fastest;
    }

    public boolean getSlowest() {
        return slowest;
    }

    public void setFastest(boolean fastest) {
        this.fastest = fastest;
    }

    public void setSlowest(boolean slowest) {
        this.slowest = slowest;
    }

    public String toString(){
        return km+"/"+totalTime+"/"+lapTimeLong+"/"+lapTimeHours+"/"+lapTimeMinutes+"/"+lapTimeSeconds+"/"+fastest+"/"+slowest;
    }

    public RunMilestone(String s){
        String[] strings = s.split("/");
        km = Double.parseDouble(strings[0]);
        totalTime = Long.parseLong(strings[1]);
        lapTimeLong = Long.parseLong(strings[2]);
        lapTimeHours = Integer.parseInt(strings[3]);
        lapTimeMinutes = Integer.parseInt(strings[4]);
        lapTimeSeconds = Integer.parseInt(strings[5]);
        fastest = Boolean.parseBoolean(strings[6]);
        slowest = Boolean.parseBoolean(strings[7]);
    }
}
