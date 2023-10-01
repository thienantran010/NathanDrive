package com.easypan.utils;


import com.easypan.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProcessUtils {
    private static final Logger logger = LoggerFactory.getLogger(ProcessUtils.class);

    public static String executeCommand(String cmd, Boolean outprintLog) throws BusinessException {
        if (StringTools.isEmpty(cmd)) {
            logger.error("--- command execution failed, because the requested FFmpeg command is null! ---");
            return null;
        }

        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try{
            process = Runtime.getRuntime().exec(cmd);
            // execute ffmpeg command
            // extract output stream and error stream info
            // warning: must extract ffmpeg's output info during execution, if we don't,
            // when output stream info fill out the buffer of storage of jvm output info, process would be blocked
            PrintStream errorStream = new PrintStream(process.getErrorStream());
            PrintStream inputStream = new PrintStream(process.getInputStream());
            errorStream.start();
            inputStream.start();
            // wait until ffmpeg command execution finish
            process.waitFor();
            // get string of execution
            String result = errorStream.stringBuffer.append(inputStream.stringBuffer + "\n").toString();
            // output executed command info

            if (outprintLog){
                logger.info("execute command:{}, executed already, execution result:{}", cmd, result);
            } else {
                logger.info("execute command:{}, executed already", cmd);
            }
            return result;
        } catch (Exception e) {
            // logger.error("execute command failed:{}", e.getMessage());
            e.printStackTrace();
            throw new BusinessException("video transfer failed");
        } finally {
            if (null != process){
                ProcessKiller ffmpegKiller = new ProcessKiller(process);
                runtime.addShutdownHook(ffmpegKiller);
            }
        }
    }

    /*
        End current FFmpeg process before exit
     */
    private static class ProcessKiller extends Thread{
        private Process process;

        public ProcessKiller(Process process) { this.process = process; }

        @Override
        public void run() { this.process.destroy(); }
    }

    /*
        To get output and error stream during execution of ffmpeg process
     */
    static class PrintStream extends Thread {
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer = new StringBuffer();

        public PrintStream(InputStream inputStream) { this.inputStream = inputStream; }

        @Override
        public void run() {
            try{
                if (null == inputStream) {
                    return;
                }
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
            } catch (Exception e) {
                logger.error("read input stream error! error info: " + e.getMessage());
            } finally {
                try {
                    if (null != bufferedReader) {
                        bufferedReader.close();
                    }
                    if (null != inputStream) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    logger.error("call PrintStream after reading output stream, error happened when close stream! ");
                }
            }
        }
    }
}
