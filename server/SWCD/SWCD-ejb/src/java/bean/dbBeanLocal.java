/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import class1.custominstruction;
import java.util.ArrayList;
import javax.ejb.Local;
import class1.doorlocklog;
import class2.deviceData;
import class2.userData;

/**
 *
 * @author vcl4
 */
@Local
public interface dbBeanLocal {
    
    
    int logIn (String id, String pw);
    
    public ArrayList<userData> showAllUserByFamily (String familyId, String familyPw);
    
    public userData showOneUserByFamily (String familyId, String familyPw, String userName);
    
    int addOneUserByFamily (String familyId, String familyPw, String userId, String userPw, String userName, String userPhone, String userAuthority);
    
    public int reviseOneUserByFamily (String familyId, String familyPw, String oldUserName, String newUserId, String newUserPw, String newUserName, String newUserPhone, String newUserAuthority);
    
    public ArrayList<deviceData> showAllDeviceByUser (String userId, String userPw);
    
    public deviceData showOneDeviceByUser (String userId, String userPw, String deviceName);
    
    public ArrayList<deviceData> showAllDeviceByFamily (String familyId, String familyPw);
    
    public deviceData showOneDeviceByFamily (String familyId, String familyPw, String deviceName);
    
    public int addOneDeviceByFamily (String familyId, String familyPw, String deviceCompany, String deviceClassification, String deviceModel, String deviceName, String deviceAddress);
    
    public int reviseOneDeviceByFamily (String familyId, String familyPw, String oldDeviceName, String newDeviceCompany, String newDeviceClassification, String newDeviceModel, String newDeviceName, String newDeviceAddress);
    
    public ArrayList<doorlocklog> showAllDoorlocklogByFamily (String familyId, String familyPw);
    
    public doorlocklog showOneDoorlocklogByFamily (String familyId, String familyPw, String doorlockDate, String doorlockTime);
    
    public ArrayList<userData> doorlockOpenByUser (String userId, String userPw);
    
    public int remoteControlByUser (String userId, String userPw, String deviceName, String userInstruction);
    
    public int addIRInstructionByFamily (String familyId, String familyPw, String deviceName, String userInstruction);
    
    public ArrayList<String> getAllDeviceCompanyByFamily (String familyId, String familyPw);
    
    public ArrayList<String> getAllDeviceClassificationByFamily (String familyId, String familyPw, String deviceCompany);
    
    public ArrayList<String> getAllDeviceModelByFamily (String familyId, String familyPw, String deviceCompany, String deviceClassification);
    
    public ArrayList<custominstruction> getAllCustomInstructionByUser (String userId, String userPw, String deviceName);
    
}
