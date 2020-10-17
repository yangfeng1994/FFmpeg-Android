
-keep class com.arthenica.mobileffmpeg.Config {
    native <methods>;
    void log(int, byte[]);
    void statistics(int, float, float, long , int, double, double);
}

-keep class com.arthenica.mobileffmpeg.AbiDetect {
    native <methods>;
}