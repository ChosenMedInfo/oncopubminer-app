package com.cn.xiaonuo.core.file.modular.local;

import java.io.InputStream;

import com.cn.xiaonuo.core.file.FileOperator;
import com.cn.xiaonuo.core.file.common.enums.BucketAuthEnum;
import com.cn.xiaonuo.core.file.common.exp.FileServiceException;
import com.cn.xiaonuo.core.file.modular.local.prop.LocalFileProperties;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;

/**
 * 阿里云文件操作
 *
 * @author xuyuxiang
 * @date 2020/5/25 2:33 下午
 */
public class LocalFileOperator implements FileOperator {

    private final LocalFileProperties localFileProperties;

    private String currentSavePath = "";

    public LocalFileOperator(LocalFileProperties localFileProperties) {
        this.localFileProperties = localFileProperties;
        initClient();
    }

    @Override
    public void initClient() {
        if (SystemUtil.getOsInfo().isWindows()) {
            String savePathWindows = localFileProperties.getLocalFileSavePathWin();
            if (!FileUtil.exist(savePathWindows)) {
                FileUtil.mkdir(savePathWindows);
            }
            currentSavePath = savePathWindows;
        } else {
            String savePathLinux = localFileProperties.getLocalFileSavePathLinux();
            if (!FileUtil.exist(savePathLinux)) {
                FileUtil.mkdir(savePathLinux);
            }
            currentSavePath = savePathLinux;
        }
    }

    @Override
    public void destroyClient() {
        // empty
    }

    @Override
    public Object getClient() {
        // empty
        return null;
    }

    @Override
    public boolean doesBucketExist(String bucketName) {
        String absolutePath = currentSavePath + "/" + bucketName;
        return FileUtil.exist(absolutePath);
    }

    @Override
    public void setBucketAcl(String bucketName, BucketAuthEnum bucketAuthEnum) {
        // empty
    }

    @Override
    public boolean isExistingFile(String bucketName, String key) {
        String absoluteFile = currentSavePath + "/" + bucketName + "/" + key;
        return FileUtil.exist(absoluteFile);
    }

    @Override
    public String storageFile(String bucketName, String key, byte[] bytes) {

        // 判断bucket存在不存在
        String bucketPath = currentSavePath + "/" + bucketName;
        if (!FileUtil.exist(bucketPath)) {
            FileUtil.mkdir(bucketPath);
        }

        // 存储文件
        String absoluteFile = currentSavePath + "/" + bucketName + "/" + key;
        FileUtil.writeBytes(bytes, absoluteFile);
        
        // 2021-03-19：返回文件绝对路径！
        return absoluteFile.replaceAll("//", "/").replaceAll("\\\\", "/");
        
    }

    @Override
    public void storageFile(String bucketName, String key, InputStream inputStream) {

        // 判断bucket存在不存在
        String bucketPath = currentSavePath + "/" + bucketName;
        if (!FileUtil.exist(bucketPath)) {
            FileUtil.mkdir(bucketPath);
        }

        // 存储文件
        String absoluteFile = currentSavePath + "/" + bucketName + "/" + key;
        FileUtil.writeFromStream(inputStream, absoluteFile);
    }

    @Override
    public byte[] getFileBytes(String bucketName, String key) {

        // 判断文件存在不存在
        String absoluteFile = currentSavePath + "/" + bucketName + "/" + key;
        if (!FileUtil.exist(absoluteFile)) {
            String message = StrUtil.format("文件不存在,bucket={},key={}", bucketName, key);
            throw new FileServiceException(message);
        } else {
            return FileUtil.readBytes(absoluteFile);
        }
    }

    @Override
    public void setFileAcl(String bucketName, String key, BucketAuthEnum bucketAuthEnum) {
        // empty
    }

    @Override
    public void copyFile(String originBucketName, String originFileKey, String newBucketName, String newFileKey) {

        // 判断文件存在不存在
        String originFile = currentSavePath + "/" + originBucketName + "/" + originFileKey;
        if (!FileUtil.exist(originFile)) {
            String message = StrUtil.format("源文件不存在,bucket={},key={}", originBucketName, originFileKey);
            throw new FileServiceException(message);
        } else {

            // 拷贝文件
            String destFile = currentSavePath + "/" + newBucketName + "/" + newFileKey;
            FileUtil.copy(originFile, destFile, true);
        }
    }

    @Override
    public String getFileAuthUrl(String bucketName, String key, Long timeoutMillis) {
        // empty
        return null;
    }

    @Override
    public void deleteFile(String bucketName, String key) {

        // 判断文件存在不存在
        String file = currentSavePath + "/" + bucketName + "/" + key;
        if (!FileUtil.exist(file)) {
            return;
        }

        // 删除文件
        FileUtil.del(file);

    }
}
