package com.MayankApp.service;

import com.MayankApp.entity.UserDetails;
import com.MayankApp.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Service
public class JavaMailService {
    @Autowired
    private UserRepository repo;

    @Autowired
    private JavaMailSender mailSender;

    public void register(UserDetails user, String siteURL) throws UnsupportedEncodingException, MessagingException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String encryptPassword = encoder.encode(user.getPassword());
        user.setPassword(encryptPassword);
        user.setPassword2(encryptPassword);
//        byte[] array = new byte[64]; // length is bounded by 64
//        new Random().nextBytes(array);
//        String randomCode = new String(array, StandardCharsets.UTF_8);
//            String randomCode = RandomString.make(64);
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(64);
        for (int i = 0; i < 64; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String randomCode = buffer.toString();

        //---------------------------------
        user.setVerificationCode(randomCode);
        user.setEnabled(false);
        repo.save(user);
        sendVerificationEmail(user, siteURL);
    }
    private void sendVerificationEmail(UserDetails user, String siteURL)throws MessagingException, UnsupportedEncodingException {
String toAddress = user.getEmail();
String fromAddress = "mankubhaiji@gmail.com";
String senderName = "Jalpari Technologies";
String subject = "Please verify your registration";
String content = "Dear [[name]],<br>"
+ "Please click the link below to verify your registration:<br>"
+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
+ "Thank you,<br>"
+ "Jellyfish.";

MimeMessage message = mailSender.createMimeMessage();
MimeMessageHelper helper = new MimeMessageHelper(message);

helper.setFrom(fromAddress, senderName);
helper.setTo(toAddress);
helper.setSubject(subject);

content = content.replace("[[name]]", user.getName());
String verifyURL = siteURL + "/user/verify?code=" + user.getVerificationCode();
content = content.replace("[[URL]]", verifyURL);

helper.setText(content, true);

mailSender.send(message);

    }

    public boolean verify(String verificationCode){
        System.out.println(verificationCode);
        UserDetails user= repo.findByVerificationCode(verificationCode);
        if(user==null || user.isEnabled()){
            return false;
        }
        else{
            user.setEnabled(true);
            user.setVerificationCode(null);
            repo.save(user);
            return true;
        }
    }
}
