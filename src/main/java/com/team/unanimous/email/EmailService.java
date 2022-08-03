package com.team.unanimous.email;

import com.team.unanimous.exceptionHandler.CustomException;
import com.team.unanimous.exceptionHandler.ErrorCode;
import com.team.unanimous.model.team.Team;
import com.team.unanimous.model.user.User;
import com.team.unanimous.repository.team.TeamRepository;
import com.team.unanimous.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailCodeRepository emailCodeRepository;

    private final UserRepository userRepository;

    private final TeamRepository teamRepository;

    private String nickName = "Unanimous";
    @Value("${spring.mail.username}")
    private String email;
    private String username = "team-unanimous";
    @Value("${spring.mail.password}")
    private String password;

    //회원가입시 코드 전송
    public ResponseEntity emailSend(EmailRequestDto emailRequestDto) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.naver.com");
        props.put("mail.smtp.port", 465);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.naver.com");
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        session.setDebug(true);
        String username = emailRequestDto.getUsername();
        String usernamePattern = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        if (username.equals("")) {
            throw new CustomException(ErrorCode.EMPTY_USERNAME);
        } else if (!Pattern.matches(usernamePattern, username)) {
            throw new CustomException(ErrorCode.USERNAME_WRONG);
        } else if (userRepository.findByUsername(username).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        String code = "";
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 4; i++) { // 인증코드 4자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
            code = key.toString();
        }
        try {
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(email, nickName));        // 별명 설정
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(emailRequestDto.getUsername()));
            mimeMessage.setSubject("제목");
            mimeMessage.setText(new StringBuffer().append("안녕하세요! Unanimous 입니다.")
                    .append("\n").append("홈페이지로 돌아가서 이메일 인증코드를 입력해주세요.")
                    .append("\n")
                    .append("\n").append("이메일 인증코드: " + code)
                    .append("\n")
                    .append("\n").append("서비스를 이용해 주셔서 감사합니다")
                    .toString());
            EmailCode emailCode1 = emailCodeRepository.findByUsername(emailRequestDto.getUsername()).orElse(null);
            if (emailCode1 == null) {
                EmailCode emailCode = new EmailCode();
                emailCode.setUsername(emailRequestDto.getUsername());
                emailCode.setCode(code);
                emailCodeRepository.save(emailCode);
            } else if (emailCode1.getUsername().equals(emailRequestDto.getUsername())) {
                emailCode1.setCode(code);
                emailCodeRepository.save(emailCode1);
            }
            Transport.send(mimeMessage);
            return ResponseEntity.ok("이메일 전송 성공 ");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("이메일 전송 실패");
        }
    }

    // 회원가입시 코드 확인
    public ResponseEntity emailsCode(EmailCodeRequestDto emailCodeRequestDto) {
        EmailCode emailCode = emailCodeRepository.findByCode(emailCodeRequestDto.getCode()).orElse(null);
        if (emailCode == null || !emailCode.getCode().equals(emailCodeRequestDto.getCode())) {
            return ResponseEntity.badRequest().body("인증코드가 일치하지 않습니다.");
        }
        emailCodeRepository.delete(emailCode);
        return ResponseEntity.ok("인증코드가 일치합니다.");
    }

    //팀 참가시 UUID 보내기
    public ResponseEntity teamEmailSend(Long teamId, TeamEmailRequestDto teamEmailRequestDto) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.naver.com");
        props.put("mail.smtp.port", 465);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.naver.com");
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        session.setDebug(true);
        System.out.println("시작한다");
        System.out.println(session);

        List<String> teamEmailRequestDtos = teamEmailRequestDto.getEmailRequestDtoList();
        System.out.println(teamEmailRequestDtos);
        for (String teamUsername : teamEmailRequestDtos) {
            String username = teamUsername;
            System.out.println(username);
            try {
                Team team = teamRepository.findById(teamId).orElse(null);
                String uuid = team.getUuid();
                Message mimeMessage = new MimeMessage(session);
                mimeMessage.setFrom(new InternetAddress(email, nickName));        // 별명 설정
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(username));
                mimeMessage.setSubject("Unanimous 에서 인증번호 발송");
                mimeMessage.setText(new StringBuffer().append("안녕하세요! Unanimous 입니다.")
                        .append("\n").append("팀에 초대 되었습니다.")
                        .append("\n").append("홈페이지로 돌아가서 팀참가 코드 를 입력해주세요.")
                        .append("\n")
                        .append("\n").append("팀참가 코드: " + uuid)
                        .append("\n")
                        .append("\n").append("서비스를 이용해 주셔서 감사합니다")
                        .toString());
                EmailCode emailCode1 = emailCodeRepository.findByUsername(username).orElse(null);
                if (emailCode1 == null) {
                    EmailCode emailCode = new EmailCode();
                    emailCode.setUsername(username);
                    emailCode.setCode(uuid);
                    emailCodeRepository.save(emailCode);
                } else if (emailCode1.getUsername().equals(username)) {
                    emailCode1.setCode(uuid);
                    emailCodeRepository.save(emailCode1);
                }
                Transport.send(mimeMessage);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("이메일 전송 실패");
            }
        }
        return ResponseEntity.ok("이메일 전송 성공 ");
    }

    //팀 참가시 코드 확인
    public ResponseEntity teamEmailCode(EmailCodeRequestDto emailCodeRequestDto) {
        String uuid = emailCodeRequestDto.getCode();
        Team team = teamRepository.findByUuid(uuid);
        if(uuid.equals("")){
            return ResponseEntity.badRequest().body("인증코드를 입력해주세요");
        }else if(!uuid.equals(team.getUuid())) {
            return ResponseEntity.badRequest().body("인증코드가 일치하지 않습니다.");
        }

        return ResponseEntity.ok("인증코드가 일치합니다." + team);
    }

    //비밀번호 찾기시 이메일 보내기
    public ResponseEntity passwordFind(EmailRequestDto emailRequestDto) {
        User usernames = userRepository.findByUsername(emailRequestDto.getUsername()).orElse(null);
         if(usernames == null){
             return ResponseEntity.badRequest().body("회원가입 되어있지 않은 이메일 입니다");
         }
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.naver.com");
        props.put("mail.smtp.port", 465);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.naver.com");
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        session.setDebug(true);
        String username = emailRequestDto.getUsername();
        String usernamePattern = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        if (username.equals("")) {
            throw new CustomException(ErrorCode.EMPTY_USERNAME);
        } else if (!Pattern.matches(usernamePattern, username)) {
            throw new CustomException(ErrorCode.USERNAME_WRONG);
        }
        String code = "";
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 4; i++) { // 인증코드 4자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
            code = key.toString();
        }
        try {
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(email, nickName));        // 별명 설정
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(emailRequestDto.getUsername()));
            mimeMessage.setSubject("Unanimous 에서 인증번호 발송");
            mimeMessage.setText(new StringBuffer().append("안녕하세요! Unanimous 입니다.")
                    .append("\n").append("홈페이지로 돌아가서 비밀번호 인증코드를 입력해주세요.")
                    .append("\n")
                    .append("\n").append("비밀번호 인증코드: " + code)
                    .append("\n")
                    .append("\n").append("서비스를 이용해 주셔서 감사합니다")
                    .toString());
            EmailCode emailCode1 = emailCodeRepository.findByUsername(emailRequestDto.getUsername()).orElse(null);
            if (emailCode1 == null) {
                EmailCode emailCode = new EmailCode();
                emailCode.setUsername(emailRequestDto.getUsername());
                emailCode.setCode(code);
                emailCodeRepository.save(emailCode);
            } else if (emailCode1.getUsername().equals(emailRequestDto.getUsername())) {
                emailCode1.setCode(code);
                emailCodeRepository.save(emailCode1);
            }
            Transport.send(mimeMessage);
            return ResponseEntity.ok("이메일 전송 성공 ");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("이메일 전송 실패");
        }
    }
}

