/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import bean.dbBeanLocal;
import bean.dbBeanRemote;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import class3.context;
import class2.familyAndUserData;
import class2.userData;



/**
 * REST Web Service
 *
 * @author vcl4
 */
@Path("userservice")
public class UserserviceResource {

    private dbBeanLocal bean;
    //private dbBeanRemote bean2;
    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of UserserviceResource
     */
    public UserserviceResource() {
    }
    
    
    @GET
    @Consumes("html/text")
    @Produces("application/json")
    @Path("logIn")
    public Response logIn (@QueryParam("id")String id, @QueryParam("pw")String pw)
    {
        context c = new context ();
        int t = 1;
        String resultmsg = null;
        Gson gson = new Gson ();
        String htmlResult = null;
        
        
        try
        {
            bean = (dbBeanLocal) c.start();
            
            if (bean == null)
            {
                System.out.println("login: context fail.");
                resultmsg = "error";
            }
            else
            {
                t = bean.logIn(id, pw);
                
                if (t == 0)
                    resultmsg = "family";
                else if (t == 1)
                    resultmsg = "user";
                else if (t == 2)
                    resultmsg = "fail";
                else
                    resultmsg = "error";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        /*
        Context c = null;
        
        try
        {
            c = new InitialContext ();
            Object o = c.lookup("java:global/SWCD/SWCD-ejb/dbBean!bean.dbBeanLocal");
            bean = (dbBeanLocal) o;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        */
        
        htmlResult = gson.toJson(resultmsg);
        
        System.out.println("login: success.");
        
        return Response.ok(htmlResult)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
            .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
            .build();
    }
    
    @GET
    @Consumes("html/text")
    @Produces("application/json")
    @Path("showAllUserByFamily")
    public Response showAllUserByFamily (@QueryParam("familyId")String familyId, @QueryParam("familyPw")String familyPw)
    {
        context c = new context ();
        ArrayList<userData> uds = new ArrayList<userData> ();
        Gson gson = new Gson ();
        String htmlResult = null;
        
        
        try
        {
            bean = (dbBeanLocal) c.start();
            
            if (bean == null)
            {
                htmlResult = "error";
                System.out.println("showAllUserByFamily: context fail.");
            }
            else
            {
                uds = bean.showAllUserByFamily(familyId, familyPw);
                htmlResult = gson.toJson(uds);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        System.out.println("showAllUserByFamily: success.");
        
        return Response.ok(htmlResult)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
            .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
            .build();
    }
    
    @GET
    @Consumes("html/text")
    @Produces("application/json")
    @Path("showOneUserByFamily")
    public Response showOneUserByFamily (@QueryParam("familyId")String familyId, @QueryParam("familyPw")String familyPw, @QueryParam("userName")String userName)
    {
        context c = new context ();
        userData ud = new userData ();
        Gson gson = new Gson ();
        String htmlResult = null;
        
        try
        {
            bean = (dbBeanLocal) c.start();
            
            if (bean == null)
            {
                htmlResult = "error";
                System.out.println("showOneUserByFamily: context fail.");
            }
            else
            {
                ud = bean.showOneUserByFamily(familyId, familyPw, userName);
                htmlResult = gson.toJson(ud);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        System.out.println("showOneUserByFamily: success.");
        
        return Response.ok(htmlResult)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
            .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
            .build();
    }
    
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("addOneUserByFamily")
    public Response addOneUserByFamily (String json)
    {
        context c = new context ();
        int t = 1;
        String resultmsg = null;
        Gson gson = new Gson ();
        String htmlResult = null;
        familyAndUserData faud = new familyAndUserData ();
        String familyId = null;
        String familyPw = null;
        String oldUserName = null;
        String userId = null;
        String userPw = null;
        String userName = null;
        String userPhone = null;
        String userAuthority = null;
        Type type = new TypeToken<familyAndUserData>(){}.getType();
        
        
        faud = new Gson().fromJson(json, type);
        
        familyId = faud.getFamilyId();
        familyPw = faud.getFamilyPw();
        userId = faud.getNewUserId();
        userPw = faud.getNewUserPw();
        userName = faud.getNewUserName();
        userPhone = faud.getNewUserPhone();
        userAuthority = faud.getNewUserAuthority();
        
        
        try
        {
            bean = (dbBeanLocal) c.start();
            
            if (bean == null)
            {
                System.out.println("addOneUserByFamily: context fail.");
                resultmsg = "error";
            }
            else
            {
                t = bean.addOneUserByFamily(familyId, familyPw, userId, userPw, userName, userPhone, userAuthority);
                
                if (t == 0)
                    resultmsg = "success";
                else if (t == 1)
                    resultmsg = "familyId or Pw fail";
                else if (t == 2)
                    resultmsg = "userId fail";
                else if (t == 3)
                    resultmsg = "userName fail";
                else
                    resultmsg = "error";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        htmlResult = gson.toJson(resultmsg);
        
        System.out.println("addOneUserByFamily: success.");
        
        return  Response.ok(htmlResult)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
                .build();
    }
    
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("reviseOneUserByFamily")
    public Response reviseOneUserByFamily (String json)
    {
        int t = 1;
        String resultmsg = "\0";
        context c = new context ();
        Gson gson = new Gson ();
        String htmlResult = null;
        familyAndUserData faud = new familyAndUserData ();
        String familyId = null;
        String familyPw = null;
        String oldUserName = null;
        String newUserId = null;
        String newUserPw = null;
        String newUserName = null;
        String newUserPhone = null;
        String newUserAuthority = null;
        Type type = new TypeToken<familyAndUserData>(){}.getType();
        
        
        faud = new Gson().fromJson(json, type);
        
        familyId = faud.getFamilyId();
        familyPw = faud.getFamilyPw();
        oldUserName = faud.getOldUserName();
        newUserId = faud.getNewUserId();
        newUserPw = faud.getNewUserPw();
        newUserName = faud.getNewUserName();
        newUserPhone = faud.getNewUserPhone();
        newUserAuthority = faud.getNewUserAuthority();
        
        try
        {
            bean = (dbBeanLocal) c.start();
            
            if (bean == null)
            {
                System.out.println("reviseOneUserByFamily: context fail.");
                resultmsg = "fail";
            }
            else
            {
                t = bean.reviseOneUserByFamily(familyId, familyPw, oldUserName, newUserId, newUserPw, newUserName, newUserPhone, newUserAuthority);
                
                if (t == 0)
                    resultmsg = "success";
                else if (t == 1)
                    resultmsg = "familyId or Pw fail";
                else if (t == 2)
                    resultmsg = "userName fail";
                else
                    resultmsg = "error";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        htmlResult = gson.toJson(resultmsg);
        
        System.out.println("reviseOneUserByFamily: success.");
        
        return Response.ok(htmlResult)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
            .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
            .build();
        
        
    }
    
    
    
    
    
    
    
    
    
    /**
     * Retrieves representation of an instance of web.UserserviceResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of UserserviceResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
