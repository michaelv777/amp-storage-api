 
package com.amp.managed.storage.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.amp.common.api.impl.ToolkitDatabase;
import com.amp.common.api.impl.ToolkitSQL;
import com.amp.jpa.entities.WorkerData;
import com.amp.jpaentities.mo.WorkerDataListMO;
import com.amp.jpaentities.mo.WorkerDataMO;
import com.amp.managed.storage.service.StorageService;
import com.amp.service.rest.model.WorkerDataRequestRO;

@RestController
@RequestMapping({"/StatusService"})
public class StatusController
{
	private static final Logger LOG = 
			LoggerFactory.getLogger(StatusController.class);
	
	//---class variables----------------------
	@Autowired
	private StorageService storageService;
	
	//---getters/setters----------------------
	public StorageService getStorageService() {
		return storageService;
	}

	public void setStorageService(StorageService storageService) {
		this.storageService = storageService;
	}
	
	//---class methods------------------------
	/**
     * Default constructor. 
     */
    public StatusController() 
    {
    	super();
		
		String methodName = "";
		
		// TODO Auto-generated method stub
		try
    	{
			
    	}    		
    	catch( Exception e)
    	{
    		LOG.error(methodName + "::" + e.getStackTrace(), e);
    	}
    }

	//---
    @SuppressWarnings("rawtypes")
	@PostMapping(value =  "/setItemOpStatus", 
			consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE},
		    produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
  	public ResponseEntity<String> setItemOpStatus(
  			@RequestBody WorkerDataRequestRO requestObject,
  			HttpServletRequest request, 
  			HttpServletResponse response) 
  	{
      	boolean cRes = true;
      	
      	String cMethodName = "";
      	
      	Session hbsSession = null;
		
		Transaction tx = null;
		
      	try
      	{
      		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
  	        StackTraceElement ste = stacktrace[1];
  	        cMethodName = ste.getMethodName();
		  
  	        if ( StringUtils.isEmpty(requestObject.getItemKey())     || 
  	        	 StringUtils.isEmpty(requestObject.getDescription()) || 
  	        	 StringUtils.isEmpty(requestObject.getStatus()))
	        {
	        	cRes = false;
	        	
	        	LOG.error(cMethodName + "::Error:params is null. Check parameters for null value!");
	        }
  	       
	        //--------
    		if ( cRes )
    		{
    			ToolkitDatabase toolkitDatabase = this.getStorageService().getcToolkitDataProvider().gettDatabase();
    			
    			hbsSession = toolkitDatabase.getHbsSessions().openSession();
    			
    			tx = hbsSession.beginTransaction();
    			
    			Timestamp updateDate = new Timestamp(System.currentTimeMillis());
    			
    			String hashClean = 
    					requestObject.getSourceName() + "_" + requestObject.getTargetName() + "_" + 
    					requestObject.getWorkerName() + "_" + requestObject.getOpTypeName() + "_" +
    					requestObject.getThreadName() + "_" + requestObject.getItemKey()    + "_" +  
    					requestObject.getStatus()     + "_" + updateDate.toString();
    			
    			String hashEncr = this.getStorageService().hashString(hashClean);
    			
    			NativeQuery query1 = hbsSession.createSQLQuery("DELETE FROM WORKER_DATA WHERE " 
    	    			+ "WORKERDATA2SOURCE=(select sourceid from source where name=:sourceName) AND "
    	    			+ "WORKERDATA2TARGET=(select targetid from target where name=:targetName) AND "
    	    			+ "WORKERDATA2WORKER=(select workerid from worker where name=:workerName) AND "
    	    			+ "WORKERDATA2THREAD=(select threadid from thread where name=:threadName) AND "
    	    			+ "WORKERDATA2OPTYPE=(select operationtypeid from operationtype_m where name=:opTypeName) AND "
    	    			+ "ITEMID=:itemKey ");
    			
    			query1.setParameter("sourceName", requestObject.getSourceName());
    			query1.setParameter("targetName", requestObject.getTargetName());
    			query1.setParameter("workerName", requestObject.getWorkerName());
    			query1.setParameter("threadName", requestObject.getThreadName());
    			query1.setParameter("opTypeName", requestObject.getOpTypeName());
    			query1.setParameter("itemKey",    requestObject.getItemKey());	
    	    			
    			query1.executeUpdate();	
    	    		
    			//---
    			NativeQuery query2 = hbsSession.createSQLQuery("INSERT INTO WORKER_DATA " + 
    			"(WORKERDATA2SOURCE, "
    			+ "WORKERDATA2TARGET, "
    			+ "WORKERDATA2WORKER, "
    			+ "WORKERDATA2THREAD, "
    			+ "WORKERDATA2OPTYPE, "
    			+ "WORKERDATA2STATUS, "
    			+ "ITEMID, "
    			+ "DESCRIPTION, "
    			+ "WORKERDATAHASH, "
    			+ "UPDATEDATE) " + 
    			"VALUES ("
    			+ " (select sourceid from source where name=:sourceName), "
    			+ " (select targetid from target where name=:targetName), "
    			+ " (select workerid from worker where name=:workerName), "
    			+ " (select threadid from thread where name=:threadName), "
    			+ " (select operationtypeid from operationtype_m where name=:opTypeName), "
    			+ " (select statusid from status_m where name=:status), "
    			+ " :itemKey, "
    			+ " :description, "
    			+ " :hash,"
    			+ " :updateDate" + ")");
    			
    			query2.setParameter("sourceName", requestObject.getSourceName());
    			query2.setParameter("targetName", requestObject.getTargetName());
    			query2.setParameter("workerName", requestObject.getWorkerName());
    			query2.setParameter("threadName", requestObject.getThreadName());
    			query2.setParameter("opTypeName", requestObject.getOpTypeName());
    			query2.setParameter("status",     requestObject.getStatus());
    			query2.setParameter("itemKey",    requestObject.getItemKey());
    			query2.setParameter("updateDate", updateDate);
    			query2.setParameter("description", requestObject.getDescription());
    			query2.setParameter("hash", hashEncr);
    			
    			query2.executeUpdate();
    		}
	        
  	        LOG.info("------------------");
  	        
  	        if ( tx != null )
  	        {
				tx.commit();
  	        }
  	      
  	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        ResponseEntity<String> responseObject = 
	        		new ResponseEntity<String>(
	        				String.valueOf(cRes), headers, HttpStatus.OK);
	        
  	        return responseObject;
      	}
      	catch( Exception e)
      	{
      		tx.rollback();
      		
      		LOG.error(cMethodName + "::Exception:" + e.getMessage(), e);
      		
      		throw new ResponseStatusException(
			           HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
      	}
      	finally
		{
			if ( hbsSession != null )
    		{
    			hbsSession.close();
    		}
		}
  	}
    //---
    @PostMapping(value =  "/setItemOpStatusMO", 
			consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE},
		    produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
  	public ResponseEntity<String> setItemOpStatusMO(
  			@RequestBody WorkerDataMO cWorkerDataMO,
  			HttpServletRequest request, 
  			HttpServletResponse response)
  	{
      	boolean cRes = true;
      	
      	String cMethodName = "";
      	
      	Session hbsSession = null;
		
		Transaction tx = null;
		
      	try
      	{
      		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
  	        StackTraceElement ste = stacktrace[1];
  	        cMethodName = ste.getMethodName();
		  
		    if ( null == cWorkerDataMO )
	        {
	        	cRes = false;
	        	
	        	LOG.error(cMethodName + "::Error:params is null. "
	        			+ "Check MultivaluedMap<String, String> params parameter!");
	        }
		    
	        //--------
    		if ( cRes )
    		{
	    		if ( null == this.getStorageService().getcToolkitDataProvider() )
	    		{
	    			LOG.error(cMethodName + 
	    					"::cToolkitDataProvider is NULL. Reinitialize.");
	    			
	    			cRes = this.getStorageService().init();
	    		}
    		} 
	        //--------
    		if ( cRes )
    		{
    			ToolkitDatabase toolkitDatabase = this.getStorageService().getcToolkitDataProvider().gettDatabase();
    			
    			hbsSession = toolkitDatabase.getHbsSessions().openSession();
    			
    			tx = hbsSession.beginTransaction();
    			
    			WorkerData cWorkerData = cWorkerDataMO.getcWorkerData();
    			
    			String sourceId = String.valueOf(cWorkerData.getSource().getSourceid());
      			String targetId = String.valueOf(cWorkerData.getTarget().getTargetid());
      			String workerId = String.valueOf(cWorkerData.getWorker().getWorkerid());
      			String threadId = String.valueOf(cWorkerData.getThread().getThreadid());
      			String opTypeName = String.valueOf(cWorkerData.getOperationtypeM().getOperationtypeid());
      			String itemKey    = cWorkerData.getItemid();
      			String status     = cWorkerData.getStatusM().getName();
    			
      			Timestamp updateDate = new Timestamp(System.currentTimeMillis());
      			
      			cWorkerData.setUpdatedate(new Date(updateDate.getTime()));
      			
    			String hashClean = sourceId + "_" + targetId + "_" + 
				                   workerId + "_" + threadId + "_" + 
				                   opTypeName + "_" + itemKey    + "_" +
				                   status 	  + "_" + updateDate.toString();
		
    			String hashEncr  = this.getStorageService().hashString(hashClean);
 			
    			cWorkerData.setWorkerdatahash(hashEncr);
    			
    			hbsSession.save(cWorkerData);
    		}
	        
  	        LOG.info("------------------");
  	        
  	        if ( tx != null )
			{
				tx.commit();
			}
  	      
  	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        ResponseEntity<String> responseObject = 
	        		new ResponseEntity<String>(
	        				String.valueOf(cRes), headers, HttpStatus.OK);
	        
	        return responseObject;
      	}
      	catch( Exception e)
      	{
      		tx.rollback();
      		
      		LOG.error(cMethodName + "::Exception:" + e.getMessage(), e);
      		
      		throw new ResponseStatusException(
			           HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
      	}
      	finally
		{
			if ( hbsSession != null )
    		{
    			hbsSession.close();
    		}
		}
  	}
  	
  	//---
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping(value = "/getItemOpStatus", 
			consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE},
		    produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<WorkerDataListMO> getItemOpStatus(
			@RequestBody WorkerDataRequestRO requestObject,
  			HttpServletRequest request, 
  			HttpServletResponse response)
	{
		String cMethodName = "";
		
		String sqlQuery = "";
		
		Session hbsSession = null;
		
		Transaction tx = null;
		
		List<WorkerData> cWorkerDataList = 
				new ArrayList<WorkerData>();
		
		WorkerDataListMO cWorkerDataListMO = 
				new WorkerDataListMO();
				
		boolean cRes = true;
		
		try
		{
			StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
	        StackTraceElement ste = stacktrace[1];
	        cMethodName = ste.getMethodName();
	        
	        if ( StringUtils.isEmpty(requestObject.getWorkerName()) || 
	        	 StringUtils.isEmpty(requestObject.getThreadName()) || 
	        	 StringUtils.isEmpty(requestObject.getSourceName()) || 
	        	 StringUtils.isEmpty(requestObject.getTargetName()) || 
	        	 StringUtils.isEmpty(requestObject.getOpTypeName()) || 
	        	 StringUtils.isEmpty(requestObject.getItemKey()))
    		{
    			LOG.error(cMethodName + "::Null Parameters. Check Parameters!");
    			
    			cRes = false;
    		}
	        
    		//------
    		if ( cRes )
    		{
	    		if ( null == this.getStorageService().getcToolkitDataProvider() )
	    		{
	    			LOG.error(cMethodName + "::cToolkitDataProvider is NULL. Reinitialize.");
	    			
	    			cRes = this.getStorageService().init();
	    		}
    		}
    		//------
    		if ( cRes )
    		{
    			ToolkitSQL toolkitSql = this.getStorageService().getcToolkitDataProvider().gettSQL();
    			
    			sqlQuery = toolkitSql.getSqlQueryByFunctionName(cMethodName);
    			
    			if ( null == sqlQuery || StringUtils.isEmpty(sqlQuery))
        		{
        			LOG.error(cMethodName + "::sqlQuery is NULL for the Method:" + cMethodName);
        			
        			cRes = false;
        		}
    		}
    		//------
    		if ( cRes )
    		{
    			ToolkitDatabase toolkitDatabase = this.getStorageService().getcToolkitDataProvider().gettDatabase();
    			
    			hbsSession = toolkitDatabase.getHbsSessions().openSession();
    			
    			NativeQuery cQuery = hbsSession.createSQLQuery(sqlQuery);
    			
    			cQuery.addEntity(WorkerData.class);
    			
    			cQuery.setParameter("sourceName", requestObject.getSourceName());
    			cQuery.setParameter("targetName", requestObject.getThreadName());
    			cQuery.setParameter("workerName", requestObject.getWorkerName());
    			cQuery.setParameter("threadName", requestObject.getThreadName());
    			cQuery.setParameter("opTypeName", requestObject.getOpTypeName());
    			cQuery.setParameter("itemKey", requestObject.getItemKey());
    			
    			tx = hbsSession.beginTransaction();
    			
				cWorkerDataList = (List<WorkerData>)cQuery.list();
				
				if ( null == cWorkerDataList )
				{
					LOG.error(cMethodName + "::cConfiguration  is NULL!");
					
					cRes = false;
				}
				else
				{
					for( WorkerData cWorkerData : cWorkerDataList )
					{
						WorkerDataMO cWorkerDataMO = new WorkerDataMO();
						
						cWorkerDataMO.getcWorkerData().getSource().setSourceid(
								cWorkerData.getSource().getSourceid());
						
						cWorkerDataMO.getcWorkerData().getTarget().setTargetid(
								cWorkerData.getTarget().getTargetid());
						
						cWorkerDataMO.getcWorkerData().getOperationtypeM().setOperationtypeid(
								cWorkerData.getOperationtypeM().getOperationtypeid());
						
						cWorkerDataMO.getcWorkerData().getOperationtypeM().setName(
								cWorkerData.getOperationtypeM().getName());
						
						cWorkerDataMO.getcWorkerData().getWorker().setWorkerid(
								cWorkerData.getWorker().getWorkerid());
								
						cWorkerDataMO.getcWorkerData().getThread().setThreadid(
								cWorkerData.getThread().getThreadid());
						
						cWorkerDataMO.getcWorkerData().getStatusM().setName(
								cWorkerData.getStatusM().getName());
						
						cWorkerDataMO.getcWorkerData().setWorkerdatahash(
								cWorkerData.getWorkerdatahash());
						
						cWorkerDataMO.getcWorkerData().setItemid(
								cWorkerData.getItemid());
						
						cWorkerDataMO.getcWorkerData().setUpdatedate(
								cWorkerData.getUpdatedate());
						
						cWorkerDataMO.getcWorkerData().setDescription(
								cWorkerData.getDescription());
						
						cWorkerDataListMO.cWorkerData.add(cWorkerDataMO);
					}
				}
    		}
    		
    		if ( tx != null )
			{
				tx.commit();
			}
    		
    		HttpHeaders headers = new HttpHeaders();
 	        headers.setContentType(MediaType.APPLICATION_JSON);
 	        
 	        ResponseEntity<WorkerDataListMO> responseObject = 
 	        		new ResponseEntity<WorkerDataListMO>(
 	        				cWorkerDataListMO, headers, HttpStatus.OK);
 	        
 	        return responseObject;
		}
		catch( Exception e)
		{
			tx.rollback();
			
			LOG.error(cMethodName + "::" + e.getMessage(), e);
			
			throw new ResponseStatusException(
			           HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
		finally
		{
			if ( hbsSession != null )
    		{
    			hbsSession.close();
    		}
		}
	}
}