
package eone.eclipse.equinox.http.servlet;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class BridgeFilter extends BridgeServlet implements Filter {

	private static final long serialVersionUID = 1309373924501049438L;

	private ServletConfigAdaptor servletConfig;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.servletConfig = new ServletConfigAdaptor(filterConfig);
		super.init();
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		// Call process class with FilterChain.
		super.process(req, resp, chain);
	}

	public ServletConfig getServletConfig() {
		return servletConfig;
	}

	/**
	 * 
	 * Class which adapt {@link FilterConfig} to a {@link ServletConfig}.
	 *
	 */
	private static class ServletConfigAdaptor implements ServletConfig {

		private FilterConfig filterConfig;

		public ServletConfigAdaptor(FilterConfig filterConfig) {
			this.filterConfig = filterConfig;
		}

		public String getInitParameter(String arg0) {
			return filterConfig.getInitParameter(arg0);
		}

		public Enumeration<String> getInitParameterNames() {
			return filterConfig.getInitParameterNames();
		}

		public ServletContext getServletContext() {
			return filterConfig.getServletContext();
		}

		public String getServletName() {
			return filterConfig.getFilterName();
		}

	}
}
