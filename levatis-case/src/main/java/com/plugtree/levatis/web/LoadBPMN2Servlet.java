package com.plugtree.levatis.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Loads the bpmn2 key and the URL to open the designer
 */
public class LoadBPMN2Servlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String key = "process-demo";
		req.setAttribute("key", key);
		req.setAttribute("designerUrl", "/designer/editor/?uuid=" + key + "&profile=jbpm");
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
	}
}
