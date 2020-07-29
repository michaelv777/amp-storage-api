/**
 * 
 */
package com.amp.managed.storage.controller.test;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.amp.jpaentities.mo.WorkerConfigurationListMO;
import com.amp.jpaentities.mo.WorkerConfigurationMO;
import com.amp.managed.storage.controller.ConfigurationController;

/**	
 * @author MVEKSLER
 *
 */
public class ConfigurationControllerTest 
{
	private static final Logger LOGGER = 
			LoggerFactory.getLogger(ConfigurationControllerTest.class);
	
	private static URI getConfigBaseURI() 
	{
		try 
		{
			//URI uri = new URI("http://localhost:21080/amp-storage-api/ConfigurationService");
			URI uri = new URI("http://localhost:8090/amp-storage-api/ConfigurationService");
			return uri;
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
	}
	
	@SuppressWarnings("unused")
	private static URI getStatusBaseURI()
	{
		try 
		{
			URI uri = new URI("http://localhost:21080/amp-storage-api/StatusService");
			
			return uri;
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
		String cMethodName = "";
		
		try
		{
			StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
	        StackTraceElement ste = stacktrace[1];
	        cMethodName = ste.getMethodName();
	        
			
		}
		catch( Exception e)
		{
			System.out.println(cMethodName + "::Exception:" + e.getMessage());
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception 
	{
		
	}
	
	@SuppressWarnings("unused")
	@Ignore
	@Test
	public void testStorageBaseLogic() 
	{
		String cMethodName = "";
		
		try
		{
			StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
	        StackTraceElement ste = stacktrace[1];
	        cMethodName = ste.getMethodName();
	        
	        ConfigurationController cService = new ConfigurationController();
	        
		}
		catch( Exception e)
		{
			LOGGER.error(cMethodName + "::Exception:" + e.getMessage(), e);
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
		}
	}
	//---------------
	
	@Ignore
	@Test
	public void testGetWorkerConfigV1() 
	{
		String cMethodName = "";
		
		try
		{
			StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
	        StackTraceElement ste = stacktrace[1];
	        cMethodName = ste.getMethodName();
	        
	        String cWorkerName = "FacebookWorkerBean";
    		
	        ClientHttpRequestFactory requestFactory = 
					new HttpComponentsClientHttpRequestFactory();
	        
	        RestTemplate restTemplate = new RestTemplate(requestFactory);
	        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
	        		   
		    final String endpointURL = getConfigBaseURI().toString() + "/getWorkerConfig?cWorkerName={0}";
		    
		    String formattedURL = MessageFormat.format(endpointURL, cWorkerName);
		    
		    LOGGER.info(cMethodName + "::formattedURL: " + formattedURL);
		    
		    HttpHeaders headers = new HttpHeaders();   
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
		    
		    HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);
		    
		    ResponseEntity<WorkerConfigurationListMO> response = restTemplate.exchange(
		    		formattedURL, HttpMethod.GET, requestEntity, WorkerConfigurationListMO.class);
		  
		    WorkerConfigurationListMO cSourceConfigurations =  response.getBody();
    		for ( WorkerConfigurationMO cSourceConfig : cSourceConfigurations.cSourceConfiguration)
    		{
    			LOGGER.info(cMethodName + "::" + cSourceConfig.getConfigkey() + ":" + cSourceConfig.getConfigvalue());
    		}
		}
		catch( Exception e)
		{
			LOGGER.error(cMethodName + "::Exception:" + e.getMessage(), e);
		}
	}
	
	//---------------
	@Test
	public void testGetWorkerConfigV2() 
	{
		String cMethodName = "";
		
		try
		{
			StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
	        StackTraceElement ste = stacktrace[1];
	        cMethodName = ste.getMethodName();
	        
	        String cWorkerName = "FacebookWorkerBean";
    		
	        ClientHttpRequestFactory requestFactory = 
					new HttpComponentsClientHttpRequestFactory();
	        
	        RestTemplate restTemplate = new RestTemplate(requestFactory);
	        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
	        		   
		    final String endpointURL = getConfigBaseURI().toString() + "/getWorkerConfig";
		    
		    LOGGER.info(cMethodName + "::formattedURL: " + endpointURL);
		    
		    HttpHeaders headers = new HttpHeaders();   
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
		    
		    Map<String, String> params = new HashMap<String, String>();
		    params.put("cWorkerName", cWorkerName);
		    
		    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(endpointURL);
		    for (Map.Entry<String, String> entry : params.entrySet()) {
		        builder.queryParam(entry.getKey(), entry.getValue());
		    }
		    
		    HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);
		    
		    ResponseEntity<WorkerConfigurationListMO> response = restTemplate.exchange(
		    		builder.toUriString(), HttpMethod.GET, requestEntity, WorkerConfigurationListMO.class);
		  
		    WorkerConfigurationListMO cSourceConfigurations =  response.getBody();
    		for ( WorkerConfigurationMO cSourceConfig : cSourceConfigurations.cSourceConfiguration)
    		{
    			LOGGER.info(cMethodName + "::" + cSourceConfig.getConfigkey() + ":" + cSourceConfig.getConfigvalue());
    		}
		}
		catch( Exception e)
		{
			LOGGER.error(cMethodName + "::Exception:" + e.getMessage(), e);
		}
	}
	
	//---------------
	
	/*
	@Ignore
	@Test
	public void testGetWorkerConfigSA() 
	{
		String cMethodName = "";
		
		try
		{
			StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
	        StackTraceElement ste = stacktrace[1];
	        cMethodName = ste.getMethodName();
	        
	        String cWorkerName = "FacebookWorkerBean";
    		
	        ConfigurationController cConfigService = 
	        		new ConfigurationController();

	        
	        javax.ws.rs.core.Response response = cConfigService.getWorkerConfig(cWorkerName);
    		
	        if ( response != null )
	        {
	        	Object responseObject = response.getEntity();
	        	
	        	System.out.println(cMethodName + "::" + responseObject.toString());
	        }
	        
		}
		catch( Exception e)
		{
			System.out.println(cMethodName + "::Exception:" + e.getMessage());
		}
	}
	*/
}
