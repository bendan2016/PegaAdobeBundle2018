package com.adobe.restservice.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.restservice.MerklePegaService;

import com.adobe.granite.contexthub.api.ContextHub;
import com.adobe.granite.contexthub.api.Mode;
import com.adobe.granite.contexthub.api.Module;
import com.adobe.granite.contexthub.api.Store;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

//This is a component so it can provide or consume services
@Component(metatype = true, label = "pega NBA service", description = "pega NBA service")

@Service
@Property(name = "pega_nba_service.address", description = "adress of pega NBA service including port", value = "http://18.220.74.152:8080/prweb/PRRestService")

public class MerklePegaServiceImp implements MerklePegaService {

	private static final String DEFAULT_ADDRESS = "http://18.220.74.152:8080/prweb/PRRestService";
	private static final String ADDRESS = "pega_nba_service.address";
	private String pega_address;

	static String NBAUri = "/PegaMKTContainer/V2/Container";
	static String CaptureRespUri = "/PegaMKTContainer/V2/CaptureResponse";
	static String DMPIDSyncUR = "/CometBizAgility/NBAACustomer/DMPIDSync";

	private static final Logger log = LoggerFactory.getLogger(MerklePegaServiceImp.class);

	public MerklePegaServiceImp() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Activate
	protected void activate(ComponentContext ctx) {
		pega_address = PropertiesUtil.toString(ctx.getProperties().get(ADDRESS), DEFAULT_ADDRESS);
	}

	@Override
	public String getNBA() {

		log.debug("Pega Json call getNBA");

		try {
			// user info

			// json

			JSONObject webAttributes1 = new JSONObject();
			webAttributes1.put("Type", "WebAttributes");
			webAttributes1.put("Value", "Y");
			webAttributes1.put("Key", "InMarket_mortgage");

			JSONObject webAttributes2 = new JSONObject();
			webAttributes2.put("Type", "WebAttributes");
			webAttributes2.put("Value", "Y");
			webAttributes2.put("Key", "seg_traveler");

			JSONObject webAttributes3 = new JSONObject();
			webAttributes3.put("Type", "WebAttributes");
			webAttributes3.put("Value", "Y");
			webAttributes3.put("Key", "ALL_VW_CC_All_pages");

			JSONArray Contexts = new JSONArray();
			Contexts.put(webAttributes1);
			Contexts.put(webAttributes2);
			Contexts.put(webAttributes3);

			JSONObject json = new JSONObject();
			// json.put("CustomerID", "117");
			// json.put("ContainerName", "TopOffers");
			json.put("ContainerName", "WebHomepage");
			json.put("Channel", "Web");
			json.put("Direction", "Inbound");
			json.put("PreviousPage", "index.html");
			json.put("CurrentPage", "savings.html");
			json.put("VisitorID", "mid_58264880555319436101703661211333037432");
			json.put("Contexts", Contexts);

			log.debug("json request = " + json.toString());

			DefaultHttpClient httpClient = new DefaultHttpClient();

			HttpPost postRequest = new HttpPost(pega_address + NBAUri);
			postRequest.addHeader("accept", "application/json");

			StringEntity input = new StringEntity(json.toString());
			input.setContentType("application/json");
			postRequest.setEntity(input);

			HttpResponse response = httpClient.execute(postRequest);

			if (response.getStatusLine().getStatusCode() != 200) {

				log.debug("json request failed = " + pega_address + NBAUri + "|"
						+ response.getStatusLine().getStatusCode());
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

			String output;
			String myJSON = "";
			while ((output = br.readLine()) != null) {
				// System.out.println(output);
				myJSON = myJSON + output;
			}

			httpClient.getConnectionManager().shutdown();
			log.debug("pega response=" + myJSON);
			return myJSON;
		}

		catch (Exception e) {
			e.printStackTrace();
			log.error("MerklePegaServiceImp request error = " + e.getMessage());
		}

		return null;

	}

	@Override
	public String captureNBAResponse(String respJsonPayload) {

		String ServiceURl = pega_address + CaptureRespUri;
		log.debug("Pega Json call captureNBAResponse = |"+ServiceURl);
		
		String result = PegaServices(ServiceURl, respJsonPayload);
		
		return result;
	}

	@Override
	public String getNextBestAction(String DMPNBAJSonPayload) {
		// TODO Auto-generated method stub
		String ServiceURl = pega_address + NBAUri;
		log.debug("Pega Json call getNextBestAction = |"+ServiceURl);
		
		String result = PegaServices(ServiceURl, DMPNBAJSonPayload);
		
		return result;
	}

	@Override
	public String dmpIDSync(String IDJSonPayload) {
		
		String ServiceURl = pega_address + DMPIDSyncUR;
		log.debug("Pega Json call dmpIDSync = |"+ServiceURl);
		
		String result = PegaServices(ServiceURl, IDJSonPayload);
		
		return result;
	}
	
	
	protected String PegaServices(String ServiceURL, String JSonPayLoad) {
		log.debug("Pega Json call PegaServices");
		try {
			JSONObject json = new JSONObject(JSonPayLoad);

			log.debug("json request PegaServices = " + json.toString());

			DefaultHttpClient httpClient = new DefaultHttpClient();

			HttpPost postRequest = new HttpPost(ServiceURL);
			postRequest.addHeader("accept", "application/json");

			StringEntity input = new StringEntity(json.toString());
			input.setContentType("application/json");
			postRequest.setEntity(input);

			HttpResponse response = httpClient.execute(postRequest);

			if (response.getStatusLine().getStatusCode() != 200) {

				log.debug("json request failed = " + ServiceURL + "|"
						+ response.getStatusLine().getStatusCode());
				throw new RuntimeException(
						"Failed : HTTP error code : " + ServiceURL + "|" + response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

			String output;
			String myJSON = "";
			while ((output = br.readLine()) != null) {
				// System.out.println(output);
				myJSON = myJSON + output;
			}

			httpClient.getConnectionManager().shutdown();
			log.debug("PegaServices response=" + myJSON);
			return myJSON;

		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("MerklePegaServiceImp PegaServices request error = " + ex.getMessage());
		}
		

		return null;
	}

}
