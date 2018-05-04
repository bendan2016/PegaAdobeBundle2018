package com.adobe.restservice;

public interface MerklePegaService {
	 public String getNBA();
	 public String getNextBestAction(String DMPJSonPayload); 
	 public String captureNBAResponse(String respPayload); 
	 public String dmpIDSync(String IDPayload); 
	
	 
}
