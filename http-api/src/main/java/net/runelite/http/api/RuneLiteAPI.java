/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.http.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class RuneLiteAPI
{
	private static final int OLDSCHOOL_VERSION = 181;

	private static final String RUNELITE_VERSION = "1.5.31.1";
	private static final String HTTP_SERVICE_VERSION = "1.5.30-SNAPSHOT";
	private static final int HTTP_SERVICE_PORT = 8080;
	private static final String commit = "9089756";

	public static final OkHttpClient CLIENT;
	public static final OkHttpClient RSCLIENT;
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private static final String STATICBASE = "https://raw.githubusercontent.com/runelite/static.runelite.net/gh-pages/";
	private static final String RUNELITE_BASE = "https://api.runelite.net/runelite-";
	public static final String RUNELITE_PRICES = getRuneLiteBase() + "/item/prices.js";
	public static final String RUNELITE_WORLDS = getRuneLiteBase() + "/worlds.js";
	private static String userAgent = "RuneLite/" + RUNELITE_VERSION + "-" + commit + ("");;

	static
	{

		CLIENT = new OkHttpClient.Builder()
			.connectTimeout(3655, TimeUnit.MILLISECONDS)
			.writeTimeout(3655, TimeUnit.MILLISECONDS)
			.followRedirects(false).followSslRedirects(true)
			.addNetworkInterceptor(chain -> {
				Request userAgentRequest = chain.request()
					.newBuilder()
					.header("User-Agent", userAgent)
					.build();
				return chain.proceed(userAgentRequest);
			})
			.build();

		RSCLIENT = new OkHttpClient.Builder()
				.pingInterval(30, TimeUnit.SECONDS)
				.connectTimeout(3655, TimeUnit.MILLISECONDS)
				.writeTimeout(3655, TimeUnit.MILLISECONDS)
				.addNetworkInterceptor(chain -> {
					Request userAgentRequest = chain.request()
							.newBuilder()
							.header("User-Agent", userAgent)
							.build();
					return chain.proceed(userAgentRequest);
				})
				.build();
	}

	public static HttpUrl getLocalApiBase()
	{
		return HttpUrl.parse("http://localhost:"+ HTTP_SERVICE_PORT +"/http-service-" + HTTP_SERVICE_VERSION);
	}

	public static HttpUrl getRuneLiteBase()
	{
		return HttpUrl.parse(RUNELITE_BASE + getVersion());
	}

	public static String getVersion()
	{
		return RUNELITE_VERSION;
	}

	public static HttpUrl getStaticBase()
	{
		return HttpUrl.parse(STATICBASE);
	}

	public static int getRsVersion()
	{
		return OLDSCHOOL_VERSION;
	}
}
