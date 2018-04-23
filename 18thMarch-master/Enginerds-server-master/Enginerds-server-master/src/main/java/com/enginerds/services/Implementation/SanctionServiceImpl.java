package com.enginerds.services.Implementation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enginerds.ServerApplication;
import com.enginerds.domain.SanctionList;
import com.enginerds.domain.Transaction;
import com.enginerds.repositories.SanctionRepository;
import com.enginerds.services.SanctionService;

@Service("sanctionService")
public class SanctionServiceImpl implements SanctionService{

	@Autowired
	SanctionRepository sanctionRepository;
	private static final Logger logger = LogManager.getLogger(ServerApplication.class);
	
	@Override
	public SanctionList saveSanctionEntry(SanctionList sanctionList) throws Exception {
		return sanctionRepository.save(sanctionList);
	}

	@Override
	public void readSanctionList(String filename) {		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line = new String();
			while((line=br.readLine()) != null)
			{
			    if(line.contains("Name"))
			    {
				    line = line.substring(9);
				    StringTokenizer st = new StringTokenizer(line,",");
				    SanctionList sanction;
				    while (st.hasMoreTokens()) 
				    {
			    		sanction = new SanctionList(st.nextToken().replaceAll("\\s", ""));
			    		try {
							saveSanctionEntry(sanction);
						} catch (Exception e) {
							e.printStackTrace();
						}
				    } 
			    }
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}		
	}	
	@Override
	public boolean CheckSanctionList(Transaction transaction)
	{
    	logger.info("\nINSIDE CHECK SANCTION LIST FUNCTION");
    	for(SanctionList s: sanctionRepository.findAll())
		{
			String Name1=transaction.getPayerName();
			String Name2=transaction.getPayeeName();
			
			if(Name1.equalsIgnoreCase(s.getName()) || Name2.equalsIgnoreCase(s.getName()))
			{
				logger.info("\nYOUR NAME IS IN THE SANCTION LIST");
				return false;
			}
		}		
		return true;
	}
}
