/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package eone.base.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.util.KeyNamePair;

/** Generated Model for AD_Client
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_AD_Client extends PO implements I_AD_Client, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20210130L;

    /** Standard Constructor */
    public X_AD_Client (Properties ctx, int AD_Client_ID, String trxName)
    {
      super (ctx, AD_Client_ID, trxName);
      /** if (AD_Client_ID == 0)
        {
			setAutoArchive (null);
// N
			setIsPostImmediate (false);
// N
			setIsSecureSMTP (false);
// N
			setIsServerEMail (false);
			setIsSmtpAuthorization (false);
// N
			setIsUseASP (false);
			setMMPolicy (null);
// F
			setName (null);
			setValue (null);
        } */
    }

    /** Load Constructor */
    public X_AD_Client (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 7 - System - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuilder sb = new StringBuilder ("X_AD_Client[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** AD_Language AD_Reference_ID=327 */
	public static final int AD_LANGUAGE_AD_Reference_ID=327;
	/** Set Language.
		@param AD_Language 
		Language for this entity
	  */
	public void setAD_Language (String AD_Language)
	{

		set_Value (COLUMNNAME_AD_Language, AD_Language);
	}

	/** Get Language.
		@return Language for this entity
	  */
	public String getAD_Language () 
	{
		return (String)get_Value(COLUMNNAME_AD_Language);
	}

	public eone.base.model.I_AD_PasswordRule getAD_PasswordRule() throws RuntimeException
    {
		return (eone.base.model.I_AD_PasswordRule)MTable.get(getCtx(), eone.base.model.I_AD_PasswordRule.Table_Name)
			.getPO(getAD_PasswordRule_ID(), get_TrxName());	}

	/** Set Password Policies.
		@param AD_PasswordRule_ID Password Policies	  */
	public void setAD_PasswordRule_ID (int AD_PasswordRule_ID)
	{
		if (AD_PasswordRule_ID < 1) 
			set_Value (COLUMNNAME_AD_PasswordRule_ID, null);
		else 
			set_Value (COLUMNNAME_AD_PasswordRule_ID, Integer.valueOf(AD_PasswordRule_ID));
	}

	/** Get Password Policies.
		@return Password Policies	  */
	public int getAD_PasswordRule_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_PasswordRule_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Address.
		@param Address Address	  */
	public void setAddress (String Address)
	{
		set_Value (COLUMNNAME_Address, Address);
	}

	/** Get Address.
		@return Address	  */
	public String getAddress () 
	{
		return (String)get_Value(COLUMNNAME_Address);
	}

	/** AutoArchive AD_Reference_ID=334 */
	public static final int AUTOARCHIVE_AD_Reference_ID=334;
	/** None = N */
	public static final String AUTOARCHIVE_None = "N";
	/** All (Reports, Documents) = 1 */
	public static final String AUTOARCHIVE_AllReportsDocuments = "1";
	/** Documents = 2 */
	public static final String AUTOARCHIVE_Documents = "2";
	/** External Documents = 3 */
	public static final String AUTOARCHIVE_ExternalDocuments = "3";
	/** Set Auto Archive.
		@param AutoArchive 
		Enable and level of automatic Archive of documents
	  */
	public void setAutoArchive (String AutoArchive)
	{

		set_Value (COLUMNNAME_AutoArchive, AutoArchive);
	}

	/** Get Auto Archive.
		@return Enable and level of automatic Archive of documents
	  */
	public String getAutoArchive () 
	{
		return (String)get_Value(COLUMNNAME_AutoArchive);
	}

	public eone.base.model.I_C_Currency getC_Currency() throws RuntimeException
    {
		return (eone.base.model.I_C_Currency)MTable.get(getCtx(), eone.base.model.I_C_Currency.Table_Name)
			.getPO(getC_Currency_ID(), get_TrxName());	}

	/** Set Currency.
		@param C_Currency_ID 
		The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID)
	{
		if (C_Currency_ID < 1) 
			set_Value (COLUMNNAME_C_Currency_ID, null);
		else 
			set_Value (COLUMNNAME_C_Currency_ID, Integer.valueOf(C_Currency_ID));
	}

	/** Get Currency.
		@return The Currency for this record
	  */
	public int getC_Currency_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Currency_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public eone.base.model.I_C_Element getC_Element() throws RuntimeException
    {
		return (eone.base.model.I_C_Element)MTable.get(getCtx(), eone.base.model.I_C_Element.Table_Name)
			.getPO(getC_Element_ID(), get_TrxName());	}

	/** Set Element.
		@param C_Element_ID 
		Accounting Element
	  */
	public void setC_Element_ID (int C_Element_ID)
	{
		if (C_Element_ID < 1) 
			set_Value (COLUMNNAME_C_Element_ID, null);
		else 
			set_Value (COLUMNNAME_C_Element_ID, Integer.valueOf(C_Element_ID));
	}

	/** Get Element.
		@return Accounting Element
	  */
	public int getC_Element_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Element_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Document Directory.
		@param DocumentDir 
		Directory for documents from the application server
	  */
	public void setDocumentDir (String DocumentDir)
	{
		set_Value (COLUMNNAME_DocumentDir, DocumentDir);
	}

	/** Get Document Directory.
		@return Directory for documents from the application server
	  */
	public String getDocumentDir () 
	{
		return (String)get_Value(COLUMNNAME_DocumentDir);
	}

	/** Set EMail Test.
		@param EMailTest 
		Test EMail
	  */
	public void setEMailTest (String EMailTest)
	{
		set_Value (COLUMNNAME_EMailTest, EMailTest);
	}

	/** Get EMail Test.
		@return Test EMail
	  */
	public String getEMailTest () 
	{
		return (String)get_Value(COLUMNNAME_EMailTest);
	}

	/** Set IsCheckMaterial.
		@param IsCheckMaterial IsCheckMaterial	  */
	public void setIsCheckMaterial (boolean IsCheckMaterial)
	{
		set_Value (COLUMNNAME_IsCheckMaterial, Boolean.valueOf(IsCheckMaterial));
	}

	/** Get IsCheckMaterial.
		@return IsCheckMaterial	  */
	public boolean isCheckMaterial () 
	{
		Object oo = get_Value(COLUMNNAME_IsCheckMaterial);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsGroup.
		@param IsGroup IsGroup	  */
	public void setIsGroup (boolean IsGroup)
	{
		set_Value (COLUMNNAME_IsGroup, Boolean.valueOf(IsGroup));
	}

	/** Get IsGroup.
		@return IsGroup	  */
	public boolean isGroup () 
	{
		Object oo = get_Value(COLUMNNAME_IsGroup);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsMultiCurrency.
		@param IsMultiCurrency IsMultiCurrency	  */
	public void setIsMultiCurrency (boolean IsMultiCurrency)
	{
		set_Value (COLUMNNAME_IsMultiCurrency, Boolean.valueOf(IsMultiCurrency));
	}

	/** Get IsMultiCurrency.
		@return IsMultiCurrency	  */
	public boolean isMultiCurrency () 
	{
		Object oo = get_Value(COLUMNNAME_IsMultiCurrency);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Multi Lingual Documents.
		@param IsMultiLingualDocument 
		Documents are Multi Lingual
	  */
	public void setIsMultiLingualDocument (boolean IsMultiLingualDocument)
	{
		set_Value (COLUMNNAME_IsMultiLingualDocument, Boolean.valueOf(IsMultiLingualDocument));
	}

	/** Get Multi Lingual Documents.
		@return Documents are Multi Lingual
	  */
	public boolean isMultiLingualDocument () 
	{
		Object oo = get_Value(COLUMNNAME_IsMultiLingualDocument);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Post Immediately (Deprecated).
		@param IsPostImmediate 
		Post the accounting immediately for testing (Deprecated)
	  */
	public void setIsPostImmediate (boolean IsPostImmediate)
	{
		set_Value (COLUMNNAME_IsPostImmediate, Boolean.valueOf(IsPostImmediate));
	}

	/** Get Post Immediately (Deprecated).
		@return Post the accounting immediately for testing (Deprecated)
	  */
	public boolean isPostImmediate () 
	{
		Object oo = get_Value(COLUMNNAME_IsPostImmediate);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set SMTP SSL/TLS.
		@param IsSecureSMTP 
		Use SSL/TLS for SMTP
	  */
	public void setIsSecureSMTP (boolean IsSecureSMTP)
	{
		set_Value (COLUMNNAME_IsSecureSMTP, Boolean.valueOf(IsSecureSMTP));
	}

	/** Get SMTP SSL/TLS.
		@return Use SSL/TLS for SMTP
	  */
	public boolean isSecureSMTP () 
	{
		Object oo = get_Value(COLUMNNAME_IsSecureSMTP);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Server EMail.
		@param IsServerEMail 
		Send EMail from Server
	  */
	public void setIsServerEMail (boolean IsServerEMail)
	{
		set_Value (COLUMNNAME_IsServerEMail, Boolean.valueOf(IsServerEMail));
	}

	/** Get Server EMail.
		@return Send EMail from Server
	  */
	public boolean isServerEMail () 
	{
		Object oo = get_Value(COLUMNNAME_IsServerEMail);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set SMTP Authentication.
		@param IsSmtpAuthorization 
		Your mail server requires Authentication
	  */
	public void setIsSmtpAuthorization (boolean IsSmtpAuthorization)
	{
		set_Value (COLUMNNAME_IsSmtpAuthorization, Boolean.valueOf(IsSmtpAuthorization));
	}

	/** Get SMTP Authentication.
		@return Your mail server requires Authentication
	  */
	public boolean isSmtpAuthorization () 
	{
		Object oo = get_Value(COLUMNNAME_IsSmtpAuthorization);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsUseASP.
		@param IsUseASP IsUseASP	  */
	public void setIsUseASP (boolean IsUseASP)
	{
		set_Value (COLUMNNAME_IsUseASP, Boolean.valueOf(IsUseASP));
	}

	/** Get IsUseASP.
		@return IsUseASP	  */
	public boolean isUseASP () 
	{
		Object oo = get_Value(COLUMNNAME_IsUseASP);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Use Beta Functions.
		@param IsUseBetaFunctions 
		Enable the use of Beta Functionality
	  */
	public void setIsUseBetaFunctions (boolean IsUseBetaFunctions)
	{
		set_Value (COLUMNNAME_IsUseBetaFunctions, Boolean.valueOf(IsUseBetaFunctions));
	}

	/** Get Use Beta Functions.
		@return Enable the use of Beta Functionality
	  */
	public boolean isUseBetaFunctions () 
	{
		Object oo = get_Value(COLUMNNAME_IsUseBetaFunctions);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set ldapquery.
		@param ldapquery ldapquery	  */
	public void setldapquery (String ldapquery)
	{
		set_Value (COLUMNNAME_ldapquery, ldapquery);
	}

	/** Get ldapquery.
		@return ldapquery	  */
	public String getldapquery () 
	{
		return (String)get_Value(COLUMNNAME_ldapquery);
	}

	/** MMPolicy AD_Reference_ID=335 */
	public static final int MMPOLICY_AD_Reference_ID=335;
	/** LiFo = L */
	public static final String MMPOLICY_LiFo = "L";
	/** FiFo = F */
	public static final String MMPOLICY_FiFo = "F";
	/** Average = A */
	public static final String MMPOLICY_Average = "A";
	/** None = N */
	public static final String MMPOLICY_None = "N";
	/** Set Material Policy.
		@param MMPolicy 
		Material Movement Policy
	  */
	public void setMMPolicy (String MMPolicy)
	{

		set_Value (COLUMNNAME_MMPolicy, MMPolicy);
	}

	/** Get Material Policy.
		@return Material Movement Policy
	  */
	public String getMMPolicy () 
	{
		return (String)get_Value(COLUMNNAME_MMPolicy);
	}

	/** Set Model Validation Classes.
		@param ModelValidationClasses 
		List of data model validation classes separated by ;
	  */
	public void setModelValidationClasses (String ModelValidationClasses)
	{
		set_Value (COLUMNNAME_ModelValidationClasses, ModelValidationClasses);
	}

	/** Get Model Validation Classes.
		@return List of data model validation classes separated by ;
	  */
	public String getModelValidationClasses () 
	{
		return (String)get_Value(COLUMNNAME_ModelValidationClasses);
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
    }

	/** Set Request EMail.
		@param RequestEMail 
		EMail address to send automated mails from or receive mails for automated processing (fully qualified)
	  */
	public void setRequestEMail (String RequestEMail)
	{
		set_Value (COLUMNNAME_RequestEMail, RequestEMail);
	}

	/** Get Request EMail.
		@return EMail address to send automated mails from or receive mails for automated processing (fully qualified)
	  */
	public String getRequestEMail () 
	{
		return (String)get_Value(COLUMNNAME_RequestEMail);
	}

	/** Set Request Folder.
		@param RequestFolder 
		EMail folder to process incoming emails; if empty INBOX is used
	  */
	public void setRequestFolder (String RequestFolder)
	{
		set_Value (COLUMNNAME_RequestFolder, RequestFolder);
	}

	/** Get Request Folder.
		@return EMail folder to process incoming emails; if empty INBOX is used
	  */
	public String getRequestFolder () 
	{
		return (String)get_Value(COLUMNNAME_RequestFolder);
	}

	/** Set Request User.
		@param RequestUser 
		User Name (ID) of the email owner
	  */
	public void setRequestUser (String RequestUser)
	{
		set_Value (COLUMNNAME_RequestUser, RequestUser);
	}

	/** Get Request User.
		@return User Name (ID) of the email owner
	  */
	public String getRequestUser () 
	{
		return (String)get_Value(COLUMNNAME_RequestUser);
	}

	/** Set Request User Password.
		@param RequestUserPW 
		Password of the user name (ID) for mail processing
	  */
	public void setRequestUserPW (String RequestUserPW)
	{
		set_Value (COLUMNNAME_RequestUserPW, RequestUserPW);
	}

	/** Get Request User Password.
		@return Password of the user name (ID) for mail processing
	  */
	public String getRequestUserPW () 
	{
		return (String)get_Value(COLUMNNAME_RequestUserPW);
	}

	/** Set RoundStandardCalculate.
		@param RoundStandardCalculate RoundStandardCalculate	  */
	public void setRoundStandardCalculate (int RoundStandardCalculate)
	{
		set_Value (COLUMNNAME_RoundStandardCalculate, Integer.valueOf(RoundStandardCalculate));
	}

	/** Get RoundStandardCalculate.
		@return RoundStandardCalculate	  */
	public int getRoundStandardCalculate () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RoundStandardCalculate);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set RoundStandardFinal.
		@param RoundStandardFinal RoundStandardFinal	  */
	public void setRoundStandardFinal (int RoundStandardFinal)
	{
		set_Value (COLUMNNAME_RoundStandardFinal, Integer.valueOf(RoundStandardFinal));
	}

	/** Get RoundStandardFinal.
		@return RoundStandardFinal	  */
	public int getRoundStandardFinal () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RoundStandardFinal);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set RoundStandardUnitPrice.
		@param RoundStandardUnitPrice RoundStandardUnitPrice	  */
	public void setRoundStandardUnitPrice (int RoundStandardUnitPrice)
	{
		set_Value (COLUMNNAME_RoundStandardUnitPrice, Integer.valueOf(RoundStandardUnitPrice));
	}

	/** Get RoundStandardUnitPrice.
		@return RoundStandardUnitPrice	  */
	public int getRoundStandardUnitPrice () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RoundStandardUnitPrice);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set ShowListItem.
		@param ShowListItem ShowListItem	  */
	public void setShowListItem (String ShowListItem)
	{
		set_Value (COLUMNNAME_ShowListItem, ShowListItem);
	}

	/** Get ShowListItem.
		@return ShowListItem	  */
	public String getShowListItem () 
	{
		return (String)get_Value(COLUMNNAME_ShowListItem);
	}

	/** Set Mail Host.
		@param SMTPHost 
		Hostname of Mail Server for SMTP and IMAP
	  */
	public void setSMTPHost (String SMTPHost)
	{
		set_Value (COLUMNNAME_SMTPHost, SMTPHost);
	}

	/** Get Mail Host.
		@return Hostname of Mail Server for SMTP and IMAP
	  */
	public String getSMTPHost () 
	{
		return (String)get_Value(COLUMNNAME_SMTPHost);
	}

	/** Set SMTP Port.
		@param SMTPPort 
		SMTP Port Number
	  */
	public void setSMTPPort (int SMTPPort)
	{
		set_Value (COLUMNNAME_SMTPPort, Integer.valueOf(SMTPPort));
	}

	/** Get SMTP Port.
		@return SMTP Port Number
	  */
	public int getSMTPPort () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SMTPPort);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Code.
		@param Value 
		Code
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Code.
		@return Code
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}

	/** Set VerifyDepartment.
		@param VerifyDepartment VerifyDepartment	  */
	public void setVerifyDepartment (String VerifyDepartment)
	{
		set_Value (COLUMNNAME_VerifyDepartment, VerifyDepartment);
	}

	/** Get VerifyDepartment.
		@return VerifyDepartment	  */
	public String getVerifyDepartment () 
	{
		return (String)get_Value(COLUMNNAME_VerifyDepartment);
	}
}