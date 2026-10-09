package com.example.app; // ⚠️ 如果你的包名不是 com.example.app，请改成你自己的

import android.os.Bundle;
import android.os.Process;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

// 👇 关键点：必须要有 extends AppCompatActivity
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ============ 崩溃日志捕获代码 ============
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                try {
                    // 1. 获取详细的错误堆栈
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    throwable.printStackTrace(pw);
                    String errorLog = sw.toString();

                    // 2. 把错误日志写入手机文件
                    File crashFile = new File(getExternalFilesDir(null), "crash_log.txt");
                    FileWriter writer = new FileWriter(crashFile);
                    writer.write(errorLog);
                    writer.flush();
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 3. 记录完毕后结束 App
                Process.killProcess(Process.myPid());
                System.exit(1);
            }
        });
        // ==========================================
    }
}
