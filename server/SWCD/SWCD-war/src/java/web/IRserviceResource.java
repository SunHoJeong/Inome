/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import bean.dbBeanLocal;
import bean.dbBeanRemote;
import class1.custominstruction;
import class1.doorlocklog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import class3.context;
import class2.deviceData;
import class2.familyAndDeviceData;
import class2.userData;

/**
 * REST Web Service
 *
 * @author vcl4
 */
@Path("IRservice")
public class IRserviceResource {
    
    private dbBeanLocal bean;
    //private dbBeanRemote bean2;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of IRserviceResource
     */
    public IRserviceResource() {
    }
    
    
    @GET
    @Consumes("html/text")
    @Produces("application/json")
    @Path("showAllDeviceByFamily")
    public Response showAllDeviceByFamily (@QueryParam("familyId")String familyId, @QueryParam("familyPw")String familyPw)
    {
        context c = new context ();
        ArrayList<deviceData> dds = new ArrayList<deviceData> ();
        Gson gson = new Gson ();
        String htmlResult = null;
        
        
        try
        {
            bean = (dbBeanLocal) c.start();
            
            if (bean == null)
            {
                htmlResult = "error";
                System.out.println("showAllDeviceByFamily: context fail.");
            }
            else
            {
                dds = bean.showAllDeviceByFamily(familyId, familyPw);
                htmlResult = gson.toJson(dds);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        System.out.println("showAllDeviceByFamily: success.");
        
        return Response.ok(htmlResult)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
            .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
            .build();
    }
    
    @GET
    @Consumes("html/text")
    @Produces("application/json")
    @Path("showOneDeviceByFamily")
    public Response showOneDeviceByFamily (@QueryParam("familyId")String familyId, @QueryParam("familyPw")String familyPw, @QueryParam("deviceName")String deviceName)
    {
        context c = new context ();
        deviceData dd = new deviceData ();
        Gson gson = new Gson ();
        String htmlResult = null;
        
        try
        {
            bean = (dbBeanLocal) c.start();
            
            if (bean == null)
            {
                htmlResult = "error";
                System.out.println("showOneDeviceByFamily: context fail.");
            }
            else
            {
                dd = bean.showOneDeviceByFamily(familyId, familyPw, deviceName);
                htmlResult = gson.toJson(dd);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        System.out.println("showOneDeviceByFamily: success.");
        
        return Response.ok(htmlResult)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
            .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
            .build();
    }
    
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("addOneDeviceByFamily")
    public Response addOneDeviceByFamily (String json)
    {
        int t = 4;
        String resultmsg = null;
        context c = new context ();
        Gson gson = new Gson ();
        String htmlResult = null;
        familyAndDeviceData fadd = new familyAndDeviceData ();
        String familyId = null;
        String familyPw = null;
        String deviceCompany = null;
        String deviceClassification = null;
        String deviceModel = null;
        String deviceName = null;
        String deviceAddress = null;
        Type type = new TypeToken<familyAndDeviceData>(){}.getType();
        
        
        fadd = new Gson().fromJson(json, type);
        
        familyId = fadd.getFamilyId();
        familyPw = fadd.getFamilyPw();
        deviceCompany = fadd.getNewDeviceCompany();
        deviceClassification = fadd.getNewDeviceClassification();
        deviceModel = fadd.getNewDeviceModel();
        deviceName = fadd.getNewDeviceName();
        deviceAddress = fadd.getNewDeviceAddress();
        
        try
        {
            bean = (dbBeanLocal) c.start();
            
            if (bean == null)
            {
                htmlResult = "error";
                System.out.println("addOneDeviceByFamily: context fail.");
            }
            else
            {
                t = bean.addOneDeviceByFamily(familyId, familyPw, deviceCompany, deviceClassification, deviceModel, deviceName, deviceAddress);
                
                if (t == 0)
                    resultmsg = "success";
                else if (t == 1)
                    resultmsg = "familyId or Pw fail";
                else if (t == 2)
                    resultmsg = "deviceClass fail";
                else if (t == 3)
                    resultmsg = "deviceName fail";
                else if (t == -1)
                    resultmsg = "deviceId fail";
                else
                    resultmsg = "error";
                
                htmlResult = gson.toJson(resultmsg);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        System.out.println("addOneDeviceByFamily: success.");
        
        return Response.ok(htmlResult)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
            .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
            .build();
    
    }
    
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("reviseOneDeviceByFamily")
    public Response reviseOneDeviceByFamily (String json)
    {
        int t = 4;
        String resultmsg = null;
        context c = new context ();
        Gson gson = new Gson ();
        String htmlResult = null;
        familyAndDeviceData fadd = new familyAndDeviceData ();
        String familyId = null;
        String familyPw = null;
        String oldDeviceName = null;
        String newDeviceCompany = null;
        String newDeviceClassification = null;
        String newDeviceModel = null;
        String newDeviceName = null;
        String newDeviceAddress = null;
        Type type = new TypeToken<familyAndDeviceData>(){}.getType();
        
        
        fadd = new Gson().fromJson(json, type);
        
        familyId = fadd.getFamilyId();
        familyPw = fadd.getFamilyPw();
        oldDeviceName = fadd.getOldDeviceName();
        newDeviceCompany = fadd.getNewDeviceCompany();
        newDeviceClassification = fadd.getNewDeviceClassification();
        newDeviceModel = fadd.getNewDeviceModel();
        newDeviceName = fadd.getNewDeviceName();
        newDeviceAddress = fadd.getNewDeviceAddress();
        
        try
        {
            bean = (dbBeanLocal) c.start();
            
            if (bean == null)
            {
                htmlResult = "error";
                System.out.println("reviseOneDeviceByFamily: context fail.");
            }
            else
            {
                t = bean.reviseOneDeviceByFamily(familyId, familyPw, oldDeviceName, newDeviceCompany, newDeviceClassification, newDeviceModel, newDeviceName, newDeviceAddress);
                
                if (t == 0)
                    resultmsg = "success";
                else if (t == 1)
                    resultmsg = "familyId or Pw fail";
                else if (t == 2)
                    resultmsg = "deviceClass fail";
                else if (t == 3)
                    resultmsg = "newDeviceName fail";
                else if (t == -1)
                    resultmsg = "newDeviceId fail";
                else if (t == -2)
                    resultmsg = "oldDeviceName fail";
                else
                    resultmsg = "error";
                
                htmlResult = gson.toJson(resultmsg);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        System.out.println("reviseOneDeviceByFamily: success.");
        
        return Response.ok(htmlResult)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
            .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
            .build();
    
    }
    
    @GET
    @Consumes("html/text")
    @Produces("application/json")
    @Path("showAllDeviceByUser")
    public Response showAllDeviceByUser (@QueryParam("userId")String userId, @QueryParam("userPw")String userPw)
    {
        context c = new context ();
        ArrayList<deviceData> d2s = new ArrayList<deviceData> ();
        Gson gson = new Gson ();
        String htmlResult = null;
        
        
        try
        {
            bean = (dbBeanLocal) c.start();
            
            if (bean == null)
            {
                htmlResult = "error";
                System.out.println("showAllDeviceByFamily: context fail.");
            }
            else
            {
                d2s = bean.showAllDeviceByUser(userId, userPw);
                htmlResult = gson.toJson(d2s);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        System.out.println("showAllDeviceByUser: success.");
        
        return Response.ok(htmlResult)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
            .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
            .build();
    }
    
    @GET
    @Consumes("html/text")
    @Produces("application/json")
    @Path("showOneDeviceByUser")
    public Response showOneDeviceByUser (@QueryParam("userId")String userId, @QueryParam("userPw")String userPw, @QueryParam("deviceName")String deviceName)
    {
        context c = new context ();
        deviceData d2 = new deviceData ();
        Gson gson = new Gson ();
        String htmlResult = null;
        
        try
        {
            bean = (dbBeanLocal) c.start();
            
            if (bean == null)
            {
                htmlResult = "error";
                System.out.println("showOneDeviceByUser: context fail.");
            }
            else
            {
                d2 = bean.showOneDeviceByUser(userId, userPw, deviceName);
                htmlResult = gson.toJson(d2);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        System.out.println("showOneDeviceByUser: success.");
        
        return Response.ok(htmlResult)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
            .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
            .build();
    }
    
    @GET
    @Consumes("html/text")
    @Produces("application/json")
    @Path("showAllDoorlocklogByFamily")
    public Response showAllDoorlocklogByFamily (@QueryParam("familyId")String familyId, @QueryParam("familyPw")String familyPw)
    {
        context c = new context ();
        ArrayList<doorlocklog> dlls = new ArrayList<doorlocklog> ();
        Gson gson = new Gson ();
        String htmlResult = null;
        
        
        try
        {
            bean = (dbBeanLocal) c.start();
            
            if (bean == null)
            {
                htmlResult = "error";
                System.out.println("showAllDoorlocklogByFamily: context fail.");
            }
            else
            {
                dlls = bean.showAllDoorlocklogByFamily(familyId, familyPw);
                htmlResult = gson.toJson(dlls);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        System.out.println("showAllDoorlocklogByFamily: success.");
        
        
        return Response.ok(htmlResult)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
            .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
            .build();
    }
    
    @GET
    @Consumes("html/text")
    @Produces("application/json")
    @Path("showOneDoorlocklogByFamily")
    public Response showOneDoorlocklogByFamily (@QueryParam("familyId")String familyId, @QueryParam("familyPw")String familyPw, @QueryParam("doorlockDate")String doorlockDate, @QueryParam("doorlockTime")String doorlockTime)
    {
        context c = new context ();
        doorlocklog dll = new doorlocklog ();
        Gson gson = new Gson ();
        String htmlResult = null;
        
        try
        {
            bean = (dbBeanLocal) c.start();
            
            if (bean == null)
            {
                htmlResult = "error";
                System.out.println("showOneDoorlocklogByFamily: context fail.");
            }
            else
            {
                dll = bean.showOneDoorlocklogByFamily(familyId, familyPw, doorlockDate, doorlockTime);
                htmlResult = gson.toJson(dll);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        System.out.println("showOneDoorlocklogByFamily: success.");
        
        return Response.ok(htmlResult)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
            .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
            .build();
    }
    
    @GET
    @Consumes("html/text")
    @Produces("application/json")
    @Path("doorlockOpenByUser")
    public Response doorlockOpenByUser (@QueryParam("userId")String userId, @QueryParam("userPw")String userPw)
    {
        int t = 3;
        context c = new context ();
        Gson gson = new Gson ();
        String resultmsg = null;
        String htmlResult = null;
        ArrayList<userData> uds = new ArrayList<userData> ();
        
        try
        {
            bean = (dbBeanLocal) c.start();
            
            if (bean == null)
            {
                htmlResult = "error";
                System.out.println("doorlockOpenByUser: context fail.");
            }
            else
            {
                uds = bean.doorlockOpenByUser(userId, userPw);
                htmlResult = gson.toJson(uds);
                /*
                if  (t == 0)
                    resultmsg = "success";
                else if (t == 1)
                    resultmsg = "familyId or pw fail";
                else if (t == 2)
                    resultmsg = "userId or pw fail";
                else if (t == -1)
                    resultmsg = "doorlock address fail";
                else
                    resultmsg = "error";
                
                htmlResult = gson.toJson(resultmsg);
                */
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        System.out.println("doorlockOpenByUser: success.");
        
        return Response.ok(htmlResult)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
            .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
            .build();
    }
    
    @GET
    @Consumes("html/text")
    @Produces("application/json")
    @Path("remoteControlByUser")
    public Response remoteControlByUser (@QueryParam("userId")String userId, @QueryParam("userPw")String userPw, @QueryParam("deviceName")String deviceName, @QueryParam("userInstruction")String userInstruction)
    {
        int t = 4;
        String resultmsg = null;
        context c = new context ();
        Gson gson = new Gson ();
        String htmlResult = null;
        
        try
        {
            bean = (dbBeanLocal) c.start();
            
            if (bean == null)
            {
                htmlResult = "error";
                System.out.println("remoteControlByUser: context fail.");
            }
            else
            {
                t = bean.remoteControlByUser(userId, userPw, deviceName, userInstruction);
                
                if (t == 0)
                    resultmsg = "success";
                else if (t == 1)
                    resultmsg = "familyId fail";
                else if (t == 2)
                    resultmsg = "userId or pw fail";
                else if (t == 3)
                    resultmsg = "deviceName fail";
                else if (t == 4)
                    resultmsg = "userInstruction fail";
                else
                    resultmsg = "error";
                
                htmlResult = gson.toJson(resultmsg);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        System.out.println("remoteControlByUser: success.");
        
        return Response.ok(htmlResult)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
            .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
            .build();
    
    }
    
    @GET
    @Consumes("html/text")
    @Produces("application/json")
    @Path("addIRInstructionByFamily")
    public Response addIRInstructionByFamily (@QueryParam("familyId")String familyId, @QueryParam("familyPw")String familyPw, @QueryParam("deviceName")String deviceName, @QueryParam("userInstruction")String userInstruction)
    {
        int t = 4;
        String resultmsg = null;
        context c = new context ();
        Gson gson = new Gson ();
        String htmlResult = null;
        
        try
        {
            bean = (dbBeanLocal) c.start();
            
            if (bean == null)
            {
                htmlResult = "error";
                System.out.println("addOneDeviceByFamily: context fail.");
            }
            else
            {
                t = bean.addIRInstructionByFamily(familyId, familyPw, deviceName, userInstruction);
                
                if (t == 0)
                    resultmsg = "success";
                else if (t == 1)
                    resultmsg = "familyId or Pw fail";
                else if (t == 2)
                    resultmsg = "deviceClass fail";
                else if (t == 3)
                    resultmsg = "deviceName fail";
                else if (t == -1)
                    resultmsg = "userInstruction fail";
                else if (t == -2)
                    resultmsg = "userInstruction fail";
                else if (t == -3)
                    resultmsg = "IRInstruction fail";
                else if (t == -4)
                    resultmsg = "IRInstruction fail";
                else
                    resultmsg = "error";
                
                htmlResult = gson.toJson(resultmsg);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        System.out.println("addIRInstructionByFamily: success.");
        
        return Response.ok(htmlResult)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
            .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
            .build();
    }
    
    @GET
    @Consumes("html/text")
    @Produces("application/json")
    @Path("getAllDeviceCompanyByFamily")
    public Response getAllDeviceCompanyByFamily (@QueryParam("familyId")String familyId, @QueryParam("familyPw")String familyPw)
    {
        context c = new context ();
        Gson gson = new Gson ();
        String htmlResult = null;
        ArrayList<String> ss = new ArrayList<String> ();
        
        try
        {
            bean = (dbBeanLocal) c.start();
            
            if (bean == null)
            {
                htmlResult = "error";
                System.out.println("getAllDeviceCompanyByFamily: context fail.");
            }
            else
            {
                ss = bean.getAllDeviceCompanyByFamily(familyId, familyPw);
                htmlResult = gson.toJson(ss);
                /*
                if  (t == 0)
                    resultmsg = "success";
                else if (t == 1)
                    resultmsg = "familyId or pw fail";
                else if (t == 2)
                    resultmsg = "userId or pw fail";
                else if (t == -1)
                    resultmsg = "doorlock address fail";
                else
                    resultmsg = "error";
                
                htmlResult = gson.toJson(resultmsg);
                */
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        System.out.println("getAllDeviceCompanyByFamily: success.");
        
        return Response.ok(htmlResult)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
            .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
            .build();
    }
    
    @GET
    @Consumes("html/text")
    @Produces("application/json")
    @Path("getAllDeviceClassificationByFamily")
    public Response getAllDeviceClassificationByFamily (@QueryParam("familyId")String familyId, @QueryParam("familyPw")String familyPw, @QueryParam("deviceCompany")String deviceCompany)
    {
        context c = new context ();
        Gson gson = new Gson ();
        String htmlResult = null;
        ArrayList<String> ss = new ArrayList<String> ();
        
        try
        {
            bean = (dbBeanLocal) c.start();
            
            if (bean == null)
            {
                htmlResult = "error";
                System.out.println("getAllDeviceClassificationByFamily: context fail.");
            }
            else
            {
                ss = bean.getAllDeviceClassificationByFamily(familyId, familyPw, deviceCompany);
                htmlResult = gson.toJson(ss);
                /*
                if  (t == 0)
                    resultmsg = "success";
                else if (t == 1)
                    resultmsg = "familyId or pw fail";
                else if (t == 2)
                    resultmsg = "userId or pw fail";
                else if (t == -1)
                    resultmsg = "doorlock address fail";
                else
                    resultmsg = "error";
                
                htmlResult = gson.toJson(resultmsg);
                */
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        System.out.println("getAllDeviceClassificationByFamily: success.");
        
        return Response.ok(htmlResult)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
            .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
            .build();
    }
    
    @GET
    @Consumes("html/text")
    @Produces("application/json")
    @Path("getAllDeviceModelByFamily")
    public Response getAllDeviceModelByFamily (@QueryParam("familyId")String familyId, @QueryParam("familyPw")String familyPw, @QueryParam("deviceCompany")String deviceCompany, @QueryParam("deviceClassification")String deviceClassification)
    {
        context c = new context ();
        Gson gson = new Gson ();
        String htmlResult = null;
        ArrayList<String> ss = new ArrayList<String> ();
        
        try
        {
            bean = (dbBeanLocal) c.start();
            
            if (bean == null)
            {
                htmlResult = "error";
                System.out.println("getAllDeviceModelByFamily: context fail.");
            }
            else
            {
                ss = bean.getAllDeviceModelByFamily(familyId, familyPw, deviceCompany, deviceClassification);
                htmlResult = gson.toJson(ss);
                /*
                if  (t == 0)
                    resultmsg = "success";
                else if (t == 1)
                    resultmsg = "familyId or pw fail";
                else if (t == 2)
                    resultmsg = "userId or pw fail";
                else if (t == -1)
                    resultmsg = "doorlock address fail";
                else
                    resultmsg = "error";
                
                htmlResult = gson.toJson(resultmsg);
                */
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        System.out.println("getAllDeviceModelByFamily: success.");
        
        return Response.ok(htmlResult)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
            .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
            .build();
    }
    
    @GET
    @Consumes("html/text")
    @Produces("application/json")
    @Path("getAllCustomInstructionByUser")
    public Response getAllCustomInstructionByUser (@QueryParam("userId")String userId, @QueryParam("userPw")String userPw, @QueryParam("deviceName")String deviceName)
    {
        context c = new context ();
        Gson gson = new Gson ();
        String htmlResult = null;
        ArrayList<custominstruction> cis = new ArrayList<custominstruction> ();
        
        try
        {
            bean = (dbBeanLocal) c.start();
            
            if (bean == null)
            {
                htmlResult = "error";
                System.out.println("getAllCustomInstructionByUser: context fail.");
            }
            else
            {
                cis = bean.getAllCustomInstructionByUser(userId, userPw, deviceName);
                htmlResult = gson.toJson(cis);
                /*
                if  (t == 0)
                    resultmsg = "success";
                else if (t == 1)
                    resultmsg = "familyId or pw fail";
                else if (t == 2)
                    resultmsg = "userId or pw fail";
                else if (t == -1)
                    resultmsg = "doorlock address fail";
                else
                    resultmsg = "error";
                
                htmlResult = gson.toJson(resultmsg);
                */
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        System.out.println("getAllCustomInstructionByUser: success.");
        
        return Response.ok(htmlResult)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
            .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
            .build();
    }
    
    
    
    
    
    
    
    
    
    
    
    

    /**
     * Retrieves representation of an instance of web.IRserviceResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of IRserviceResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
