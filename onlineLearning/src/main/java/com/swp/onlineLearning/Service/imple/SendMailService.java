package com.swp.onlineLearning.Service.imple;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
@Slf4j
public class SendMailService {
    @Value("${spring.mail.username}")
    private String gmail;
    @Autowired
    private JavaMailSender mailSender;
    public void sendMail(String title, String content, String button, String mailAccount, String url)  throws MessagingException {
        String messageToSend = "<!DOCTYPE html>\n" +
                "<html lang=\"en-US\">\n" +
                "<head>\n" +
                "<meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\" />\n" +
                "<title>Reset Password Email Template</title>\n" +
                "<meta name=\"description\" content=\"Reset Password Email Template.\" />\n" +
                "<style type=\"text/css\">\n" +
                "\t\t\ta:hover {\n" +
                "\t\t\t\ttext-decoration: underline !important;\n" +
                "\t\t\t}\n" +
                "\t\t</style>\n" +
                "\t</head>\n" +
                "\n" +
                "\t<body\n" +
                "\t\tmarginheight=\"0\"\n" +
                "\t\ttopmargin=\"0\"\n" +
                "\t\tmarginwidth=\"0\"\n" +
                "\t\tstyle=\"margin: 0px; background-color: #f2f3f8\"\n" +
                "\t\tleftmargin=\"0\"\n" +
                "\t>\n" +
                "\t\t<!--100% body table-->\n" +
                "\t\t<table\n" +
                "\t\t\tcellspacing=\"0\"\n" +
                "\t\t\tborder=\"0\"\n" +
                "\t\t\tcellpadding=\"0\"\n" +
                "\t\t\twidth=\"100%\"\n" +
                "\t\t\tbgcolor=\"#f2f3f8\"\n" +
                "\t\t\tstyle=\"\n" +
                "\t\t\t\t@import url(https://fonts.googleapis.com/css?family=Rubik:300,400,500,700|Open+Sans:300,400,600,700);\n" +
                "\t\t\t\tfont-family: 'Open Sans', sans-serif;\n" +
                "\t\t\t\"\n" +
                "\t\t>\n" +
                "\t\t\t<tr>\n" +
                "\t\t\t\t<td>\n" +
                "\t\t\t\t\t<table\n" +
                "\t\t\t\t\t\tstyle=\"background-color: #f2f3f8; max-width: 670px; margin: 0 auto\"\n" +
                "\t\t\t\t\t\twidth=\"100%\"\n" +
                "\t\t\t\t\t\tborder=\"0\"\n" +
                "\t\t\t\t\t\talign=\"center\"\n" +
                "\t\t\t\t\t\tcellpadding=\"0\"\n" +
                "\t\t\t\t\t\tcellspacing=\"0\"\n" +
                "\t\t\t\t\t>\n" +
                "\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t<td style=\"height: 80px\">&nbsp;</td>\n" +
                "\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t<td style=\"text-align: center\">\n" +
                "\t\t\t\t\t\t\t\t<a\n" +
                "\t\t\t\t\t\t\t\t\thref=\""+url+"\"\n" +
                "\t\t\t\t\t\t\t\t\ttitle=\"logo\"\n" +
                "\t\t\t\t\t\t\t\t\ttarget=\"_blank\"\n" +
                "\t\t\t\t\t\t\t\t>\n" +
                "\t\t\t\t\t\t\t\t\t<img\n" +
                "\t\t\t\t\t\t\t\t\t\twidth=\"60\"\n" +
                "\t\t\t\t\t\t\t\t\t\tsrc=\"https://res.cloudinary.com/sttruyen/image/upload/v1678276800/another/wduj3dzkgsg5dmysll7j.jpg\"\n" +
                "\t\t\t\t\t\t\t\t\t\ttitle=\"logo\"\n" +
                "\t\t\t\t\t\t\t\t\t\talt=\"logo\"\n" +
                "\t\t\t\t\t\t\t\t\t/>\n" +
                "\t\t\t\t\t\t\t\t</a>\n" +
                "\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t<td style=\"height: 20px\">&nbsp;</td>\n" +
                "\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t<table\n" +
                "\t\t\t\t\t\t\t\t\twidth=\"95%\"\n" +
                "\t\t\t\t\t\t\t\t\tborder=\"0\"\n" +
                "\t\t\t\t\t\t\t\t\talign=\"center\"\n" +
                "\t\t\t\t\t\t\t\t\tcellpadding=\"0\"\n" +
                "\t\t\t\t\t\t\t\t\tcellspacing=\"0\"\n" +
                "\t\t\t\t\t\t\t\t\tstyle=\"\n" +
                "\t\t\t\t\t\t\t\t\t\tmax-width: 670px;\n" +
                "\t\t\t\t\t\t\t\t\t\tbackground: #fff;\n" +
                "\t\t\t\t\t\t\t\t\t\tborder-radius: 3px;\n" +
                "\t\t\t\t\t\t\t\t\t\ttext-align: center;\n" +
                "\t\t\t\t\t\t\t\t\t\t-webkit-box-shadow: 0 6px 18px 0 rgba(0, 0, 0, 0.06);\n" +
                "\t\t\t\t\t\t\t\t\t\t-moz-box-shadow: 0 6px 18px 0 rgba(0, 0, 0, 0.06);\n" +
                "\t\t\t\t\t\t\t\t\t\tbox-shadow: 0 6px 18px 0 rgba(0, 0, 0, 0.06);\n" +
                "\t\t\t\t\t\t\t\t\t\"\n" +
                "\t\t\t\t\t\t\t\t>\n" +
                "\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t<td style=\"height: 40px\">&nbsp;</td>\n" +
                "\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t<td style=\"padding: 0 35px\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t<h1\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tcolor: #1e1e2d;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tfont-weight: 500;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tmargin: 0;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tfont-size: 32px;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tfont-family: 'Rubik', sans-serif;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t"+title+"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t</h1>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t<span\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tdisplay: inline-block;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tvertical-align: middle;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tmargin: 29px 0 26px;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tborder-bottom: 1px solid #cecece;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\twidth: 100px;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t></span>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t<p\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tcolor: #455056;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tfont-size: 15px;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tline-height: 24px;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tmargin: 0;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t"+content+"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t</p>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t<a\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\thref=\""+url+"\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tbackground: #20e277;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\ttext-decoration: none !important;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tfont-weight: 500;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tmargin-top: 35px;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tcolor: #fff;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\ttext-transform: uppercase;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tfont-size: 14px;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tpadding: 10px 24px;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tdisplay: inline-block;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tborder-radius: 50px;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t>"+button+"</a\n" +
                "\t\t\t\t\t\t\t\t\t\t\t>\n" +
                "\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t<td style=\"height: 40px\">&nbsp;</td>\n" +
                "\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t</tr>\n" +
                "\n" +
                "\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t<td style=\"height: 20px\">&nbsp;</td>\n" +
                "\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t<td style=\"text-align: center\">\n" +
                "\t\t\t\t\t\t\t\t<p\n" +
                "\t\t\t\t\t\t\t\t\tstyle=\"\n" +
                "\t\t\t\t\t\t\t\t\t\tfont-size: 14px;\n" +
                "\t\t\t\t\t\t\t\t\t\tcolor: rgba(69, 80, 86, 0.7411764705882353);\n" +
                "\t\t\t\t\t\t\t\t\t\tline-height: 18px;\n" +
                "\t\t\t\t\t\t\t\t\t\tmargin: 0 0 0;\n" +
                "\t\t\t\t\t\t\t\t\t\"\n" +
                "\t\t\t\t\t\t\t\t>\n" +
                "\t\t\t\t\t\t\t\t\t&copy; <strong>swplearning group</strong>\n" +
                "\t\t\t\t\t\t\t\t</p>\n" +
                "\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t<td style=\"height: 80px\">&nbsp;</td>\n" +
                "\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t</table>\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t</tr>\n" +
                "\t\t</table>\n" +
                "\t\t<!--/100% body table-->\n" +
                "\t</body>\n" +
                "</html>\n";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
        mimeMessageHelper.setFrom(gmail.trim());
        mimeMessageHelper.setTo(mailAccount);
        mimeMessageHelper.setSubject("Register confirm to user");
        mimeMessageHelper.setText(messageToSend);

        message.setContent(messageToSend, "text/html; charset=utf-8");
        mailSender.send(message);
    }
}
