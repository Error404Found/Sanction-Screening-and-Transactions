package com.enginerds.services.Implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.enginerds.ServerApplication;

public class RollbackException extends Exception 
{
	private static final Logger logger = LogManager.getLogger(ServerApplication.class);

	RollbackException()
	{
		logger.warn("TRANSACTION IS ROLLED BACK");
	}
	
}
