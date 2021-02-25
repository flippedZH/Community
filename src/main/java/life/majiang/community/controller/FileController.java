package life.majiang.community.controller;

import life.majiang.community.dto.FileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

@Controller
public class FileController {

//    @Autowired
//    private OssClientAuthorization ossClientAuthorization;

    @RequestMapping("/file/upload") //返回地址
    @ResponseBody //对象
    public FileDTO upload(HttpServletRequest request){
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
//        try {
//            String urlFileName = ossClientAuthorization.upload(file.getInputStream(),file.getOriginalFilename());
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(1);
            fileDTO.setUrl("/img/com.jpg");
            return fileDTO;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        FileDTO fileDTO = new FileDTO();
//        fileDTO.setSuccess(1);
//        fileDTO.setUrl("/images/iceman.png");
//        return fileDTO;
    }

}