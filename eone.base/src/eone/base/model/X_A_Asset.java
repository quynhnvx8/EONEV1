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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for A_Asset
 *  @author iDempiere (generated) 
 *  @version Version 1.0 - $Id$ */
public class X_A_Asset extends PO implements I_A_Asset, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200906L;

    /** Standard Constructor */
    public X_A_Asset (Properties ctx, int A_Asset_ID, String trxName)
    {
      super (ctx, A_Asset_ID, trxName);
      /** if (A_Asset_ID == 0)
        {
			setA_Asset_Group_ID (0);
			setA_Asset_ID (0);
			setIsDepreciated (false);
			setIsDisposed (false);
			setName (null);
			setProcessed (false);
// N
			setValue (null);
        } */
    }

    /** Load Constructor */
    public X_A_Asset (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
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
      StringBuilder sb = new StringBuilder ("X_A_Asset[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	public eone.base.model.I_A_Asset_Group getA_Asset_Group() throws RuntimeException
    {
		return (eone.base.model.I_A_Asset_Group)MTable.get(getCtx(), eone.base.model.I_A_Asset_Group.Table_Name)
			.getPO(getA_Asset_Group_ID(), get_TrxName());	}

	/** Set Asset Group.
		@param A_Asset_Group_ID 
		Group of Assets
	  */
	public void setA_Asset_Group_ID (int A_Asset_Group_ID)
	{
		if (A_Asset_Group_ID < 1) 
			set_Value (COLUMNNAME_A_Asset_Group_ID, null);
		else 
			set_Value (COLUMNNAME_A_Asset_Group_ID, Integer.valueOf(A_Asset_Group_ID));
	}

	/** Get Asset Group.
		@return Group of Assets
	  */
	public int getA_Asset_Group_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_A_Asset_Group_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Asset.
		@param A_Asset_ID 
		Asset used internally or by customers
	  */
	public void setA_Asset_ID (int A_Asset_ID)
	{
		if (A_Asset_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_A_Asset_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_A_Asset_ID, Integer.valueOf(A_Asset_ID));
	}

	/** Get Asset.
		@return Asset used internally or by customers
	  */
	public int getA_Asset_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_A_Asset_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set A_Asset_UU.
		@param A_Asset_UU A_Asset_UU	  */
	public void setA_Asset_UU (String A_Asset_UU)
	{
		set_Value (COLUMNNAME_A_Asset_UU, A_Asset_UU);
	}

	/** Get A_Asset_UU.
		@return A_Asset_UU	  */
	public String getA_Asset_UU () 
	{
		return (String)get_Value(COLUMNNAME_A_Asset_UU);
	}

	public eone.base.model.I_C_ElementValue getAccount_Cr() throws RuntimeException
    {
		return (eone.base.model.I_C_ElementValue)MTable.get(getCtx(), eone.base.model.I_C_ElementValue.Table_Name)
			.getPO(getAccount_Cr_ID(), get_TrxName());	}

	/** Set Account Cr.
		@param Account_Cr_ID 
		Account Cr
	  */
	public void setAccount_Cr_ID (int Account_Cr_ID)
	{
		if (Account_Cr_ID < 1) 
			set_Value (COLUMNNAME_Account_Cr_ID, null);
		else 
			set_Value (COLUMNNAME_Account_Cr_ID, Integer.valueOf(Account_Cr_ID));
	}

	/** Get Account Cr.
		@return Account Cr
	  */
	public int getAccount_Cr_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Account_Cr_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public eone.base.model.I_C_ElementValue getAccount_Dr() throws RuntimeException
    {
		return (eone.base.model.I_C_ElementValue)MTable.get(getCtx(), eone.base.model.I_C_ElementValue.Table_Name)
			.getPO(getAccount_Dr_ID(), get_TrxName());	}

	/** Set Account Dr.
		@param Account_Dr_ID 
		Account Dr
	  */
	public void setAccount_Dr_ID (int Account_Dr_ID)
	{
		if (Account_Dr_ID < 1) 
			set_Value (COLUMNNAME_Account_Dr_ID, null);
		else 
			set_Value (COLUMNNAME_Account_Dr_ID, Integer.valueOf(Account_Dr_ID));
	}

	/** Get Account Dr.
		@return Account Dr
	  */
	public int getAccount_Dr_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Account_Dr_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AccumulateAmt.
		@param AccumulateAmt AccumulateAmt	  */
	public void setAccumulateAmt (BigDecimal AccumulateAmt)
	{
		set_Value (COLUMNNAME_AccumulateAmt, AccumulateAmt);
	}

	/** Get AccumulateAmt.
		@return AccumulateAmt	  */
	public BigDecimal getAccumulateAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AccumulateAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Approved.
		@param Approved Approved	  */
	public void setApproved (String Approved)
	{
		set_Value (COLUMNNAME_Approved, Approved);
	}

	/** Get Approved.
		@return Approved	  */
	public String getApproved () 
	{
		return (String)get_Value(COLUMNNAME_Approved);
	}

	/** Asset = AS */
	public static final String ASSETTYPE_Asset = "AS";
	/** Tools = TO */
	public static final String ASSETTYPE_Tools = "TO";
	/** Expense = EX */
	public static final String ASSETTYPE_Expense = "EX";
	/** Set AssetType.
		@param AssetType AssetType	  */
	public void setAssetType (String AssetType)
	{

		set_Value (COLUMNNAME_AssetType, AssetType);
	}

	/** Get AssetType.
		@return AssetType	  */
	public String getAssetType () 
	{
		return (String)get_Value(COLUMNNAME_AssetType);
	}

	/** Set BaseAmtCurrent.
		@param BaseAmtCurrent BaseAmtCurrent	  */
	public void setBaseAmtCurrent (BigDecimal BaseAmtCurrent)
	{
		set_Value (COLUMNNAME_BaseAmtCurrent, BaseAmtCurrent);
	}

	/** Get BaseAmtCurrent.
		@return BaseAmtCurrent	  */
	public BigDecimal getBaseAmtCurrent () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_BaseAmtCurrent);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set BaseAmtOriginal.
		@param BaseAmtOriginal BaseAmtOriginal	  */
	public void setBaseAmtOriginal (BigDecimal BaseAmtOriginal)
	{
		set_Value (COLUMNNAME_BaseAmtOriginal, BaseAmtOriginal);
	}

	/** Get BaseAmtOriginal.
		@return BaseAmtOriginal	  */
	public BigDecimal getBaseAmtOriginal () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_BaseAmtOriginal);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_C_TypeCost getC_TypeCost() throws RuntimeException
    {
		return (I_C_TypeCost)MTable.get(getCtx(), I_C_TypeCost.Table_Name)
			.getPO(getC_TypeCost_ID(), get_TrxName());	}

	/** Set TypeCost.
		@param C_TypeCost_ID TypeCost	  */
	public void setC_TypeCost_ID (int C_TypeCost_ID)
	{
		if (C_TypeCost_ID < 1) 
			set_Value (COLUMNNAME_C_TypeCost_ID, null);
		else 
			set_Value (COLUMNNAME_C_TypeCost_ID, Integer.valueOf(C_TypeCost_ID));
	}

	/** Get TypeCost.
		@return TypeCost	  */
	public int getC_TypeCost_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_TypeCost_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Canceled.
		@param Canceled Canceled	  */
	public void setCanceled (String Canceled)
	{
		set_Value (COLUMNNAME_Canceled, Canceled);
	}

	/** Get Canceled.
		@return Canceled	  */
	public String getCanceled () 
	{
		return (String)get_Value(COLUMNNAME_Canceled);
	}

	/** Set Country.
		@param Country Country	  */
	public void setCountry (String Country)
	{
		set_Value (COLUMNNAME_Country, Country);
	}

	/** Get Country.
		@return Country	  */
	public String getCountry () 
	{
		return (String)get_Value(COLUMNNAME_Country);
	}

	/** Set Create Date.
		@param CreateDate Create Date	  */
	public void setCreateDate (Timestamp CreateDate)
	{
		set_ValueNoCheck (COLUMNNAME_CreateDate, CreateDate);
	}

	/** Get Create Date.
		@return Create Date	  */
	public Timestamp getCreateDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_CreateDate);
	}

	/** Set Depreciation Date.
		@param DepreciationDate 
		Date of last depreciation
	  */
	public void setDepreciationDate (Timestamp DepreciationDate)
	{
		set_Value (COLUMNNAME_DepreciationDate, DepreciationDate);
	}

	/** Get Depreciation Date.
		@return Date of last depreciation
	  */
	public Timestamp getDepreciationDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DepreciationDate);
	}

	/** Set Depreciation Split.
		@param DepreciationSplit Depreciation Split	  */
	public void setDepreciationSplit (boolean DepreciationSplit)
	{
		set_Value (COLUMNNAME_DepreciationSplit, Boolean.valueOf(DepreciationSplit));
	}

	/** Get Depreciation Split.
		@return Depreciation Split	  */
	public boolean isDepreciationSplit () 
	{
		Object oo = get_Value(COLUMNNAME_DepreciationSplit);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set Disposal Date.
		@param DisposalDate 
		Date when the asset is/was disposed
	  */
	public void setDisposalDate (Timestamp DisposalDate)
	{
		set_Value (COLUMNNAME_DisposalDate, DisposalDate);
	}

	/** Get Disposal Date.
		@return Date when the asset is/was disposed
	  */
	public Timestamp getDisposalDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DisposalDate);
	}

	/** DocStatus AD_Reference_ID=131 */
	public static final int DOCSTATUS_AD_Reference_ID=131;
	/** Drafted = DR */
	public static final String DOCSTATUS_Drafted = "DR";
	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";
	/** In Progress = IP */
	public static final String DOCSTATUS_InProgress = "IP";
	/** Reject = RE */
	public static final String DOCSTATUS_Reject = "RE";
	/** Set Document Status.
		@param DocStatus 
		The current status of the document
	  */
	public void setDocStatus (String DocStatus)
	{

		set_Value (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	public String getDocStatus () 
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
	}

	/** Set EndDateCurrent.
		@param EndDateCurrent EndDateCurrent	  */
	public void setEndDateCurrent (Timestamp EndDateCurrent)
	{
		set_Value (COLUMNNAME_EndDateCurrent, EndDateCurrent);
	}

	/** Get EndDateCurrent.
		@return EndDateCurrent	  */
	public Timestamp getEndDateCurrent () 
	{
		return (Timestamp)get_Value(COLUMNNAME_EndDateCurrent);
	}

	/** Set EndDateOriginal.
		@param EndDateOriginal EndDateOriginal	  */
	public void setEndDateOriginal (Timestamp EndDateOriginal)
	{
		set_Value (COLUMNNAME_EndDateOriginal, EndDateOriginal);
	}

	/** Get EndDateOriginal.
		@return EndDateOriginal	  */
	public Timestamp getEndDateOriginal () 
	{
		return (Timestamp)get_Value(COLUMNNAME_EndDateOriginal);
	}

	/** Set Guarantee Date.
		@param GuaranteeDate 
		Date when guarantee expires
	  */
	public void setGuaranteeDate (Timestamp GuaranteeDate)
	{
		set_Value (COLUMNNAME_GuaranteeDate, GuaranteeDate);
	}

	/** Get Guarantee Date.
		@return Date when guarantee expires
	  */
	public Timestamp getGuaranteeDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_GuaranteeDate);
	}

	/** Set Inventory No.
		@param InventoryNo Inventory No	  */
	public void setInventoryNo (String InventoryNo)
	{
		set_Value (COLUMNNAME_InventoryNo, InventoryNo);
	}

	/** Get Inventory No.
		@return Inventory No	  */
	public String getInventoryNo () 
	{
		return (String)get_Value(COLUMNNAME_InventoryNo);
	}

	/** Set Depreciate.
		@param IsDepreciated 
		The asset will be depreciated
	  */
	public void setIsDepreciated (boolean IsDepreciated)
	{
		set_Value (COLUMNNAME_IsDepreciated, Boolean.valueOf(IsDepreciated));
	}

	/** Get Depreciate.
		@return The asset will be depreciated
	  */
	public boolean isDepreciated () 
	{
		Object oo = get_Value(COLUMNNAME_IsDepreciated);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Disposed.
		@param IsDisposed 
		The asset is disposed
	  */
	public void setIsDisposed (boolean IsDisposed)
	{
		set_Value (COLUMNNAME_IsDisposed, Boolean.valueOf(IsDisposed));
	}

	/** Get Disposed.
		@return The asset is disposed
	  */
	public boolean isDisposed () 
	{
		Object oo = get_Value(COLUMNNAME_IsDisposed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsRecordUsed.
		@param IsRecordUsed IsRecordUsed	  */
	public void setIsRecordUsed (boolean IsRecordUsed)
	{
		set_Value (COLUMNNAME_IsRecordUsed, Boolean.valueOf(IsRecordUsed));
	}

	/** Get IsRecordUsed.
		@return IsRecordUsed	  */
	public boolean isRecordUsed () 
	{
		Object oo = get_Value(COLUMNNAME_IsRecordUsed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set RemainAmt.
		@param RemainAmt RemainAmt	  */
	public void setRemainAmt (BigDecimal RemainAmt)
	{
		set_Value (COLUMNNAME_RemainAmt, RemainAmt);
	}

	/** Get RemainAmt.
		@return RemainAmt	  */
	public BigDecimal getRemainAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_RemainAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Revaluation Date.
		@param RevalDate Revaluation Date	  */
	public void setRevalDate (Timestamp RevalDate)
	{
		set_Value (COLUMNNAME_RevalDate, RevalDate);
	}

	/** Get Revaluation Date.
		@return Revaluation Date	  */
	public Timestamp getRevalDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_RevalDate);
	}

	/** Set Serial No.
		@param SerNo 
		Product Serial Number 
	  */
	public void setSerNo (String SerNo)
	{
		set_Value (COLUMNNAME_SerNo, SerNo);
	}

	/** Get Serial No.
		@return Product Serial Number 
	  */
	public String getSerNo () 
	{
		return (String)get_Value(COLUMNNAME_SerNo);
	}

	/** None = NO */
	public static final String STATUSUSE_None = "NO";
	/** Using = US */
	public static final String STATUSUSE_Using = "US";
	/** UnUse = UN */
	public static final String STATUSUSE_UnUse = "UN";
	/** Set StatusUse.
		@param StatusUse StatusUse	  */
	public void setStatusUse (String StatusUse)
	{

		set_Value (COLUMNNAME_StatusUse, StatusUse);
	}

	/** Get StatusUse.
		@return StatusUse	  */
	public String getStatusUse () 
	{
		return (String)get_Value(COLUMNNAME_StatusUse);
	}

	/** By Day = BD */
	public static final String TYPECALCULATE_ByDay = "BD";
	/** By Month = BM */
	public static final String TYPECALCULATE_ByMonth = "BM";
	/** Set TypeCalculate.
		@param TypeCalculate TypeCalculate	  */
	public void setTypeCalculate (String TypeCalculate)
	{

		set_Value (COLUMNNAME_TypeCalculate, TypeCalculate);
	}

	/** Get TypeCalculate.
		@return TypeCalculate	  */
	public String getTypeCalculate () 
	{
		return (String)get_Value(COLUMNNAME_TypeCalculate);
	}

	/** Set UseDate.
		@param UseDate UseDate	  */
	public void setUseDate (Timestamp UseDate)
	{
		set_Value (COLUMNNAME_UseDate, UseDate);
	}

	/** Get UseDate.
		@return UseDate	  */
	public Timestamp getUseDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_UseDate);
	}

	/** Set UseLifes.
		@param UseLifes UseLifes	  */
	public void setUseLifes (BigDecimal UseLifes)
	{
		set_Value (COLUMNNAME_UseLifes, UseLifes);
	}

	/** Get UseLifes.
		@return UseLifes	  */
	public BigDecimal getUseLifes () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_UseLifes);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set Version No.
		@param VersionNo 
		Version Number
	  */
	public void setVersionNo (String VersionNo)
	{
		set_Value (COLUMNNAME_VersionNo, VersionNo);
	}

	/** Get Version No.
		@return Version Number
	  */
	public String getVersionNo () 
	{
		return (String)get_Value(COLUMNNAME_VersionNo);
	}
}