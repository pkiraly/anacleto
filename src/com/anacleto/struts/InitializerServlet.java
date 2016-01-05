/*
 * Created on Mar 7, 2005
 *
 */
package com.anacleto.struts;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anacleto.base.Configuration;

/**
 * @author robi
 *
 */
public class InitializerServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Constructor of the object.
     */
    public InitializerServlet() {
        super();
    }


    /**
     * The doGet method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to get.
     * 
     * @param request the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException if an error occurred
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    /**
     * The doPost method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to post.
     * 
     * @param request the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    /**
     * Initialization of the servlet. <br>
     *
     * @throws ServletException if an error occure
     */
    public void init(ServletConfig cfg) throws ServletException {
        super.init(cfg);

        log("Initializer Servlet loaded, initializing ...");
        
        //TODO: choose a transformer implementation
        /*
        System.setProperty("javax.xml.transform.TransformerFactory",
                          "org.apache.xalan.processor.TransformerFactoryImpl");
        */
        Configuration conf = new Configuration();
		conf.init();
		
    }


	static public String charToHex(char c) {
	      // Returns hex String representation of char c
	      byte hi = (byte) (c >>> 8);
	      byte lo = (byte) (c & 0xff);
	      return byteToHex(hi) + byteToHex(lo);
	   }
	
	 static public String byteToHex(byte b) {
	      // Returns hex String representation of byte b
	      char hexDigit[] = {
	         '0', '1', '2', '3', '4', '5', '6', '7',
	         '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
	      };
	      char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
	      return new String(array);
	   }

    /**
     * Destruction of the servlet. <br>
     */
    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
        
        //close scheduler, etc.
        Configuration conf = new Configuration();
		conf.destroy();
    }

}
