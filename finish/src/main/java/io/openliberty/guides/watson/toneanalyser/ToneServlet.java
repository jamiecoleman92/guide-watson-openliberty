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

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.watson.tone_analyzer.v3.model.ToneAnalysis;

/**
 * Servlet class, handling get/post requests towards the Watson 
 * Tone Analyser service, from the webpage.
 * 
 *	@author Scott C Curtis
 */
@WebServlet("analyseinput/")
public class ToneServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	Analyser toneAnalyser;

	/**
	 * Handles a GET request from the .jsp frontend, to the Watson service.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//takes the input string from the frontend form
		String input = request.getParameter("inputText");
		
		//returns the analysis of the input text
		ToneAnalysis analysis = toneAnalyser.analyseInput(input);
		
		//analysis object is now available as an attribute on frontend
		request.setAttribute("toneResponse", analysis);
		
		RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");

		rd.forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	}

}
