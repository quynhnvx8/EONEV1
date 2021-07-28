
package eone.base.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;


public class MStorage extends X_M_Storage
{
	private static final long serialVersionUID = 7980515458720808532L;


	public static MStorage get (Properties ctx)
	{
		final String whereClause = "1=1";
		MStorage retValue =  new Query(ctx,I_M_Storage.Table_Name,whereClause,null)
		.first();
		return retValue;
	}	//	get
	
	//private static CLogger		s_log = CLogger.getCLogger (MAccount.class);
	
	public static String sqlInsert = PO.getSqlInsert(X_M_Storage.Table_ID, null);
	public static String sqlUpdate = PO.getSqlUpdate(X_M_Storage.Table_ID);

	public MStorage (Properties ctx, int M_Storage_ID, String trxName)
	{
		super (ctx, M_Storage_ID, trxName);
		
	}   //  MAccount


	public MStorage (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}   //  MAccount


	public MStorage ()
	{
		this (Env.getCtx(), 0, null);
	}	//	Account


	public String toString()
	{
		return "";
	}	//	toString

	
	
	protected boolean beforeSave (boolean newRecord)
	{
		
		return true;
	}	//	beforeSave
	
	//private static CCache<Integer,MStorage> s_cache	= new CCache<Integer,MStorage>(Table_Name, 40, CCache.DEFAULT_TIMEOUT);

	/*
	 * DateTrx: 		Ngay phat sinh giao dich => Lay ngay tu bang M_InOut
	 * Qty: 			So luong giao dich => Lay bang M_InOutLine
	 * RemainQty: 		So luong con lai => Tu dong tinh
	 * TypeInOut: 		Trang thai nhap hay xuat => Tu dong xac dinh
	 * Record_ID: 		Tuong ung truong M_InoutLine_ID
	 * 
	 */
	
	/*
	 * Ham nay check ton kho cua vat tu tai ngay xuat (ko phai ton kho tai thoi diem hien tai)
	 * Luu y:	Neu khong cau hinh check ton kho thi bo qua.
	 */
	public static BigDecimal getQtyRemain(int M_Product_ID, int M_Warehouse_ID, Timestamp DateTrx) {
		List<Object> params = new ArrayList<Object>();
		params.add(Env.getAD_Client_ID(Env.getCtx()));
		Timestamp startDate = DB.getSQLValueTS(null, "Select Max(StartDate) From C_Year Where StatusBalance = 'Y' And AD_Client_ID = ?", params);
		
		if (startDate == null) {
			startDate = TimeUtil.getDay(2000, 1, 1);
		}
		String sql = 
				" Select Sum(Qty) Qty"+
				" From "+
				" ( "+
				" 	select Sum(Qty) Qty, Sum(Amount) Amount from M_Storage "+ 
				" 	Where (TypeInOut in ('IN','OP')) And DateTrx >= ? And DateTrx <= ? And M_Warehouse_ID = ? And M_Product_ID = ? "+ //#1,#2,#3,#4
				" 	Union All "+
				" 	select -Sum(Qty) Qty, -Sum(Amount) Amount from M_Storage "+ 
				" 	Where TypeInOut = 'OU' And DateTrx >= ? And DateTrx <= ?  And M_Warehouse_ID = ? And M_Product_ID = ? "+ //#5,#6,#7,#8
				" )B Having Sum(Qty) != 0";
		Object [] param = new Object[] {startDate, DateTrx, M_Warehouse_ID, M_Product_ID, startDate, DateTrx, M_Warehouse_ID, M_Product_ID};
		BigDecimal qty =  DB.getSQLValueBD(null, sql, param);
		if (qty == null)
			qty = Env.ZERO;
		return qty;
	}
	
	public static Object [] getQtyPrice(int M_Product_ID, int M_Warehouse_ID, Timestamp DateTrx) {
		
		List<Object> params = new ArrayList<Object>();
		params.add(Env.getAD_Client_ID(Env.getCtx()));
		Timestamp startDate = DB.getSQLValueTS(null, "Select Max(StartDate) From C_Year Where StatusBalance = 'Y' And AD_Client_ID = ?", params);
		
		if (startDate == null) {
			startDate = TimeUtil.getDay(2000, 1, 1);
		}
		
		String sql = "";
		
		
		String MaterialPolicy = Env.getMaterialPolicy(Env.getCtx());
		
		//Binh quan cuoi ky
		if (MClient.MMPOLICY_Average.equals(MaterialPolicy))
		{
			sql = 
					" Select Sum(Qty) Qty, round(Sum(Amount)/Sum(Qty),4) Price, Sum(Amount) Amount"+
					" From "+
					" ( "+
					" 	select Sum(Qty) Qty, Sum(Amount) Amount from M_Storage "+ 
					" 	Where (TypeInOut in ('IN','OP')) And DateTrx >= ? And DateTrx <= ? And M_Warehouse_ID = ? And M_Product_ID = ? "+ //#1,#2,#3,#4
					" 	Union All "+
					" 	select -Sum(Qty) Qty, -Sum(Amount) Amount from M_Storage "+ 
					" 	Where TypeInOut = 'OU' And DateTrx >= ? And DateTrx <= ?  And M_Warehouse_ID = ? And M_Product_ID = ? "+ //#5,#6,#7,#8
					" )B Having Sum(Qty) != 0";
			Object [] param = new Object[] {startDate, DateTrx, M_Warehouse_ID, M_Product_ID, startDate, DateTrx, M_Warehouse_ID, M_Product_ID};
			return DB.getObjectValuesEx(null, sql, 3, param);
			
			
		}//End binh quan
		
		//FIFO
		if (MClient.MMPOLICY_FiFo.equals(MaterialPolicy))
		{//Phan nay se lam sau (khi nao co don vi ap dung thi lam vi no phuc tap)
			
			
		}//End FIFO
		
		//LIFO
		if (MClient.MMPOLICY_LiFo.equals(MaterialPolicy))
		{
			
		}//End LIFO
		
		return null;
	}
}

