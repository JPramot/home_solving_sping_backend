package com.spring.home_solver.service;

import com.spring.home_solver.DTO.*;
import com.spring.home_solver.DTO.postDTO.CommentDTO;
import com.spring.home_solver.DTO.postDTO.PostByIdResponseDTO;
import com.spring.home_solver.DTO.postDTO.PostInfoDTO;
import com.spring.home_solver.DTO.postDTO.UserInPost;
import com.spring.home_solver.entity.*;
import com.spring.home_solver.enumulation.Role;
import com.spring.home_solver.exception.ApiErrorExc;
import com.spring.home_solver.exception.NotFoundExc;
import com.spring.home_solver.repository.AppealPostRepository;
import com.spring.home_solver.repository.PostImageRepository;
import com.spring.home_solver.repository.PostRepository;
import com.spring.home_solver.utils.AuthUtil;
import com.spring.home_solver.utils.FileUpload;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService{

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostImageRepository postImageRepository;

    @Autowired
    private AppealPostRepository appealPostRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private FileUpload fileUpload;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ModelMapper modelMapper;

    private final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Override
    public AllPostResponseDTO getAllPost() {
        List<Post> posts = postRepository.findAllPost();
        UserProfile profile = posts.getFirst().getUser().getUserProfile();
        List<PostInfoDTO> postInfoDTOS = posts.stream()
                .map(post -> {
                    PostInfoDTO postInfoDTO = modelMapper.map(post, PostInfoDTO.class);
                    UserInPost user = modelMapper.map(post.getUser(), UserInPost.class);
                    postInfoDTO.setUser(user);
                    postInfoDTO.setUserId(user.getId());
                    return postInfoDTO;
                }).toList();
        logger.info("comment ===== > {}",postInfoDTOS.getFirst().getComments());
        return new AllPostResponseDTO(postInfoDTOS);
    }

    @Transactional
    @Override
    public PostResponseDTO createPost(PostBodyDTO body, MultipartFile[] images) {

        User loginUser = authUtil.loginUser();
        Post newPost = modelMapper.map(body, Post.class);
        newPost.setUser(loginUser);
        postRepository.save(newPost);

        if(images != null){
            logger.info("upload image to cloud now...");
            List<String> uploadedImg = uploadWithMultiThread(images);

            uploadedImg.forEach(image -> {
                PostImage postImage = new PostImage(image);
                newPost.addPostImage(postImage);
            });
        }

        postRepository.save(newPost);
        PostResponseDTO responseDTO = modelMapper.map(newPost, PostResponseDTO.class);

        logger.info("post image =====> :{}",newPost.getPostImages());

        return responseDTO;
    }

    @Transactional
    @Override
    public PostResponseDTO updatePost(PostBodyDTO body, MultipartFile[] images, Integer postId ,
                                      String  postImageId) {
        Post existsPost = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundExc("post", "postId", postId));

        logger.info("test============> {}",postImageId);

        if(postImageId != null) {
            int[] imageId = Arrays.stream(postImageId.split(","))
                    .mapToInt(Integer::parseInt).toArray();
            List<PostImage> postImages = postImageRepository.findByIds(imageId);

            if(postImages.size() != imageId.length) {
                throw new ApiErrorExc("can't edit your post please try again");
            }
            List<String> publicUrls = postImages.stream()
                    .map(image ->{
                        String url = image.getImage();
                        existsPost.removeImage(image);
                        postImageRepository.deleteById(image.getId());
                        return url;
                    })
                    .toList();
            deleteOldImageFromCloud(publicUrls);
        }

        if(images != null) {
            logger.debug("upload image to cloud now...");

            List<String> publicUrls = uploadWithMultiThread(images);
            publicUrls.forEach(url -> {
                PostImage newPostImage = new PostImage(url);
                existsPost.addPostImage(newPostImage);
            });
        }
            existsPost.setContent(body.getContent());
            existsPost.setTitle(body.getTitle());
            postRepository.save(existsPost);

        return modelMapper.map(existsPost, PostResponseDTO.class);
    }

    @Override
    public PostByIdResponseDTO getPostByPostId(Integer postId) {

        Post existsPost = postRepository.findPostById(postId);
        if(existsPost == null) throw new NotFoundExc("post", "postId",postId);

        List<CommentDTO> commentDTOS = existsPost.getComments().stream()
                .map(comment -> {
                    CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
                    CommentDTO.UserWhoComment user = modelMapper.map(comment.getUser(), CommentDTO.UserWhoComment.class);
                    commentDTO.setUser(user);
                    return commentDTO;
                }).toList();

        PostByIdResponseDTO responseDTO = modelMapper.map(existsPost, PostByIdResponseDTO.class);
        responseDTO.setComments(commentDTOS);
//        responseDTO.setUserId(existsPost.getUser().getId());
        logger.info("response post ======> {}",responseDTO);
        logger.info("post comment ======> {}",commentDTOS);
        return responseDTO;
    }

    @Transactional
    @Override
    public void deletePostById(Integer postId) {
        Post existsPost = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundExc("post", "postId", postId));

        postRepository.deleteById(postId);

        if(!existsPost.getPostImages().isEmpty()) {
            List<String> publicUrls = existsPost.getPostImages().stream()
                    .map(PostImage::getImage).toList();
            deleteOldImageFromCloud(publicUrls);
        }

    }

    @Transactional
    @Override
    public ApiMessageResponse appealPost(Integer postId, AppealPostRequestDTO body) {
        User loginUser = authUtil.loginUser();

        Post existsPost = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundExc("post", "postId", postId));
        AppealPost newAppealPost = new AppealPost(body.getContent()).setUser(loginUser).setPost(existsPost);
        appealPostRepository.save(newAppealPost);
        logger.info("appeal post =====> : {}",newAppealPost);
        return new ApiMessageResponse(new ApiMessageResponse.Message("post was appealed"));
    }

    @Override
    public AllAppealPostResponseDTO getAllAppealPost() {
        User loginUser = authUtil.loginUser();
        if(loginUser.getRole() != Role.admin) throw new ApiErrorExc("can't get appealed post");

        List<AppealPost> appealPosts = appealPostRepository.findAll();

        Set<AppealPostDTO> dtos = appealPosts.stream()
                .map(appealPost -> {
                    AppealPostDTO.PostAppealedInfo postInfo = modelMapper.map(appealPost.getPost(), AppealPostDTO.PostAppealedInfo.class);
                    AppealPostDTO.AppealPostOwner owner = modelMapper.map(appealPost.getUser(), AppealPostDTO.AppealPostOwner.class);
                    AppealPostDTO appealPostDTO = modelMapper.map(appealPost, AppealPostDTO.class);
                    appealPostDTO.setPost(postInfo);
                    appealPostDTO.setUser(owner);
                    return appealPostDTO;
                }).collect(Collectors.toSet());

        return new AllAppealPostResponseDTO(dtos);
    }

    @Override
    public void deleteAppealPostById(Integer appealPostId) {
        appealPostRepository.findById(appealPostId)
                .orElseThrow(()-> new NotFoundExc("appealPost", "appealPostId",appealPostId));
        appealPostRepository.deleteById(appealPostId);
    }

    private void deleteOldImageFromCloud(List<String> urls) {
        final int THREAD_COUNT = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        urls.forEach(url -> {
            String  publicUrl = fileUpload.getPublicUrl(url);
            executorService.submit(()-> {
                try {
                    cloudinaryService.delete(publicUrl);
                }catch (Exception exc) {
                    logger.error("delete image from cloudinary fail: {}",exc.getMessage());
                }
            });
        });
        executorService.shutdown();
        try {
            if(!executorService.awaitTermination(30, TimeUnit.SECONDS)){
                executorService.shutdownNow();
            }
        } catch (InterruptedException exc) {
            executorService.shutdownNow();
        }
    }

    private List<String> uploadWithMultiThread(MultipartFile[] images) {
        final int THREAD_COUNT = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        List<Future<String>> futures = new ArrayList<>();
        List<String> uploadUrls = new ArrayList<>();

        List.of(images).forEach( image -> {
            if(!image.isEmpty()){
                String originalName = image.getOriginalFilename() == null
                        ? UUID.randomUUID().toString()
                        : image.getOriginalFilename();
                String filename = fileUpload.generateFileName(originalName);
                Callable<String> task = ()-> cloudinaryService.upload(image, filename);
                futures.add(executorService.submit(task));
            }
        });

        futures.forEach(future -> {
            try {
                uploadUrls.add(future.get());
            }catch (InterruptedException | ExecutionException exc) {
                String message = String.format("fail to upload postImage: %s",exc.getMessage());
                throw new ApiErrorExc(message);
            }
        });

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow(); // บังคับปิดหากรอเกิน 60 วินาที
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow(); // บังคับปิดหากเกิด InterruptedException
        }

        return uploadUrls;
    }


}
