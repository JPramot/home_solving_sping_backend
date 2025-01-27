package com.spring.home_solver.service;

import com.cloudinary.Cloudinary;
import com.spring.home_solver.exception.ApiErrorExc;
import com.spring.home_solver.utils.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService{

    @Autowired
    private Cloudinary cloudinary;

    private final Logger logger = LoggerFactory.getLogger(CloudinaryServiceImpl.class);
    @Transactional
    @Override
    public String upload(MultipartFile image, String fileName) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("public_id", FileUpload.DIRECTORY + fileName);
            Map<?,?> response = cloudinary.uploader().upload(image.getBytes(), params);
            return (String) response.get("secure_url");

        }catch (IOException exc) {
            String message = String.format("upload image fail: %s",exc.getMessage());
            throw new ApiErrorExc(message);
        }
    }

    @Override
    public void delete(String publicUrl) {
        try {
            Map<?,?> response = cloudinary.uploader().destroy(publicUrl,Map.of());
            logger.info("delete image from cloudinary: {}",response.get("result"));
        }catch (Exception exc) {
            logger.error("can't delete image from cloudinary: {}",exc.getMessage());
        }
    }
}
