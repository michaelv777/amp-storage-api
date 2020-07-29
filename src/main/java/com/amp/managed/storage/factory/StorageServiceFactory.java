/**
 * 
 */
package com.amp.managed.storage.factory;

import com.amp.managed.storage.service.StorageService;

/**
 * @author MVEKSLER
 *
 */
public class StorageServiceFactory 
{
	public StorageServiceFactory() 
	{
		
	}

	public static StorageService getStorageService(Class<?> clazz)
	{
		try
		{
			if ( null == clazz )
			{
				return null;
			}
			else
			{
				return (StorageService)clazz.newInstance();
			}
		}
		catch( InstantiationException ie )
		{
			return null;
		}
		catch( IllegalAccessException ile )
		{
			return null;
		}
		catch( Exception e )
		{
			return null;
		}
	}
}
