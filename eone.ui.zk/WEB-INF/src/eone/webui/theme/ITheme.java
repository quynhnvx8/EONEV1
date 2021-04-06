/******************************************************************************
 * Copyright (C) 2009 Low Heng Sin                                            *
 * Copyright (C) 2009 Idalica Corporation                                     *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package eone.webui.theme;

/**
 * Interface to hold global theme constant
 * @author hengsin
 *
 */
public interface ITheme {
	//default theme
	public static final String ZK_THEME_DEFAULT = "default";
	//theme resource url prefix
	public static final String THEME_PATH_PREFIX = "/theme/";

	//css for login window and box
	public static final String LOGIN_WINDOW_CLASS = "login-window";
	public static final String LOGIN_BOX_HEADER_CLASS = "login-box-header";
	public static final String LOGIN_BOX_HEADER_TXT_CLASS = "login-box-header-txt";
	public static final String LOGIN_BOX_HEADER_LOGO_CLASS = "login-box-header-logo";
	public static final String LOGIN_BOX_BODY_CLASS = "login-box-body";
	public static final String LOGIN_BOX_FOOTER_CLASS = "login-box-footer";
	public static final String LOGIN_BOX_FOOTER_PANEL_CLASS = "login-box-footer-pnl";

	//css for login control
	public static final String LOGIN_BUTTON_CLASS = "login-btn";
	public static final String LOGIN_LABEL_CLASS = "login-label";
	public static final String LOGIN_FIELD_CLASS = "login-field";

	//logo
	public static final String LOGIN_LOGO_IMAGE = "/erp/images/login-logo.png";
	public static final String HEADER_LOGO_IMAGE = "/images/header-logo.png";
	public static final String BROWSER_ICON_IMAGE= "/erp/images/icon_logo.png";
	//public static final String LOGIN_LOGO_IMAGE = "/images/login-logo.png";
	//public static final String HEADER_LOGO_IMAGE = "/images/header-logo.png";
	//public static final String BROWSER_ICON_IMAGE= "/images/icon.png";

	//stylesheet url
	public static final String THEME_STYLESHEET = "/css/theme.css.dsp";
	//http://books.zkoss.org/wiki/ZK_Developer's_Reference/Internationalization/Locale-Dependent_Resources#Specifying_Locale-_and_browser-dependent_URL
	public static final String THEME_STYLESHEET_BY_BROWSER = "/css/theme*.css.dsp*";
	
	//theme preference
	public static final String THEME_PREFERENCE = "/preference.zul";
	
	public static final String USE_CSS_FOR_WINDOW_SIZE = "#THEME_USE_CSS_FOR_WINDOW_SIZE";
	
	public static final String USE_FONT_ICON_FOR_IMAGE = "#THEME_USE_FONT_ICON_FOR_IMAGE";
	
	public static final String ZK_TOOLBAR_BUTTON_SIZE = "#ZK_Toolbar_Button_Size";
	
	public static String ZK_COPYRIGHT = "Copyright (C) 2020 - Developed by Eone";
	public static String ZK_TITLE = "EONE";
	
	public static String ZUL_LOGIN = ThemeManager.getThemeResource("zul/login.zul");
	public static String ZUL_DESKTOP = ThemeManager.getThemeResource("zul/desktop/desktop.zul");
	public static String ZUL_CALENDAR = ThemeManager.getThemeResource("zul/calendar/calendar.zul");
	
	//Login
	public static String IMG_USER = ThemeManager.getThemeResource("erp/images/login/icon_user1.png");
	public static String IMG_PASS = ThemeManager.getThemeResource("erp/images/login/icon_password1.png");
	public static String IMG_LANG = ThemeManager.getThemeResource("erp/images/login/icon_lang.png");
	
	//Menu
	public static String MNU_REPORT = ThemeManager.getThemeResource("erp/images/icons/Report16.png");
	public static String MNU_PROCESS = ThemeManager.getThemeResource("erp/images/icons/Process16.png");
	public static String MNU_WFLOW = ThemeManager.getThemeResource("erp/images/icons/WorkFlow16.png");
	public static String MNU_WINDOW = ThemeManager.getThemeResource("erp/images/icons/window16.png");
	
	//Acct viewer
	public static String ACCT_FIND = ThemeManager.getThemeResource("images/Find16.png");
	public static String ACCT_ZOOM = ThemeManager.getThemeResource("images/Zoom16.png");
	public static String ACCT_REFERESH = ThemeManager.getThemeResource("images/Refresh16.png");
	public static String ACCT_EXPORT = ThemeManager.getThemeResource("images/Export16.png");
	public static String ACCT_SAVE = ThemeManager.getThemeResource("images/Save16.png");
	
	public static String LOGO_MEDIUM = ThemeManager.getThemeResource("erp/images/logo_medium.png");
	public static String LOGO_SMALL = ThemeManager.getThemeResource("erp/images/logo_small.png");
	
	public static String ICON_DELETE = ThemeManager.getThemeResource("erp/images/icons/Delete24.png");
	public static String ICON_REFRESH = ThemeManager.getThemeResource("erp/images/icons/Refresh24.png");
	
	public static String IMG_LOGOUT = ThemeManager.getThemeResource("erp/images/Logout24.png");
	
	public static String FONTPDF = ThemeManager.getThemeResource("font/arial.ttf");
}
