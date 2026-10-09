package com.example.app; // ⚠️ 替换为你实际的包名

import android.os.Bundle;
import android.os.Process;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 这里必须和你 res/layout 文件夹里的布局文件名一致（默认是 activity_main）
        setContentView(R.layout.activity_main);

        // ============ 崩溃日志捕获代码（开始） ============
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                try {
                    // 1. 将报错堆栈转换成字符串
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    throwable.printStackTrace(pw);
                    String errorLog = sw.toString();

                    // 2. 把日志写入手机应用私有目录 /Android/data/包名/files/crash_log.txt
                    File crashFile = new File(getExternalFilesDir(null), "crash_log.txt");
                    FileWriter writer = new FileWriter(crashFile);
                    writer.write(errorLog);
                    writer.flush();
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 3. 记录完毕后强制结束 App
                Process.killProcess(Process.myPid());
                System.exit(1);
            }
        });
        // ============ 崩溃日志捕获代码（结束） ============
    }
}
