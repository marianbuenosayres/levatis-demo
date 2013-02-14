package com.plugtree.levatis.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.plugtree.levatis.repo.Item;
import com.plugtree.levatis.repo.Repository;

public class OryxEditorServlet extends HttpServlet {

    private Repository repository = Repository.getInstance();

    public void service(HttpServletRequest request,
                        HttpServletResponse response)
            throws ServletException,
            IOException {
        System.out.println("Incoming request from Oryx Designer:" + request.getRequestURL());
        String uuid = request.getParameter("uuid");

        if (uuid == null) {
            throw new ServletException(new IllegalArgumentException("Parameter uuid not specified."));
        }

        try {
            Item asset = repository.loadByKey(uuid);
            if (asset.getContent() != null) {
                response.setContentType("application/xml");
                response.setCharacterEncoding("UTF-8");
                byte[] content = asset.getContent();
                if (content != null) {
                    response.getOutputStream().write(content);
                    response.getOutputStream().close();
                } else {
                    setDefaultResponse(response);
                }

            } else {
                setDefaultResponse(response);
            }
        } catch (Throwable t) {
        	t.printStackTrace();
            setDefaultResponse(response);
        }

    }

    private void setDefaultResponse(HttpServletResponse response) throws ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String result = "";
        response.setContentLength(result.length());
        try {
            response.getOutputStream().write(result.getBytes());
            response.getOutputStream().close();
        } catch (IOException e) {
            throw new ServletException(e.getMessage());
        }
    }
}
