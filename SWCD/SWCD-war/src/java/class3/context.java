/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package class3;

import bean.dbBeanLocal;
import javax.naming.Context;
import javax.naming.InitialContext;

/**
 *
 * @author vcl4
 */
public class context {
    
    
    public Object start ()
    {
        Context c = null;
        
        
        try
        {
            c = new InitialContext ();
            Object o = c.lookup("java:global/SWCD/SWCD-ejb/dbBean!bean.dbBeanLocal");
            return (dbBeanLocal) o;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
}
