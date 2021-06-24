package eone.base.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.compiere.util.DB;
import org.compiere.util.Env;

public class MPromotionLine extends X_M_PromotionLine
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1247516669047870893L;

	public MPromotionLine (Properties ctx, int M_PromotionLine_ID, String trxName)
	{
		super (ctx, M_PromotionLine_ID, trxName);
		
	}	//	MAssetUse

	
	@Override
	protected boolean beforeSave(boolean newRecord) {
		
		//getContext
		String requiredQty = Env.getContext(getCtx(), "#REQUIED_QTY");
		String requiredAmt = Env.getContext(getCtx(), "#REQUIED_AMT");
		String requiredGif = Env.getContext(getCtx(), "#REQUIED_GIF");
		String requiredPer = Env.getContext(getCtx(), "#REQUIED_PER");
		String requiredDis = Env.getContext(getCtx(), "#REQUIED_DIS");
		
		//MPromotion pro = MPromotion.get(getCtx(), getM_Promotion_ID());
		//MPromotionType proType = MPromotionType.getById(getCtx(), pro.getM_PromotionType_ID(), null);
		
		
		
		//Kiểm tra xem số lượng có phải là kiểu số hay không. DO số lượng là "số" hoặc "số,số,sô" nên phải như này.
		if (newRecord || is_ValueChanged(COLUMNNAME_QtyMultiplier) || is_ValueChanged(COLUMNNAME_M_Product_ID) || is_ValueChanged(COLUMNNAME_Amount)) {
			
			if("Y".equals(requiredQty) && !getQtyMultiplier().matches("^[\\d,]+$")) {
				log.saveError("Save Error!", "Số lượng không đúng định dạng!");
				return false;
			}
			
			if("Y".equals(requiredAmt) && !getAmount().matches("^[\\d,]+$")) {
				log.saveError("Save Error!", "Số tiền không đúng định dạng!");
				return false;
			}
			
			if (is_ValueChanged(COLUMNNAME_M_Product_ID) && getM_Product_ID() != null) {
				String [] listCheck = getM_Product_ID().split(",");
				if (!checkExistsProduct(listCheck)) {
					log.saveError("Save Error!", "Sản phẩm đã tồn tại trong kịch bản khuyến mại khác rồi");
					return false;
				}
			}
			
			String [] listProduct = null;
			if(getM_Product_ID() != null)
				listProduct = getM_Product_ID().split(",");
			String [] listQty = null;
			if (getQtyMultiplier() != null)
				listQty = getQtyMultiplier().split(",");
			String [] listAmt = null;
			if(getAmount() != null)
				listAmt = getAmount().split(",");
			if ("Y".equals(requiredQty) && listProduct != null && listQty != null && listProduct.length != listQty.length) {
				log.saveError("Save Error!", "Số lượng và sản phẩm không tương ứng từng cặp!");
				return false;
			}
			
			if ("Y".equals(requiredAmt) && listProduct != null && listAmt!= null && listProduct.length != listAmt.length) {
				log.saveError("Save Error!", "Số tiền và sản phẩm không tương ứng từng cặp!");
				return false;
			}
		}
		
		if (newRecord || is_ValueChanged(COLUMNNAME_QtyFree) || is_ValueChanged(COLUMNNAME_M_Product_Free_ID)) {
			if("Y".equals(requiredGif) && !getQtyFree().matches("^[\\d,]+$")) {
				log.saveError("Save Error!", "Số lượng khuyến mại không đúng định dạng!");
				return false;
			}
			
			String [] listProduct = null;
			if(getM_Product_Free_ID() != null)
				listProduct = getM_Product_Free_ID().split(",");
			String [] listQty = null;
			if(getQtyFree()!= null )
				listQty = getQtyFree().split(",");
			if (listProduct != null && listQty != null && listProduct.length != listQty.length) {
				log.saveError("Save Error!", "Số lượng và sản phẩm khuyến mại không tương ứng từng cặp!");
				return false;
			}
		}
		
		if ("N".equals(requiredQty))
			setQtyMultiplier(null);
		if ("N".equals(requiredAmt))
			setAmount(null);
		if ("N".equals(requiredPer))
			setDiscountPercent(null);
		if ("N".equals(requiredDis))
			setDiscountAmt(null);
		if ("N".equals(requiredGif)) {
			setQtyFree(null);
			setM_Product_Free_ID(null);
		}
		
		if ("Y".equals(requiredPer) && (getDiscountPercent().compareTo(Env.ZERO) <= 0 ||  getDiscountPercent().compareTo(Env.ONEHUNDRED) > 0)) {
			log.saveError("Save Error!", "Phần trăm phải thuộc phạm vi từ 0 đến 100 !");
			return false;
		}
		
		if ("Y".equals(requiredDis) && getDiscountAmt().compareTo(Env.ZERO) <= 0) {
			log.saveError("Save Error!", "Giá trị chiết khấu phải lớn hơn 0 !");
			return false;
		}
		
		return true;
	}

	protected boolean checkExistsProduct(String [] listInput) {
		String sql = "Select M_Product_ID From M_PromotionLine Where M_Promotion_ID = ? And M_PromotionLine_ID != ? And M_PromotionLine_ID > 0";
		PreparedStatement ps = DB.prepareCall(sql);
		ResultSet rs = null;
		try {
			ps.setInt(1, getM_Promotion_ID());
			ps.setInt(2, getM_PromotionLine_ID());
			rs = ps.executeQuery();
			while (rs.next()) {
				String [] arr = rs.getString("M_Product_ID").split(",");
				List<String> arrList = new ArrayList<String>(Arrays.asList(arr));
				for(int i = 0; i < listInput.length; i++) {
					if (arrList.contains(listInput[i]))
						return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs, ps);
			rs = null;
			ps = null;
		}
		return true;
	}

	public MPromotionLine (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MAssetUse


	protected boolean afterSave (boolean newRecord,boolean success)
	{
		log.info ("afterSave");
		if (!success)
			return success;
		
		return success;
		 
		
	}	//	afterSave



}
