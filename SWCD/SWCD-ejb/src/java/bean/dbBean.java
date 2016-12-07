/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import class1.custominstruction;
import db.db;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Calendar;
import javax.ejb.Stateless;
import class1.doorlocklog;
import class2.deviceData;
import class2.userData;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;

/**
 *
 * @author vcl4
 */
@Stateless
public class dbBean implements dbBeanLocal {//, dbBeanRemote {

    int doorlockOpenClientPort = 15150;
    int doorlockOpenServerPort = 15151;
    int remoteControlClientPort = 15152;
    int addIRInstructionClientPort = 15153;
    int addIRInstructionServerPort = 15154;
    public String FCMServerKey = "AIzaSyDRt5yaP8Q8zM6Uy7SfSFAifvQtc25kZL4";
    public String FCMApiUrl = "https://fcm.googleapis.com/fcm/send";
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    db d = new db ();
    
    public dbBean ()
    {
         d.connect("jdbc:mysql://localhost:3306/db?autoReconnect=true&useSSL=false", "root", "3306");
    }
    
    public int logIn (String id, String pw)
    {
        d.select("SELECT ID FROM FAMILY WHERE FAMILY.ID=\"" + id + "\" AND FAMILY.PW=\"" + pw + "\";");
        
        try
        {
            d.getRs().beforeFirst();
            
            if (d.getRs().next())
            {
                System.out.println("login: success.");
                return 0;
            }
            else
            {
                d.select("SELECT ID FROM USER WHERE USER.ID=\"" + id + "\" AND USER.PW=\"" + pw + "\";");
                
                try
                {
                    d.getRs().beforeFirst();
            
                    if (d.getRs().next())
                    {
                        System.out.println("login: success.");
                        return 1;
                    }
                    else
                    {
                        System.out.println("login: fail.");
                        return 2;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("login: error.");
                    return 3;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("login: error.");
            return 3;
        }
        
    }
    
    public ArrayList<userData> showAllUserByFamily (String familyId, String familyPw)
    {
        ArrayList<userData> uds = new ArrayList<userData> ();
        
        d.select("SELECT ID FROM FAMILY WHERE FAMILY.ID=\"" + familyId + "\" AND FAMILY.PW=\"" + familyPw + "\";");
            
        try
        {
            d.getRs().beforeFirst();
            
            if (d.getRs().next())
            {
                d.select("SELECT ID, PW, NAME, PHONE, AUTHORITY FROM FAMILY_USER JOIN USER ON FAMILY_USER.USERID=USER.ID WHERE FAMILY_USER.FAMILYID=\"" + familyId + "\";");
                
                try
                {
                    d.getRs().beforeFirst();
                    
                    if (d.getRs().next())
                    {
                        d.getRs().beforeFirst();
                        
                        while (d.getRs().next())
                        {
                            userData ud = new userData ();
                            ud.setId(d.getRs().getString("id"));
                            ud.setPw(d.getRs().getString("pw"));
                            ud.setName(d.getRs().getString("name"));
                            ud.setPhone(d.getRs().getString("phone"));
                            ud.setAuthority(d.getRs().getString("authority"));
                            
                            uds.add(ud);
                        }
                        
                        System.out.println("showAllUserByFamily: success.");
                    }
                    else
                    {
                        System.out.println("showAllUserByFamily: user = 0");
                    }
                    
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("showAllUserByFamily: error.");
                }
            }
            else
            {
                System.out.println("showAllUserByFamily: fail.");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("showAllUserByFamily: error.");
        }
        
        
        return uds;
    }
    
    public userData showOneUserByFamily (String familyId, String familyPw, String userName)
    {
        userData ud = new userData ();
        
        d.select("SELECT ID FROM FAMILY WHERE FAMILY.ID=\"" + familyId + "\" AND FAMILY.PW=\"" + familyPw + "\";");
            
        try
        {
            d.getRs().beforeFirst();
            
            if (d.getRs().next())
            {
                d.select("SELECT ID, PW, NAME, PHONE, AUTHORITY FROM FAMILY_USER JOIN USER ON FAMILY_USER.USERID=USER.ID WHERE FAMILY_USER.FAMILYID=\"" + familyId + "\" AND USER.NAME=\"" + userName + "\";");
                
                try
                {
                    d.getRs().beforeFirst();
                    
                    if (d.getRs().next())
                    {
                        ud.setId(d.getRs().getString("id"));
                        ud.setPw(d.getRs().getString("pw"));
                        ud.setName(d.getRs().getString("name"));
                        ud.setPhone(d.getRs().getString("phone"));
                        ud.setAuthority(d.getRs().getString("authority"));
                
                        System.out.println("showOneUserByFamily: success.");
                    }
                    else
                    {
                        System.out.println("showOneUserByFamily: userName fail.");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("showAllUserByFamily: error.");
                }
            }
            else
            {
                System.out.println("showOneUserByFamily: familyId or Pw fail.");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("showOneUserByFamily: error.");
        }
        
        return ud;
    }
    
    public int addOneUserByFamily (String familyId, String familyPw, String userId, String userPw, String userName, String userPhone, String userAuthority)
    {
        /*
        db d = new db ();
        d.connect("jdbc:mysql://localhost:3306/db?autoReconnect=true&useSSL=false", "root", "3306");
        */
        d.select("SELECT ID FROM FAMILY WHERE FAMILY.ID=\"" + familyId + "\" AND FAMILY.PW=\"" + familyPw + "\";");
            
        try
        {
            d.getRs().beforeFirst();
            
            if (d.getRs().next())
            {
                d.select("SELECT ID FROM USER WHERE USER.ID=\"" + userId + "\";");
        
                try
                {
                    d.getRs().beforeFirst();
            
                    if (d.getRs().next())
                    {
                        System.out.println("addOneUserByFamily: userId fail.");
                        return 2;
                    }
                    else
                    {
                        d.select("select id from family_user join user on family_user.userid=user.id where familyid= \"" + familyId + "\" and name=\"" + userName + "\";");
                        
                        try
                        {
                            d.getRs().beforeFirst();
                            
                            if (d.getRs().next())
                            {
                                System.out.println("addOneUserByFamily: userName fail.");
                                return 3;
                            }
                            else
                            {
                                d.update("INSERT INTO USER (ID, PW, NAME, PHONE) VALUE (\"" + userId + "\", \"" + userPw + "\", \"" + userName + "\", \"" + userPhone + "\");");
                                d.update("INSERT INTO FAMILY_USER (FAMILYID, USERID, AUTHORITY) VALUE (\"" + familyId + "\", \"" + userId + "\", \"" + userAuthority + "\");");
                                System.out.println("addOneUserByFamily: success.");
                                return 0;
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            System.out.println("addOneUserByFamily: error.");
                            return 4;
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("addOneUserByFamily: error.");
                    return 4;
                }
            }
            else
            {
                System.out.println("addOneUserByFamily: familyId or Pw fail.");
                return 1;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("addOneUserByFamily: error.");
            return 4;
        }
    }
    
    public int reviseOneUserByFamily (String familyId, String familyPw, String oldUserName, String newUserId, String newUserPw, String newUserName, String newUserPhone, String newUserAuthority)
    {
        String oldUserId = null;
        
        d.select("SELECT ID FROM FAMILY WHERE FAMILY.ID=\"" + familyId + "\" AND FAMILY.PW=\"" + familyPw + "\";");
        
        try
        {
            d.getRs().beforeFirst();
            
            if (d.getRs().next())
            {
                d.select("SELECT ID FROM FAMILY_USER JOIN USER ON FAMILY_USER.USERID=USER.ID WHERE FAMILY_USER.FAMILYID=\"" + familyId + "\" AND USER.NAME=\"" + oldUserName + "\";");
                
                try
                {
                    d.getRs().beforeFirst();
            
                    if (d.getRs().next())
                    {
                        oldUserId = d.getRs().getString("id");
                        
                        d.update("set foreign_key_checks=0;");
                        d.update("update user set id=\"" + newUserId +"\", pw=\""+ newUserPw + "\", name=\""+ newUserName+ "\", phone=\"" + newUserPhone + "\" where id=\""+ oldUserId +"\" and name=\""+ oldUserName + "\";");
                        d.update("update family_user set userId=\"" + newUserId + "\", authority=\"" + newUserAuthority + "\" where userId=\"" + oldUserId + "\";");
                        d.update("set foreign_key_checks=1;");
                        
                        System.out.println("reviseOneUserByFamily: success.");
                        
                        return 0;
                    }
                    else
                    {
                        System.out.println("reviseOneUserByFamily: userName fail.");
                        return 2;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("reviseOneUserByFamily: error.");
                    return 4;
                }
            }
            else
            {
                System.out.println("reviseOneUserByFamily: familyId or Pw fail.");
                return 1;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("reviseOneUserByFamily: error.");
            return 3;
        }
    }
    
    public ArrayList<deviceData> showAllDeviceByFamily (String familyId, String familyPw)
    {
        ArrayList<deviceData> dds = new ArrayList<deviceData> ();
        
        d.select("SELECT ID FROM FAMILY WHERE FAMILY.ID=\"" + familyId + "\" AND FAMILY.PW=\"" + familyPw + "\";");
        
        try
        {
            d.getRs().beforeFirst();
            
            if (d.getRs().next())
            {
                d.select("select id, company, classification, model, name, address from family_device join device on family_device.deviceId=device.id where family_device.familyId=\"" + familyId + "\";");
                
                try
                {
                    d.getRs().beforeFirst();
                    
                    if (d.getRs().next())
                    {
                        d.getRs().beforeFirst();
                        
                        while (d.getRs().next())
                        {
                            deviceData dd = new deviceData ();
                            
                            dd.setId(d.getRs().getString("id"));
                            dd.setCompany(d.getRs().getString("company"));
                            dd.setClassification(d.getRs().getString("classification"));
                            dd.setModel(d.getRs().getString("model"));
                            dd.setName(d.getRs().getString("name"));
                            dd.setAddress(d.getRs().getString("address"));
                            
                            dds.add(dd);
                        }
                        
                        System.out.println("showAllDeviceByFamily: success.");
                    }
                    else
                    {
                        System.out.println("showAllDeviceByFamily: device = 0");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("showAllDeviceByFamily: error.");
                }
            }
            else
            {
                System.out.println("showAllDeviceByFamily: familyId or Pw fail.");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("showAllDeviceByFamily: error.");
        }
        
        
        return dds;
    }
    
    public deviceData showOneDeviceByFamily (String familyId, String familyPw, String deviceName)
    {
        deviceData dd = new deviceData ();
        
        d.select("SELECT ID FROM FAMILY WHERE FAMILY.ID=\"" + familyId + "\" AND FAMILY.PW=\"" + familyPw + "\";");
            
        try
        {
            d.getRs().beforeFirst();
            
            if (d.getRs().next())
            {
                d.select("select id, company, classification, model, name, address from family_device join device on family_device.deviceId=device.id where family_device.familyId=\"" + familyId + "\" and family_device.name=\"" + deviceName + "\";");
                
                try
                {
                    d.getRs().beforeFirst();
                    
                    if (d.getRs().next())
                    {
                        dd.setId(d.getRs().getString("id"));
                        dd.setCompany(d.getRs().getString("company"));
                        dd.setClassification(d.getRs().getString("classification"));
                        dd.setModel(d.getRs().getString("model"));
                        dd.setName(d.getRs().getString("name"));
                        dd.setAddress(d.getRs().getString("address"));
                
                        System.out.println("showOneDeviceByFamily: success.");
                    }
                    else
                    {
                        System.out.println("showOneDeviceByFamily: deviceName fail.");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("showOneDeviceByFamily: error.");
                }
            }
            else
            {
                System.out.println("showOneDeviceByFamily: familyId or Pw fail.");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("showOneDeviceByFamily: error.");
        }
        
        return dd;
    }
    
    public int addOneDeviceByFamily (String familyId, String familyPw, String deviceCompany, String deviceClassification, String deviceModel, String deviceName, String deviceAddress)
    {
        String deviceId;
        
        d.select("SELECT ID FROM FAMILY WHERE FAMILY.ID=\"" + familyId + "\" AND FAMILY.PW=\"" + familyPw + "\";");
        
        try
        {
            d.getRs().beforeFirst();
            
            if (d.getRs().next())
            {
                d.select("select id from device where company=\"" + deviceCompany + "\" and classification=\"" + deviceClassification + "\" and model=\"" + deviceModel + "\";");
                
                try
                {
                    d.getRs().beforeFirst();
                    
                    if (d.getRs().next())
                    {
                        deviceId = d.getRs().getString("id");
                        
                        d.select("select deviceid from family_device where family_device.familyId=\"" + familyId + "\" and family_device.name=\"" + deviceName + "\";");
                
                        try
                        {
                            d.getRs().beforeFirst();
                    
                            if (d.getRs().next())
                            {
                                System.out.println("addOneDeviceByFamily: deviceName fail.");
                                return 3;
                            }
                            else
                            {
                                if (deviceClassification.equals("doorlock"))
                                {
                                    d.select("select deviceid from family_device join device on family_device.deviceid=device.id where family_device.familyid=\"" + familyId + "\" and device.classification=\"doorlock\";");
                                    
                                    try
                                    {
                                        d.getRs().beforeFirst();
                                
                                        if (d.getRs().next())
                                        {
                                            System.out.println("addOneDeviceByFamily: deviceId fail.");
                                            return -1;
                                        }
                                        else
                                        {
                                            //d.update("insert into device (id, company, classification, model) value (\"" + deviceId + "\", \"" + deviceCompany + "\", \"" + deviceClassification + "\", \"" + deviceModel + "\");");
                                            d.update("insert into family_device (familyid, deviceid, name, address) value (\"" + familyId + "\", \"" + deviceId + "\", \"" + deviceName + "\", \"" + deviceAddress + "\");");
                                            System.out.println("addOneDeviceByFamily: success.");
                                            return 0;
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                        System.out.println("addOneDeviceByFamily: error.");
                                        return 4;
                                    }
                                }
                                else
                                {
                                    //d.update("insert into device (id, company, classification, model) value (\"" + deviceId + "\", \"" + deviceCompany + "\", \"" + deviceClassification + "\", \"" + deviceModel + "\");");
                                    d.update("insert into family_device (familyid, deviceid, name, address) value (\"" + familyId + "\", \"" + deviceId + "\", \"" + deviceName + "\", \"" + deviceAddress + "\");");
                                    System.out.println("addOneDeviceByFamily: success.");
                                    return 0;
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            System.out.println("addOneDeviceByFamily: error.");
                            return 4;
                        }
                    }
                    else
                    {
                        System.out.println("addOneDeviceByFamily: class fail.");
                        return 2;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("addOneDeviceByFamily: error.");
                    return 4;
                }
            }
            else
            {
                System.out.println("addOneDeviceByFamily: familyId or Pw fail.");
                return 1;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("addOneDeviceByFamily: error.");
            return 4;
        }
    }
    
    public int reviseOneDeviceByFamily (String familyId, String familyPw, String oldDeviceName, String newDeviceCompany, String newDeviceClassification, String newDeviceModel, String newDeviceName, String newDeviceAddress)
    {
        String newDeviceId;
        String oldClassification;
        
        d.select("SELECT ID FROM FAMILY WHERE FAMILY.ID=\"" + familyId + "\" AND FAMILY.PW=\"" + familyPw + "\";");
        
        try
        {
            d.getRs().beforeFirst();
            
            if (d.getRs().next())
            {
                d.select("select id from device where company=\"" + newDeviceCompany + "\" and classification=\"" + newDeviceClassification + "\" and model=\"" + newDeviceModel + "\";");
                
                try
                {
                    d.getRs().beforeFirst();
                    
                    if (d.getRs().next())
                    {
                        newDeviceId = d.getRs().getString("id");
                        
                        d.select("select classification from family_device join device on family_device.deviceid=device.id where family_device.familyId=\"" + familyId + "\" and family_device.name=\"" + oldDeviceName + "\";");
                
                        try
                        {
                            d.getRs().beforeFirst();
                    
                            if (d.getRs().next())
                            {
                                oldClassification = d.getRs().getString("classification");
                                
                                
                                d.select("select deviceid from family_device join device on family_device.deviceid=device.id where family_device.familyId=\"" + familyId + "\" and family_device.name=\"" + newDeviceName + "\";");
                                
                                try
                                {
                                    d.getRs().beforeFirst();
                                    
                                    
                                    if (d.getRs().next())
                                    {
                                        if (oldDeviceName.equals(newDeviceName))
                                        {
                                            if (newDeviceClassification.equals("doorlock"))
                                            {
                                                if (oldClassification.equals("doorlock"))
                                                {
                                                    d.update("update family_device set deviceid=\"" + newDeviceId + "\", name=\"" + newDeviceName + "\", address=\"" + newDeviceAddress + "\" where name=\"" + oldDeviceName + "\";");
                                                    System.out.println("reviseOneDeviceByFamily: success.");
                                                    return 0;
                                                }
                                                else
                                                {
                                                    d.select("select deviceid from family_device join device on family_device.deviceid=device.id where family_device.familyid=\"" + familyId + "\" and device.classification=\"doorlock\";");
                                        
                                                    try
                                                    {
                                                        d.getRs().beforeFirst();
                    
                                                        if (d.getRs().next())
                                                        {
                                                            System.out.println("reviseOneDeviceByFamily: newDeviceId fail.");
                                                            return -1;
                                                        }
                                                        else
                                                        {
                                                            d.update("update family_device set deviceid=\"" + newDeviceId + "\", name=\"" + newDeviceName + "\", address=\"" + newDeviceAddress + "\" where name=\"" + oldDeviceName + "\";");
                                                            System.out.println("reviseOneDeviceByFamily: success.");
                                                            return 0; 
                                                        }
                                                    }
                                                    catch (Exception e)
                                                    {
                                                        e.printStackTrace();
                                                        System.out.println("reviseOneDeviceByFamily: error.");
                                                        return 4;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                d.update("update family_device set deviceid=\"" + newDeviceId + "\", name=\"" + newDeviceName + "\", address=\"" + newDeviceAddress + "\" where name=\"" + oldDeviceName + "\";");
                                                System.out.println("reviseOneDeviceByFamily: success.");
                                                return 0;
                                            }
                                        }
                                        else
                                        {
                                            System.out.println("reviseOneDeviceByFamily: oldDeviceName fail.");
                                            return -2;
                                        }
                                    }
                                    else
                                    {
                                        if (newDeviceClassification.equals("doorlock"))
                                        {
                                            if (oldClassification.equals("doorlock"))
                                            {
                                                d.update("update family_device set deviceid=\"" + newDeviceId + "\", name=\"" + newDeviceName + "\", address=\"" + newDeviceAddress + "\" where name=\"" + oldDeviceName + "\";");
                                                System.out.println("reviseOneDeviceByFamily: success.");
                                                return 0;
                                            }
                                            else
                                            {
                                                d.select("select deviceid from family_device join device on family_device.deviceid=device.id where family_device.familyid=\"" + familyId + "\" and device.classification=\"doorlock\";");
                                        
                                                try
                                                {
                                                    d.getRs().beforeFirst();
                    
                                                    if (d.getRs().next())
                                                    {
                                                        System.out.println("reviseOneDeviceByFamily: newDeviceId fail.");
                                                        return -1;
                                                    }
                                                    else
                                                    {
                                                        d.update("update family_device set deviceid=\"" + newDeviceId + "\", name=\"" + newDeviceName + "\", address=\"" + newDeviceAddress + "\" where name=\"" + oldDeviceName + "\";");
                                                        System.out.println("reviseOneDeviceByFamily: success.");
                                                        return 0; 
                                                    }
                                                }
                                                catch (Exception e)
                                                {
                                                    e.printStackTrace();
                                                    System.out.println("reviseOneDeviceByFamily: error.");
                                                    return 4;
                                                }
                                            }
                                        }
                                        else
                                        {
                                            d.update("update family_device set deviceid=\"" + newDeviceId + "\", name=\"" + newDeviceName + "\", address=\"" + newDeviceAddress + "\" where name=\"" + oldDeviceName + "\";");
                                            System.out.println("reviseOneDeviceByFamily: success.");
                                            return 0;
                                        }
                                    }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                    System.out.println("reviseOneDeviceByFamily: error.");
                                    return 4;
                                }
                            }
                            else
                            {
                                System.out.println("reviseOneDeviceByFamily: newDeviceName fail.");
                                return 3;
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            System.out.println("reviseOneDeviceByFamily: error.");
                            return 4;
                        }
                    }
                    else
                    {
                        System.out.println("reviseOneDeviceByFamily: class fail.");
                        return 2;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("reviseOneDeviceByFamily: error.");
                    return 4;
                }
            }
            else
            {
                System.out.println("reviseOneDeviceByFamily: familyId or Pw fail.");
                return 1;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("reviseOneDeviceByFamily: error.");
            return 4;
        }
    }
    
    public ArrayList<deviceData> showAllDeviceByUser (String userId, String userPw)
    {
        String familyId = null;
        ArrayList<deviceData> dds = new ArrayList<deviceData> ();
        
        d.select("select id from user where user.id=\"" + userId + "\" and user.pw=\"" + userPw + "\";");
        
        try
        {
            d.getRs().beforeFirst();
            
            if (d.getRs().next())
            {
                d.select("select familyid from family_user join user on family_user.userid=user.id where user.id=\"" + userId + "\";");
                
                try
                {
                    d.getRs().beforeFirst();
                    
                    if (d.getRs().next())
                    {
                        familyId = d.getRs().getString("familyId");
                       
                        d.select("select id, company, classification, model, name, address from family_device join device on family_device.deviceId=device.id where family_device.familyId=\"" + familyId + "\";");
                        
                        try
                        {
                            d.getRs().beforeFirst();
                    
                            if (d.getRs().next())
                            {
                                d.getRs().beforeFirst();
                        
                                while (d.getRs().next())
                                {
                                    deviceData dd = new deviceData ();
                            
                                    dd.setId(d.getRs().getString("id"));
                                    dd.setCompany(d.getRs().getString("company"));
                                    dd.setClassification(d.getRs().getString("classification"));
                                    dd.setModel(d.getRs().getString("model"));
                                    dd.setName(d.getRs().getString("name"));
                                    dd.setAddress(d.getRs().getString("address"));
                            
                                    dds.add(dd);
                                }
                                
                                System.out.println("showAllDeviceByUser: success.");
                            }
                            else
                            {
                                System.out.println("showAllDeviceByUser: device = 0");
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            System.out.println("showAllDeviceByUser: error.");
                        }
                    }
                    else
                    {
                        System.out.println("showAllDeviceByUser: familyId fail.");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("showAllDeviceByUser: error.");
                }
            }
            else
            {
                System.out.println("showAllDeviceByUser: userId fail.");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("showAllDeviceByUser: error.");
        }
        
        return dds;
    }
    
    public deviceData showOneDeviceByUser (String userId, String userPw, String deviceName)
    {
        String familyId = null;
        deviceData dd = new deviceData ();
        
        d.select("select id from user where user.id=\"" + userId + "\" and user.pw=\"" + userPw + "\";");
        
        try
        {
            d.getRs().beforeFirst();
            
            if (d.getRs().next())
            {
                d.select("select familyid from family_user join user on family_user.userid=user.id where user.id=\"" + userId + "\";");
                
                try
                {
                    d.getRs().beforeFirst();
                    
                    if (d.getRs().next())
                    {
                        familyId = d.getRs().getString("familyId");
                       
                        d.select("select id, company, classification, model, name, address from family_device join device on family_device.deviceId=device.id where family_device.familyId=\"" + familyId + "\" and family_device.name=\"" + deviceName + "\";");
                        
                        try
                        {
                            d.getRs().beforeFirst();
                    
                            if (d.getRs().next())
                            {
                                dd.setId(d.getRs().getString("id"));
                                dd.setCompany(d.getRs().getString("company"));
                                dd.setClassification(d.getRs().getString("classification"));
                                dd.setModel(d.getRs().getString("model"));
                                dd.setName(d.getRs().getString("name"));
                                dd.setAddress(d.getRs().getString("address"));
                
                                System.out.println("showOneDeviceByUser: success.");
                            }
                            else
                            {
                                System.out.println("showOneDeviceByUser: deviceName fail.");
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            System.out.println("showOneDeviceByUser: error.");
                        }
                    }
                    else
                    {
                        System.out.println("showOneDeviceByUser: familyId fail.");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("showOneDeviceByUser: error.");
                }
            }
            else
            {
                System.out.println("showOneDeviceByUser: userId fail.");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("showOneDeviceByUser: error.");
        }
        
        return dd;
    }
    
    public ArrayList<doorlocklog> showAllDoorlocklogByFamily (String familyId, String familyPw)
    {
        ArrayList<doorlocklog> dlls = new ArrayList<doorlocklog> ();
        
        d.select("SELECT ID FROM FAMILY WHERE FAMILY.ID=\"" + familyId + "\" AND FAMILY.PW=\"" + familyPw + "\";");
        
        try
        {
            d.getRs().beforeFirst();
            
            if (d.getRs().next())
            {
                d.select("select photo, id, date, time from doorlocklog where doorlocklog.id in (select id from family_user join user on family_user.userid=user.id where family_user.familyid=\"" + familyId + "\");");
                
                try
                {
                    d.getRs().beforeFirst();
                    
                    if (d.getRs().next())
                    {
                        d.getRs().beforeFirst();
                        
                        while (d.getRs().next())
                        {
                            doorlocklog dll = new doorlocklog ();
                            
                            dll.setPhoto(d.getRs().getBytes("photo"));
                            dll.setId(d.getRs().getString("id"));
                            dll.setDate(d.getRs().getString("date"));
                            dll.setTime(d.getRs().getString("time"));
                            
                            dlls.add(dll);
                        }
                        
                        System.out.println("showAllDoorlocklogByFamily: success.");
                    }
                    else
                    {
                        System.out.println("showAllDoorlocklogByFamily: doorlocklog = 0");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("showAllDoorlocklogByFamily: error.");
                }
            }
            else
            {
                System.out.println("showAllDoorlocklogByFamily: familyId or Pw fail.");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("showAllDoorlocklogByFamily: error.");
        }
        
        
        return dlls;
    }
    
    public doorlocklog showOneDoorlocklogByFamily (String familyId, String familyPw, String doorlockDate, String doorlockTime)
    {
        doorlocklog dll = new doorlocklog ();
        
        d.select("SELECT ID FROM FAMILY WHERE FAMILY.ID=\"" + familyId + "\" AND FAMILY.PW=\"" + familyPw + "\";");
        
        try
        {
            d.getRs().beforeFirst();
            
            if (d.getRs().next())
            {
                d.select("select photo, id, date, time from doorlocklog where doorlocklog.id in (select id from family_user join user on family_user.userid=user.id where family_user.familyid=\"" + familyId + "\") and doorlocklog.date=\"" + doorlockDate + "\" and doorlocklog.time=\"" + doorlockTime + "\";");
                
                try
                {
                    d.getRs().beforeFirst();
                    
                    if (d.getRs().next())
                    {
                        dll.setPhoto(d.getRs().getBytes("photo"));
                        dll.setId(d.getRs().getString("id"));
                        dll.setDate(d.getRs().getString("date"));
                        dll.setTime(d.getRs().getString("time"));
                
                        System.out.println("showOneDoorlocklogByFamily: success.");
                    }
                    else
                    {
                        System.out.println("showOneDoorlocklogByFamily: doorlockDate or time fail.");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("showOneDoorlocklogByFamily: error.");
                }
            }
            else
            {
                System.out.println("showOneDoorlocklogByFamily: familyId or Pw fail.");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("showOneDoorlocklogByFamily: error.");
        }
        
        return dll;
    }
    
    public ArrayList<userData> doorlockOpenByUser (String userId, String userPw)
    {
        String familyId = null;
        String authority = null;
        Calendar now = Calendar.getInstance();
        String date = null;
        String time = null;
        String address = null;
        String userName = null;
        
        ArrayList<userData> uds = new ArrayList<userData> ();
        
        d.select("select id from user where user.id=\"" + userId + "\" and user.pw=\"" + userPw + "\";");
        
        try
        {
            d.getRs().beforeFirst();
            
            if (d.getRs().next())
            {
                d.select("select familyid, authority, name from family_user join user on family_user.userid=user.id where user.id=\"" + userId + "\";");
                
                try
                {
                    d.getRs().beforeFirst();
                    
                    if (d.getRs().next())
                    {
                        familyId = d.getRs().getString("familyId");
                        authority = d.getRs().getString("authority");
                        userName = d.getRs().getString("name");
                        
                        d.select("select address from family_device join device on family_device.deviceId=device.id where family_device.familyid=\"" + familyId + "\" and device.classification=\"doorlock\";");
                        
                        try
                        {
                            d.getRs().beforeFirst();
                            
                            if (d.getRs().next())
                            {
                                address = d.getRs().getString("address");
                                
                                Socket s = new Socket (address, doorlockOpenClientPort);
                                //Socket s = new Socket ("192.168.137.104", 15150);
                                System.out.println("doorlockOpenByUser: connect success.");
                                        
                                OutputStream os = s.getOutputStream();
                                os.write("open".getBytes());
                                System.out.println("doorlockOpenByUser: send success.");
                          
                                s.close();
                                os.close();
                                
                                //
                                //ServerSocket ss = new ServerSocket (doorlockOpenServerPort);
                                ServerSocket ss = new ServerSocket (15151);
                                System.out.println("doorlockOpenByUser: server start.");
                        
                                s = ss.accept();
                                //s.setReceiveBufferSize(1024 * 8);
                                System.out.println("doorlockOpenByUser: connect success.");
                                
                                InputStream is = s.getInputStream();
                                /*
                                synchronized(this)
                                {
                                    wait(10000);
                                }
                                */
                                while (is.available() < 1024)
                                {
                                    ;
                                }
                                
                                byte[] doorlockPhoto = new byte [is.available()];
                                is.read(doorlockPhoto);
                                System.out.println("doorlockOpenByUser: receive success.");
                        
                                ss.close();
                                s.close();
                                is.close();
                                
                                //
                                
                                PreparedStatement pstmt = d.getConn().prepareStatement("INSERT INTO doorlocklog (photo, id, date, time) VALUE (?, ?, ?, ?);");
                        
                                date = now.get(now.YEAR) + "-" + (now.get(now.MONTH) + 1) + "-" + now.get(now.DATE);
                                time = now.get(now.HOUR_OF_DAY) + ":" + now.get(now.MINUTE) + ":" + now.get(now.SECOND);
                                
                                pstmt.setBytes(1, doorlockPhoto);
                                pstmt.setString(2, userId);
                                pstmt.setString(3, date);
                                pstmt.setString(4, time);
            
                                pstmt.execute();
                        
                                //need sending e-mail function
                                
                                if (authority.equals("send"))
                                {
                                    d.select("SELECT ID, PW, NAME, PHONE, AUTHORITY FROM FAMILY_USER JOIN USER ON FAMILY_USER.USERID=USER.ID WHERE FAMILY_USER.FAMILYID=\"" + familyId + "\";");
                
                                    try
                                    {
                                        d.getRs().beforeFirst();
                    
                                        if (d.getRs().next())
                                        {
                                            d.getRs().beforeFirst();
                        
                                            while (d.getRs().next())
                                            {
                                                userData ud = new userData ();
                                                ud.setId(d.getRs().getString("id"));
                                                ud.setPw(d.getRs().getString("pw"));
                                                ud.setName(d.getRs().getString("name"));
                                                ud.setPhone(d.getRs().getString("phone"));
                                                ud.setAuthority(d.getRs().getString("authority"));
                                            
                                                if (ud.getAuthority().equals("receive"))
                                                {
                                                    URL url = new URL (FCMApiUrl);
                                                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                                                    conn.setUseCaches(false);
                                                    conn.setDoInput(true);
                                                    conn.setDoOutput(true);

                                                    conn.setRequestMethod("POST");
                                                    conn.setRequestProperty("Authorization","key=" + FCMServerKey);
                                                    conn.setRequestProperty("Content-Type","application/json");

                                                    JSONObject json = new JSONObject();
                                                    json.put("to",ud.getPhone().trim());
                                                    JSONObject info = new JSONObject();
                                                    
                                                    info.put("title", "Inome 스마트 도어락 오픈 알림");
                                                    info.put("body", userName + "님이 " + date + " " + time + "에 스마트 도어락을 오픈하셨습니다.");
                                                    json.put("notification", info);

                                                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                                                    wr.write(json.toString());
                                                    wr.flush();
                                                    conn.getInputStream();
                                                    
                                                    uds.add(ud);
                                                }
                                            }
                                        }
                                        else
                                        {
                                            System.out.println("doorlockOpenByUser: user = 0");
                                        }
                    
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                        System.out.println("doorlockOpenByUser: error.");
                                    }            
                                }
                                
                                System.out.println("doorlockOpenByUser: success.");
                                                
                                //return 0;
                            }
                            else
                            {
                                System.out.println("doorlockOpenByUser: doorlock fail.");
                                //return -1;
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            System.out.println("doorlockOpenByUser: error.");
                    
                            //return 3;
                        }
                        
                    }
                    else
                    {
                        System.out.println("doorlockOpenByUser: familyId fail.");
                        
                        //return 1;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("doorlockOpenByUser: error.");
                    
                    //return 3;
                }
            }
            else
            {
                System.out.println("doorlockOpenByUser: userId fail.");
                
                //return 2;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("doorlockOpenByUser: error.");
            
            //return 3;
        }
        
        return uds;
    }
    
    public int remoteControlByUser (String userId, String userPw, String deviceName, String userInstruction)
    {
        String familyId = null;
        String deviceId = null;
        String address = null;
        String IRInstruction = null;
        
        
        d.select("select id from user where user.id=\"" + userId + "\" and user.pw=\"" + userPw + "\";");
        
        try
        {
            d.getRs().beforeFirst();
            
            if (d.getRs().next())
            {
                d.select("select familyid from family_user join user on family_user.userid=user.id where user.id=\"" + userId + "\";");
                
                try
                {
                    d.getRs().beforeFirst();
                    
                    if (d.getRs().next())
                    {
                        familyId = d.getRs().getString("familyId");
                        
                        d.select("select deviceId, address from family_device where family_device.familyid=\"" + familyId + "\" and family_device.name=\"" + deviceName + "\";");
                        
                        try
                        {
                            d.getRs().beforeFirst();
                            
                            if (d.getRs().next())
                            {
                                deviceId = d.getRs().getString("deviceId");
                                address = d.getRs().getString("address");
                                
                                d.select("select IRInstruction from device_instruction join instruction on device_instruction.instructionId=instruction.id where device_instruction.deviceId=\"" + deviceId + "\" and instruction.userInstruction=\"" + userInstruction + "\";");
                                
                                try
                                {
                                    d.getRs().beforeFirst();
                            
                                    if (d.getRs().next())
                                    {
                                        IRInstruction = d.getRs().getString("IRInstruction");
                                        
                                        Socket s = new Socket (address, remoteControlClientPort);
                                        //Socket s = new Socket ("192.168.137.203", 15152);
                                        System.out.println("remoteControlByUser: connect success.");
                                        
                                        OutputStream os = s.getOutputStream();
                                        os.write(IRInstruction.getBytes());
                                        System.out.println("remoteControlByUser: send success.");
                                        
                                        System.out.println("remoteControlByUser: success.");
                                        
                                        s.close();
                                        os.close();
                                        
                                        return 0;
                                    }
                                    else
                                    {
                                        d.select("select irinstruction from custominstruction where familyid=\"" + familyId + "\" and deviceId=\"" + deviceId + "\" and userinstruction=\"" + userInstruction + "\";");
                                        
                                        try
                                        {
                                            d.getRs().beforeFirst();
                                            
                                            if (d.getRs().next())
                                            {
                                                IRInstruction = d.getRs().getString("IRInstruction");
                                        
                                                Socket s = new Socket (address, remoteControlClientPort);
                                                //Socket s = new Socket ("192.168.137.203", 15152);
                                                System.out.println("remoteControlByUser: connect success.");
                                        
                                                OutputStream os = s.getOutputStream();
                                                os.write(IRInstruction.getBytes());
                                                System.out.println("remoteControlByUser: send success.");
                                        
                                                System.out.println("remoteControlByUser: success.");
                                        
                                                s.close();
                                                os.close();
                                        
                                                return 0;
                                            }
                                            else
                                            {
                                                System.out.println("remoteControlByUser: userInstruction fail.");
                                                return 4;
                                            }
                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                            System.out.println("remoteControlByUser: error.");
                                            return 5;
                                        }
                                    }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                    System.out.println("remoteControlByUser: error.");
                                    return 5;
                                }
                                
                            }
                            else
                            {
                                System.out.println("remoteControlByUser: deviceName fail.");
                                return 3;
                            }
                            
                            
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            System.out.println("remoteControlByUser: error.");
                            return 5;
                        }
                        
                        
                    }
                    else
                    {
                        System.out.println("remoteControlByUser: familyId fail.");
                        return 1;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("remoteControlByUser: error.");
                    return 5;
                }
            }
            else
            {
                System.out.println("remoteControlByUser: userId or pw fail.");
                return 2;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("remoteControlByUser: error.");
            return 5;
        }
    }
    
    public int addIRInstructionByFamily (String familyId, String familyPw, String deviceName, String userInstruction)
    {
        String deviceId = null;
        String address = null;
        String classification = null;
        
        
        d.select("select id from family where family.id=\"" + familyId + "\" and family.pw=\"" + familyPw + "\";");
        
        try
        {
            d.getRs().beforeFirst();
            
            if (d.getRs().next())
            {
                d.select("select deviceId, classification, address from family_device join device on family_device.deviceid=device.id where family_device.familyid=\"" + familyId + "\" and family_device.name=\"" + deviceName + "\";");
                        
                try
                {
                    d.getRs().beforeFirst();
                            
                    if (d.getRs().next())
                    {
                        deviceId = d.getRs().getString("deviceId");
                        address = d.getRs().getString("address");
                        classification = d.getRs().getString("classification");
                        
                        if (classification.equals("doorlock"))
                        {
                            System.out.println("addIRInstructionByFamily: deviceClassification fail.");
                            return 2;
                        }
                        else
                        {
                            d.select("select IRInstruction from device_instruction join instruction on device_instruction.instructionId=instruction.id where device_instruction.deviceId=\"" + deviceId + "\" and instruction.userInstruction=\"" + userInstruction + "\";");
                                
                            try
                            {
                                d.getRs().beforeFirst();
                            
                                if (d.getRs().next())
                                {  
                                    System.out.println("addIRInstructionByFamily: userInstruction fail.");
                                    return -1;
                                }
                                else
                                {
                                    d.select("select IRInstruction from custominstruction where custominstruction.familyid=\"" + familyId + "\"and custominstruction.deviceId=\"" + deviceId + "\" and custominstruction.userInstruction=\"" + userInstruction + "\";");
                                    
                                    try
                                    {
                                        d.getRs().beforeFirst();
                            
                                        if (d.getRs().next())
                                        {  
                                            System.out.println("addIRInstructionByFamily: userInstruction fail.");
                                            return -2;
                                        }
                                        else
                                        {
                                            //thread
                                            Socket s = new Socket (address, addIRInstructionClientPort);
                                            //Socket s = new Socket ("192.168.137.203", 15153);
                                            System.out.println("addIRInstructionByFamily: connect success.");
                                        
                                            OutputStream os = s.getOutputStream();
                                            os.write("add".getBytes());
                                            System.out.println("addIRInstructionByFamily: send success.");
                                        
                                            s.close();
                                            os.close();
                                
                                            //
                                            //ServerSocket ss = new ServerSocket (addIRInstructionServerPort);
                                            ServerSocket ss = new ServerSocket (15154);
                                            System.out.println("addIRInstructionByFamily: server start.");
                        
                                            s = ss.accept();
                                            System.out.println("addIRInstructionByFamily: connect success.");
                                
                                            InputStream is = s.getInputStream();
                                            /*
                                            synchronized(this)
                                            {
                                                wait(10000);
                                            }
                                            */
                                            while (is.available() < 7)
                                            {
                                                ;
                                            }
                                
                                            byte[] IRInstruction = new byte [is.available()];
                                            System.out.println(is.available());
                                            is.read(IRInstruction);
                                            System.out.println("addIRInstructionByFamily: IRInstruction = " + new String (IRInstruction));
                                            System.out.println("addIRInstructionByFamily: receive success.");
                        
                                            ss.close();
                                            s.close();
                                            is.close();
                                            
                                            d.select("select userinstruction from device_instruction join instruction on device_instruction.instructionid=instruction.id where device_instruction.deviceid=\"" + deviceId + "\" and instruction.irinstruction=\"" + new String (IRInstruction) + "\";");
                                    
                                            try
                                            {
                                                d.getRs().beforeFirst();
                            
                                                if (d.getRs().next())
                                                {
                                                    System.out.println("addIRInstructionByFamily: IRInstruction fail.");
                                                    return -3;
                                                }
                                                else
                                                {
                                                    d.select("select userinstruction from custominstruction where custominstruction.familyid=\"" + familyId + "\" and custominstruction.deviceid=\"" + deviceId + "\" and custominstruction.irinstruction=\"" + new String (IRInstruction) + "\";");
                                            
                                                    try
                                                    {
                                                        d.getRs().beforeFirst();
                            
                                                        if (d.getRs().next())
                                                        {
                                                            System.out.println("addIRInstructionByFamily: IRInstruction fail.");
                                                            return -4;
                                                        }
                                                        else
                                                        {
                                                            d.update("insert into custominstruction (familyid, deviceid, userinstruction, irinstruction) value (\"" + familyId + "\", \"" + deviceId + "\", \"" + userInstruction + "\", \"" + new String (IRInstruction) + "\");");
                                                            System.out.println("addIRInstructionByFamily: success."+new String (IRInstruction));
                                                            return 0;
                                                        }
                                                    }
                                                    catch (Exception e)
                                                    {
                                                        e.printStackTrace();
                                                        System.out.println("remoteControlByUser: error.");
                                                        return 5;
                                                    }
                                                }
                                            }
                                            catch (Exception e)
                                            {
                                                e.printStackTrace();
                                                System.out.println("remoteControlByUser: error.");
                                                return 5;
                                            }
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                        System.out.println("remoteControlByUser: error.");
                                        return 5;
                                    }
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                                System.out.println("remoteControlByUser: error.");
                                return 5;
                            }
                        }    
                    }
                    else
                    {
                        System.out.println("remoteControlByUser: deviceName fail.");
                        return 3;
                    }     
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("remoteControlByUser: error.");
                    return 5;
                }     
            }
            else
            {
                System.out.println("remoteControlByUser: familyId fail.");
                return 1;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("remoteControlByUser: error.");
            return 5;
        }
    }
    
    public ArrayList<String> getAllDeviceCompanyByFamily (String familyId, String familyPw)
    {
        ArrayList<String> ss = new ArrayList<String> ();
        
        d.select("SELECT ID FROM FAMILY WHERE FAMILY.ID=\"" + familyId + "\" AND FAMILY.PW=\"" + familyPw + "\";");
        
        try
        {
            d.getRs().beforeFirst();
            
            if (d.getRs().next())
            {
        
                d.select("select distinct(company) from device;");
        
                try
                {
                    d.getRs().beforeFirst();
                    
                        if (d.getRs().next())
                        {
                            d.getRs().beforeFirst();
                        
                            while (d.getRs().next())
                            {  
                                ss.add(d.getRs().getString("company"));
                            }
                    
                            System.out.println("getAllDeviceCompanyByFamily: success.");
                        }
                        else
                        {
                            System.out.println("getAllDeviceCompanyByFamily: deviceCompany = 0.");
                        }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("getAllDeviceCompanyByFamily: error.");
                }
            }
            else
            {
                System.out.println("getAllDeviceCompanyByFamily: familyId or pw fail.");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("getAllDeviceCompanyByFamily: error.");
        }
         
        return ss;
    }
    
    public ArrayList<String> getAllDeviceClassificationByFamily (String familyId, String familyPw, String deviceCompany)
    {
        ArrayList<String> ss = new ArrayList<String> ();
        
        d.select("SELECT ID FROM FAMILY WHERE FAMILY.ID=\"" + familyId + "\" AND FAMILY.PW=\"" + familyPw + "\";");
        
        try
        {
            d.getRs().beforeFirst();
            
            if (d.getRs().next())
            {
        
                d.select("select distinct(classification) from device where device.company=\"" + deviceCompany + "\";");
        
                try
                {
                    d.getRs().beforeFirst();
                    
                        if (d.getRs().next())
                        {
                            d.getRs().beforeFirst();
                        
                            while (d.getRs().next())
                            {  
                                ss.add(d.getRs().getString("classification"));
                            }
                    
                            System.out.println("getAllDeviceClassificationByFamily: success.");
                        }
                        else
                        {
                            System.out.println("getAllDeviceClassificationByFamily: deviceClassification = 0.");
                        }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("getAllDeviceClassificationByFamily: error.");
                }
            }
            else
            {
                System.out.println("getAllDeviceClassificationByFamily: familyId or pw fail.");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("getAllDeviceClassificationByFamily: error.");
        }
         
        return ss;
    }
    
    public ArrayList<String> getAllDeviceModelByFamily (String familyId, String familyPw, String deviceCompany, String deviceClassification)
    {
        ArrayList<String> ss = new ArrayList<String> ();
        
        d.select("SELECT ID FROM FAMILY WHERE FAMILY.ID=\"" + familyId + "\" AND FAMILY.PW=\"" + familyPw + "\";");
        
        try
        {
            d.getRs().beforeFirst();
            
            if (d.getRs().next())
            {
        
                d.select("select distinct(model) from device where device.company=\"" + deviceCompany + "\" and device.classification=\"" + deviceClassification + "\";");
        
                try
                {
                    d.getRs().beforeFirst();
                    
                        if (d.getRs().next())
                        {
                            d.getRs().beforeFirst();
                        
                            while (d.getRs().next())
                            {  
                                ss.add(d.getRs().getString("model"));
                            }
                    
                            System.out.println("getAllDeviceModelByFamily: success.");
                        }
                        else
                        {
                            System.out.println("getAllDeviceModelByFamily: deviceCompany = 0.");
                        }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("getAllDeviceModelByFamily: error.");
                }
            }
            else
            {
                System.out.println("getAllDeviceModelByFamily: familyId or pw fail.");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("getAllDeviceModelByFamily: error.");
        }
         
        return ss;
    }
    
    public ArrayList<custominstruction> getAllCustomInstructionByUser (String userId, String userPw, String deviceName)
    {
        String familyId = null;
        String deviceId = null;
        ArrayList<custominstruction> cis = new ArrayList<custominstruction> ();
        
        
        d.select("select id from user where user.id=\"" + userId + "\" and user.pw=\"" + userPw + "\";");
        
        try
        {
            d.getRs().beforeFirst();
            
            if (d.getRs().next())
            {
                d.select("select familyid from family_user join user on family_user.userid=user.id where user.id=\"" + userId + "\";");
                
                try
                {
                    d.getRs().beforeFirst();
                    
                    if (d.getRs().next())
                    {
                        familyId = d.getRs().getString("familyId");
                        
                        d.select("select deviceId, address from family_device where family_device.familyid=\"" + familyId + "\" and family_device.name=\"" + deviceName + "\";");
                        
                        try
                        {
                            d.getRs().beforeFirst();
                            
                            if (d.getRs().next())
                            {
                                deviceId = d.getRs().getString("deviceId");
                                
                                d.select("select familyid, deviceid, userinstruction, irinstruction from custominstruction where familyid=\"" + familyId + "\" and deviceid=\"" + deviceId + "\";");
                                
                                if (d.getRs().next())
                                {
                                    d.getRs().beforeFirst();
                        
                                    while (d.getRs().next())
                                    {
                                        custominstruction ci = new custominstruction ();
                                        
                                        ci.setFamilyId(d.getRs().getString("familyId"));
                                        ci.setDeviceId(d.getRs().getString("deviceId"));
                                        ci.setUserInstruction(d.getRs().getString("userInstruction"));
                                        ci.setIRInstruction(d.getRs().getString("IRInstruction"));
                                        
                                        cis.add(ci);
                                    }
                                }
                                else
                                {
                                    System.out.println("getAllCustomInstructionByUser: deviceId fail.");
                                }
                            }
                            else
                            {
                                System.out.println("getAllCustomInstructionByUser: deviceName fail.");
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            System.out.println("getAllCustomInstructionByUser: error.");
                        }
                    }
                    else
                    {
                        System.out.println("getAllCustomInstructionByUser: familyId fail.");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("getAllCustomInstructionByUser: error.");
                }
            }
            else
            {
                System.out.println("getAllCustomInstructionByUser: userId fail.");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("getAllCustomInstructionByUser: error.");
        }
        
        return cis;
    }
                                
            
    
    
        
    
}
