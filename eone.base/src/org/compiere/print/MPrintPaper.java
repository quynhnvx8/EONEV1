
package org.compiere.print;

import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import javax.print.attribute.EnumSyntax;
import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Language;

import eone.base.model.X_AD_PrintPaper;
import eone.exceptions.EONEException;

public class MPrintPaper extends X_AD_PrintPaper
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3609557177958141344L;

	/**
	 * 	Get Paper
	 * 	@param AD_PrintPaper_ID id
	 * 	@return Paper
	 */
	static public MPrintPaper get (int AD_PrintPaper_ID)
	{
		Integer key = Integer.valueOf(AD_PrintPaper_ID);
		MPrintPaper pp = (MPrintPaper)s_papers.get(key);
		if (pp == null)
		{
			pp = new MPrintPaper (Env.getCtx(), AD_PrintPaper_ID, null);
			s_papers.put(key, pp);
		}
		else
			if (s_log.isLoggable(Level.CONFIG)) s_log.config("AD_PrintPaper_ID=" + AD_PrintPaper_ID);
		return pp;
	}	//	get

	/**
	 * 	Create Paper and save
	 * 	@param name name
	 * 	@param landscape landscape
	 * 	@return Paper
	 */
	static MPrintPaper create (String name, boolean landscape)
	{
		MPrintPaper pp = new MPrintPaper (Env.getCtx(), 0, null);
		pp.setName(name);
		pp.setIsLandscape(landscape);
		pp.saveEx();
		return pp;
	}	//	create

	/**	Logger				*/
	private static CLogger s_log = CLogger.getCLogger(MPrintPaper.class);
	/** Cached Fonts						*/
	static private CCache<Integer,MPrintPaper> s_papers 
		= new CCache<Integer,MPrintPaper>(Table_Name, 5);
	
	
	/**************************************************************************
	 *	Constructor
	 *  @param ctx context
	 *  @param AD_PrintPaper_ID ID if 0 A4
	 *  @param trxName transaction
	 */
	public MPrintPaper(Properties ctx, int AD_PrintPaper_ID, String trxName)
	{
		super(ctx, AD_PrintPaper_ID, trxName);
		if (AD_PrintPaper_ID == 0)
		{
			setIsDefault (false);
			setIsLandscape (true);
			setCode ("iso-a4");
			setMarginTop (36);
			setMarginBottom (36);
			setMarginLeft (36);
			setMarginRight (36);
		}
	}	//	MPrintPaper
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MPrintPaper (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MPrintPaper


	/** Media Size			*/
	private MediaSize		m_mediaSize = null;

	/**************************************************************************
	 * 	Get Media Size.
	 *  The search is hard coded as the javax.print.MediaSize* info is private
	 * 	@return MediaSize from Code
	 */
	public MediaSize getMediaSize()
	{
		if (m_mediaSize != null)
			return m_mediaSize;
		//
		String nameCode = getCode();
		if (nameCode != null)
		{
			//	Get Name
			MediaSizeName nameMedia = null;
			CMediaSizeName msn = new CMediaSizeName(4);
			String[] names = msn.getStringTable();
			for (int i = 0; i < names.length; i++)
			{
				String name = names[i];
				if (name.equalsIgnoreCase(nameCode))
				{
					nameMedia = (MediaSizeName)msn.getEnumValueTable()[i];
					if (log.isLoggable(Level.FINER)) log.finer("Name=" + nameMedia);
					break;
				}
			}
			if (nameMedia != null)
			{
				m_mediaSize = MediaSize.getMediaSizeForName(nameMedia);
				if (log.isLoggable(Level.FINE)) log.fine("Name->Size=" + m_mediaSize);
			}
		}
		//	Create New Media Size
		if (m_mediaSize == null)
		{
			float x = getSizeX().floatValue();
			float y = getSizeY().floatValue();
			if (x > 0 && y > 0)
			{
				m_mediaSize = new MediaSize(x, y, getUnitsInt(), MediaSizeName.A);
				if (log.isLoggable(Level.FINE)) log.fine("Size=" + m_mediaSize);
			}
		}
		//	Fallback
		if (m_mediaSize == null)
				m_mediaSize = getMediaSizeDefault();
		return m_mediaSize;
	}	//	getMediaSize

	/**
	 * 	Get Media Size
	 * 	@return Default Media Size based on Language
	 */
	public MediaSize getMediaSizeDefault()
	{
		m_mediaSize = Language.getLoginLanguage().getMediaSize();
		if (m_mediaSize == null)
			m_mediaSize = MediaSize.ISO.A4;
		if (log.isLoggable(Level.FINE)) log.fine("Size=" + m_mediaSize);
		return m_mediaSize;
	}	//	getMediaSizeDefault

	/**
	 * 	Get Units Int
	 *	@return units
	 */
	public int getUnitsInt()
	{
		String du = getDimensionUnits();
		if (du == null || DIMENSIONUNITS_MM.equals(du))
			return Size2DSyntax.MM;
		else if (DIMENSIONUNITS_Inch.equals(du))
			return Size2DSyntax.INCH; 
		else
			throw new EONEException("@NotSupported@ @DimensionUnit@ : "+du);
	}	//	getUnits
	
	/**
	 * 	Get CPaper
	 * 	@return CPaper
	 */
	public CPaper getCPaper()
	{
		//Modify Lines By AA Goodwill : Custom Paper Support 
		CPaper retValue;
		if (getCode().toLowerCase().startsWith("custom"))
		{
			retValue = new CPaper (getSizeX().doubleValue(), getSizeY().doubleValue(), getUnitsInt(),
					isLandscape(),
					getMarginLeft(), getMarginTop(), getMarginRight(), getMarginBottom());			
		}
		else
		{
			retValue = new CPaper (getMediaSize(), isLandscape(),
					getMarginLeft(), getMarginTop(), getMarginRight(), getMarginBottom());
		}
		//End Of AA Goodwill
		return retValue;
	}	//	getCPaper
	
	@Override
	protected boolean beforeSave(boolean newRecord)
	{
		// Check all settings are correct by reload all data
		m_mediaSize = null;
		getMediaSize();
		getCPaper();
		
		return true;
	}



	/**
	 * 	Media Size Name 
	 */
	static class CMediaSizeName extends MediaSizeName
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 8561532175435930293L;

		/**
		 * 	CMediaSizeName
		 *	@param code
		 */
	    public CMediaSizeName(int code) 
	    {
	    	super (code);
	    }	//	CMediaSizeName

		/**
		 * 	Get String Table
		 *	@return string
		 */
		public String[] getStringTable ()
		{
			return super.getStringTable ();
		}
		
		/**
		 * 	Get Enum Value Table
		 *	@return Media Sizes
		 */
		public EnumSyntax[] getEnumValueTable ()
		{
			return super.getEnumValueTable ();
		}
	}	//	CMediaSizeName	
	
}	//	MPrintPaper
