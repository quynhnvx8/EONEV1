
package org.compiere.interfaces;

import java.util.Properties;

import org.compiere.util.EMail;

import eone.base.process.ProcessInfo;

/**
 * Interface for adempiere/Server.
 */
public interface Server
{
   
   /**
    * Process Remote
    * @param ctx Context
    * @param pi Process Info
    * @return resulting Process Info    */
   public ProcessInfo process( Properties ctx, ProcessInfo pi );

   
   /**
    * Send EMail from Server
    * @param ctx Context
    * @param email
    * @return message return from email server */
   public String sendEMail( Properties ctx, EMail email);

   /**
    * Execute task on server
    * @param ctx Context
    * @param AD_Task_ID task
    * @return execution trace    */
   public String executeTask( Properties ctx, int AD_Task_ID );

   /**
    * Cash Reset
    * @param ctx Context
    * @param tableName table name
    * @param Record_ID record or 0 for all
    * @return number of records reset    */
   public int cacheReset( Properties ctx, String tableName,int Record_ID );

   /**
    * Execute db proces on server
    * @param ctx Context
    * @param processInfo
    * @param procedureName
    * @return ProcessInfo    */
   public ProcessInfo dbProcess( Properties ctx, ProcessInfo processInfo, String procedureName );
}
