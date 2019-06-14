/*******************************************************************************
 * Copyright (c) 2017, 2018, 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package io.openliberty.guides.watson.toneanalyser;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import com.ibm.cloud.sdk.core.service.exception.NotFoundException;
import com.ibm.cloud.sdk.core.service.exception.RequestTooLargeException;
import com.ibm.cloud.sdk.core.service.exception.ServiceResponseException;
import com.ibm.cloud.sdk.core.service.security.IamOptions;
import com.ibm.watson.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.tone_analyzer.v3.model.ToneOptions;

/**
 * Class to handle logic for requesting the Watson Tone Analyser service. The
 * API key is currently stored in plain text. This is bad practice, but
 * sufficient for the tutorial.
 * 
 * @author Scott C Curtis
 */
@ApplicationScoped
public class Analyser {

	// fields to authenticate with the service
	private String apiKey = /*Insert your Watson Tone Service API key here!*/;
	private String version = "2017-09-21";
	private String endpoint = /*Insert your Watson Tone Service URL here!*/;

	// field for processing the input
	private ToneAnalyzer toneAnalyser;

	/**
	 * Authenticates with IAM, and creates the tone analyser service. The apiKey and
	 * endpoint can be found on the user's cloud dashboard. The version is the
	 * latest version of the tone analyser.
	 */
	@PostConstruct
	public void authenticate() {
		//sign into IAM with the api key
		IamOptions options = new IamOptions.Builder().apiKey(apiKey).build();
		
		//create an instance of the tone anaylser with your credentials and current version.
		this.toneAnalyser = new ToneAnalyzer(version, options);
		
		//points where your instance is hosted
		toneAnalyser.setEndPoint(endpoint);

	}

	/**
	 * Makes a request to the ToneAnalyser service, given an input string, and
	 * returns the tone analysis of the input.
	 * 
	 * @param input - the string to be analysed.
	 * @return a ToneAnalysis object, containing the input string, and the
	 *         associated tones for the input.
	 */
	public ToneAnalysis analyseInput(String input) {
		
		String text = input;

		// can be used to specify searching for specific tones, etc.
		ToneOptions toneOptions = new ToneOptions.Builder().text(text).build();

		// stores the results of tone analysis
		ToneAnalysis toneAnalysis = null;

		try {
			// Invoke the Tone Analyser method, with the given options
			toneAnalysis = toneAnalyser.tone(toneOptions).execute().getResult();

		} catch (NotFoundException e) {
			// Handle Not Found (404) exception
			System.out.println("Service not found :" + e.getMessage());

		} catch (RequestTooLargeException e) {
			// Handle Request Too Large (413) exception
			System.out.println("Service returned status code " + e.getStatusCode() + ":" + e.getMessage());
		} catch (ServiceResponseException e) {
			// Base class for all exceptions caused by error responses from the service
			System.out.println("Service returned status code " + e.getStatusCode() + ": " + e.getMessage());
		}

		return toneAnalysis;

	}

}
