package org.mlink.iwm.http;
/*
 * Copyright 2003 Jayson Falkner (jayson@jspinsider.com)
 * This code is from "Servlets and JavaServer pages; the J2EE Web Tier",
 * http://www.jspbook.com. You may freely use the code both commercially
 * and non-commercially. If you like the code, please pick up a copy of
 * the book and help support the authors, development of more free code,
 * and the JSP/Servlet/J2EE community. http://www.onjava.com/lpt/a/4361
 */

import org.apache.log4j.Logger;
import org.mlink.iwm.util.Config;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class GZIPFilter implements Filter {
    private static final Logger logger = Logger.getLogger(GZIPFilter.class);


  public void doFilter(ServletRequest req, ServletResponse res,
      FilterChain chain) throws IOException, ServletException {
    if (req instanceof HttpServletRequest) {
      HttpServletRequest request = (HttpServletRequest) req;
      HttpServletResponse response = (HttpServletResponse) res;
      String ae = request.getHeader("accept-encoding");
      if (ae != null && ae.indexOf("gzip") != -1 && "true".equals(Config.getProperty(Config.USE_SERVLET_OUT_COMPRESSION,"false"))) {
        logger.debug("GZIP supported, compressing.");
        GZIPResponseWrapper wrappedResponse =
          new GZIPResponseWrapper(response);
        chain.doFilter(req, wrappedResponse);
        wrappedResponse.finishResponse();
        return;
      }
      chain.doFilter(req, res);
    }
  }

  public void init(FilterConfig filterConfig) {
    // noop
  }

  public void destroy() {
    // noop
  }
}
