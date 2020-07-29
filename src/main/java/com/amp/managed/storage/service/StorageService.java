/**
 * 
 */
package com.amp.managed.storage.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.amp.common.api.impl.ToolkitDataProvider;

/**
 * @author MVEKSLER
 *
 */
@Service
public  class StorageService 
{
	private static final Logger LOG = 
			LoggerFactory.getLogger(StorageService.class);
	
	//---class variables
	@Autowired
	protected ToolkitDataProvider cToolkitDataProvider;
	
	@Autowired
	protected ApplicationContext applicationContext;
	
	//---getters/setters
	public ToolkitDataProvider getcToolkitDataProvider() {
		return cToolkitDataProvider;
	}

	public void setcToolkitDataProvider(ToolkitDataProvider cToolkitDataProvider) {
		this.cToolkitDataProvider = cToolkitDataProvider;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	//---class methods
	/**
	 * 
	 */
	public StorageService() 
	{
		try
    	{
			
    	}    		
    	catch( Exception e)
    	{
    		LOG.error("StorageServiceBase:" + e.getStackTrace(), e);
    	}
	}
	
	//---
	@PostConstruct
	public boolean init()
	{
		boolean cRes = true;
		
		String  methodName = "";
	
		try
    	{
			StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
	        StackTraceElement ste = stacktrace[1];
	        methodName = ste.getMethodName();
    		
	        /*
    		if ( cRes )
    		{
    			cRes = this.configureSpringExt();
    		}
    		*/
	        if ( cRes )
	        {
	        	if ( null == this.applicationContext )
	        	{
	        		cRes = false;
    				
	    			LOG.error(methodName + "::applicationContext is NULL!");
	        	}
	        }
	        //---
    		if ( cRes )
    		{
    			if ( null == this.cToolkitDataProvider )
    			{
	    			this.cToolkitDataProvider = (ToolkitDataProvider)
	    						this.applicationContext.getBean("toolkitDataProvider");
	    			
	    			if ( null == this.cToolkitDataProvider )
		    		{
	    				cRes = false;
	    				
		    			LOG.error(methodName + "::cToolkitDataProvider is NULL!");
		    		}
	    			else
	    			{
	    				cRes = this.cToolkitDataProvider.isLcRes();
	    				
	    				LOG.info(methodName + "::cToolkitDataProvider status is " + cRes);
	    			}
    			}
    		}	
    		//---
    		if ( cRes )
    		{
    			List<Class<? extends Object>> clazzes = this.cToolkitDataProvider.
    					gettDatabase().getPersistanceClasses();
    			
    			this.cToolkitDataProvider.
    					gettDatabase().getHibernateSession(clazzes);
    		}
    		
    		//---
    		
    		return cRes;	 
    	}
		catch(  NoSuchBeanDefinitionException e )
		{
			LOG.error(methodName + "::" + e.getMessage(), e);
    		
    		return false;
		}
		catch(  BeansException e )
		{
			LOG.error(methodName + "::" + e.getMessage(), e);
    		
    		return false;
		}
    	catch( Exception e)
    	{
    		LOG.error(methodName + "::" + e.getMessage(), e);
    		
    		return false;
    	}
	}
	//---
	public String hashString(String s) throws NoSuchAlgorithmException 
	{
	    byte[] hash = null;
	    
	    try 
	    {
	        MessageDigest md = MessageDigest.getInstance("SHA-256");
	        hash = md.digest(s.getBytes());
	        
	        StringBuilder sb = new StringBuilder();
		    
		    for (int i = 0; i < hash.length; ++i) 
		    {
		        String hex = Integer.toHexString(hash[i]);
		        if (hex.length() == 1) 
		        {
		            sb.append(0);
		            sb.append(hex.charAt(hex.length() - 1));
		        } 
		        else 
		        {
		            sb.append(hex.substring(hex.length() - 2));
		        }
		    }
		    
		    String hashStr = sb.toString() + "_" + RandomStringUtils.randomAlphanumeric(20).toUpperCase();
		    
		    return hashStr;
	    } 
	    catch (NoSuchAlgorithmException e) 
	    { 
	    	LOG.error("NoSuchAlgorithmException::" + e.getMessage(), e);
	    	
	    	String hashStr = RandomStringUtils.randomAlphanumeric(20).toUpperCase();
	    	
	    	return hashStr;
	    }
	}
}
