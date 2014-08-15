package sample;
public class Engine {
    protected final int power, maxRpm, minRpm;
    public Engine(int power, int maxRpm, int minRpm) {
        this.power = power; this.maxRpm = maxRpm; this.minRpm = minRpm;
    }
    public int getPower() { return power; }
    public int getMaxRpm() { return maxRpm; }
    public int getMinRpm() { return minRpm; }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Engine engine = (Engine) o;
        if (maxRpm != engine.maxRpm) return false;
        if (minRpm != engine.minRpm) return false;
        if (power != engine.power) return false;
        return true;
    }
    @Override public int hashCode() {
        int result = power;
        result = 31 * result + maxRpm;
        result = 31 * result + minRpm;
        return result;
    }
    @Override public String toString() {
        return "Engine{" +
                "power=" + power +
                ", maxRpm=" + maxRpm +
                ", minRpm=" + minRpm +
                '}';
    }
}