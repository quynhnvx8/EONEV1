package org.idempiere.adinterface;

import java.util.Map;
import java.util.logging.Level;

import org.compiere.model.Lookup;
import org.compiere.model.MProcess;
import org.compiere.model.MProcessPara;
import org.compiere.model.PrintInfo;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.util.CLogger;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.ProcessUtil;
import org.compiere.util.Trx;
import org.compiere.util.Util;
import org.idempiere.adInterface.x10.DataField;
import org.idempiere.adInterface.x10.GetProcessParamsDocument;
import org.idempiere.adInterface.x10.LookupValues;
import org.idempiere.adInterface.x10.ProcessParam;
import org.idempiere.adInterface.x10.ProcessParamList;
import org.idempiere.adInterface.x10.ProcessParams;
import org.idempiere.adInterface.x10.ProcessParamsDocument;
import org.idempiere.adInterface.x10.RunProcess;
import org.idempiere.adInterface.x10.RunProcessDocument;
import org.idempiere.adInterface.x10.RunProcessResponse;
import org.idempiere.adInterface.x10.RunProcessResponseDocument;

import eone.base.process.ProcessInfo;
import eone.base.process.ProcessInfoParameter;
import eone.base.process.ProcessInfoUtil;

/*
 * ADEMPIERE/COMPIERE
 * 
 * GridField na GridField
 * GridTab na GridTab
 */

public class Process {
	
	private static final CLogger	log = CLogger.getCLogger(Process.class);

	/**
	 * @param cs
	 * @param req
	 * @return
	 * @deprecated - method not used - will be deleted in future versions
	 */
	public static ProcessParamsDocument getProcessParams( CompiereService cs, GetProcessParamsDocument req ) 
	{
		ProcessParamsDocument res = ProcessParamsDocument.Factory.newInstance();
		ProcessParams params = res.addNewProcessParams();
		ProcessParamList PL = params.addNewParams();
		
		int AD_Menu_ID = req.getGetProcessParams().getADMenuID();
		int AD_Process_ID = req.getGetProcessParams().getADProcessID();
		MProcess process  = null;
		
		if (AD_Menu_ID>0 && AD_Process_ID==0 )
			process = MProcess.getFromMenu( cs.getCtx(), AD_Menu_ID);			
		else
		if (AD_Menu_ID==0 && AD_Process_ID>0 )
			process = new MProcess( cs.getCtx(), AD_Process_ID, null);

		if (process != null)
		{

			params.setDescription(process.getDescription());
			params.setHelp("");
			params.setName( process.getName() );
			params.setADProcessID( process.getAD_Process_ID());

			MProcessPara[] parameter = process.getParameters();
			for (int i = 0; i < parameter.length; i++)
			{
				MProcessPara para = parameter[i];
				
				ProcessParam p = PL.addNewParam();
				p.setName( para.getName() );
				p.setDescription( para.getDescription() );
				p.setDisplayType( para.getAD_Reference_ID() );
				p.setIsMandatory( para.isMandatory() );
				p.setFieldLength( para.getFieldLength() );
				p.setIsRange( para.isRange() );
				p.setColumnName( para.getColumnName() );
				p.setDefaultValue( para.getDefaultValue() );
				p.setDefaultValue2( para.getDefaultValue2() );												
				if (para.getDefaultValue()!=null )
				{				
					if (DisplayType.isDate(para.getAD_Reference_ID()))
					{
						if (para.getDefaultValue().indexOf( "@#Date@")>=0) {
							//Object t = Env.getContextAsDate( cs.getM_ctx(), "#Date" );
							//String t = Env.getContext( cs.getM_ctx(), "#Date" );
							String t= cs.dateFormat.format( Env.getContextAsDate( cs.getCtx(), "#Date") );	
							
							p.setDefaultValue( t ); //cs.dateFormat.format( t ));
						}
					} else
					if (DisplayType.YesNo ==para.getAD_Reference_ID() )
					{
						if ("Y".equalsIgnoreCase(para.getDefaultValue())) 
								p.setDefaultValue("true");
						else
							    p.setDefaultValue("false");
					}
				} else {
					if (DisplayType.YesNo ==para.getAD_Reference_ID()) 
							    p.setDefaultValue("false");
				}

				if (para.getDefaultValue2()!=null)
				{				
					if (DisplayType.isDate(para.getAD_Reference_ID()))
					{						
						if (para.getDefaultValue2().indexOf( "@#Date@")>=0) {
							//Object t = Env.getContextAsDate( cs.getM_ctx(), "#Date" );
							//String t = Env.getContext( cs.getM_ctx(), "#Date" );
							String t= cs.dateFormat.format( Env.getContextAsDate( cs.getCtx(), "#Date") );
							p.setDefaultValue2( t ); //cs.dateFormat.format( t ) );
						}							
					}
				}
				
				
				if (para.isLookup())
				{
					LookupValues lvs = p.addNewLookup();
					Lookup lookup = para.getLookup();	
					try {
					ADLookup.fillLookupValues( lvs, lookup, para.isMandatory(), false /*isReadOnly*/, false ); // IDEMPIERE 90
					} catch (Exception ex) {
						System.out.println("getProcessParams exception: " +ex.getMessage());
						ex.printStackTrace();
					}
				}
			}
		}
		
		return res;
	}
	
	/**************************************************************************
	 * 	Run process
	 *	@param m_cs
	 *  @param req
	 *	@return {@link RunProcessResponseDocument}
	 */
	public static RunProcessResponseDocument runProcess (CompiereService m_cs, RunProcessDocument req )
	{
		return runProcess(m_cs, req, null, null);
	}
	
	/**************************************************************************
	 * 	Run process
	 *	@param m_cs
	 *  @param req
	 *  @param requestCtx
	 *  @param trxName
	 *	@return {@link RunProcessResponseDocument}
	 */
	public static RunProcessResponseDocument runProcess (CompiereService m_cs, RunProcessDocument req, Map<String, Object> requestCtx, String trxName )
	{
		RunProcessResponseDocument res = RunProcessResponseDocument.Factory.newInstance();
		RunProcessResponse r= res.addNewRunProcessResponse();

		RunProcess rp = req.getRunProcess();
		int AD_Menu_ID = rp.getADMenuID();
		int AD_Process_ID = rp.getADProcessID();
		int m_record_id = rp.getADRecordID();
	  	
		MProcess process = null;
		if (AD_Menu_ID <= 0 && AD_Process_ID > 0)
			process = MProcess.get(m_cs.getCtx(), AD_Process_ID);
		else if (AD_Menu_ID > 0 && AD_Process_ID <= 0)
			process = MProcess.getFromMenu(m_cs.getCtx(), AD_Menu_ID);
		if (process == null)
		{
			r.setError("Process not found");
			r.setIsError( true );
			return res;
		}
		
		
		DataField[] fields = rp.getParamValues().getFieldArray();
		for(DataField field : fields) {
			if ("AD_Record_ID".equals(field.getColumn())) {
				Object value = null;
				String s = field.getVal();
				if (requestCtx != null && !Util.isEmpty(s) && s.charAt(0) == '@') {
					value = ModelADServiceImpl.parseVariable(m_cs, requestCtx, field.getColumn(), s);
					if (value != null) {
						if (value instanceof Number) {
							m_record_id = ((Number)value).intValue();
						} else {
							try {
								m_record_id = Integer.parseInt(value.toString());
							} catch (Exception e){}
						}
					}
				} else if (!Util.isEmpty(s)) {
					try {
						m_record_id = Integer.parseInt(s);
					} catch (Exception e){}
				}
			}
		}
		/*
		if (m_record_id>0)
		{
			pInstance.setRecord_ID( m_record_id);
			pInstance.saveEx();
		}
		*/
		//
		ProcessInfo pi = new ProcessInfo (process.getName(), process.getAD_Process_ID());
		pi.setAD_User_ID(Env.getAD_User_ID(m_cs.getCtx()));
		pi.setAD_Client_ID(Env.getAD_Client_ID(m_cs.getCtx()));
		if (m_record_id >0)
			pi.setRecord_ID( m_record_id  );
		ProcessInfoParameter[] parameters = pi.getParameter();
		if (parameters == null)
		{
			ProcessInfoUtil.setParameterFromDB(pi);
			parameters = pi.getParameter();
		}
		for(DataField field : fields) {
			if (isDataURI(field.getVal())) {
				for(ProcessInfoParameter param : parameters) {
					if (param.getParameterName().equals(field.getColumn())) {
						String data = field.getVal().substring(field.getVal().indexOf(";base64,")+";base64,".length());
						param.setParameter(data);
						break;
					}
				}
			}
		}
		
		boolean processOK = false;
		boolean jasperreport =
				(process != null
				 && (process.getJasperReport() != null
				     || (process.getClassname() != null
				         && process.getClassname().indexOf(ProcessUtil.JASPER_STARTER_CLASS) >= 0
				        )
				    )
				);
		
		if (jasperreport)
		{
			processOK = true;
		}
		
		
	
		if (process.isJavaProcess() && !jasperreport)
		{
			Trx trx = trxName == null ? Trx.get(Trx.createTrxName("WebPrc"), true) : Trx.get(trxName, true);
			if (trxName == null)
				trx.setDisplayName(Process.class.getName()+"_runProcess");
			try
			{
				processOK = process.processIt(pi, trx, false);
				if (trxName == null && processOK)
					trx.commit();	
				else if (trxName == null && !processOK)
					trx.rollback();
			}
			catch (Throwable t)
			{
				trx.rollback();
			}
			finally
			{
				if (trxName == null)
					trx.close();
			}
			if (!processOK || pi.isError())
			{
				r.setSummary(pi.getSummary());
				r.setError(pi.getSummary());
				r.setLogInfo(pi.getLogInfo(true));
				r.setIsError( true );				
				processOK = false;
			} 
			else
			{
				try{
					if( pi.getExportFile() != null ){
						r.setData(java.nio.file.Files.readAllBytes(pi.getExportFile().toPath()));
						r.setReportFormat(pi.getExportFileExtension());
					}
					r.setSummary(pi.getSummary());
					r.setError(pi.getSummary());
					r.setLogInfo(pi.getLogInfo(true));
					r.setIsError( false );
				}
				catch (Exception e)
				{
					r.setError("Cannot get the export file:" + e.getMessage());
					r.setLogInfo(pi.getLogInfo(true) );
					r.setIsError( true );
					return res;
				}
			}
		}
		
		//	Report
		if ((process.isReport() || jasperreport))
		{
			pi.setReportingProcess(true);
			r.setIsReport(true);
			ReportEngine re=null;
			if (!jasperreport) 
				re = start(pi);

			if (re == null && !jasperreport)
			{
				; 
			}
			else
			{
				try
				{
					boolean ok = false;
					String file_type = "pdf"; 
					if (!jasperreport)
					{
						byte dat[] = re.createPDFData();
						file_type ="pdf";
						r.setData(dat);		
						r.setReportFormat(file_type);
						
						ok = true;
					}
					else
					{
						Trx trx = trxName == null ? Trx.get(Trx.createTrxName("WebPrc"), true) : Trx.get(trxName, true);
						pi.setPrintPreview (false);
						pi.setIsBatch(true);
						ProcessUtil.startJavaProcess(Env.getCtx(), pi, trx, true, null);
						file_type ="pdf";				
						r.setData(java.nio.file.Files.readAllBytes(pi.getPDFReport().toPath()));
						r.setReportFormat(file_type);
						ok = true;
					}
											
					if (ok)
					{
					}
					else
					{
						r.setError("Cannot create report");
						r.setLogInfo(pi.getLogInfo(true) );
						r.setIsError( true );
						return res;								
					}
				}
				catch (Exception e)
				{
					r.setError("Cannot create report:" + e.getMessage());
					r.setLogInfo(pi.getLogInfo(true) );
					r.setIsError( true );
					return res;								
					// , 
				}  
			}
		}
		return res;
	}	//	createProcessPage

	
	
	static public ReportEngine start (ProcessInfo pi)
	{
		if (log.isLoggable(Level.INFO)) log.info("start - " + pi);

		
		return startStandardReport (pi);
	}	//	create

	
	/**************************************************************************
	 *	Start Standard Report.
	 *  - Get Table Info & submit
	 *  @param pi Process Info
	 *  @param IsDirectPrint if true, prints directly - otherwise View
	 *  @return true if OK
	 */
	static public ReportEngine startStandardReport (ProcessInfo pi)
	{
		ReportEngine re = ReportEngine.get(Env.getCtx(), pi, null);
		if (re == null)
		{
			pi.setSummary("No ReportEngine");
			return null;
		}
		return re;
	}	//	startStandardReport

	


	private static boolean isDataURI(String s)
	{
		if (Util.isEmpty(s)) return false;
		
		if (s.startsWith("data:") && s.indexOf(";base64,") > 0)
			return true;
		else
			return false;
	}
	
	/**
	 *	Start Financial Report.
	 *  @param pi Process Info
	 *  @return true if OK
	 */
	static public ReportEngine startFinReport (ProcessInfo pi)
	{
		//	Get PrintFormat
		MPrintFormat format = (MPrintFormat)pi.getTransientObject();
		if (format == null)
			format = (MPrintFormat)pi.getSerializableObject();
		if (format == null)
		{
			log.log(Level.SEVERE, "startFinReport - No PrintFormat");
			return null;
		}
		PrintInfo info = new PrintInfo(pi);

		ReportEngine re = new ReportEngine(Env.getCtx(), format, info, null);
		//new Viewer(re);
		return re;
	}
}
