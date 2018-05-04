package com.adobe.restservice;

import com.adobe.restservice.impl.MerklePegaServiceImp;

public class PegaServiceTest {

	public PegaServiceTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MerklePegaServiceImp service = new MerklePegaServiceImp();
		
		String payload = "{ \"brand\": \"Merkle Bank\", \"ip_address\": \"67.86.146.127\", \"Longitude\": \"40.562598\", \"Latitude\": \"-74.348742\", \"device\": \"Windows NT 10.0\", \"browser\": \"IE\", \"PreviousPage\": \"index.html\", \"CurrentPage\": \"/merkle-bank2/\", \"CampaignID\": \"\", \"ContainerName\": \"WebHomepage\", \"Channel\": \"Web\", \"Direction\": \"Inbound\", \"Contexts\": [{ \"Type\": \"WebAttributes\", \"Value\": \"Y\", \"Key\": \"InMarket_mortgage\" }, { \"Type\": \"WebAttributes\", \"Value\": \"Y\", \"Key\": \"seg_traveler\" }], \"VisitorID\": \"mid_58264880555319436101703661211333037432\" }";
		
		//String resp = service.getNBA();
		
		String resp = service.getNextBestAction(payload);
		
		System.out.println("resp = "+resp);

	}

}
