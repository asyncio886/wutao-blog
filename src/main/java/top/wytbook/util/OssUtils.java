package top.wytbook.util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import top.wytbook.config.CosProperties;

@Component
public class OssUtils {
    @Autowired
    COSClient cosClient;
    @Autowired
    CosProperties properties;
    @Value("#{randomUtils}")
    RandomUtils randomUtils;
    private String getFileName(MultipartFile file) {
        String contentType = file.getContentType();
        assert contentType != null;
        String fileSuffix = contentType.substring(contentType.indexOf('/') + 1);
        String filePreName = Md5Utils.getMd5String(System.currentTimeMillis() + randomUtils.getRandomCode(10), randomUtils.getRandomCode(5));
        return filePreName + "." +fileSuffix;
    }

    // TODO oss接入
    public String uploadToOss(MultipartFile file, String fileDirWithFileName) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            PutObjectRequest putObjectRequest = new PutObjectRequest(properties.getBucketName(), fileDirWithFileName, file.getInputStream(), metadata);
            cosClient.putObject(putObjectRequest);
            return properties.getBaseUrl() + fileDirWithFileName;
        }
        catch (Exception e) {
            return null;
        }
    }

    public String uploadArticlePictureToOss(MultipartFile file) {
        String fileName = getFileName(file);
        String fileDirWithFileName = properties.getArticlePicturePath() + "/" + fileName;
        return uploadToOss(file, fileDirWithFileName);
    }

    public String uploadAdminUserAvatar(MultipartFile file) {
        String fileName = getFileName(file);
        String fileDirWithFileName = properties.getUserAvatarPath() + "/" + fileName;
        return uploadToOss(file, fileDirWithFileName);
    }
}
