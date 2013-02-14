package com.plugtree.levatis.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.plugtree.levatis.repo.Repository;

/**
 * Saves the BPMN2 file
 */
public class SaveBPMN2Servlet extends HttpServlet {

	private Repository repository = Repository.getInstance();
	private String designerBaseUrl = "http://localhost:8080/designer";
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		this.designerBaseUrl = config.getInitParameter("designer.base.url");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		InputStream input = req.getInputStream();
		String modelJson = IOUtils.toString(input);
		String designerURL = designerBaseUrl + "/uuidRepository?profile=jbpm&action=toXML&pp=";
		byte[] content = serialize(designerURL, modelJson).getBytes("UTF-8");
		String name = req.getParameter("key");
		repository.save(name, content);
		IOUtils.write("SAVE OK", resp.getOutputStream());
	}
	
	//transforms the JSON to an XML file (a BPMN2 file)
    public static String serialize(String serializeUrl, String modelJson) throws IOException {
    	OutputStream out = null;
    	InputStream content = null;
    	ByteArrayOutputStream bos = null;

    	try {
    		modelJson = "&data=" + URLEncoder.encode( modelJson, "UTF-8" );
    		byte[] bytes = modelJson.getBytes( "UTF-8" );

    		HttpURLConnection connection = (HttpURLConnection) new URL( serializeUrl ).openConnection();
    		connection.setRequestMethod( "POST" );
    		connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded;charset=UTF-8" );
    		//connection.setFixedLengthStreamingMode( bytes.length );
    		connection.setDoOutput( true );
    		out = connection.getOutputStream();
    		out.write( bytes );
    		out.close();

    		content = connection.getInputStream();

    		bos = new ByteArrayOutputStream();
    		int b = 0;
    		while ( (b = content.read()) > -1 ) {
    			bos.write( b );
    		}
    		bytes = bos.toByteArray();
    		content.close();
    		bos.close();
    		return new String( bytes );
    	} finally {
            try {
                if ( out != null ) {
                    out.close();
                }
                if ( content != null ) {
                    content.close();
                }
                if ( bos != null ) {
                    bos.close();
                }
            } catch ( IOException e ) {
            }
        }
    }

}
