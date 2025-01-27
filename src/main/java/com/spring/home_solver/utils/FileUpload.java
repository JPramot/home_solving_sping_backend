package com.spring.home_solver.utils;

import com.spring.home_solver.exception.ApiErrorExc;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FileUpload {

    private final long MAX_SIZE = 10 * 1024 * 1024;

    public final String IMG_PATTERN = "([^\\s]+(\\.(jpg|png|hmp))$)";

    public final String DATE_FORMAT = "yyyyMMddHHmmss";

    public final String FILE_NAME_FORMAT = "%s_%s";

    public static final String  DIRECTORY = "homeSolving/";

    public void checkFileAllow(MultipartFile file) {

        if(file.getSize() > MAX_SIZE) throw new ApiErrorExc("file maximum size is 10MB");

        String fileName = file.getOriginalFilename();
        if(!isAllowExtension(fileName)) throw new ApiErrorExc("only jpg and png file are allowed");
    }

    private boolean isAllowExtension(String fileName) {
        final Matcher matcher = Pattern.compile(IMG_PATTERN, Pattern.CASE_INSENSITIVE)
                .matcher(fileName);
        return matcher.matches();
    }

    public String generateFileName(String originalName) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String date = dateFormat.format(System.currentTimeMillis());

        boolean isJpg = originalName.endsWith("jpg");
        if(isJpg) {
            return String.format(FILE_NAME_FORMAT,
                    originalName.substring(0,originalName.indexOf(".jpg")),date);
        }else {
            return String.format(FILE_NAME_FORMAT,
                    originalName.substring(0,originalName.indexOf(".png")),date);
        }
    }

    public String getPublicUrl(String imageUrl) {
        String publicUrl = imageUrl.substring(imageUrl.indexOf(DIRECTORY));
        boolean isJpg = imageUrl.endsWith("jpg");
        if(isJpg) {
            return publicUrl.substring(0,publicUrl.indexOf(".jpg"));
        }else {
            return publicUrl.substring(0,publicUrl.indexOf(".png"));
        }
    }

}
