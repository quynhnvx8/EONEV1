package org.compiere.util;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Properties;

import eone.base.model.GridTab;
import eone.base.model.PO;

public class CalloutUtil 
{
	public static String getDocumentNo(Properties ctx, PO po, int WindowNo, GridTab mTab)
	{
		return getCalloutValue(ctx, po, WindowNo, mTab, "DocumentNo");
	}
	
	public static String getValue(Properties ctx, PO po, int WindowNo, GridTab mTab)
	{
		return getCalloutValue(ctx, po, WindowNo, mTab, "Value");
	}
	

	private static CCache<Integer, Hashtable<String, KeyNamePair>> m_docNoElementValues 
					= new CCache<Integer, Hashtable<String, KeyNamePair>>("Callout_DocumentNo", 100,  CCache.DEFAULT_TIMEOUT);
	
	
	private static String getCalloutValue(Properties ctx, PO po, int WindowNo, GridTab mTab, String dataColumnName)
	{
		int columnIndex = 0;
		
		int C_DocType_ID = 0;
		String p_calloutFormat = null;
		String p_dateColumn = null;
		
		boolean withHistory = true;
		boolean checkOldValue = true;
		boolean allowGetContext = false;
		
		if (mTab != null)
		{
			Object m_DocType_ID = mTab.getValue("C_DocType_ID");
			if (m_DocType_ID != null && m_DocType_ID instanceof Integer)
				C_DocType_ID = ((Integer)m_DocType_ID).intValue();
		} else if (po != null) {
			columnIndex = po.get_ColumnIndex("C_DocType_ID");
			if (columnIndex >= 0)
				C_DocType_ID = po.get_ValueAsInt(columnIndex);
		}
		
		int p_param_ID = 0;
		p_param_ID = C_DocType_ID;
		String sql = "SELECT d.DocBaseType, d.DateColumn, d.DocNoFormat "	//	3
					+ " FROM C_DocType d "
					+ " WHERE C_DocType_ID=?";													//	#1
			
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, p_param_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				p_dateColumn = rs.getString(2);
				p_calloutFormat = rs.getString(3);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; 
			pstmt = null;
		}
		
		if (p_calloutFormat == null || p_calloutFormat.length() == 0)
			return "";
		
		String p_calloutValue = parseDisplayFormat(ctx, po, C_DocType_ID, WindowNo, mTab, 
										dataColumnName, p_calloutFormat, p_dateColumn, 
										withHistory, checkOldValue, allowGetContext);
		return p_calloutValue;
	}
	
	
	public static String getCalloutValue(Properties ctx, PO po, int WindowNo, int C_DocType_ID, String dataColumnName)
	{
		
		String p_calloutFormat = null;
		String p_dateColumn = null;
		
		boolean withHistory = true;
		boolean checkOldValue = true;
		boolean allowGetContext = false;
		
		String sql = "SELECT d.DocBaseType, d.DateColumn, d.DocNoFormat "	//	3
					+ " FROM C_DocType d "
					+ " WHERE C_DocType_ID=?";													//	#1
			
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_DocType_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				p_dateColumn = rs.getString(2);
				p_calloutFormat = rs.getString(3);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; 
			pstmt = null;
		}
		
		if (p_calloutFormat == null || p_calloutFormat.length() == 0)
			return "";
		
		String p_calloutValue = parseDisplayFormat(ctx, po, C_DocType_ID, WindowNo, null, 
										dataColumnName, p_calloutFormat, p_dateColumn, 
										withHistory, checkOldValue, allowGetContext);
		return p_calloutValue;
	}
	
	@SuppressWarnings("deprecation")
	private static String parseDisplayFormat(Properties ctx, PO po, int C_DocType_ID, 
			int WindowNo, GridTab mTab, 
			String dataColumnName, String p_elementFormat, String p_dateColumn, 
			boolean withHistory, boolean checkOldValue, boolean allowGetContext)
	{
		int AD_Client_ID = Env.getAD_Client_ID(ctx);
		
		String p_dataTableName = null;
		String p_documentNo = null;
		int p_Record_ID = 0;
		int columnIndex = 0;
		
		if (mTab != null) {
			p_dataTableName = mTab.getTableName();
			p_documentNo = mTab.get_ValueAsString(dataColumnName);
			p_Record_ID = mTab.getRecord_ID();
		} else if (po != null) {
			p_dataTableName = po.get_TableName();
			p_Record_ID = po.get_ID();
			columnIndex = po.get_ColumnIndex(dataColumnName);
			if (columnIndex >= 0)
				p_documentNo = po.get_ValueAsString(columnIndex);
		}
		
		boolean isCheckOldDocNo = (checkOldValue && p_Record_ID > 0 && p_documentNo != null && p_documentNo.length() > 0);
		Timestamp p_dateValue = null;
		
		if (p_dateColumn != null && p_dateColumn.length() > 0) {
			if (mTab != null) {
				Object dateValue = mTab.getValue(p_dateColumn);
				if (dateValue != null && dateValue instanceof Timestamp)
					p_dateValue = (Timestamp)dateValue;
				else if (allowGetContext)
					p_dateValue = Env.getContextAsDate(ctx, WindowNo, p_dateColumn);
			} else if (po != null) {
				columnIndex = po.get_ColumnIndex(p_dateColumn);
				if (columnIndex >= 0) {
					Object dateValue = po.get_Value(columnIndex);
					if (dateValue != null && dateValue instanceof Timestamp)
						p_dateValue = (Timestamp)dateValue;
				}
			}
		}
		
		StringBuffer doc = new StringBuffer();
		Hashtable<String, Object> p_Values = new Hashtable<String, Object>();
		
		//	History
		Hashtable<String, KeyNamePair> p_elementValues = null;
		boolean isChangeElement = false;
		boolean isHasElementValue = false;
		// Patten for calculate auto Number
		StringBuffer docPatten = new StringBuffer();
		if (withHistory) {
			isHasElementValue = m_docNoElementValues.containsKey(C_DocType_ID);
			if (isHasElementValue) {
				p_elementValues = m_docNoElementValues.get(C_DocType_ID);
			} else {
				p_elementValues = new Hashtable<String, KeyNamePair>();
				isChangeElement = true;
			}
		}
		//	End History
		
		int m_xNumberIndex = -1;
		String m_xNumber = null;
		
		int index = p_elementFormat.indexOf("{");
		boolean isNoChangeDocumentNo = false;
		
		while (index >= 0 && !isNoChangeDocumentNo) {
			int index2 = p_elementFormat.indexOf("}", index + 1);
			if (index2 < 0)
				break;

			if (index > 0) {
				doc.append(p_elementFormat.substring(0, index));
				docPatten.append(p_elementFormat.substring(0, index));
			}

			String p_fieldName = p_elementFormat.substring(index + 1, index2);
			p_elementFormat = p_elementFormat.substring(index2 + 1);

			if (p_fieldName != null && p_fieldName.length() > 0) {
				String p_tableName = null; // Ten bang lien ket
				String p_keyColumn = null; // Ten cot khoa cua bang lien ket
				String p_valueColumn = null; // Ten cot gia tri bang lien ket
				String p_format = null; // Dinh dang hien thi gia tri

				// Cac tham so de cat du lieu
				int p_valueStartIndex = -1; // Tu vi tri
				int p_valueEndIndex = -1; // Den vi tri
				int p_valueStartLength = -1; // So luong ky tu dau tien
				int p_valueEndLength = -1; // So luong ky tu cuoi cung

				// Tang so tu tang theo gia tri nay?
				boolean isIncrease = p_fieldName.endsWith("+");
				if (isIncrease)
					p_fieldName = p_fieldName.substring(0, p_fieldName.length() - 1);

				

				String p_selectValue = null;

				if (p_fieldName.endsWith("_ID")) {
					Object p_value = null;
					if (mTab != null) {
						p_value = mTab.getValue(p_fieldName);
						if (p_value == null && allowGetContext)
							p_value = Env.getContextAsInt(ctx, WindowNo, p_fieldName);
					} else if (po != null) {
						columnIndex = po.get_ColumnIndex(p_fieldName);
						if (columnIndex >= 0)
							p_value = po.get_Value(columnIndex);
					}

					if (p_value != null) {

						

						if (p_tableName == null || p_tableName.length() == 0)
							p_tableName = p_fieldName.substring(0, p_fieldName.length() - 3);
						
						if (p_keyColumn == null || p_keyColumn.length() == 0)
							p_keyColumn = p_tableName + "_ID";
						
						if ("C_DocType".equalsIgnoreCase(p_tableName))
							p_valueColumn = "Name";
						else if (p_valueColumn == null
								|| p_valueColumn.length() == 0)
							p_valueColumn = "Value";

						if (!p_keyColumn.endsWith("_ID")
								|| p_value instanceof Integer) {
							int p_intValue = ((Integer) p_value).intValue();
							if (p_intValue > 0) {
								

								// History
								boolean isHasElement = false;
								if (withHistory) {
									isHasElement = p_elementValues.containsKey(p_fieldName);
									if (isHasElement) {
										KeyNamePair p_elementValue = p_elementValues.get(p_fieldName);
										if (p_elementValue.getKey() == p_intValue)
											p_selectValue = p_elementValue.getName();
									}
								}
								// End History

								if (!isHasElement || p_selectValue == null
										|| p_selectValue.length() == 0) {
									String sqlSelect = "SELECT " + p_valueColumn 
													+ " FROM " + p_tableName 
													+ " WHERE " + p_keyColumn + "=?";
									p_selectValue = DB.getSQLValueString(null, sqlSelect, new Object[] { p_intValue });
									// History
									if (withHistory) {
										if (isHasElement)
											p_elementValues.remove(p_fieldName);
										
										p_elementValues.put(p_fieldName, new KeyNamePair(p_intValue, p_selectValue));
										isChangeElement = true;
									}
									// End History
								}
							}
						}
					}
				} else if (p_fieldName.equalsIgnoreCase("y")
						|| p_fieldName.equalsIgnoreCase("yy")
						|| p_fieldName.equalsIgnoreCase("yyy")
						|| p_fieldName.equalsIgnoreCase("yyyy")) {
					if (p_dateValue != null) {
						p_selectValue = "" + (p_dateValue.getYear() + 1900);
						p_selectValue = p_selectValue.substring(p_selectValue.length() - p_fieldName.length());

						if (isIncrease)
							p_Values.put("YYYY", p_dateValue.getYear() + 1900);
					}
				} else if (p_fieldName.equalsIgnoreCase("m")
						|| p_fieldName.equalsIgnoreCase("mm")) {
					if (p_dateValue != null) {
						p_selectValue = "00" + (p_dateValue.getMonth() + 1);
						p_selectValue = p_selectValue.substring(p_selectValue.length() - p_fieldName.length());

						if (isIncrease)
							p_Values.put("MM", p_dateValue.getMonth() + 1);
					}
				} else if ((p_fieldName.startsWith("X") 
						|| p_fieldName.startsWith("x")) && p_fieldName.indexOf("_") < 0) {
					m_xNumber = p_fieldName;
					m_xNumberIndex = doc.length();
				}
				// Cac truong du lieu binh thuong
				else {
					String[] fieldFormats = p_fieldName.split("\\|");
					p_fieldName = fieldFormats[0];
					if (fieldFormats.length > 1)
						p_format = fieldFormats[1];

					Object p_value = null;
					if (mTab != null) {
						p_value = mTab.getValue(p_fieldName);
						if (p_value == null && allowGetContext)
							p_value = Env.getContext(ctx, WindowNo, p_fieldName);
					} else if (po != null) {
						columnIndex = po.get_ColumnIndex(p_fieldName);
						if (columnIndex >= 0)
							p_value = po.get_Value(columnIndex);
					}
					if (p_value != null) {
						if (isIncrease)
							p_Values.put(p_fieldName, p_value);
						
						p_selectValue = formatValue(p_value, p_format);
					}
				}

				if (!isNoChangeDocumentNo 
						&& p_selectValue != null
						&& p_selectValue.length() > 0) {
					// Cat ku tu
					int p_valueLength = p_selectValue.length();
					if (p_valueStartIndex >= 0) {
						if (p_valueEndIndex < p_valueStartIndex)
							p_selectValue = "";
						else if (p_valueLength <= p_valueStartIndex)
							p_selectValue = "";
						else {
							if (p_valueEndIndex > p_valueLength)
								p_valueEndIndex = p_valueLength;

							p_selectValue = p_selectValue.substring(p_valueStartIndex, p_valueEndIndex);
						}
					} else if (p_valueStartLength >= 0) {
						if (p_valueStartLength > p_valueLength)
							p_valueStartLength = p_valueLength;

						p_selectValue = p_selectValue.substring(0, p_valueStartLength);
					} else if (p_valueEndLength >= 0) {
						if (p_valueEndLength > p_valueLength)
							p_valueEndLength = p_valueLength;

						p_selectValue = p_selectValue.substring(p_valueLength - p_valueEndLength);
					}
					if (p_selectValue.length() > 0) {
						doc.append(p_selectValue);
						if (isIncrease) {
							docPatten.append(p_selectValue);
						} else {
							docPatten.append("%");
						}
					}
				}
			}

			index = p_elementFormat.indexOf("{");
		}

		// Tinh so tu tang
		if (m_xNumberIndex >= 0 && m_xNumber != null && m_xNumber.length() > 0) {
			String sqlSelectDocNo = null;
			ArrayList<Object> p_docNoParams = null;

			if (checkOldValue && isCheckOldDocNo) {
				sqlSelectDocNo = "SELECT " + dataColumnName 
							  + " FROM " + p_dataTableName 
							  + " WHERE " + p_dataTableName + "_ID=?";
				p_docNoParams = new ArrayList<Object>();
				p_docNoParams.add(p_Record_ID);
			}

			String sqlSelect = "SELECT NVL(TO_NUMBER(MAX(SUBSTR("
					+ dataColumnName + ", " + (m_xNumberIndex + 1) + ", "
					+ m_xNumber.length() + "))), 0) FROM " + p_dataTableName
					+ " WHERE AD_Client_ID=" + AD_Client_ID
					+ "		AND IsActive='Y'";
			if (DB.isPostgreSQL()) {
				sqlSelect = "SELECT NVL(TO_NUMBER(MAX(SUBSTR("
						+ dataColumnName + ", " + (m_xNumberIndex + 1) + ", "
						+ m_xNumber.length() + ")),'9999.99'), 0) FROM " + p_dataTableName
						+ " WHERE AD_Client_ID=" + AD_Client_ID
						+ "		AND IsActive='Y'";
			}
			ArrayList<Object> p_params = new ArrayList<Object>();
			

			// Lay so tu dong
			if (p_Values.size() > 0) {
				boolean hasDate = false;

				Enumeration<String> keys = p_Values.keys();
				while (keys.hasMoreElements()) {
					String key = keys.nextElement();
					if (key.equalsIgnoreCase("YYYY")
							|| key.equalsIgnoreCase("MM")) {
						if (!hasDate && p_dateColumn != null
								&& p_dateColumn.length() > 0) {
							if (p_Values.containsKey("YYYY")) {
								int p_year = ((Integer) p_Values.get("YYYY")).intValue();
								int p_fromMonth = 1;
								int p_toMonth = 12;

								if (p_Values.containsKey("MM")) {
									p_fromMonth = ((Integer) p_Values.get("MM")).intValue();
									p_toMonth = p_fromMonth;
								}

								hasDate = true;
								Timestamp p_fromDate = new Timestamp(p_year - 1900, p_fromMonth - 1, 1, 0, 0, 0, 0);
								Timestamp p_toDate = new Timestamp(p_year - 1900, p_toMonth - 1, 1, 0, 0, 0, 0);

								Calendar cal = GregorianCalendar.getInstance();
								cal.setTime(p_toDate);
								cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
								p_toDate.setTime(cal.getTimeInMillis());

								sqlSelect += " AND (" + p_dateColumn
										+ " BETWEEN ? AND ?)";
								p_params.add(p_fromDate);
								p_params.add(p_toDate);

								if (checkOldValue && isCheckOldDocNo) {
									sqlSelectDocNo += " AND (" + p_dateColumn
											+ " BETWEEN ? AND ?)";
									p_docNoParams.add(p_fromDate);
									p_docNoParams.add(p_toDate);
								}
							}
						}
					} else {
						sqlSelect += " AND " + key + "=?";
						p_params.add(p_Values.get(key));

						if (checkOldValue && isCheckOldDocNo) {
							sqlSelectDocNo += " AND " + key + "=?";
							p_docNoParams.add(p_Values.get(key));
						}
					}
				}
			}
			/*
			 * Kiem tra gia tri
			 */
			if (docPatten != null) {
				docPatten.append("%");
				sqlSelect += " AND " + dataColumnName + " LIKE ?";
				p_params.add(docPatten.toString());
			}
			// Kiem tra gia tri cap nhat lan truoc
			if (checkOldValue && isCheckOldDocNo 
					&& sqlSelectDocNo != null
					&& sqlSelectDocNo.length() > 0) {
				String p_docNo = DB.getSQLValueString(null, sqlSelectDocNo, p_docNoParams);
				if (p_docNo != null && p_docNo.length() > 0) {
					isNoChangeDocumentNo = true;
					doc = new StringBuffer(p_docNo);
				}
			}

			if (!isNoChangeDocumentNo) {
				BigDecimal value = null;
				try {
					value = DB.getSQLValueBDEx(null, sqlSelect, p_params);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				int p_value = 0;
				if (value != null) {
					p_value = value.intValue();
				}
				p_value++;
				if (p_value <= 0)
					p_value = 1;

				int len = m_xNumber.length();
				String p_selectValue = null;

				// Kiem tra su ton tai cua so chung tu, moi lan 10 so
				int size = 10;
				String currentDocNo = doc.toString();
				String[] docNoList = new String[size];

				while (p_selectValue == null) {
					boolean notHaveDocNo = true;
					String sql = "SELECT UPPER(" + dataColumnName + ") FROM "
							+ p_dataTableName + " WHERE AD_Client_ID=?"
							+ " AND " + dataColumnName
							+ " IN (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
							+ " ORDER BY " + dataColumnName + " ASC";

					ArrayList<String> listValues = new ArrayList<String>();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = DB.prepareStatement(sql, null);
						pstmt.setInt(1, AD_Client_ID);

						for (int i = 0; i < size; i++) {
							StringBuffer tmpDoc = new StringBuffer(currentDocNo);
							String tmpValue = "" + (p_value + i);
							while (tmpValue.length() < len)
								tmpValue = "0" + tmpValue;

							tmpDoc.insert(m_xNumberIndex, tmpValue);
							docNoList[i] = tmpDoc.toString().toUpperCase();

							pstmt.setString(2 + i, docNoList[i]);
						}

						rs = pstmt.executeQuery();
						while (rs.next())
							listValues.add(rs.getString(1));
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						DB.close(rs, pstmt);
						rs = null;
						pstmt = null;
					}

					notHaveDocNo = (listValues.size() == 0);
					if (!notHaveDocNo) {
						String[] existValues = new String[listValues.size()];
						listValues.toArray(existValues);

						int docIndex = searchStringArray(docNoList, existValues);
						if (docIndex >= 0) {
							p_selectValue = "" + (p_value + docIndex);
							while (p_selectValue.length() < len)
								p_selectValue = "0" + p_selectValue;
						}
					}

					if (notHaveDocNo) {
						p_selectValue = "" + p_value;
						while (p_selectValue.length() < len)
							p_selectValue = "0" + p_selectValue;
						break;
					}

					p_value += 10;
				}
				
				doc.insert(m_xNumberIndex, p_selectValue);
			}
		}

		// History
		if (withHistory && isChangeElement) {
			if (isHasElementValue)
				m_docNoElementValues.remove(C_DocType_ID);
			m_docNoElementValues.put(C_DocType_ID, p_elementValues);
		}
		// End History

		return doc.toString();
	}
	
	//	Return first index of difference target in source
	private static int searchStringArray(String[] source, String[] target) 
	{
		if (source == null || target == null)
			return -1;

		for (int i = 0; i < source.length; i++) {
			boolean isInTarget = false;
			for (int j = 0; j < target.length; j++) {
				isInTarget = source[i].equalsIgnoreCase(target[j]);
				if (isInTarget)
					break;
			}

			if (!isInTarget)
				return i;
		}

		return -1;
	}
	
	@SuppressWarnings("deprecation")
	public static BigDecimal RoundAmount(BigDecimal input, BigDecimal rate) {
		BigDecimal output = Env.ZERO;
		if (rate == null) {
			rate = Env.ONE;
		}
		if (rate.compareTo(Env.ONE) <= 0) {
			output = input.setScale(0, BigDecimal.ROUND_HALF_UP);
		} else {
			output = input.setScale(2, BigDecimal.ROUND_HALF_UP);			
		}
		return output;
	}
	
	private static String formatValue(Object p_value, String p_format) 
	{
		if (p_value == null)
			return null;

		int displayType = 0;
		if (p_format != null && p_format.length() > 0) {
			if (p_format.equalsIgnoreCase("DateTime"))
				displayType = DisplayType.DateTime;
			if (p_format.equalsIgnoreCase("Date"))
				displayType = DisplayType.Date;
			else if (p_format.equalsIgnoreCase("Time"))
				displayType = DisplayType.Time;
			else if (p_format.equalsIgnoreCase("Amount"))
				displayType = DisplayType.Amount;
			else if (p_format.equalsIgnoreCase("Number"))
				displayType = DisplayType.Number;
			else if (p_format.equalsIgnoreCase("Quantity"))
				displayType = DisplayType.Quantity;
			else if (p_format.equalsIgnoreCase("CostPrice"))
				displayType = DisplayType.CostPrice;
		}

		SimpleDateFormat p_dateFormat = null;
		DecimalFormat p_numberFormat = null;

		if (DisplayType.isDate(displayType)) {
			p_dateFormat = DisplayType.getDateFormat(displayType);

			Timestamp p_timeValue = null;
			if (!(p_value instanceof Timestamp)) {
				try {
					Date m_dateValue = p_dateFormat.parse(p_value.toString());
					p_timeValue = new Timestamp(m_dateValue.getTime());
				} catch (Exception ex) {
					// Hungtq24 added on 03/04/2014
					ex.printStackTrace();
				}
			} else
				p_timeValue = (Timestamp) p_value;

			if (p_timeValue != null)
				return p_dateFormat.format(p_timeValue);
		} else if (DisplayType.isNumeric(displayType)) {
			p_numberFormat = DisplayType.getNumberFormat(displayType);

			Object p_numberValue = null;
			if (!(p_value instanceof Integer || p_value instanceof Double || p_value instanceof BigDecimal)) {
				try {
					p_numberValue = new BigDecimal(p_value.toString());
				} catch (Exception ex) {
				}
			} else
				p_numberValue = p_value;

			if (p_numberValue != null)
				return p_numberFormat.format(p_numberValue);
		} else if (p_value instanceof Timestamp) {
			if (displayType == 0)
				displayType = DisplayType.Date;

			p_dateFormat = DisplayType.getDateFormat(displayType);
			return p_dateFormat.format((Timestamp) p_value);
		} else if (p_value instanceof Integer || p_value instanceof Double
				|| p_value instanceof BigDecimal) {
			if (displayType == 0)
				displayType = DisplayType.Number;

			p_numberFormat = DisplayType.getNumberFormat(displayType);
			return p_numberFormat.format(p_value);
		}

		return p_value.toString();
	}
	
	public static String getDocumentNoByColumn(Properties ctx, PO po, int WindowNo, GridTab mTab, String columnName)
	{
		return getCalloutValue(ctx, po, WindowNo, mTab, columnName);
	}
}
