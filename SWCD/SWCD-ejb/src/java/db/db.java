/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author vcl4
 */
public class db {
    
    
    private String dbInfo;
    private String dbId;
    private String dbPw;
    
    private java.sql.Connection conn;
    private java.sql.Statement stmt;
    private java.sql.ResultSet rs;
    
    //db d = new db ();

        
    public void connect (String dbInfo, String dbId, String dbPw)
    {
        this.dbInfo = dbInfo;
        this.dbId = dbId;
        this.dbPw = dbPw;
                
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            
            this.conn = java.sql.DriverManager.getConnection(this.dbInfo, this.dbId, this.dbPw);
            this.stmt = this.conn.createStatement();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
   }
    
    public void update (String dbCmd)
    {
        try
        {
            this.stmt.executeUpdate(dbCmd);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
   
   public void select (String dbCmd)
    {
        try
        {
            this.rs = this.stmt.executeQuery(dbCmd);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void close ()
    {
        try
        {
            conn.close();
            //stmt.close();
            //rs.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void createTable ()
    {
        this.update("CREATE TABLE FAMILY (ID VARCHAR (64) NOT NULL, PW VARCHAR (64) NOT NULL);");
        this.update("CREATE TABLE USER (ID VARCHAR (64) NOT NULL, PW VARCHAR (64) NOT NULL, NAME VARCHAR (64) NOT NULL, PHONE VARCHAR (256) NOT NULL);");
        this.update("CREATE TABLE FAMILY_USER (FAMILYID VARCHAR (64) NOT NULL, USERID VARCHAR (64) NOT NULL, AUTHORITY VARCHAR (7) NOT NULL);");
        this.update("CREATE TABLE DEVICE (ID VARCHAR (64) NOT NULL, COMPANY VARCHAR (64) NOT NULL, CLASSIFICATION VARCHAR (64) NOT NULL, MODEL VARCHAR (64) NOT NULL);");
        this.update("CREATE TABLE FAMILY_DEVICE (FAMILYID VARCHAR (64) NOT NULL, DEVICEID VARCHAR (64) NOT NULL, ADDRESS VARCHAR (15) NOT NULL, NAME VARCHAR (64) NOT NULL);");
        this.update("CREATE TABLE INSTRUCTION (ID VARCHAR (64) NOT NULL, USERINSTRUCTION VARCHAR (64) NOT NULL, IRINSTRUCTION VARCHAR (10) NOT NULL);");
        this.update("CREATE TABLE DEVICE_INSTRUCTION (DEVICEID VARCHAR (64) NOT NULL, INSTRUCTIONID VARCHAR (64) NOT NULL);");
        this.update("CREATE TABLE DOORLOCKLOG (PHOTO BLOB NOT NULL, ID VARCHAR (64) NOT NULL, DATE DATE NOT NULL, TIME TIME NOT NULL);");
        this.update("create table custominstruction (familyid varchar (64) not null, deviceid varchar (64) not null, userinstruction varchar (64) not null, irinstruction varchar (10) not null);");
        
    }
    
    public void setRelation ()
    {
        this.update("ALTER TABLE FAMILY ADD PRIMARY KEY (ID);");
        this.update("ALTER TABLE USER ADD PRIMARY KEY (ID);");
        this.update("ALTER TABLE FAMILY_USER ADD PRIMARY KEY (FAMILYID, USERID);");
        this.update("ALTER TABLE FAMILY_USER ADD CONSTRAINT FAMILY_USER_IBFK_1 FOREIGN KEY (FAMILYID) REFERENCES FAMILY (ID);");
        this.update("ALTER TABLE FAMILY_USER ADD CONSTRAINT FAMILY_USER_IBFK_2 FOREIGN KEY (USERID) REFERENCES USER (ID);");
        this.update("ALTER TABLE DEVICE ADD PRIMARY KEY (ID);");
        //this.update("ALTER TABLE FAMILY_DEVICE ADD PRIMARY KEY (FAMILYID, DEVICEID);");
        this.update("ALTER TABLE FAMILY_DEVICE ADD CONSTRAINT FAMILY_DEVICE_IBFK_1 FOREIGN KEY (FAMILYID) REFERENCES FAMILY (ID);");
        this.update("ALTER TABLE FAMILY_DEVICE ADD CONSTRAINT FAMILY_DEVICE_IBFK_2 FOREIGN KEY (DEVICEID) REFERENCES DEVICE (ID);");
        this.update("ALTER TABLE INSTRUCTION ADD PRIMARY KEY (ID);");
        this.update("ALTER TABLE DEVICE_INSTRUCTION ADD CONSTRAINT DEVICE_INSTRUCTION_IBFK_1 FOREIGN KEY (DEVICEID) REFERENCES DEVICE (ID);");
        this.update("ALTER TABLE DEVICE_INSTRUCTION ADD CONSTRAINT DEVICE_INSTRUCTION_IBFK_2 FOREIGN KEY (INSTRUCTIONID) REFERENCES INSTRUCTION (ID);");
        //this.update("ALTER TABLE DOORLOCKLOG ADD PRIMARY KEY (DATE, TIME);");
        this.update("ALTER TABLE DOORLOCKLOG ADD CONSTRAINT DOORLOCKLOG_IBFK_1 FOREIGN KEY (ID) REFERENCES USER (ID);");
        this.update("alter table custominstruction add constraint constraintcustominstruction_ibfk_1 foreign key (familyid) references family (id);");
        this.update("alter table custominstruction add constraint constraintcustominstruction_ibfk_2 foreign key (deviceid) references device (id);");
    }
    
    public static void main (String[] args)
    {
        db d = new db ();
        
        
        d.connect("jdbc:mysql://localhost:3306/db?autoReconnect=true&useSSL=false", "root", "3306");
        d.createTable();
        d.setRelation();
        
        //d.sample();
        d.sample2();
        
        
        //d.update("insert into family_device (familyId, deviceId, name, address) value (\"_id\", \"hanjoDoorlock\", \"hjdl\", \"127.0.0.1\");");
        d.close();
    }
    
    public void sample2 ()
    {
        this.update("insert into family (id, pw) value (\"hanjo\", \"hanjo\");");
        this.update("insert into user (id, pw, name, phone) value (\"h\", \"h\", \"준호\", \"dGcCz2ewV7A:APA91bHyq1QtrtfZ1Z61An7xwNiDXe2eC_AKm5sr8PaeiWn08JjqGDOw1PeiXDyv1bmaD-2mOKlzVWsLj2_bRglGlK1X6Trt6G358kftC6tS6ZBRIBq5pyZIK3cZzOibvV88_uY4A_VU\");");
        this.update("insert into family_user (familyid, userid, authority) value (\"hanjo\", \"h\", \"send\");");
        this.update("insert into user (id, pw, name, phone) value (\"hanjo2\", \"hanjo2\", \"순호\", \"dGcCz2ewV7A:APA91bHyq1QtrtfZ1Z61An7xwNiDXe2eC_AKm5sr8PaeiWn08JjqGDOw1PeiXDyv1bmaD-2mOKlzVWsLj2_bRglGlK1X6Trt6G358kftC6tS6ZBRIBq5pyZIK3cZzOibvV88_uY4A_VU\");");
        this.update("insert into family_user (familyid, userid, authority) value (\"hanjo\", \"hanjo2\", \"receive\");");
        this.update("insert into user (id, pw, name, phone) value (\"hanjo3\", \"hanjo3\", \"상구\", \"dGcCz2ewV7A:APA91bHyq1QtrtfZ1Z61An7xwNiDXe2eC_AKm5sr8PaeiWn08JjqGDOw1PeiXDyv1bmaD-2mOKlzVWsLj2_bRglGlK1X6Trt6G358kftC6tS6ZBRIBq5pyZIK3cZzOibvV88_uY4A_VU\");");
        this.update("insert into family_user (familyid, userid, authority) value (\"hanjo\", \"hanjo3\", \"receive\");");
        
        this.update("insert into device (id, company, classification, model) value (\"hanjoDoorlock\", \"hanjo\", \"doorlock\", \"hjdl\");");
        this.update("insert into device (id, company, classification, model) value (\"samsungTv1\", \"samsung\", \"tv\", \"KS9800\");");
        this.update("insert into device (id, company, classification, model) value (\"samsungTv2\", \"samsung\", \"tv\", \"KU6190\");");
        this.update("insert into device (id, company, classification, model) value (\"samsungTv3\", \"samsung\", \"tv\", \"K5550\");");
        this.update("insert into device (id, company, classification, model) value (\"lgTv1\", \"lg\", \"tv\", \"OLED77G6K\");");
        this.update("insert into device (id, company, classification, model) value (\"lgTv2\", \"lg\", \"tv\", \"60LF6500\");");
        this.update("insert into device (id, company, classification, model) value (\"lgAircondition1\", \"lg\", \"aircondition\", \"FQ251LC1W\");");
        this.update("insert into device (id, company, classification, model) value (\"lgAircondition2\", \"lg\", \"aircondition\", \"LPQ1000VP\");");
        this.update("insert into family_device (familyId, deviceId, name, address) value (\"hanjo\", \"hanjoDoorlock\", \"현관문도어락\", \"192.168.137.104\");");
        this.update("insert into family_device (familyId, deviceId, name, address) value (\"hanjo\", \"samsungTv1\", \"거실티비\", \"192.168.137.203\");");
        this.update("insert into family_device (familyId, deviceId, name, address) value (\"hanjo\", \"lgAircondition1\", \"팔달관에어컨\", \"192.168.137.203\");");
        
        this.addOneDoorlocklogByUser("h", "image1.bmp", "2016-12-03", "12:15:47");
        this.addOneDoorlocklogByUser("h", "image1.bmp", "2016-12-03", "12:45:46");
        this.addOneDoorlocklogByUser("hanjo2", "image1.bmp", "2016-12-03", "16:51:45");
        this.addOneDoorlocklogByUser("hanjo2", "image1.bmp", "2016-12-04", "05:25:08");
        this.addOneDoorlocklogByUser("hanjo3", "image1.bmp", "2016-12-04", "07:01:23");
        this.addOneDoorlocklogByUser("h", "image1.bmp", "2016-12-04", "08:00:01");
        this.addOneDoorlocklogByUser("hanjo2", "image1.bmp", "2016-12-04", "08:00:03");
        this.addOneDoorlocklogByUser("hanjo3", "image1.bmp", "2016-12-04", "08:50:15");
        this.addOneDoorlocklogByUser("h", "image1.bmp", "2016-12-04", "15:12:24");
        this.addOneDoorlocklogByUser("hanjo2", "image1.bmp", "2016-12-04", "17:43:34");
        
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_Power\", \"TV_Power\", \"E0E040BF\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_Source\", \"TV_Source\", \"E0E0807F\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_1\", \"TV_1\", \"E0E020DF\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_2\", \"TV_2\", \"E0E0A05F\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_3\", \"TV_3\", \"E0E0609F\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_4\", \"TV_4\", \"E0E010EF\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_5\", \"TV_5\", \"E0E0906F\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_6\", \"TV_6\", \"E0E050AF\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_7\", \"TV_7\", \"E0E030CF\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_8\", \"TV_8\", \"E0E0B04F\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_9\", \"TV_9\", \"E0E0708F\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_0\", \"TV_0\", \"E0E08877\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_Pause\", \"TV_Pause\", \"E0E052AD\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_Rewind\", \"TV_Rewind\", \"E0E0A25D\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_Stop\", \"TV_Stop\", \"E0E0629D\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_Play\", \"TV_Play\", \"E0E0E21D\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_Fastforward\", \"TV_Fastforward\", \"E0E012ED\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_Vol+\", \"TV_Vol+\", \"E0E0E01F\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_Vol-\", \"TV_Vol-\", \"E0E0D02F\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_Mute\", \"TV_Mute\", \"E0E0F00F\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_Chan_up\", \"TV_Chan_up\", \"E0E048B7\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_Chan_down\", \"TV_Chan_down\", \"E0E008F7\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_Menu\", \"TV_Menu\", \"E0E058A7\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_Return\", \"TV_Return\", \"E0E01AE5\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_Info\", \"TV_Info\", \"E0E0F807\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_Exit\", \"TV_Exit\", \"E0E0B44B\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_Arrow_up\", \"TV_Arrow_up\", \"E0E006F9\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_Arrow_down\", \"TV_Arrow_down\", \"E0E08679\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_Arrow_left\", \"TV_Arrow_left\", \"E0E0A659\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_Arrow_right\", \"TV_Arrow_right\", \"E0E046B9\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_Enter\", \"TV_Enter\", \"E0E016E9\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1TV_Sleep\", \"TV_Sleep\", \"E0E0C03F\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"lgAircondition1Aircondition_Power\", \"Aircondition_Power\", \"880084C\");");
        
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_Power\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_Source\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_1\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_2\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_3\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_4\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_5\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_6\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_7\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_8\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_9\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_0\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_Pause\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_Rewind\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_Stop\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_Play\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_Fastforward\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_Vol+\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_Vol-\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_Mute\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_Chan_up\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_Chan_down\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_Menu\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_Return\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_Info\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_Exit\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_Arrow_up\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_Arrow_down\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_Arrow_left\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_Arrow_right\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_Enter\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1TV_Sleep\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"lgAircondition1\", \"lgAircondition1Aircondition_Power\");");
        
        this.update("insert into custominstruction (familyid, deviceid, userinstruction, irinstruction) value (\"hanjo\", \"samsungTv1\", \"TV_Red\", \"E0E036C9\");");
        this.update("insert into custominstruction (familyid, deviceid, userinstruction, irinstruction) value (\"hanjo\", \"samsungTv1\", \"TV_Green\", \"E0E028D7\");");
        this.update("insert into custominstruction (familyid, deviceid, userinstruction, irinstruction) value (\"hanjo\", \"samsungTv1\", \"TV_Yellow\", \"E0E0A857\");");
        this.update("insert into custominstruction (familyid, deviceid, userinstruction, irinstruction) value (\"hanjo\", \"samsungTv1\", \"TV_Blue\", \"rㅕE0E06897\");");
        
        
        
        
        
    }
    
    public void sample ()
    {
        this.update("INSERT INTO FAMILY (ID, PW) VALUE (\"family1\", \"family1\");");
        this.update("INSERT INTO USER (ID, PW, NAME, PHONE) VALUE (\"user1\", \"user1\", \"name1\", \"phone1\");");
        this.update("INSERT INTO FAMILY_USER (FAMILYID, USERID, AUTHORITY) VALUE (\"family1\", \"user1\", \"send\")");
        this.update("INSERT INTO USER (ID, PW, NAME, PHONE) VALUE (\"user2\", \"user2\", \"name2\", \"phone2\");");
        this.update("INSERT INTO FAMILY_USER (FAMILYID, USERID, AUTHORITY) VALUE (\"family1\", \"user2\", \"send\")");
        this.update("INSERT INTO USER (ID, PW, NAME, PHONE) VALUE (\"user3\", \"user3\", \"name3\", \"phone3\");");
        this.update("INSERT INTO FAMILY_USER (FAMILYID, USERID, AUTHORITY) VALUE (\"family1\", \"user3\", \"receive\")");
        
        this.update("INSERT INTO FAMILY (ID, PW) VALUE (\"family2\", \"family2\");");
        this.update("INSERT INTO USER (ID, PW, NAME, PHONE) VALUE (\"user4\", \"user4\", \"name4\", \"phone4\");");
        this.update("INSERT INTO FAMILY_USER (FAMILYID, USERID, AUTHORITY) VALUE (\"family2\", \"user4\", \"send\")");
        this.update("INSERT INTO USER (ID, PW, NAME, PHONE) VALUE (\"user5\", \"user5\", \"name5\", \"phone5\");");
        this.update("INSERT INTO FAMILY_USER (FAMILYID, USERID, AUTHORITY) VALUE (\"family2\", \"user5\", \"receive\")");
        
        this.update("INSERT INTO FAMILY (ID, PW) VALUE (\"f\", \"f\");");
        
        this.update("insert into device (id, company, classification, model) value (\"hanjoDoorlock\", \"hanjo\", \"doorlock\", \"hanjodoorlock\");");
        this.update("insert into family_device (familyId, deviceId, name, address) value (\"family1\", \"hanjoDoorlock\", \"hjdl\", \"127.0.0.1\");");
        this.update("insert into device (id, company, classification, model) value (\"samsungTv1\", \"samsung\", \"tv\", \"11111111\");");
        this.update("insert into device (id, company, classification, model) value (\"samsungTv2\", \"samsung\", \"tv\", \"22222222\");");
        this.update("insert into device (id, company, classification, model) value (\"samsungTv3\", \"samsung\", \"tv\", \"33333333\");");
        this.update("insert into device (id, company, classification, model) value (\"samsungTv4\", \"samsung\", \"tv\", \"44444444\");");
        this.update("insert into family_device (familyId, deviceId, name, address) value (\"family1\", \"samsungTv1\", \"tv1\", \"127.0.0.1\");");
        this.update("insert into family_device (familyId, deviceId, name, address) value (\"family1\", \"samsungTv2\", \"tv2\", \"222.222.222.222\");");
        this.update("insert into device (id, company, classification, model) value (\"lgTv1\", \"lg\", \"tv\", \"55555555\");");
        this.update("insert into device (id, company, classification, model) value (\"lgTv2\", \"lg\", \"tv\", \"66666666\");");
        this.update("insert into device (id, company, classification, model) value (\"lgAircondition1\", \"lg\", \"aircondition\", \"77777777\");");
        this.update("insert into device (id, company, classification, model) value (\"lgAircondition2\", \"lg\", \"aircondition\", \"88888888\");");
        this.update("insert into family_device (familyId, deviceId, name, address) value (\"family1\", \"lgTv1\", \"tv3\", \"111.111.111.111\");");
        this.update("insert into family_device (familyId, deviceId, name, address) value (\"family1\", \"lgAircondition1\", \"aircondition1\", \"222.222.222.222\");");
        
        
        this.addOneDoorlocklogByUser("user1", "testImage1.jpg", "2016-12-01", "10:10:10");
        this.addOneDoorlocklogByUser("user1", "testImage2.jpg", "2016-12-01", "11:15:12");
        this.addOneDoorlocklogByUser("user1", "testImage3.jpg", "2016-12-01", "12:37:48");
        this.addOneDoorlocklogByUser("user2", "testImage4.jpg", "2016-12-02", "02:01:49");
        this.addOneDoorlocklogByUser("user2", "testImage5.jpg", "2016-12-02", "02:01:58");
        
        //this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1Up\", \"TV_Power\", \"0xE0E040BF\");");
        
        
        
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1Up\", \"up\", \"11111111\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1Down\", \"down\", \"22222222\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1Left\", \"left\", \"33333333\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"samsungTv1Right\", \"right\", \"44444444\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1Up\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1Down\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1Left\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"samsungTv1\", \"samsungTv1Right\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"lgTv1Up\", \"up\", \"55555555\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"lgTv1Down\", \"down\", \"66666666\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"lgTv1Left\", \"left\", \"77777777\");");
        this.update("insert into instruction (id, userInstruction, IRInstruction) value (\"lgTv1Right\", \"right\", \"88888888\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"lgTv1\", \"lgTv1Up\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"lgTv1\", \"lgTv1Down\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"lgTv1\", \"lgTv1Left\");");
        this.update("insert into device_instruction (deviceId, instructionid) value (\"lgTv1\", \"lgTv1Right\");");
        
        
        
        
                
    }
    
    public void addOneDoorlocklogByUser (String userId, String imageName, String imageDate, String imageTime)
    {
        try
        {
            File file = new File (imageName);
            InputStream is = new FileInputStream (file);
            
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO doorlocklog (photo, id, date, time) VALUE (?, ?, ?, ?);");
            
            pstmt.setBinaryStream(1, is, (int) file.length());
            pstmt.setString(2, userId);
            pstmt.setString(3, imageDate);
            pstmt.setString(4, imageTime);
            
            pstmt.execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public Connection getConn() {
        return conn;
    }
    
    public ResultSet getRs() {
        return rs;
    }
}
