/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomClasses;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author Kusal
 */
public class Validations {

    public static SimpleDateFormat sdf;

    /**
     * @param img    image thai want to validate
     * @param size   size of image
     * @param height height of image
     * @param width  width of image
     * @return boolean- true if image is correct, else if image incorrect
     */
    public static boolean imgValidation(File img, int size, int height, int width) {
        System.out.println((img.length() / 1024) + " SIZE");
        if ((img.length() / 1024) < size) {
            return true;
        } else {
            return false;
        }

    }

    public static String dateObjToString(Date date, String format) {

        return new SimpleDateFormat(format).format(date);

    }


    public static boolean conValidation(String con) {

        if (con.length() < 12 && con.length() >= 10) {

            try {
                Integer.parseInt(con);
                return true;
            } catch (Exception e) {
                return false;
            }

        } else {
            return false;
        }

    }

//    public static boolean idnoValidation(String idno) {
////        System.out.println(idno.length());
//
//        try {
//            ResultSet rs1 = DBClass.DB.search("SELECT * FROM admin WHERE idno = '" + idno + "'");
//
//            if (rs1.next()) {
//                return false;
//            } else {
//
//                if (idno.length() == 10) {
//
//                    String pattern = "[0-9]{9}[V|v]";
//                    if (idno.matches(pattern)) {
////                System.out.println("One Match");
//                        return true;
//                    } else {
////                System.out.println("One NOT Match");
//                        return false;
//                    }
//
//                } else if (idno.length() == 12) {
//
//                    String pattern = "[0-9]{11}[V|v]";
//                    if (idno.matches(pattern)) {
////                System.out.println("Two Match");
//                        return true;
//                    } else {
////                System.out.println("Two NOT Match");
//                        return false;
//                    }
//
//                } else {
//                    System.out.println("NOT MATCH LENGTH");
//                    return false;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//
//    }
//
//    public static boolean emailValidation(String email) {
//
//        try {
//            ResultSet rs1 = DBClass.DB.search("SELECT * FROM admin WHERE email = '" + email + "'");
//
//            if (rs1.next()) {
//                return false;
//            } else {
//
//                boolean b = false;
//                try {
//                    final String sender_mail = "futuresoftcus@gmail.com";
//                    final String sender_password = "shanuka3942";
//
//                    Properties props = new Properties();
//                    props.put("mail.smtp.auth", "true");
//                    props.put("mail.smtp.starttls.enable", "true");
//                    props.put("mail.smtp.host", "smtp.gmail.com");
//                    props.put("mail.smtp.port", "587");
//                    props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
//
//                    Session session = Session.getInstance(props,
//                            new javax.mail.Authenticator() {
//                                @Override
//                                protected PasswordAuthentication getPasswordAuthentication() {
//                                    return new PasswordAuthentication(sender_mail, sender_password);
//                                }
//                            }
//                    );
//
//                    String msg_subject = "no";
//                    String msg_content = "no";
//
//                    //text ekak yawana widiha
//                    msg_subject = "Welcome to Sri lanka Voting Site";
//                    msg_content = "CONGRATULATIONS! You are selected as a Admin of Sri Lanka Voting Site";
//
//                    msg_content += "<br><br><br><br> <center> <h6>Thank You </h6> </center>";
//
//                    Message message = new MimeMessage(session);
//                    message.setFrom(new InternetAddress(sender_mail));
//                    message.setRecipients(Message.RecipientType.TO,
//                            InternetAddress.parse(email));
//                    message.setSubject(msg_subject);
//
//                    //html content ekak yawana widiha
//                    message.setContent(msg_content, "text/html;charset=utf-8");
//
//                    Transport.send(message);
//                    System.out.println("Mail Yawanoooooooooooooooo ");
//
//                    b = true;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    b = false;
//                }
//                return b;
//            }
//        } catch (Exception e) {
////            e.printStackTrace();
//            return false;
//        }
//
//    }
//
//    public static boolean cityValidation(String citid) {
//        try {
//            ResultSet rs1 = DBClass.DB.search("SELECT * FROM city WHERE citid = '" + citid + "'");
//
//            if (rs1.next()) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//
//    }

    public static String generateCode(int length, boolean useNumbers, boolean usePunctuation) {

        StringBuilder code = new StringBuilder(length);
        Random random = new Random(System.nanoTime());
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        if (usePunctuation) {
            characters += "!@#$%&*()_+-=[]|,./?><";
        }

        if (useNumbers) {
            characters += "0123456789";
        }

        for (int i = 0; i < length; i++) {
            int position = random.nextInt(characters.length());
            code.append(characters.charAt(position));
        }

        return new String(code);
    }

    public static String encryptCode(String code) {

        if (code.matches("[a-zA-Z0-9]*")) { // '/[^a-z\d]/i' should also work.
            // string contains only english letters & digits
//            echo 'STR - [' . $str . ']<br>Length =' . strlen($str) . '<br>';
            String ncode = "";
            for (int i = 0; i < code.length(); i++) {
                switch (code.charAt(i)) {
                    case 'A':
                        ncode += "Hj";
                        break;
                    case 'B':
                        ncode += "k7";
                        break;
                    case 'C':
                        ncode += "F7";
                        break;
                    case 'D':
                        ncode += "q6";
                        break;
                    case 'E':
                        ncode += "P8";
                        break;
                    case 'F':
                        ncode += "r3";
                        break;
                    case 'G':
                        ncode += "yT";
                        break;
                    case 'H':
                        ncode += "xY";
                        break;
                    case 'I':
                        ncode += "M5";
                        break;
                    case 'J':
                        ncode += "w9";
                        break;
                    case 'K':
                        ncode += "R7";
                        break;
                    case 'L':
                        ncode += "9R";
                        break;
                    case 'M':
                        ncode += "bc";
                        break;
                    case 'N':
                        ncode += "iY";
                        break;
                    case 'O':
                        ncode += "78";
                        break;
                    case 'P':
                        ncode += "tS";
                        break;
                    case 'Q':
                        ncode += "7Y";
                        break;
                    case 'R':
                        ncode += "p4";
                        break;
                    case 'S':
                        ncode += "fH";
                        break;
                    case 'T':
                        ncode += "5H";
                        break;
                    case 'U':
                        ncode += "t5";
                        break;
                    case 'V':
                        ncode += "jr";
                        break;
                    case 'W':
                        ncode += "8t";
                        break;
                    case 'X':
                        ncode += "B9";
                        break;
                    case 'Y':
                        ncode += "1t";
                        break;
                    case 'Z':
                        ncode += "g7";
                        break;
                    case 'a':
                        ncode += "tf";
                        break;
                    case 'b':
                        ncode += "Cg";
                        break;
                    case 'c':
                        ncode += "8S";
                        break;
                    case 'd':
                        ncode += "L0";
                        break;
                    case 'e':
                        ncode += "69";
                        break;
                    case 'f':
                        ncode += "o8";
                        break;
                    case 'g':
                        ncode += "A7";
                        break;
                    case 'h':
                        ncode += "l7";
                        break;
                    case 'i':
                        ncode += "85";
                        break;
                    case 'j':
                        ncode += "0U";
                        break;
                    case 'k':
                        ncode += "gt";
                        break;
                    case 'l':
                        ncode += "N5";
                        break;
                    case 'm':
                        ncode += "6t";
                        break;
                    case 'n':
                        ncode += "25";
                        break;
                    case 'o':
                        ncode += "zI";
                        break;
                    case 'p':
                        ncode += "g4";
                        break;
                    case 'q':
                        ncode += "F8";
                        break;
                    case 'r':
                        ncode += "p6";
                        break;
                    case 's':
                        ncode += "G8";
                        break;
                    case 't':
                        ncode += "T6";
                        break;
                    case 'u':
                        ncode += "u4";
                        break;
                    case 'v':
                        ncode += "i9";
                        break;
                    case 'w':
                        ncode += "56";
                        break;
                    case 'x':
                        ncode += "ij";
                        break;
                    case 'y':
                        ncode += "VR";
                        break;
                    case 'z':
                        ncode += "8T";
                        break;
                    case '0':
                        ncode += "l8";
                        break;
                    case '1':
                        ncode += "Os";
                        break;
                    case '2':
                        ncode += "1e";
                        break;
                    case '3':
                        ncode += "l9";
                        break;
                    case '4':
                        ncode += "45";
                        break;
                    case '5':
                        ncode += "rf";
                        break;
                    case '6':
                        ncode += "54";
                        break;
                    case '7':
                        ncode += "DT";
                        break;
                    case '8':
                        ncode += "VY";
                        break;
                    case '9':
                        ncode += "Cf";
                        break;
                }
            }

//            echo 'Encryption Step 1 [' . $code . ']<br>Length =' . strlen($code) . '<br>';
//            $code_final = '';
//
//            for ($i = 0; $i < strlen($code); $i++) {
//                $code_final .= $this->randomPassword(1) . $code[$i];
//            }
//            echo 'Encryption Step 2 [' . $code_final . ']<br>Length =' . strlen($code_final) . '<br>';
            return ncode;
        } else {
//            echo 'has other chars';
        }

        return "";
    }

    public static String decryptCode(String code) {

        String[] codear = splitToNChar(code, 2);

        String ncode = "";

        String part = "";

        for (int i = 0; i < codear.length; i++) {

            switch (codear[i].toString()) {
                case "Hj":
                    ncode += "A";
                    break;
                case "k7":
                    ncode += "B";
                    break;
                case "F7":
                    ncode += "C";
                    break;
                case "q6":
                    ncode += "D";
                    break;
                case "P8":
                    ncode += "E";
                    break;
                case "r3":
                    ncode += "F";
                    break;
                case "yT":
                    ncode += "G";
                    break;
                case "xY":
                    ncode += "H";
                    break;
                case "M5":
                    ncode += "I";
                    break;
                case "w9":
                    ncode += "J";
                    break;
                case "R7":
                    ncode += "K";
                    break;
                case "9R":
                    ncode += "L";
                    break;
                case "bc":
                    ncode += "M";
                    break;
                case "iY":
                    ncode += "N";
                    break;
                case "78":
                    ncode += "O";
                    break;
                case "tS":
                    ncode += "P";
                    break;
                case "7Y":
                    ncode += "Q";
                    break;
                case "p4":
                    ncode += "R";
                    break;
                case "fH":
                    ncode += "S";
                    break;
                case "5H":
                    ncode += "T";
                    break;
                case "t5":
                    ncode += "U";
                    break;
                case "jr":
                    ncode += "V";
                    break;
                case "8t":
                    ncode += "W";
                    break;
                case "B9":
                    ncode += "X";
                    break;
                case "1t":
                    ncode += "Y";
                    break;
                case "g7":
                    ncode += "Z";
                    break;
                case "tf":
                    ncode += "a";
                    break;
                case "Cg":
                    ncode += "b";
                    break;
                case "8S":
                    ncode += "c";
                    break;
                case "L0":
                    ncode += "d";
                    break;
                case "69":
                    ncode += "e";
                    break;
                case "o8":
                    ncode += "f";
                    break;
                case "A7":
                    ncode += "g";
                    break;
                case "l7":
                    ncode += "h";
                    break;
                case "85":
                    ncode += "i";
                    break;
                case "0U":
                    ncode += "j";
                    break;
                case "gt":
                    ncode += "k";
                    break;
                case "N5":
                    ncode += "l";
                    break;
                case "6t":
                    ncode += "m";
                    break;
                case "25":
                    ncode += "n";
                    break;
                case "zI":
                    ncode += "o";
                    break;
                case "g4":
                    ncode += "p";
                    break;
                case "F8":
                    ncode += "q";
                    break;
                case "p6":
                    ncode += "r";
                    break;
                case "G8":
                    ncode += "s";
                    break;
                case "T6":
                    ncode += "t";
                    break;
                case "u4":
                    ncode += "u";
                    break;
                case "i9":
                    ncode += "v";
                    break;
                case "56":
                    ncode += "w";
                    break;
                case "ij":
                    ncode += "x";
                    break;
                case "VR":
                    ncode += "y";
                    break;
                case "8T":
                    ncode += "z";
                    break;
                case "l8":
                    ncode += "0";
                    break;
                case "Os":
                    ncode += "1";
                    break;
                case "1e":
                    ncode += "2";
                    break;
                case "l9":
                    ncode += "3";
                    break;
                case "45":
                    ncode += "4";
                    break;
                case "rf":
                    ncode += "5";
                    break;
                case "54":
                    ncode += "6";
                    break;
                case "DT":
                    ncode += "7";
                    break;
                case "VY":
                    ncode += "8";
                    break;
                case "Cf":
                    ncode += "9";
                    break;
            }
        }

        return ncode;

    }

    private static String[] splitToNChar(String text, int size) {
        List<String> parts = new ArrayList<>();

        int length = text.length();
        for (int i = 0; i < length; i += size) {
            parts.add(text.substring(i, Math.min(length, i + size)));
        }
        return parts.toArray(new String[0]);
    }

    //
//    public static boolean sendEmail(String subject, String content, String email) {
//        try {
//            final String sender_mail = "futuresoftcus@gmail.com";
//            final String sender_password = "shanuka3942";
//
//            Properties props = new Properties();
//            props.put("mail.smtp.auth", "true");
//            props.put("mail.smtp.starttls.enable", "true");
//            props.put("mail.smtp.host", "smtp.gmail.com");
//            props.put("mail.smtp.port", "587");
//            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
//
//            Session session = Session.getInstance(props,
//                    new javax.mail.Authenticator() {
//                        @Override
//                        protected PasswordAuthentication getPasswordAuthentication() {
//                            return new PasswordAuthentication(sender_mail, sender_password);
//                        }
//                    }
//            );
//
//            String msg_subject = "no";
//            String msg_content = "no";
//
//            //text ekak yawana widiha
//            msg_subject = subject;
//            msg_content = content;
//
//            msg_content += "<br><br><br><br> <center> <h6>Thank You </h6> </center>";
//
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(sender_mail));
//            message.setRecipients(Message.RecipientType.TO,
//                    InternetAddress.parse(email));
//            message.setSubject(msg_subject);
//
//            //html content ekak yawana widiha
//            message.setContent(msg_content, "text/html;charset=utf-8");
//
//            Transport.send(message);
//            System.out.println("Mail Yawanoooooooooooooooo ");
//
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
    public static boolean pwValidation(String npw) {
        if (npw.length() >= 6) {
//            System.out.println("Length Ok");
//            if (npw.matches("[a-zA-Z0-9]+")) {
//                System.out.println("Char Ok");
            return true;
//            } else {
//                System.out.println("Char Not Ok");
//                return false;
//            }

        } else {
//            System.out.println("Length Not Ok");
            return false;
        }

    }

    public static Date stringDateToDateObj(String date, String dateFormat) {

        Date parse = null;
        try {
            parse = new SimpleDateFormat(dateFormat).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }

    public static String convert24hTo12h(String hhmmtime) {

        try {
            Validations.sdf = new SimpleDateFormat("H:mm");
//            final Date dateObj;
//            dateObj = sdf.parse(hhmmtime);
            return new SimpleDateFormat("hh:mm a").format(sdf.parse(hhmmtime));

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

//
//    public static boolean yearValidate() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    public static boolean yearValidate(String year) {
//        try {
//            sdf = new SimpleDateFormat("yyyy");
//
//            if (Integer.parseInt(sdf.format(new Date())) <= Integer.parseInt(year)) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//
//    }
//
//    public static boolean dateInYearValidate(String date, String year) {
//
//        try {
//
//            sdf = new SimpleDateFormat("yyyy-MM-dd");
//            sdf.parse(date);
//            sdf = new SimpleDateFormat("yyyy");
//            if (sdf.format(sdf.parse(date)).equals(year)) {
//                sdf = new SimpleDateFormat("yyyy-MM-dd");
//                if (sdf.parse(date).after(new Date())) {
//                    return true;
//                } else {
//                    System.out.println("fin");
//                    return false;
//                }
//            } else {
//                System.out.println("fout");
//                return false;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//
//    }
//
//    public static boolean sTimeETimeValidate(String stime, String etime) {
//
//        try {
//
//            sdf = new SimpleDateFormat("hh:mm");
//
//            if (sdf.parse(etime).after(sdf.parse(stime))) {
//                return true;
//            } else {
//                return false;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//
//    }
//
//    public static boolean imgHWValidation(File pimg, int height, int width) {
//        try {
//
//            System.out.println(pimg.length() + " /////////////////////////////////////////");
//            System.out.println(pimg.getName() + " /////////////////////////////////////////");
//
//            BufferedImage bimg = ImageIO.read(pimg);
//            System.out.println(bimg.getHeight() + "-" + bimg.getWidth());
//            if (bimg.getHeight() == height && bimg.getWidth() == width) {
//                return true;
//            } else {
//                return false;
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//
//    }
//
//    public static boolean dateValidate(String date) {
//
//        try {
//
//            sdf = new SimpleDateFormat("yyyy-MM-dd");
//            sdf.parse(date);
//
//            return true;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

}
