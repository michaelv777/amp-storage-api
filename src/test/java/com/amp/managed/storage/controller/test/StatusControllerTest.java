/**
 * 
 */
package com.amp.managed.storage.controller.test;

import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.amp.jpa.entities.WorkerData;
import com.amp.jpaentities.mo.WorkerDataListMO;
import com.amp.jpaentities.mo.WorkerDataMO;
import com.amp.service.rest.model.WorkerDataRequestRO;



/**
 * @author MVEKSLER
 *
 */
public class StatusControllerTest 
{
	private static final Logger LOGGER = 
			LoggerFactory.getLogger(StatusControllerTest.class);
	
	private static URI getConfigBaseURI() throws URISyntaxException 
	{
		try 
		{
			//URI uri = new URI("http://localhost:8090/amp-storage-api/ConfigurationService");
			URI uri = new URI("http://localhost:21080/amp-storage-api/ConfigurationService");
			
			return uri;
			
		} catch (URISyntaxException e)
		{
			LOGGER.error("::Exception:" + e.getMessage(), e);
			
			fail(e.getMessage());
			
			throw(e);
		}
	}
	
	private static URI getStatusBaseURI() throws URISyntaxException
	{
		try
		{
			//URI uri = new URI("http://localhost:8090/amp-storage-api/ConfigurationService");
			URI uri = new URI("http://localhost:21080/amp-storage-api/StatusService");
			
			return uri;
		} 
		catch (URISyntaxException e) 
		{
			LOGGER.error("::Exception:" + e.getMessage(), e);
			
			fail(e.getMessage());
			
			throw(e);
		}
	}
	
	@Ignore
	@Test
	public void testSayHello() 
	{
		String cMethodName = "";
		
		try
		{
			StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
	        StackTraceElement ste = stacktrace[1];
	        cMethodName = ste.getMethodName();
	        
	        ClientHttpRequestFactory requestFactory = 
					new HttpComponentsClientHttpRequestFactory();
	        
	        RestTemplate restTemplate = new RestTemplate(requestFactory);
		    
		    final String fullUrl = getConfigBaseURI().toString() + "/sayHello";
		    
		    URI uri = new URI(fullUrl);
		    
		    LOGGER.info(cMethodName + "::" + uri.toString());
		    
		    HttpHeaders headers = new HttpHeaders();   
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
		    
		    HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);
		    
		    ResponseEntity<String> response = restTemplate.exchange(
		    		fullUrl, HttpMethod.GET, requestEntity, String.class);
		    
		    Assert.assertEquals(200, response.getStatusCodeValue());
		}
		catch( Exception e )
		{
			LOGGER.error(cMethodName + "::Exception:" + e.getMessage(), e);
			
			fail(e.getMessage());
		}
	}
	
	
	@Ignore
	@Test
	public void testSetItemOpStatus() 
	{
		String cMethodName = "";
		
		try
		{
			StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
	        StackTraceElement ste = stacktrace[1];
	        cMethodName = ste.getMethodName();
	        
	        String sourceName  = "1774237332905314";
	        String targetName  = "1774237332905314"; 
	        String workerName  = "FacebookWorkerBean"; 
	        String opTypeName  = "ProcessPost"; 
		    String itemKey     = "1774237332905314";
		    String description = "1774237332905314"; 
		    String status      = "Normal";
    		String threadName  = "FacebookWorkerBeanThread1";
    		
		    WorkerDataRequestRO requestObject = new WorkerDataRequestRO();
		    requestObject.setSourceName(sourceName);
		    requestObject.setTargetName(targetName);
		    requestObject.setWorkerName(workerName);
		    requestObject.setThreadName(threadName);
		    requestObject.setOpTypeName(opTypeName);
		    requestObject.setItemKey(itemKey);
		    requestObject.setDescription(description);
		    requestObject.setStatus(status);
		      
		    HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
		    
		    ClientHttpRequestFactory requestFactory = 
					new HttpComponentsClientHttpRequestFactory();
	        
	        RestTemplate restTemplate = new RestTemplate(requestFactory);
	        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
	        		   
		    final String endpointURL = getStatusBaseURI().toString() + "/setItemOpStatus";
		    
		    LOGGER.info(cMethodName + "::formattedURL: " + endpointURL);
	        
		    HttpEntity<?> requestEntity = new HttpEntity<WorkerDataRequestRO>(requestObject, headers);
		   
		    ResponseEntity<String> response = restTemplate.exchange(
		    		endpointURL, HttpMethod.POST, requestEntity, String.class);
	        
    		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

			String responseBody = response.getBody();
    		
			LOGGER.info(cMethodName + "::" + responseBody);
    		
		}
		catch( Exception e)
		{
			LOGGER.error(cMethodName + "::Exception:" + e.getMessage(), e);
			
			fail(e.getMessage());
		}
	}
	
	//---------------
	
	@Ignore
	@Test
	public void testSetItemOpStatusMO() 
	{
		String cMethodName = "";
		
		try
		{
			StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
	        StackTraceElement ste = stacktrace[1];
	        cMethodName = ste.getMethodName();
	        
	        long sourceId  = 3;
	        long targetId  = 3; 
	        long statusId  = 1;
	        long workerId  = 1;
	        
	        String sourceName  = "1774237332905314";
	        String targetName  = "1774237332905314"; 
	        @SuppressWarnings("unused")
			String workerName  = "FacebookWorkerBean"; 
	        String opTypeName  = "ReadPosts"; 
		    String itemKey     = "1774237332905314";
		    String description = "1774237332905314"; 
		    String statusName  = "Normal";
    		
		    WorkerDataMO cWorkerDataMO = new WorkerDataMO();
		    cWorkerDataMO.getcWorkerData().getSource().setSourceid(sourceId);
		    cWorkerDataMO.getcWorkerData().getSource().setName(sourceName);
		    
		    cWorkerDataMO.getcWorkerData().getTarget().setTargetid(targetId);
		    cWorkerDataMO.getcWorkerData().getTarget().setName(targetName);

		    cWorkerDataMO.getcWorkerData().getWorker().setWorkerid(workerId);
		    
		    cWorkerDataMO.getcWorkerData().getOperationtypeM().setOperationtypeid(5);
		    cWorkerDataMO.getcWorkerData().getOperationtypeM().setName(opTypeName);
		    
		    cWorkerDataMO.getcWorkerData().setItemid(itemKey);
		    cWorkerDataMO.getcWorkerData().setDescription(description);
		    
		    cWorkerDataMO.getcWorkerData().getStatusM().setStatusid(statusId);
		    cWorkerDataMO.getcWorkerData().getStatusM().setName(statusName);
		    
		    HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
		    
		    ClientHttpRequestFactory requestFactory = 
					new HttpComponentsClientHttpRequestFactory();
	        
	        RestTemplate restTemplate = new RestTemplate(requestFactory);
	        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
	        		   
		    final String endpointURL = getStatusBaseURI().toString() + "/setItemOpStatusMO";
		    
		    LOGGER.info(cMethodName + "::formattedURL: " + endpointURL);
	        
		    HttpEntity<?> requestEntity = new HttpEntity<WorkerDataMO>(cWorkerDataMO, headers);
		   
		    ResponseEntity<String> response = restTemplate.exchange(
		    		endpointURL, HttpMethod.POST, requestEntity, String.class);
	        
    		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

			String responseBody = response.getBody();
    		
			LOGGER.info(cMethodName + "::" + responseBody);
    		
		}
		catch( Exception e)
		{
			LOGGER.error(cMethodName + "::Exception:" + e.getMessage(), e);
			
			fail(e.getMessage());
		}
	}
	
	
	
	@Ignore
	@Test
	public void testGetItemOpStatusFacebook() 
	{
		String cMethodName = "";
		
		try
		{
			StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
	        StackTraceElement ste = stacktrace[1];
	        cMethodName = ste.getMethodName();
	        
	        String sourceName  = "1774237332905314";
	        String targetName  = "1774237332905314"; 
	        String workerName  = "FacebookWorkerBean";
	        String threadName  = "FacebookWorkerBeanThread1";
	        //String opTypeName  = "PostLink"; 
	        String opTypeName  = "ProcessPost";
		    String itemKey     = "1774237332905314_1830718860590494";
		    
		    WorkerDataRequestRO requestObject = new WorkerDataRequestRO();
		    requestObject.setSourceName(sourceName);
		    requestObject.setTargetName(targetName);
		    requestObject.setWorkerName(workerName);
		    requestObject.setThreadName(threadName);
		    requestObject.setOpTypeName(opTypeName);
		    requestObject.setItemKey(itemKey);
		    
		    HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
		    
		    ClientHttpRequestFactory requestFactory = 
					new HttpComponentsClientHttpRequestFactory();
	        
	        RestTemplate restTemplate = new RestTemplate(requestFactory);
	        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
	        		   
		    final String endpointURL = getStatusBaseURI().toString() + "/getItemOpStatus";
		    
		    LOGGER.info(cMethodName + "::formattedURL: " + endpointURL);
	        
		    HttpEntity<?> requestEntity = new HttpEntity<WorkerDataRequestRO>(requestObject, headers);
		   
		    ResponseEntity<WorkerDataListMO> response = restTemplate.exchange(
		    		endpointURL, HttpMethod.POST, requestEntity, WorkerDataListMO.class);
	        
    		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

    		WorkerDataListMO cWorkerDataListMO = response.getBody();
			
    		for ( WorkerDataMO cWorkerDataMO : cWorkerDataListMO.cWorkerData)
    		{
    			WorkerData cWorkerData = cWorkerDataMO.cWorkerData;
    			
    			System.out.println(cMethodName + "::" + 
    					cWorkerData.getItemid() + ":" +
    					cWorkerData.getOperationtypeM().getName() + ":" +
    					cWorkerData.getUpdatedate().toString());
    		}
		}
		catch( Exception e)
		{
			LOGGER.error(cMethodName + "::Exception:" + e.getMessage(), e);
			
			fail(e.getMessage());
		}
	}
	
	
	@Ignore
	@Test
	public void testGetItemOpStatusYoutube() 
	{
		String cMethodName = "";
		
		try
		{
			StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
	        StackTraceElement ste = stacktrace[1];
	        cMethodName = ste.getMethodName();
	        
	        String sourceName  = "Amazon";
	        String targetName  = "Youtube Video"; 
	        String workerName  = "YoutubeWorkerBean";
	        String threadName  = "YoutubeWorkerBeanThread1";
	        //String opTypeName  = "PostLink"; 
	        String opTypeName  = "ProcessPost";
		    String itemKey     = "ZpJ7r0JtWn8";
		   
		    WorkerDataRequestRO requestObject = new WorkerDataRequestRO();
		    requestObject.setSourceName(sourceName);
		    requestObject.setTargetName(targetName);
		    requestObject.setWorkerName(workerName);
		    requestObject.setThreadName(threadName);
		    requestObject.setOpTypeName(opTypeName);
		    requestObject.setItemKey(itemKey);
		    
		    HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
		    
		    ClientHttpRequestFactory requestFactory = 
					new HttpComponentsClientHttpRequestFactory();
	        
	        RestTemplate restTemplate = new RestTemplate(requestFactory);
	        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
	        		   
		    final String endpointURL = getStatusBaseURI().toString() + "/getItemOpStatus";
		    
		    LOGGER.info(cMethodName + "::formattedURL: " + endpointURL);
	        
		    HttpEntity<?> requestEntity = new HttpEntity<WorkerDataRequestRO>(requestObject, headers);
		   
		    ResponseEntity<WorkerDataListMO> response = restTemplate.exchange(
		    		endpointURL, HttpMethod.POST, requestEntity, WorkerDataListMO.class);
	        
    		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

    		WorkerDataListMO cWorkerDataListMO = response.getBody();
		    
    		for ( WorkerDataMO cWorkerDataMO : cWorkerDataListMO.cWorkerData)
    		{
    			WorkerData cWorkerData = cWorkerDataMO.cWorkerData;
    			
    			LOGGER.info(cMethodName + "::" + 
    					cWorkerData.getItemid() + ":" +
    					cWorkerData.getOperationtypeM().getName() + ":" +
    					cWorkerData.getUpdatedate().toString());
    		}
		}
		catch( Exception e)
		{
			LOGGER.error(cMethodName + "::Exception:" + e.getMessage(), e);
			
			fail(e.getMessage());
		}
	}
	
}
