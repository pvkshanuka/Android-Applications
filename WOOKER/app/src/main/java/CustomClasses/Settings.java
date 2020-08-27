package CustomClasses;

public class Settings {

    boolean nor = false;
    boolean rem = false;
    String time = null;
    String from = null;

    public Settings(){}

    public Settings(boolean nor, boolean rem, String time,String from) {
        this.nor = nor;
        this.rem = rem;
        this.time = time;
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean isNor() {
        return nor;
    }

    public void setNor(boolean nor) {
        this.nor = nor;
    }

    public boolean isRem() {
        return rem;
    }

    public void setRem(boolean rem) {
        this.rem = rem;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
