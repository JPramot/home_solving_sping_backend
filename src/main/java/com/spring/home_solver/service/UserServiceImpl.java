package com.spring.home_solver.service;

import com.spring.home_solver.DTO.*;
import com.spring.home_solver.DTO.postDTO.PostInfoDTO;
import com.spring.home_solver.DTO.userProfilAndAllPostDTO.UserProFileAndAllPostDTO;
import com.spring.home_solver.entity.User;
import com.spring.home_solver.entity.UserProfile;
import com.spring.home_solver.exception.ApiErrorExc;
import com.spring.home_solver.exception.NotFoundExc;
import com.spring.home_solver.repository.UserProfileRepository;
import com.spring.home_solver.repository.UserRepository;
import com.spring.home_solver.utils.AuthUtil;
import com.spring.home_solver.utils.FileUpload;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private FileUpload fileUpload;

    @Autowired
    private ModelMapper modelMapper;

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Transactional
    @Override
    public UserProfileResponseDTO createOrUpdateProfile(UpdateUserProfileDTO updateUserProfileDTO, MultipartFile image) {

        User loginUser = authUtil.loginUser();

        String secureUrl = "";
        UserProfile existsProfile = userProfileRepository.findByUserId(loginUser.getId())
                .orElse(null);

        if(image != null) {
            if(checkOldImage(existsProfile)){
                String publicUrl = fileUpload.getPublicUrl(existsProfile.getProfileImage());
                cloudinaryService.delete(publicUrl);
            }

            fileUpload.checkFileAllow(image);
            String filename = fileUpload.generateFileName(Objects.requireNonNull(image.getOriginalFilename()));
            secureUrl = cloudinaryService.upload(image, filename);
        }

        UserProfile userProfile = loginUser.getUserProfile();
        if(userProfile == null) {
            userProfile = modelMapper.map(updateUserProfileDTO, UserProfile.class);
        }else {
            modelMapper.map(updateUserProfileDTO, userProfile);
        }
        if(secureUrl.startsWith("https")){
            logger.info("it bug secure url ======> {}",secureUrl);
            userProfile.setProfileImage(secureUrl);
        }
        userProfile.setUser(loginUser);
        userProfileRepository.save(userProfile);

        UserProfileResponseDTO responseDTO = modelMapper.map(userProfile, UserProfileResponseDTO.class);
        logger.info("userProfile info ======> {}", responseDTO);

        return responseDTO;
    }

    @Override
    public UserProfileAndAllPostResponseDTO getUserProfileByUserId(Integer userId) {
        User existUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundExc("user", "userId", userId));

        UserProfile existUserProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundExc("userProfile", "userId", userId));

        Set<PostInfoDTO> postInfoDTOS = existUser.getPosts().stream()
                .map(post -> {
                    PostInfoDTO postInfoDTO = modelMapper.map(post, PostInfoDTO.class);
                    return postInfoDTO;
                }).collect(Collectors.toSet());

//        UserProFileAndAllPostDTO userProFileAndAllPostDTO = modelMapper.map(existUserProfile, UserProFileAndAllPostDTO.class);
        UserProFileAndAllPostDTO dto = new UserProFileAndAllPostDTO().setId(existUserProfile.getId())
                        .setProfileImage(existUserProfile.getProfileImage())
                                .setGender(existUserProfile.getGender())
                                        .setBirthDate(existUserProfile.getBirthDate())
                                                .setAlias(existUserProfile.getAlias())
                                                        .setIntroduction(existUserProfile.getIntroduction())
                                                                .setFirstName(existUserProfile.getFirstName())
                                                                        .setLastName(existUserProfile.getLastName());
        dto.setUser(new UserProFileAndAllPostDTO.UserInfo(existUser.getUsername(), postInfoDTOS));

        logger.info("userProfile with all =====> {}" ,dto);

        return new UserProfileAndAllPostResponseDTO(dto);
    }

    @Override
    public ApiMessageResponse banUser(Integer userId) {
        User existsUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundExc("user","userId",userId));
        if(existsUser.getIsBan()) throw new ApiErrorExc("user already banned");
        existsUser.setIsBan(true);
        userRepository.save(existsUser);

        return new ApiMessageResponse(new ApiMessageResponse.Message("user was banned"));
    }

    @Override
    public ApiMessageResponse unBanUser(Integer userId) {
        User existsUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundExc("user", "userId", userId));
        if(!existsUser.getIsBan()) throw new ApiErrorExc("user was not banned");

        existsUser.setIsBan(false);
        userRepository.save(existsUser);

        return new ApiMessageResponse(new ApiMessageResponse.Message("user was unbanned"));
    }

    @Override
    public AllBannedUserResponseDTO getAllBannedUser() {
        List<User> bannedUser = userRepository.findByIsBan();

        Set<BannedUserDTO> bannedUserDTOS = bannedUser.stream()
                .map(user -> modelMapper.map(user,BannedUserDTO.class))
                .collect(Collectors.toSet());

        return new AllBannedUserResponseDTO(bannedUserDTOS);
    }

    private boolean checkOldImage(UserProfile existsProfile) {
        if(existsProfile != null) {
            return existsProfile.getProfileImage().startsWith("https");
        }
        return false;
    }
}
