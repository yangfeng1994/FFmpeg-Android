package yf.jackio.ffmpeg;

import java.util.Arrays;
import java.util.Map;

class ShellCommand {

    Process run(String[] commandString, Map<String, String> environment) {
        Process process = null;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(commandString);
            if (environment != null) {
                processBuilder.environment().putAll(environment);
            }
            process = processBuilder.start();
        } catch (Throwable t) {
            YLog.e("Exception while trying to run: " + Arrays.toString(commandString), t);
        }
        return process;
    }

}