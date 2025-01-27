package com.spring.home_solver.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

    String upload(MultipartFile image, String fileName);

    void delete(String publicUrl);
}
