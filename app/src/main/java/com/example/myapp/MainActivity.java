package com.example.myapp; // 👈 必须和文件夹路径一致

import android.os.Bundle;
import android.os.Process;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

// 👇 必须继承 AppCompatActivity
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 这里必须是 activity_main，确保你的 layout 文件夹里有这个文件
        setContentView(R.layout.activity_main); 

        // ============ 崩溃日志捕获代码（开始） ============
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                try {
                    // 1. 获取详细的错误堆栈
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    throwable.printStackTrace(pw);
                    String errorLog = sw.toString();

                    // 2. 把错误日志写入到手机的文件中
                    File crashFile = new File(getExternalFilesDir(null), "crash_log.txt");
                    FileWriter writer = new FileWriter(crashFile);
                    writer.write(errorLog);
                    writer.flush();
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 3. 记录完后结束 App
                Process.killProcess(Process.myPid());
                System.exit(1);
            }
        });
        // ============ 崩溃日志捕获代码（结束） ============
    }
}
