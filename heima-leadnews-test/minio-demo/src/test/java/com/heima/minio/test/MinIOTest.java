package com.heima.minio.test;


import com.heima.file.service.FileStorageService;
import com.heima.minio.MinIOApplication;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest(classes = MinIOApplication.class)
@RunWith(SpringRunner.class)
public class MinIOTest {

    @Autowired
    private FileStorageService fileStorageService;

    /*把list.html文件上傳到minio中 並顯示於瀏覽器*/
  /*  @Test
    public void test() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("/Users/duck/Downloads/ list.html");
        String path = fileStorageService.uploadHtmlFile("", "list.html", fileInputStream);
        System.out.println(path);

    }*/

    //把list.html文件上傳到minio中 並顯示於瀏覽器
    public static void main(String[] args) {

        try {
            FileInputStream fileInputStream = new FileInputStream("/Users/duck/Downloads/tmp/js/axios.min.js");


            //1.獲取minio的連接信息 新建一個minio客戶端
            MinioClient minioClient = MinioClient.builder()
                    .credentials("minio", "minio123")
                    .endpoint("http://172.16.227.131:9000/").build();

            //2.上傳
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object("plugins/js/axios.min.js") //文件名稱
                    .contentType("text/js") //文件類型
                    .bucket("leadnews")
                    .stream(fileInputStream, fileInputStream.available(), -1).build();
            minioClient.putObject(putObjectArgs);

            //訪問路徑
            System.out.println("http://172.16.227.131:9000/leadnews/list.html");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
