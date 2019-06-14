<!--
  Copyright (c) 2017, 2018, 2019 IBM Corporation and others.
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
 
  Contributors:
      IBM Corporation - initial API and implementation
-->
<html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<title>Tone Analyser</title>
</head>
<body>
	<h2>Open Liberty Watson Tone Analyser</h2>
	<div>
		<form action="analyseinput" autocomplete="off" method="GET">
			Enter your text:<br /> <input type="text" name="inputText" /> <br />
			<p>Click this button to analyse your text with Watson.</p>
			<input type="submit" value="Submit" /> <br />
		</form>
	</div>
	<hr />
	<!-- Hide this div depending if no user input -->
	<c:if test="${toneResponse != null}">
		<div>
			<h3>Analysis Complete!</h3>
			<hr />
			<p>Here are the dominant tones Watson identified in the whole
				input:</p>
			<table style="width: 100%">
				<tr>
					<td><b>Tone Name</b></td>
					<td><b>Tone Confidence Score</b></td>
				</tr>
				<!-- Entry per dominant tone identified -->
				<c:forEach items="${toneResponse.documentTone.tones}" var="tone">
					<tr>
						<td>${tone.toneName}</td>
						<td>${tone.score}</td>
					</tr>
				</c:forEach>
			</table>
			<hr />
			<!-- Show when sentencesTone list is initialised (more than one sentence) -->
			<c:if test="${fn:length(toneResponse.sentencesTone) gt 0}">
				<p>Here's a breakdown of the tones Watson identified for each
					input sentence:</p>
				<!-- Table per sentence -->
				<c:forEach items="${toneResponse.sentencesTone}" var="sentence">
					<table style="width: 100%">
						<tr>
							<td><b>Sentence Id</b></td>
							<td><b>Sentence Content</b></td>
						</tr>
						<tr>
							<td>${sentence.sentenceId}</td>
							<td>${sentence.text}</td>
						</tr>
						<tr>
							<td><b>Tone Name</b></td>
							<td><b>Tone Score</b></td>
						</tr>
						<!-- Entry per tone in sentence -->
						<c:forEach items="${sentence.tones}" var="tone">
							<tr>
								<td>${tone.toneName}</td>
								<td>${tone.score}</td>
							</tr>
							<br />
						</c:forEach>
					</table>
				</c:forEach>
				<hr />
			</c:if>
		</div>
	</c:if>
</body>
</html>
