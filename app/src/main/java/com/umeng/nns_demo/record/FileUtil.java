package com.umeng.nns_demo.record;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    private static String rootPath = "record_files";

    private final static String AUDIO_PCM_BASEPATH = "/" + rootPath + "/pcm/";

    private final static String AUDIO_WAV_BASEPATH = "/" + rootPath + "/wav/";

    public static String getPcmFileAbsolutePath(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            throw new NullPointerException("fileName isEmpty");
        }
        String mAudioRawPath = "";
        if (!fileName.endsWith(".pcm")) {
            fileName = fileName + ".pcm";
        }
        String fileBasePath = AudioRecorder.getAppContext().getFilesDir().getAbsolutePath() + AUDIO_PCM_BASEPATH;
        File file = new File(fileBasePath);
        //创建目录
        if (!file.exists()) {
            file.mkdirs();
        }
        mAudioRawPath = fileBasePath + fileName;

        return mAudioRawPath;
    }

    public static String getWavFileAbsolutePath(String fileName) {
        if (fileName == null) {
            throw new NullPointerException("fileName can't be null");
        }

        String mAudioWavPath = "";
        if (!fileName.endsWith(".wav")) {
            fileName = fileName + ".wav";
        }
        String fileBasePath = AudioRecorder.getAppContext().getFilesDir().getAbsolutePath() + AUDIO_WAV_BASEPATH;
        File file = new File(fileBasePath);
        //创建目录
        if (!file.exists()) {
            file.mkdirs();
        }
        mAudioWavPath = fileBasePath + fileName;
        return mAudioWavPath;
    }

}
