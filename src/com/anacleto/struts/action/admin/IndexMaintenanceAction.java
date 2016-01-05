//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package com.anacleto.struts.action.admin;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.anacleto.base.Configuration;
import com.anacleto.base.ConfigurationException;
import com.anacleto.base.Logging;
import com.anacleto.hierarchy.Book;
import com.anacleto.index.BookIndexQueue;
import com.anacleto.index.IndexManager;
import com.anacleto.sched.Scheduled;
import com.anacleto.struts.form.admin.IndexMaintenanceForm;

/** 
 * MyEclipse Struts
 * Creation date: 04-20-2005
 * 
 * XDoclet definition:
 * @struts:action path="/indexMaintenance" name="indexMaintenanceForm" input="/form/admin/indexMaintenance.jsp" scope="request" validate="true"
 */
public class IndexMaintenanceAction extends Action {

    // --------------------------------------------------------- Instance Variables
	Logger log = Logging.getAdminLogger();
    // --------------------------------------------------------- Methods

    /** 
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws SchedulerException
     */
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response) {
        IndexMaintenanceForm indexMaintForm = (IndexMaintenanceForm) form;
        
        try{
	        if (indexMaintForm.getIndexNowButt().pressed()){
	        	//index selected books
	        	Collection bookCollToIndex = new LinkedList();
	        	String selectedBooks[] = indexMaintForm.getSelectedBooks();
	        	for (int i = 0; i < selectedBooks.length; i++){
	        		bookCollToIndex.add(selectedBooks[i]);
				}
	        	
				BookIndexQueue.addBookToIndex(bookCollToIndex);
				
	        } else if (indexMaintForm.getRemoveSelectedButt().pressed()){
	        	//remove selected books
	        	IndexManager im = new IndexManager();
	        	
	        	String selectedBooks[] = indexMaintForm.getSelectedBooks();
	        	for (int i = 0; i < selectedBooks.length; i++){
	        		im.deleteBook(selectedBooks[i]);
				}

	        } else if (indexMaintForm.getIndexAtButt().pressed()){
	        	Calendar cal = getDateFromInput(indexMaintForm.getSchedDate(),
	        			indexMaintForm.getSchedTime());
	        	if (cal != null)
		          	try {
		          		Collection bookColl = new LinkedList();
		            	String selectedBooks[] = indexMaintForm.getSelectedBooks();
		            	for (int i = 0; i < selectedBooks.length; i++){
		            		bookColl.add(selectedBooks[i]);
		    			}
		            	if (bookColl != null)
		            		Scheduled.addSimpleIndexing(bookColl,cal);
					} catch (ConfigurationException e) {
						log.error("Error on indexMaintenanceForm. Root cause: " + e);
					}
					
			
	        } else if (indexMaintForm.getRemoveButt().pressed()){
	//        	Remove books from the index:
	        	IndexManager im = new IndexManager();
	        	
	        	Collection bookColl = getMissingBooksColl();
	        	Iterator it = bookColl.iterator();
	        	while (it.hasNext()) {
					String book = (String) it.next();
					im.deleteBook(book);
				}
	        }
	        
	        
	        if (indexMaintForm.getOptimizeButt().pressed()){
	        	BookIndexQueue.optimizeIndex();
	        }
	        	
	        //books:
	        IndexManager im = new IndexManager();
	        
	        Collection bookColl = Configuration.getBookColl();
	        HashMap bookCounters = im.getBookCounters();
	        Iterator it = bookColl.iterator();
	        while (it.hasNext()) {
	            Book element = (Book) it.next();
	            Object counter = bookCounters.get(element.getName());
	            if (counter != null)
	            	element.setPageNum(((Integer)counter).intValue() );
	        }
	        
	        indexMaintForm.setBookColl(bookColl);
	        
	        
	        indexMaintForm.setMissingBookColl(getMissingBooksColl());
			
	        //Set date and time for the screen:
	        Calendar cal = Calendar.getInstance();
	        indexMaintForm.setSchedDate(cal.get(Calendar.YEAR)+"/"
	        							+intToString(cal.get(Calendar.MONTH), 2)+"/"
									    +intToString(cal.get(Calendar.DAY_OF_MONTH), 2));
	        indexMaintForm.setSchedTime(intToString(cal.get(Calendar.HOUR_OF_DAY),2)+ ":"+
	        		intToString(cal.get(Calendar.MINUTE), 2));
	        
	        //Currently indexing book:
	        indexMaintForm.setCurrentlyIndexing(
	        		BookIndexQueue.getCurrentlyIndexingBook());
	        
	        //Books in the queue:
	        indexMaintForm.setBooksInQueue(
	        		BookIndexQueue.getBooksInQueue());
	        
	        
			//Get jobs:
			indexMaintForm.setSimpleJobIndexing(
					Scheduled.getSimpleJobEntries());
			indexMaintForm.setCronJobIndexing(
					Scheduled.getCronJobEntries());
			
		} catch (ConfigurationException e) {
			log.error("Error on the IndexMaintenance form (ConfigurationException): " + e);
		} catch (IOException e) {
			log.error("Error on the IndexMaintenance form (IOException): " + e);

		}
        return mapping.getInputForward();
    }
    
    private String intToString(int in, int len){
    	String intVal = "0000000000000" + String.valueOf(in);
    	return intVal.substring(intVal.length()-len);
    }
    
    private Calendar getDateFromInput(String dateString, String timeString){
    	Calendar cal = Calendar.getInstance();
    	try{
	    	int year  = Integer.valueOf(dateString.substring(0,4)).intValue();
	    	int month = Integer.valueOf(dateString.substring(5,7)).intValue();
	    	int day   = Integer.valueOf(dateString.substring(8,10)).intValue();
	    	
	    	int hour  = Integer.valueOf(timeString.substring(0,2)).intValue();
	    	int min   = Integer.valueOf(timeString.substring(3,5)).intValue();
	    	
	    	cal.set(year, month, day, hour, min, 0);
    	} catch (NumberFormatException e){
    		return null;
    	}
    	
    	return cal;
    }
    
    private Collection getMissingBooksColl() throws IOException {

    	//Get indexed entries that have no books entry:
    	IndexManager im = new IndexManager();
        
        Collection bookColl = Configuration.getBookColl();
        
        Collection indexedBooks = im.getIndexedBooks();
        HashSet bookNameHash = new HashSet();
        Iterator it = bookColl.iterator();
        while (it.hasNext()) {
        	Book element = (Book) it.next();
        	bookNameHash.add(element.getName());
        }
        
        Collection missingBookColl = new LinkedList();
        it = indexedBooks.iterator();
        while (it.hasNext()) {
            String bookName = (String) it.next();
            if (!bookNameHash.contains(bookName))
            	missingBookColl.add(bookName);
        }
        return missingBookColl;
    }

}