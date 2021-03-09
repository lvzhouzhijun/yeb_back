package com.happy.server.common;

import lombok.extern.slf4j.Slf4j;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: Happy
 * @Date: 2021/03/09/20:59
 * @Description: FastDFS 工具类
 */
@Slf4j
public class FastDFSUtils {

    /**
     * 初始化客户端
     * ClientGlobal.init(filePath); 读取配置文件并初始化对应属性
     */
    static {
        String filePath = null;
        try {
            filePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
            ClientGlobal.init(filePath);
        } catch (Exception e) {
            log.error("初始化FastDFS失败",e);
        }
    }

    /**
     * 生成 tracker 服务器
     * @return
     * @throws IOException
     */
    private static TrackerServer getTrackerServer() throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerServer;
    }

    /**
     * 生成 storage 客户端
     * @return
     * @throws IOException
     */
    private static StorageClient getStorageClient() throws IOException {
        TrackerServer trackerServer = getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        return storageClient;
    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    public static String[] upload(MultipartFile file){
        String name = file.getOriginalFilename();
        log.info("文件名："+name);
        StorageClient storageClient = null;
        String[] uploadResults = null;
        try {
            storageClient = getStorageClient();
            uploadResults = storageClient.upload_file(file.getBytes(), name.substring(name.lastIndexOf(".") + 1), null);

        }catch (Exception e){
            log.error("上传文件失败",e.getMessage());
        }
        if(null == uploadResults){
            log.error("上传失败",storageClient.getErrorCode());
        }
        return uploadResults;
    }

    /**
     * 获取文件信息
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static FileInfo getFileName(String groupName,String remoteFileName){
        StorageClient storageClient = null;
        try {
            storageClient = getStorageClient();
            FileInfo fileInfo = storageClient.get_file_info(groupName, remoteFileName);
            return fileInfo;
        } catch (Exception e) {
            log.error("文件信息获取失败",e.getMessage());
        }
        return null;
    }

    /**
     * 下载文件
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static InputStream downFile(String groupName,String remoteFileName){
        StorageClient storageClient = null;
        try {
            storageClient = getStorageClient();
            byte[] fileByte = storageClient.download_file(groupName, remoteFileName);
            return new ByteArrayInputStream(fileByte);
        } catch (Exception e) {
            log.error("文件下载失败",e.getMessage());
        }
        return null;
    }

    /**
     * 删除失败
     * @param groupName
     * @param remoteFileName
     */
    public static void deleteFile(String groupName,String remoteFileName){
        StorageClient storageClient = null;
        try {
            storageClient = getStorageClient();
            storageClient.delete_file(groupName,remoteFileName);
        } catch (Exception e) {
            log.error("文件删除失败",e.getMessage());
        }
    }

    /**
     * 获取文件路径
     * @return
     */
    public static String getTrackerUrl(){
        TrackerClient trackerClient = new TrackerClient();
        StorageServer storeStorage = null;
        try{
            TrackerServer trackerServer = trackerClient.getConnection();
            storeStorage = trackerClient.getStoreStorage(trackerServer);
        }catch (Exception e){
            log.error("文件路径获取失败",e.getMessage());
        }
        return "http://" + storeStorage.getInetSocketAddress().getHostString() + ":8888/";
    }
}
