package com.team.unanimous.service;


import com.team.unanimous.dto.ImageDto;
import com.team.unanimous.dto.requestDto.EmailRequestDto;
import com.team.unanimous.dto.requestDto.NicknameRequestDto;
import com.team.unanimous.dto.requestDto.PasswordRequestDto;
import com.team.unanimous.dto.requestDto.SignupRequestDto;
import com.team.unanimous.dto.requestDto.*;
import com.team.unanimous.dto.responseDto.ProfileResponseDto;
import com.team.unanimous.dto.responseDto.SignupResponseDto;
import com.team.unanimous.dto.responseDto.UsernameResponseDto;
import com.team.unanimous.exceptionHandler.CustomException;
import com.team.unanimous.exceptionHandler.ErrorCode;
import com.team.unanimous.model.Image;
import com.team.unanimous.model.user.User;
import com.team.unanimous.repository.ImageRepository;
import com.team.unanimous.repository.user.UserRepository;
import com.team.unanimous.security.UserDetailsImpl;
import com.team.unanimous.security.jwt.JwtTokenUtils;
import com.team.unanimous.service.S3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.regex.Pattern;

@Service
// 생성자를 만드는 번거러움을 없앨 수 있다.
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;
    private final JwtTokenUtils jwtTokenUtils;

    //이메일 코드인증(수정필요)
    public ResponseEntity email(EmailRequestDto emailRequestDto){
        String username = emailRequestDto.getUsername();
        String usernamePattern = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        if(username.equals("")) {
            throw new CustomException(ErrorCode.EMPTY_USERNAME);
        } else if(!Pattern.matches(usernamePattern, username)) {
            throw new CustomException(ErrorCode.USERNAME_WRONG);
        } else if(userRepository.findByUsername(username).isPresent()){
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        UsernameResponseDto usernameResponseDto = new UsernameResponseDto(username,"아이디 저장완료");
        return new ResponseEntity(usernameResponseDto, HttpStatus.OK);
    }

    // 비밀번호 생성 및 유저네임 유효성검사 회원가입
    public ResponseEntity signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String usernamePattern = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        String pattern = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^*+=-]).{6,12}$";
        String password = signupRequestDto.getPassword();
        String passwordCheck = signupRequestDto.getPasswordCheck();
        String nickname = "게스트";
        String defaultFileName = "defaultImage.jpeg";
        String defaultImage = "https://s3-unanimous.s3.ap-northeast-2.amazonaws.com/defaultImage.jpeg";

        boolean isGoogle = false;
        // 아아디 정규식 맞지않는 경우 오류메세지를 전달해준다.
        if(username.equals("")) {
            throw new CustomException(ErrorCode.EMPTY_USERNAME);
        } else if(!Pattern.matches(usernamePattern, username)) {
            throw new CustomException(ErrorCode.USERNAME_WRONG);
        } else if(userRepository.findByUsername(username).isPresent()){
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }else if(signupRequestDto.getPassword().equals("")){
            throw new CustomException(ErrorCode.EMPTY_PASSWORD);
        }else if( 6 > password.length() || 12 < password.length()){
            throw new CustomException(ErrorCode.PASSWORD_LEGNTH);
        }else if(!Pattern.matches(pattern,password)){
            throw new CustomException(ErrorCode.PASSWORD_WRONG);
        } else if(!signupRequestDto.getPassword().equals(passwordCheck)){
            throw new CustomException(ErrorCode.PASSWORD_CHECK);
        }
        password = passwordEncoder.encode(password);

        ImageDto imageDto = new ImageDto(defaultImage, defaultFileName);
        Image image = new Image(imageDto);
        imageRepository.save(image);
        //user 객체에 requestDto에서 받아온값을 넣는다.
        User user = new User(username, password, isGoogle, nickname, image);
        //user 객체를 저장한다.
        userRepository.save(user);

        user.setNickname(user.getNickname()+ " " + user.getId());
        userRepository.save(user);
        SignupResponseDto signupResponseDto = new SignupResponseDto(user,"회원가입 성공");
        return new ResponseEntity(signupResponseDto, HttpStatus.OK);
    }

    // 마이페이지, 회원가입 닉네임 중복 체크
    public ResponseEntity nicknameCheck(NicknameRequestDto nicknameRequestDto) {
        String nickname = nicknameRequestDto.getNickname();
        if(nickname.equals("")) {
            throw new CustomException(ErrorCode.EMPTY_NICKNAME);
        } else if(nickname.length() < 2 || nickname.length() > 10) {
            throw new CustomException(ErrorCode.NICKNAME_LEGNTH);
        } else if (userRepository.findByNickname(nickname).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }
        return new ResponseEntity("사용가능한 닉네임입니다", HttpStatus.OK);
    }


    // 마이페이지, 회원가입 닉네임 수정
    public ResponseEntity nickname(Long userId, NicknameRequestDto nicknameRequestDto, String refreshToken) {
        String nickname = nicknameRequestDto.getNickname();
        User user1 = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER));
        user1.setNickname(nickname);
        userRepository.save(user1);
        return new ResponseEntity("닉네임 저장완료", HttpStatus.OK);
    }

    // 마이페이지 s3이미지 업로드
    public ResponseEntity signupImage(MultipartFile file, Long userId) throws IOException {
        String defaultImage;
        String defaultFileName;
        User user = userRepository.findById(userId).orElseThrow(IllegalAccessError::new);
        Image image = imageRepository.findByImageId(user.getImage().getImageId());

        if(file.isEmpty()){
            defaultFileName = "defaultImage.jpeg";
            defaultImage = "https://s3-unanimous.s3.ap-northeast-2.amazonaws.com/defaultImage.jpeg";

            if(!image.getFilename().equals("defaultImage.jpeg")){
                s3Uploader.deleteUserImage(image.getImageId());
            }

            image.setImageUrl(defaultImage);
            image.setFilename(defaultFileName);
            imageRepository.save(image);
            ProfileResponseDto profileResponseDto = new ProfileResponseDto(user.getImage());
            return new ResponseEntity(profileResponseDto, HttpStatus.OK);
        }else {
            ImageDto imageDto = s3Uploader.upload(file, "ProfileImage");

            if(!image.getFilename().equals("defaultImage.jpeg")){
                s3Uploader.deleteUserImage(image.getImageId());
            }
            image.setImageUrl(imageDto.getImageUrl());
            image.setFilename(imageDto.getFileName());

//            Image image = new Image(s3Uploader.upload(file, "ProfileImage"));
//            user.setImage(image1);
            imageRepository.save(image);
            ProfileResponseDto profileResponseDto = new ProfileResponseDto(image);
            return new ResponseEntity(profileResponseDto, HttpStatus.OK);
        }
    }

    //마이페이지 비밀번호 일치 확인 후 비밀번호 변경 모달창으로 이동
    public ResponseEntity passwordCheck(UserDetailsImpl userDetails, PasswordCheckRequestDto passwordCheckRequestDto) {
        String password = passwordCheckRequestDto.getPassword();
        if(password.equals("")) {
            throw new CustomException(ErrorCode.EMPTY_PASSWORD);
        } else if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new CustomException(ErrorCode.PASSWORD_CHECK);
        }
        return new ResponseEntity("비밀번호 일치", HttpStatus.OK);
    }

    // 마이페이지 비밀번호 변경
    public ResponseEntity passwordChange(UserDetailsImpl userDetails, PasswordRequestDto passwordRequestDto) {
        String password = passwordRequestDto.getPassword();
        String passwordCheck = passwordRequestDto.getPasswordCheck();
        if(userDetails == null){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        } else if(password.equals("")) {
            throw new CustomException(ErrorCode.EMPTY_PASSWORD);
        } else if(!password.equals(passwordCheck)) {
            throw new CustomException(ErrorCode.PASSWORD_CHECK);
        } else if(password.length() < 6 || password.length() > 12) {
            throw new CustomException(ErrorCode.PASSWORD_LEGNTH);
        } else if(!Pattern.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^*+=-]).{6,12}$", password)) {
            throw new CustomException(ErrorCode.PASSWORD_WRONG);
        }

        password = passwordEncoder.encode(password);
        User user = userRepository.findUserById(userDetails.getUser().getId());
        user.setPassword(password);
        userRepository.save(user);
        return new ResponseEntity("비밀번호 변경 완료", HttpStatus.OK);
    }
}
