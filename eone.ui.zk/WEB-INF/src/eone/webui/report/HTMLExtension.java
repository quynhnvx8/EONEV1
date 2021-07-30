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
package eone.webui.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.ecs.ConcreteElement;
import org.apache.ecs.xhtml.a;
import org.apache.ecs.xhtml.body;
import org.compiere.print.IHTMLExtension;
import org.compiere.print.PrintDataItem;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import eone.base.model.MSysConfig;
import eone.exceptions.EONEException;
import eone.webui.apps.AEnv;
import eone.webui.theme.ThemeManager;

/**
 * 
 * @author hengsin
 *
 */
public class HTMLExtension implements IHTMLExtension {

	private String classPrefix;
	private String componentId;
	private String scriptURL;
	private String styleURL;

	public HTMLExtension(String contextPath, String classPrefix, String componentId) {

		String theme = MSysConfig.getValue(MSysConfig.HTML_REPORT_THEME, "/", Env.getAD_Client_ID(Env.getCtx()));

		if (! theme.startsWith("/"))
			theme = "/" + theme;
		if (! theme.endsWith("/"))
			theme = theme + "/";

		this.classPrefix = classPrefix;
		this.componentId = componentId;
		this.scriptURL = contextPath + theme + "js/report.js";
		this.styleURL = contextPath + theme + "css/report.css";
	}
	
	public void extendIDColumn(int row, ConcreteElement columnElement, a href,	PrintDataItem dataElement) {
		href.addAttribute("onclick", "showColumnMenu(event)");		//zoomQuery
		href.addAttribute ("componentId", componentId);
		href.addAttribute ("tableLink", dataElement.getTableLink());
		href.addAttribute ("zoomLogic", dataElement.getZoomLogic());
		href.addAttribute ("value", dataElement.getValueAsString());
	}

	public void extendRowElement(ConcreteElement row) {
		
	}

	public String getClassPrefix() {
		return classPrefix;
	}

	public String getScriptURL() {
		return scriptURL;
	}

	public String getStyleURL() {
		return styleURL;
	}

	public void setWebAttribute (body reportBody){
		// set attribute value for create menu context
		reportBody.addAttribute("windowIco", "/webui" + ThemeManager.getThemeResource("images/mWindow.png"));
		//reportBody.addAttribute("reportIco", "/webui" + ThemeManager.getThemeResource("images/mReport.png"));
		//reportBody.addAttribute ("reportLabel", Msg.getMsg(AEnv.getLanguage(Env.getCtx()), "Report").replace("&", ""));
		reportBody.addAttribute ("windowLabel", Msg.getMsg(AEnv.getLanguage(Env.getCtx()), "Window"));
		
	}
	
	public String getFullPathStyle() {
		String theme = MSysConfig.getValue(MSysConfig.HTML_REPORT_THEME, "/", Env.getAD_Client_ID(Env.getCtx()));
		if (! theme.startsWith("/"))
			theme = "/" + theme;
		if (! theme.endsWith("/"))
			theme = theme + "/";
		String resFile = theme + "css/report.css";
		
		URL urlFile = this.getClass().getResource(resFile);
		if (urlFile == null) {
			resFile = "/css/report.css"; // default
			urlFile = this.getClass().getResource(resFile);
		}
		if (urlFile != null) {
			FileOutputStream cssStream = null;
			File cssFile = null;
			try {
				// copy the resource to a temporary file to process it with 2pack
				InputStream stream = urlFile.openStream();
				cssFile = File.createTempFile("report", ".css");
				cssStream = new FileOutputStream(cssFile);
			    byte[] buffer = new byte[1024];
			    int read;
			    while((read = stream.read(buffer)) != -1){
			    	cssStream.write(buffer, 0, read);
			    }
			} catch (IOException e) {
				throw new EONEException(e);
			} finally{
				if (cssStream != null) {
					try {
						cssStream.close();
					} catch (Exception e2) {}
				}
			}
			return cssFile.getAbsolutePath();
		} else {
			return null;
		}
	}
}
