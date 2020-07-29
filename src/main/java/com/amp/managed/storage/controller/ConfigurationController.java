 
package com.amp.managed.storage.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.axis.utils.StringUtils;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.amp.common.api.impl.ToolkitDatabase;
import com.amp.common.api.impl.ToolkitSQL;
import com.amp.jpa.entities.WorkerConfiguration;
import com.amp.jpaentities.mo.WorkerConfigurationListMO;
import com.amp.jpaentities.mo.WorkerConfigurationMO;
import com.amp.managed.storage.service.StorageService;

@RestController
@RequestMapping({"/ConfigurationService"})
public class ConfigurationController
{
	private static final Logger LOG = 
			LoggerFactory.getLogger(ConfigurationController.class);
	
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
    public ConfigurationController() 
    {
    	super();
		
		String methodName = "";
		
		try
    	{
			
    	}    		
    	catch( Exception e)
    	{
    		LOG.error(methodName + "::" + e.getStackTrace());
    	}
    }

	@GetMapping(value =  "/sayHello", 
		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE},
	    produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public ResponseEntity<String> sayHello()
	{
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        ResponseEntity<String> responseObject = 
        		new ResponseEntity<String>(
        				String.valueOf("Hello"), headers, HttpStatus.OK);
        
        return responseObject;
	}
   
	/*//--------------------------------------------------------------------------
    @POST
  	@Path("/addWorkerDataOA")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
  	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
  	public Response addWorkerDataOA(
  			@QueryParam("sourceName")  String sourceName, 
  			@QueryParam("targetName")  String targetName,
  			@QueryParam("workerName")  String workerName,
  			@QueryParam("opTypeName")  String opTypeName,
  			@QueryParam("itemKey")     String itemKey,
  			@QueryParam("description") String description, 
  			@QueryParam("status")      String status)
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
		  
  	        if ( StringUtils.isEmpty(itemKey) || StringUtils.isEmpty(description) || StringUtils.isEmpty(status))
	        {
	        	cRes = false;
	        	
	        	cLogger.error(cMethodName + "::Error:params is null. Check parameters for null value!");
	        }
		    
	        //--------
    		if ( cRes )
    		{
	    		if ( null == this.cToolkitDataProvider )
	    		{
	    			cLogger.error(cMethodName + "::cToolkitDataProvider is NULL. Reinitialize.");
	    			
	    			cRes = this.initClassVariables();
	    		}
    		} 
	        //--------
    		if ( cRes )
    		{
    			hbsSession = cToolkitDataProvider.gettDatabase().getHbsSessions().openSession();
    			
    			tx = hbsSession.beginTransaction();
    			
    			Timestamp updateDate = new Timestamp(System.currentTimeMillis());
    			
    			String hashClean = sourceName + "_" + targetName + "_" + 
    			                   workerName + "_" + opTypeName + "_" +
    			                   opTypeName + "_" + itemKey    + "_" +
    			                   status 	  + "_" + updateDate.toString();
    			
    			String hashEncr  = this.hashString(hashClean);
    			
    			Query query = hbsSession.createSQLQuery("INSERT INTO WORKER_DATA " + 
    			"(WORKERDATA2SOURCE, "
    			+ "WORKERDATA2TARGET, "
    			+ "WORKERDATA2WORKER, "
    			+ "WORKERDATA2OPTYPE, "
    			+ "ITEMID, "
    			+ "DESCRIPTION, "
    			+ "STATUS, "
    			+ "WORKERDATAHASH, "
    			+ "UPDATEDATE) " + 
    			"VALUES ("
    			+ " (select NVL(sourceid, 0) from source where name=:sourceName), "
    			+ " (select NVL(targetid, 0) from target where name=:targetName), "
    			+ " (select NVL(workerid, 0) from worker where name=:workerName), "
    			+ " (select NVL(operationtypeid, 0) from operationtype_m where name=:opTypeName), "
    			+ " :itemKey, "
    			+ " :description, "
    			+ " :status, "
    			+ " :hash,"
    			+ " :updateDate" + ")");
    			
    			query.setString("sourceName", sourceName);
    			query.setString("targetName", targetName);
    			query.setString("workerName", workerName);
    			query.setString("opTypeName", opTypeName);
    			query.setString("itemKey", itemKey);
    			query.setTimestamp("updateDate", updateDate);
    			query.setString("description", description);
    			query.setString("status", status);
    			query.setString("hash", hashEncr);
    			
    			query.executeUpdate();
    		}
	        
  	        cLogger.info("------------------");
  	        
  	        if ( tx != null )
  	        {
				tx.commit();
  	        }
  	      
  	        return Response.ok(String.valueOf(cRes)).build();
      	}
      	catch( Exception e)
      	{
      		cLogger.error(cMethodName + "::Exception:" + e.getMessage());
      		
      		tx.rollback();
      		
      		String cError = String.valueOf(cRes = false) + ":" + e.getMessage();
      		
      		return Response.ok(cError).build();
      	}
      	finally
		{
			if ( hbsSession != null )
    		{
    			hbsSession.close();
    		}
		}
  	}
    
  	@POST
  	@Path("/addWorkerDataMO")
  	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
  	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
  	public Response addWorkerDataMO(WorkerDataMO cWorkerDataMO)
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
	        	
	        	cLogger.error(cMethodName + "::Error:params is null. Check MultivaluedMap<String, String> params parameter!");
	        }
		    
	        //--------
    		if ( cRes )
    		{
	    		if ( null == this.cToolkitDataProvider )
	    		{
	    			cLogger.error(cMethodName + "::cToolkitDataProvider is NULL. Reinitialize.");
	    			
	    			cRes = this.initClassVariables();
	    		}
    		} 
	        //--------
    		if ( cRes )
    		{
    			hbsSession = cToolkitDataProvider.gettDatabase().getHbsSessions().openSession();
    			
    			tx = hbsSession.beginTransaction();
    			
    			WorkerData cWorkerData = cWorkerDataMO.getcWorkerData();
    			
    			String sourceName = String.valueOf(cWorkerData.getSource().getSourceid());
      			String targetName = String.valueOf(cWorkerData.getTarget().getTargetid());
      			String workerName = String.valueOf(cWorkerData.getWorkerdata2worker().longValue());
      			String opTypeName = String.valueOf(cWorkerData.getOperationtypeM().getOperationtypeid());
      			String itemKey    = cWorkerData.getItemid();
      			String status     = cWorkerData.getStatus();
    			
      			Timestamp updateDate = new Timestamp(System.currentTimeMillis());
      			cWorkerData.setUpdatedate(new Date(updateDate.getTime()));
      			
    			String hashClean = sourceName + "_" + targetName + "_" + 
				                   workerName + "_" + opTypeName + "_" +
				                   opTypeName + "_" + itemKey    + "_" +
				                   status 	  + "_" + updateDate.toString();
		
    			String hashEncr  = this.hashString(hashClean);
 			
    			cWorkerData.setWorkerdatahash(hashEncr);
    			
    			hbsSession.save(cWorkerData);
    		}
	        
  	        cLogger.info("------------------");
  	        
  	        if ( tx != null )
			{
				tx.commit();
			}
  	      
  	        return Response.ok(String.valueOf(cRes)).build();
      	}
      	catch( Exception e)
      	{
      		cLogger.error(cMethodName + "::Exception:" + e.getMessage());
      		
      		tx.rollback();
      		
      		String cError = String.valueOf(cRes = false) + ":" + e.getMessage();
      		
      		return Response.ok(cError).build();
      	}
      	finally
		{
			if ( hbsSession != null )
    		{
    			hbsSession.close();
    		}
		}
  	}*/
  	
    
  	//---
  
    @SuppressWarnings("unchecked")
	@GetMapping(value =  "/getWorkerConfig", 
    	consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE},
    	produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
	public ResponseEntity<WorkerConfigurationListMO> getWorkerConfig(
		@RequestParam(name = "cWorkerName", required = false) String cWorkerName) 
	{
		String cMethodName = "";
		
		String sqlQuery = "";
		
		Session hbsSession = null;
		
		Transaction tx = null;
		
		List<WorkerConfiguration> cWorkerConfigs = 
				new ArrayList<WorkerConfiguration>();
		
		WorkerConfigurationListMO cSourceConfigurationList = 
				new WorkerConfigurationListMO();
				
		boolean cRes = true;
		
		try
		{
			StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
	        StackTraceElement ste = stacktrace[1];
	        cMethodName = ste.getMethodName();
	    
	        if ( null == cWorkerName )
    		{
    			LOG.error(cMethodName + "::(null == cWorkerName)");
    			
    			cRes = false;
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
    			
    			@SuppressWarnings("rawtypes")
				NativeQuery cQuery = hbsSession.createSQLQuery(sqlQuery);
    			
    			cQuery.addEntity(WorkerConfiguration.class);
    			cQuery.setParameter("workerName", cWorkerName);
    			
    			tx = hbsSession.beginTransaction();
    			
				cWorkerConfigs = (List<WorkerConfiguration>)cQuery.list();
				
				if ( null == cWorkerConfigs )
				{
					LOG.error(cMethodName + "::cConfiguration  is NULL!");
					
					cRes = false;
				}
				else
				{
					for( WorkerConfiguration cWorkerConfig : cWorkerConfigs )
					{
						WorkerConfigurationMO cSourceConfigMO = new WorkerConfigurationMO();
						
						//---config key/value
						cSourceConfigMO.setSourceconfigid(cSourceConfigMO.getSourceconfigid());
						cSourceConfigMO.setConfigkey(cWorkerConfig.getConfigkey());
						cSourceConfigMO.setConfigvalue(cWorkerConfig.getConfigvalue());
						cSourceConfigMO.setUnit(cWorkerConfig.getUnit());
						
						//---source worker
						cSourceConfigMO.setSourceworkerid(cWorkerConfig.getWorker().getWorkerid());
						cSourceConfigMO.setWorker_name(cWorkerConfig.getWorker().getName());
						
						//---configtype
						cSourceConfigMO.setConfigurationtypeid(cWorkerConfig.getConfigurationtypeM().getConfigurationtypeid());
						cSourceConfigMO.setTarget(cWorkerConfig.getConfigurationtypeM().getTarget());
						
						//---source
						cSourceConfigMO.setSourceid(cWorkerConfig.getSource().getSourceid());
						cSourceConfigMO.setSource_name(cWorkerConfig.getSource().getName());
						cSourceConfigMO.setCompany(cWorkerConfig.getSource().getCompany());
						
						//---source type
						cSourceConfigMO.setSourcetypeid(cWorkerConfig.getSource().getSourcetypeM().getSourcetypeid());
						cSourceConfigMO.setSourcetype_name(cWorkerConfig.getSource().getSourcetypeM().getName());
						
						cSourceConfigurationList.cSourceConfiguration.add(cSourceConfigMO);
					}
				}
    		}
    		
    		if ( tx != null )
			{
				tx.commit();
			}
    		
    		HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        ResponseEntity<WorkerConfigurationListMO> response = 
	        		new ResponseEntity<WorkerConfigurationListMO>(
	        				cSourceConfigurationList, headers, HttpStatus.OK);
	        
    		return response;
		}
		catch( Exception e)
		{
			LOG.error(cMethodName + "::" + e.getMessage(), e);
			
			tx.rollback();
			
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