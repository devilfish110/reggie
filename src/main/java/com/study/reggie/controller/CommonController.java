package com.study.reggie.controller;

import com.study.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.UUID;

/**
 * @author That's all
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.img-path}")
    private String path;

    /**
     * 文件上传类型使用MultipartFile接收
     *
     * @param file 上传的二进制文件
     * @return R
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        log.info("上传文件的文件名:{}", file.getOriginalFilename());
        //定义最后返回的文件名
        String fileName = null;
        //获取文件名
        String originalFilename = file.getOriginalFilename();
        //判断文件名是否存在
        if (originalFilename != null && originalFilename.length() > 0) {
            //使用UUID生成文件名
            String uName = UUID.randomUUID().toString();
            //获取文件名后缀，从文件最后的.截取后缀
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            fileName = uName + suffix;
            //创建文件夹
            File dir = new File(path);
            //判断当前目录是否存在
            if (!dir.exists()) {
                //不存在则创建目录
                dir.mkdirs();
            }
            try {
                //文件转存
                file.transferTo(new File(path + fileName));
            } catch (IOException e) {
                return R.error("文件上传失败!");
            }
        } else {
            R.error("文件没有文件名!");
        }
        return R.success(fileName);
    }

    /**
     * 文件下载
     * 请求url:···?name=1213
     *
     * @param fileName 请求的文件名
     * @param response 操作数据回写
     */
    @GetMapping(value = "/download", produces = MediaType.IMAGE_JPEG_VALUE)
    public void download(@RequestParam("name") String fileName, HttpServletResponse response) throws IOException {
        //1.输入流获取文件内容
        //确定文件
        File file = new File(path + fileName);
        //构建输入流
        InputStream inputStream = new BufferedInputStream(Files.newInputStream(file.toPath()));
        //构建输出流
        OutputStream outputStream = response.getOutputStream();
        //设置响应文件格式
        response.setContentType("image/jpg");

        int length = 0;
        byte[] bytes = new byte[1024];
        while ((length = inputStream.read(bytes)) != -1) {
            //2.输出流回写到前端
            outputStream.write(bytes, 0, length);
            outputStream.flush();
        }
        //释放资源
        inputStream.close();
        outputStream.close();
    }
}
