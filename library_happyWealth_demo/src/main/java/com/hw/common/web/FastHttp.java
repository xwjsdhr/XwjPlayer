package com.hw.common.web;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.xmlpull.v1.XmlSerializer;

import com.alibaba.fastjson.JSON;
import com.hw.common.utils.basicUtils.CommonUtil;
import com.hw.common.utils.basicUtils.MLogUtil;
import com.hw.common.utils.basicUtils.StringUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;

/**
 * ----------------------同步----------------------<br>
 * 需要自己开启新的线程 和构造handler<br>
 * 网络请求类 封装：<br>
 * 1 支持同步get请求<br>
 * 2 支持同步post请求<br>
 * 3 支持同步webserver请求<br>
 * 4 支持同步form请求<br>
 * ----------------------异步----------------------<br>
 * 无需开新的线程 无需handler<br>
 * 5 支持异步get请求<br>
 * 6 支持异步post请求<br>
 * 7 支持异步webserver请求<br>
 * 8 支持异步form请求<br>
 * ----------------------轮询----------------------<br>
 * 9 支持get定时轮训<br>
 * 10 支持post定时轮训<br>
 * 11 支持webserver定时轮询<br>
 * 12 支持自定义配置文件InternetConfig 可以配置编码格式 以及轮询时间（默认为30秒）<br>
 * （注意：返回的数据ResponseEntity，根据getStatus来获取状态<br>
 * 如果为FastHttp.result_ok则返回正常<br>
 * 如果为FastHttp.result_url_err则为url错误<br>
 * 如果为FastHttp.result_net_err则为网络错误<br>
 * 如果为FastHttp.result_file_err则为表单提交时文件不存在<br>
 * 如果为FastHttp.result_protocol_err则为协议错误<br>
 * 返回的数据格式为字符串 自己解析成需要格式）
 * 
 */
public class FastHttp {

	public static final int result_ok = 0;
	public static final int result_net_err = -1;
	private static final String METHOD = "method";

	public static final String BOUNDARY = java.util.UUID.randomUUID().toString();
	public static final String PREFIX = "--", LINEND = "\r\n";
	public static final String MULTIPART_FROM_DATA = "multipart/form-data";

	private static String cookies;
	private static MyHostnameVerifier hnv = new MyHostnameVerifier();

	/**
	 * 同步 get请求 无参数 使用默认的配置文件
	 * 
	 * @author gdpancheng@gmail.com 2013-5-20 下午2:39:53
	 * @param url
	 *            请求的url
	 * @return ResponseEntity 返回的数据
	 */
	public static ResponseEntity get(String url) {
		return get(url, null, InternetConfig.defaultConfig());
	}

	/**
	 * 同步 get请求 无参数 使用自定义的配置文件
	 * 
	 * @author gdpancheng@gmail.com 2013-5-20 下午2:40:21
	 * @param url
	 *            url 请求的url
	 * @param config
	 *            自定义的配置文件
	 * @return ResponseEntity 返回的数据
	 */
	public static ResponseEntity get(String url, InternetConfig config) {
		return get(url, null, config);
	}

	/**
	 * 同步 get请求 有参数 默认配置
	 * 
	 * @author gdpancheng@gmail.com 2013-5-20 下午2:41:20
	 * @param url
	 *            请求的连接
	 * @param params
	 *            请求的参数
	 * @return ResponseEntity 返回的数据
	 */
	public static ResponseEntity get(String url, LinkedHashMap<String, String> params) {
		return get(url, params, InternetConfig.defaultConfig());
	}

	/**
	 * 同步 get请求 有参数 自定义配置
	 * 
	 * @author gdpancheng@gmail.com 2013-6-8 下午1:58:23
	 * @param url
	 *            请求的连接
	 * @param params
	 *            请求的参数
	 * @param config
	 *            配置
	 * @return ResponseEntity 返回结果
	 */
	public static ResponseEntity get(String url, LinkedHashMap<String, String> params, InternetConfig config) {
		config.setRequest_type(InternetConfig.request_get);
		if (params != null) {
			if (url.indexOf("\\?") != -1) {
				url = url + "?";
			} else {
				url = url + "&";
			}
			for (Map.Entry<String, String> entry : params.entrySet()) {
				url = url + entry.getKey() + "=" + entry.getValue() + "&";
			}
			url = url.substring(0, url.length() - 1);
		}
		ResponseEntity responseEntity = new ResponseEntity();
		responseEntity.setUrl(url);
		responseEntity.setParams(params);
		responseEntity.setKey(config.getKey());

//		// 判断是否需要离线
//		if (config.isSave()) {
//			if (!NetStatusUtil.isNetworkAvailable(MyApplications.getApplication())) {
//				MLogUtil.e("无法连接到网络 将获取离线数据");
//				String result = HttpCache.getUrlCache(url, params);
//				if (result != null) {
//					responseEntity.setContent(result, false);
//					responseEntity.setStatus(result_ok);
//					return responseEntity;
//				}
//			}
//		}

		try {
			HttpURLConnection conn = getDefaultHttpClient(url, config);
			InputStream inStream = conn.getInputStream();
			getCookies(config, responseEntity, conn);
			responseEntity.setContent(inputStreamToString(inStream, config.getCharset()), config.isSave());
			responseEntity.setConfig(config);
			conn.disconnect();
			responseEntity.setStatus(result_ok);
			if (responseEntity.getContentAsString().length() == 0) {
				responseEntity.setStatus(result_net_err);
			}
			return responseEntity;
		} catch (Exception e) {
			responseEntity.setStatus(result_net_err);
			e.printStackTrace();
		}
		return responseEntity;
	}

	/**
	 * 同步 post请求 默认下载配置 无参数
	 * 
	 * @author gdpancheng@gmail.com 2013-5-20 下午2:46:18
	 * @param url
	 *            请求Url
	 * @return ResponseEntity 返回的结果
	 */
	public static ResponseEntity post(String url) {
		return post(url, null, InternetConfig.defaultConfig());
	}

	/**
	 * 同步 post请求 有参数 默认下载配置
	 * 
	 * @author gdpancheng@gmail.com 2013-5-20 下午2:47:38
	 * @param url
	 *            请求的连接
	 * @param params
	 *            请求的参数
	 * @return ResponseEntity 返回的数据
	 */
	public static ResponseEntity post(String url, LinkedHashMap<String, String> params) {
		return post(url, params, InternetConfig.defaultConfig());
	}

	/**
	 * 同步 post请求 有参数 自定义下载配置
	 * 
	 * @author gdpancheng@gmail.com 2013-5-20 下午2:48:49
	 * @param url
	 *            请求url
	 * @param params
	 *            请求参数
	 * @param config
	 *            下载配置
	 * @return ResponseEntity 返回值
	 */
	public static ResponseEntity post(String url, LinkedHashMap<String, String> params, InternetConfig config) {
		config.setRequest_type(InternetConfig.request_post);
		ResponseEntity responseEntity = new ResponseEntity();
		responseEntity.setUrl(url);
		responseEntity.setParams(params);
		responseEntity.setKey(config.getKey());

//		// 判断是否需要离线
//		if (config.isSave()) {
//			if (!NetStatusUtil.isNetworkAvailable(MyApplications.getApplication())) {
//				MLogUtil.e("无法连接到网络 将获取离线数据");
//				String result = HttpCache.getUrlCache(url, params);
//				if (result != null) {
//					responseEntity.setContent(result, false);
//					responseEntity.setStatus(result_ok);
//					return responseEntity;
//				}
//			}
//		}

		try {
			HttpURLConnection conn = getDefaultHttpClient(url, config);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.connect();
			OutputStream out = conn.getOutputStream();
			String values = "";
			if (params != null && params.size() > 0) {
				if(config.getContent_type_web() == InternetConfig.content_type_json){
					for (Map.Entry<String, String> entry : params.entrySet()) {
						if(StringUtils.isEmpty(entry.getKey())){
							values = entry.getValue();
						}
					}
				}else{
					for (Map.Entry<String, String> entry : params.entrySet()) {
						values = values + entry.getKey() + "=" + entry.getValue() + "&";
					}
					values = values.substring(0, values.length() - 1);
				}
				
				out.write(values.getBytes(config.getCharset()));
			}
			out.flush();
			out.close();
			InputStream inStream = conn.getInputStream();

			getCookies(config, responseEntity, conn);

			responseEntity.setContent(inputStreamToString(inStream, config), config.isSave());
			responseEntity.setConfig(config);
			conn.disconnect();
			responseEntity.setStatus(result_ok);
			if (responseEntity.getContentAsString().length() == 0) {
				responseEntity.setStatus(result_net_err);
			}
			return responseEntity;
		} catch (Exception e) {
			responseEntity.setStatus(result_net_err);
			e.printStackTrace();
		}
		return responseEntity;
	}

	public static ResponseEntity postString(String url, String params, InternetConfig config) {
		config.setRequest_type(InternetConfig.request_post);
		ResponseEntity responseEntity = new ResponseEntity();
		responseEntity.setUrl(url);
		try {
			HttpURLConnection conn = getDefaultHttpClient(url, config);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.connect();
			OutputStream out = conn.getOutputStream();
			if (params != null && params.length() > 0) {
				out.write(params.getBytes(config.getCharset()));
			}
			out.flush();
			out.close();
			InputStream inStream = conn.getInputStream();
			cookies = conn.getHeaderField("set-cookie");
			if (cookies != null) {
				String[] cook = cookies.split(";");
				for (String cook2 : cook) {
					String[] cook3 = cook2.split("=");
					if (cook3.length > 1) {
						responseEntity.cookie(cook3[0], cook3[1]);
					}
				}
			}
			responseEntity.setContent(inputStreamToString(inStream, config.getCharset()), config.isSave());
			conn.disconnect();
			responseEntity.setStatus(result_ok);
			if (responseEntity.getContentAsString().length() == 0) {
				responseEntity.setStatus(result_net_err);
			}
			return responseEntity;
		} catch (Exception e) {
			responseEntity.setStatus(result_net_err);
			e.printStackTrace();
		}
		return responseEntity;
	}

	/**
	 * 同步 表单请求 默认下载配置 无参数
	 * 
	 * @author gdpancheng@gmail.com 2013-5-20 下午2:46:18
	 * @param url
	 *            请求Url
	 * @return ResponseEntity 返回的结果
	 */
	public static ResponseEntity form(String url) {
		return form(url, null, null, InternetConfig.defaultConfig());
	}

	/**
	 * 同步 表单请求 有参数 默认下载配置
	 * 
	 * @author gdpancheng@gmail.com 2013-5-20 下午2:47:38
	 * @param url
	 *            请求的连接
	 * @param params
	 *            请求的参数
	 * @return ResponseEntity 返回的数据
	 */
	public static ResponseEntity form(String url, LinkedHashMap<String, String> params) {
		return form(url, params, null, InternetConfig.defaultConfig());
	}

	/**
	 * 同步 表单请求 有参数 有上传文件 默认下载配置
	 * 
	 * @author gdpancheng@gmail.com 2013-6-8 下午2:01:54
	 * @param url
	 *            请求的连接
	 * @param params
	 *            请求的参数
	 * @param files
	 *            需要上传的文件集合
	 * @return
	 * @return ResponseEntity
	 */
	public static ResponseEntity form(String url, LinkedHashMap<String, String> params, HashMap<String, File> files) {
		return form(url, params, files, InternetConfig.defaultConfig());
	}
	
	public static ResponseEntity FormByBean(String url, Object obj, HashMap<String, File> files, InternetConfig config) {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.clear();
		if(obj != null){
			params.put("str", JSON.toJSONString(obj));
		}
		
		if(config == null){
			config = InternetConfig.defaultConfig();
		}else{
			config.setRequest_type(InternetConfig.request_form);
			config.setFiles(files);
		}
		
		return formProgress(url, params, files, config, config.getProgress());
	}

	/**
	 * 同步 表单请求 有参数 有上传文件 自定义下载配置 TODO(这里用一句话描述这个方法的作用)
	 * 
	 * @author gdpancheng@gmail.com 2013-6-8 下午2:02:21
	 * @param url
	 *            请求的连接
	 * @param params
	 *            请求的参数
	 * @param files
	 *            需要上传的文件集合
	 * @param config
	 * @return
	 * @return ResponseEntity
	 */
	public static ResponseEntity form(String url, LinkedHashMap<String, String> params, HashMap<String, File> files, InternetConfig config) {
		params = params == null ? new LinkedHashMap<String, String>() : params;

		config.setRequest_type(InternetConfig.request_form);
		ResponseEntity responseEntity = new ResponseEntity();
		responseEntity.setUrl(url);
		responseEntity.setParams(params);
		responseEntity.setKey(config.getKey());

//		// 判断是否需要离线
//		if (config.isSave()) {
//			if (!NetStatusUtil.isNetworkAvailable(MyApplications.getApplication())) {
//				MLogUtil.e("无法连接到网络 将获取离线数据");
//				String result = HttpCache.getUrlCache(url, params);
//				if (result != null) {
//					responseEntity.setContent(result, false);
//					responseEntity.setStatus(result_ok);
//					return responseEntity;
//				}
//			}
//		}

		try {
			HttpURLConnection conn = getDefaultHttpClient(url, config);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false); // 不允许使用缓存

			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINEND);
				sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
				sb.append("Content-Type: text/plain; charset=" + config.getCharset() + LINEND);
				sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
				sb.append(LINEND);
				sb.append(entry.getValue());
				sb.append(LINEND);
			}

			DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
			if (sb.length() > 0) {
				outStream.write(sb.toString().getBytes());
			}

			InputStream in = null;

			if (files != null) {
				for (Map.Entry<String, File> file : files.entrySet()) {

					if (!file.getValue().exists()) {
						continue;
					}

					StringBuilder sb1 = new StringBuilder();
					sb1.append(PREFIX);
					sb1.append(BOUNDARY);
					sb1.append(LINEND);
					sb1.append("Content-Disposition: form-data; name=\"" + file.getKey() + "\"; filename=\"" + file.getValue().getName() + "\"" + LINEND);
					sb1.append("Content-Type: image/pjpeg; " + LINEND);
					sb1.append(LINEND);
					outStream.write(sb1.toString().getBytes());

					InputStream is = new FileInputStream(file.getValue());
					byte[] buffer = new byte[1024];
					int len = 0;
					int count = 0;
					while ((len = is.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
						count = count + len;
					}

					is.close();
					outStream.write(LINEND.getBytes());
				}

				// 请求结束标志
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
				outStream.write(end_data);
				outStream.flush();
				// 得到响应码
				// int res = conn.getResponseCode();
				// if (res == 200) {
				in = conn.getInputStream();
				getCookies(config, responseEntity, conn);
				responseEntity.setContent(inputStreamToString(in, config.getCharset()), config.isSave());
				responseEntity.setKey(config.getKey());
				outStream.close();
				conn.disconnect();
				responseEntity.setStatus(result_ok);
				if (responseEntity.getContentAsString().length() == 0) {
					responseEntity.setStatus(result_net_err);
				}
			} else {
				InputStream inStream = conn.getInputStream();
				responseEntity.setContent(inputStreamToString(inStream, config.getCharset()), config.isSave());
				conn.disconnect();
				responseEntity.setStatus(result_ok);
				if (responseEntity.getContentAsString().length() == 0) {
					responseEntity.setStatus(result_net_err);
				}
			}
		} catch (Exception e) {
			responseEntity.setStatus(result_net_err);
			e.printStackTrace();
		}
		return responseEntity;
	}

	public interface Progress {
		public void progress(int progress);
	}

	public static ResponseEntity formProgress(String url, LinkedHashMap<String, String> params, HashMap<String, File> files, InternetConfig config, Progress progress) {
		params = params == null ? new LinkedHashMap<String, String>() : params;
		
		config.setRequest_type(InternetConfig.request_form);
		ResponseEntity responseEntity = new ResponseEntity();
		responseEntity.setUrl(url);
		responseEntity.setParams(params);
		responseEntity.setKey(config.getKey());

//		// 判断是否需要离线
//		if (config.isSave()) {
//			if (!NetStatusUtil.isNetworkAvailable(MyApplications.getApplication())) {
//				MLogUtil.e("无法连接到网络 将获取离线数据");
//				String result = HttpCache.getUrlCache(url, params);
//				if (result != null) {
//					responseEntity.setContent(result, false);
//					responseEntity.setStatus(result_ok);
//					return responseEntity;
//				}
//			}
//		}

		try {
			long all_count = getOrtherLength(params, files, config);

			long read_count = 0;

			config.setAll_length(all_count);
			HttpURLConnection conn = getDefaultHttpClient(url, config);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false); // 不允许使用缓存

			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINEND);
				sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
				sb.append("Content-Type: text/plain; charset=" + config.getCharset() + LINEND);
				sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
				sb.append(LINEND);
				sb.append(entry.getValue());
				sb.append(LINEND);
			}
			
//			MLogUtil.e("read_count "+read_count);
//			MLogUtil.e("all_count "+all_count);
			BufferedOutputStream outStream = new BufferedOutputStream(conn.getOutputStream());
			// 将开头部分写出 
			if (sb.length() > 0) {
				outStream.write(sb.toString().getBytes());

				if (progress != null) {
					read_count = read_count + sb.toString().getBytes().length;
//					MLogUtil.e("progress1--- "+read_count * 100 / all_count);
					progress.progress((int) (read_count * 100 / all_count));
				}
			}

			InputStream in = null;
			// 写出文件数据
			if (files != null) {
				for (Map.Entry<String, File> file : files.entrySet()) {

					if (!file.getValue().exists()) {
						continue;
					}

					StringBuilder sb1 = new StringBuilder();
					sb1.append(PREFIX);
					sb1.append(BOUNDARY);
					sb1.append(LINEND);
					sb1.append("Content-Disposition: form-data; name=\"" + file.getKey() + "\"; filename=\"" + file.getValue().getName() + "\"" + LINEND);
					sb1.append("Content-Type: image/pjpeg; " + LINEND);
					sb1.append(LINEND);
					outStream.write(sb1.toString().getBytes());

					MLogUtil.e("sb1.toString() "+sb1.toString());
					if (progress != null) {
						read_count = read_count + sb1.toString().getBytes().length;
//						MLogUtil.e("progress2--- "+read_count * 100 / all_count);
						progress.progress((int) (read_count * 100 / all_count));
					}

					InputStream is = new FileInputStream(file.getValue());
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
						read_count = read_count + len;
						if (progress != null) {
//							MLogUtil.e("progress3--- "+read_count * 100 / all_count);
							progress.progress((int) (read_count * 100 / all_count));
						}
					}

					is.close();
					outStream.write(LINEND.getBytes());
					if (progress != null) {
						read_count = read_count + LINEND.getBytes().length;
//						MLogUtil.e("progress4--- "+read_count * 100 / all_count);
						progress.progress((int) (read_count * 100 / all_count));
					}
				}

				// 将结尾部分写出  
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
				outStream.write(end_data);
				outStream.flush();
				if (progress != null) {
					read_count = read_count + end_data.length;
//					MLogUtil.e("progress5--- "+read_count * 100 / all_count);
					progress.progress((int) (read_count * 100 / all_count));
				}
				// 得到响应码
				// int res = conn.getResponseCode();
				// if (res == 200) {
				in = conn.getInputStream();
				getCookies(config, responseEntity, conn);
				responseEntity.setContent(inputStreamToString(in, config.getCharset()), config.isSave());
				responseEntity.setKey(config.getKey());
				responseEntity.setConfig(config);
				outStream.close();
				conn.disconnect();
				responseEntity.setStatus(result_ok);
				if (responseEntity.getContentAsString().length() == 0) {
					responseEntity.setStatus(result_net_err);
				}
			} else {
				InputStream inStream = conn.getInputStream();
				responseEntity.setContent(inputStreamToString(inStream, config.getCharset()), config.isSave());
//				responseEntity.setContent(XMLtoJsonUtil.XMLtoJson(inputStreamToString(inStream, config.getCharset()), "ss", config.getCharset()), config.isSave());
				conn.disconnect();
				responseEntity.setStatus(result_ok);
				if (responseEntity.getContentAsString().length() == 0) {
					responseEntity.setStatus(result_net_err);
				}
			}
		} catch (Exception e) {
			responseEntity.setStatus(result_net_err);
			e.printStackTrace();
		}
		return responseEntity;
	}

	/**
	 * 异步表单提交
	 * 
	 * @author gdpancheng@gmail.com 2013-6-8 下午2:03:10
	 * @param url
	 * @param callBack
	 * @return void
	 */
	public static void ajaxForm(String url, AjaxCallBack callBack) {
		ajaxForm(url, null, null, InternetConfig.defaultConfig(), callBack);
	}

	/**
	 * 异步表单提交 自定义配置
	 * 
	 * @author gdpancheng@gmail.com 2013-6-8 下午2:03:22
	 * @param url
	 * @param config
	 * @param callBack
	 * @return void
	 */
	public static void ajaxForm(String url, InternetConfig config, AjaxCallBack callBack) {
		ajaxForm(url, null, null, config, callBack);
	}

	/**
	 * 异步表单提交 有参数
	 * 
	 * @author gdpancheng@gmail.com 2013-6-8 下午2:03:37
	 * @param url
	 * @param params
	 * @param callBack
	 * @return void
	 */
	public static void ajaxForm(String url, LinkedHashMap<String, String> params, AjaxCallBack callBack) {
		ajaxForm(url, params, null, InternetConfig.defaultConfig(), callBack);
	}

	/**
	 * 自定义表单提交有参数 有文件
	 * 
	 * @author gdpancheng@gmail.com 2013-6-8 下午2:03:57
	 * @param url
	 * @param params
	 * @param files
	 * @param callBack
	 * @return void
	 */
	public static void ajaxForm(String url, LinkedHashMap<String, String> params, HashMap<String, File> files, AjaxCallBack callBack) {
		ajaxForm(url, params, files, InternetConfig.defaultConfig(), callBack);
	}

	/**
	 * 自定义表单提交有参数 有文件 自定义下载配置
	 * 
	 * @author gdpancheng@gmail.com 2013-6-8 下午2:04:38
	 * @param url
	 * @param params
	 * @param files
	 * @param config
	 * @param callBack
	 * @return void
	 */
	public static void ajaxForm(String url, LinkedHashMap<String, String> params, HashMap<String, File> files, InternetConfig config, AjaxCallBack callBack) {
		config.setRequest_type(InternetConfig.request_form);
		config.setFiles(files);
		new Thread(new AjaxTask(url, params, config, callBack)).start();
	}
	
	public static void ajaxFormByBean(String url, Object obj, HashMap<String, File> files, InternetConfig config, AjaxCallBack callBack) {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		if(obj != null){
			params.put("str", JSON.toJSONString(obj));
		}
		
		if(config == null){
			config = InternetConfig.defaultConfig();
		}else{
			config.setRequest_type(InternetConfig.request_form);
			config.setFiles(files);
		}
		
		MLogUtil.e("params "+params);
		new Thread(new AjaxTask(url, params, config, callBack)).start();
	}

	/**
	 * 异步 post请求 无参数 默认下载配置器
	 * 
	 * @author gdpancheng@gmail.com 2013-5-20 下午2:49:57
	 * @param url
	 *            请求url
	 * @param callBack
	 *            回调函数
	 * @return void
	 */
	public static void ajax(String url, AjaxCallBack callBack) {
		ajax(url, null, InternetConfig.defaultConfig(), callBack);
	}

	/**
	 * 异步 post请求 无参数 自定义下载配置
	 * 
	 * @author gdpancheng@gmail.com 2013-5-20 下午2:50:48
	 * @param url
	 *            请求连接
	 * @param config
	 *            自定义下载配置
	 * @param callBack
	 *            回调函数
	 * @return void
	 */
	public static void ajax(String url, InternetConfig config, AjaxCallBack callBack) {
		ajax(url, null, config, callBack);
	}

	/**
	 * 异步 post请求 有参数 默认下载配置
	 * 
	 * @author gdpancheng@gmail.com 2013-5-20 下午2:51:48
	 * @param url
	 *            请求url
	 * @param params
	 *            请求参数
	 * @param callBack
	 *            回调函数
	 * @return void
	 */
	public static void ajax(String url, LinkedHashMap<String, String> params, AjaxCallBack callBack) {
		ajax(url, params, InternetConfig.defaultConfig(), callBack);
	}

	/**
	 * 异步 post异步获取 有参数 自定义下载配置
	 * 
	 * @author gdpancheng@gmail.com 2013-5-20 下午2:54:08
	 * @param url
	 *            请求连接
	 * @param params
	 *            请求参数
	 * @param config
	 *            请求配置
	 * @param callBack
	 *            回调函数
	 * @return void
	 */
	
	public static void ajax(String url, LinkedHashMap<String, String> params, InternetConfig config, AjaxCallBack callBack) {
		config.setRequest_type(InternetConfig.request_post);
//		MLogUtil.e("上传参数： "+params);
		new Thread(new AjaxTask(url, params, config, callBack)).start();
	}
	
	public static void ajaxByBean(String url, Object obj, AjaxCallBack callBack) {
		ajax(url,CommonUtil.convertBeans(obj),new InternetConfig(InternetConfig.request_post),callBack);
	}
	
	public static void ajaxByBeanWithCookies(String url, Object obj, AjaxCallBack callBack) {
		InternetConfig config = new InternetConfig(InternetConfig.request_post);
		config.setWithCookies(true);
		ajax(url,CommonUtil.convertBeans(obj),config,callBack);
	}
	
	public static void ajaxByBeanWithCookies(String url, Object obj,String cookie, AjaxCallBack callBack) {
		InternetConfig config = new InternetConfig(InternetConfig.request_post);
		if(StringUtils.isEmpty(cookie)){
			config.setWithCookies(true);
		}else{
			config.setCookies(cookie);
		}
		ajax(url,CommonUtil.convertBeans(obj),config,callBack);
	}
	
	public static void postByJosnWithCookies(String url, Object obj,String cookie, AjaxCallBack callBack) {
		InternetConfig config = new InternetConfig(InternetConfig.request_post);
		config.setContent_type_web(InternetConfig.content_type_json);
		if(StringUtils.isEmpty(cookie)){
			config.setWithCookies(true);
		}else{
			config.setCookies(cookie);
		}
		
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		if(obj != null){
			params.put("", JSON.toJSONString(obj));
		}
		ajax(url,params,config,callBack);
	}

	/**
	 * 异步post定时轮询
	 * 
	 * @author gdpancheng@gmail.com 2013-6-8 下午2:05:07
	 * @param url
	 * @param callBack
	 * @return void
	 */
	public static void ajax(String url, AjaxTimeCallBack callBack) {
		InternetConfig config = InternetConfig.defaultConfig();
		config.setRequest_type(InternetConfig.request_post);
		ajax(url, null, config, callBack);
	}

	/**
	 * 异步post定时轮询
	 * 
	 * @author gdpancheng@gmail.com 2013-6-8 下午2:06:54
	 * @param url
	 * @param params
	 * @param callBack
	 * @return void
	 */
	public static void ajax(String url, LinkedHashMap<String, String> params, AjaxTimeCallBack callBack) {
		InternetConfig config = InternetConfig.defaultConfig();
		config.setRequest_type(InternetConfig.request_post);
		ajax(url, params, config, callBack);
	}

	/**
	 * 异步 post异步获取 定时请求（轮询）有参数 自定义下载配置 回调函数
	 * 
	 * @author gdpancheng@gmail.com 2013-5-20 下午2:52:52
	 * @param url
	 *            请求连接
	 * @param params
	 *            请求参数
	 * @param config
	 *            请求配置
	 * @param callBack
	 *            回调函数
	 * @return void
	 */
	public static void ajax(String url, LinkedHashMap<String, String> params, InternetConfig config, AjaxTimeCallBack callBack) {
		config.setRequest_type(InternetConfig.request_post);
		new Thread(new TimeTask(url, params, config, callBack)).start();
	}

	// -----------------------------------------------------------------------------------------------------------------------------------
	// webservers请求处理

	/**
	 * 同步 WebServer get请求 无参数 使用默认的配置文件
	 * 
	 * @author gdpancheng@gmail.com 2013-5-20 下午2:39:53
	 * @param url
	 *            请求的url
	 * @return T 返回的数据
	 */
	public static ResponseEntity webServer(String url, String method) {
		return webServer(url, null, InternetConfig.defaultConfig(), method);
	}

	/**
	 * 同步 WebServer get请求 无参数 使用自定义的配置文件
	 * 
	 * @author gdpancheng@gmail.com 2013-5-20 下午2:40:21
	 * @param url
	 *            url 请求的url
	 * @param config
	 *            自定义的配置文件
	 * @return T 返回的数据
	 */
	public static ResponseEntity webServer(String url, InternetConfig config, String method) {
		return webServer(url, null, config, method);
	}

	/**
	 * 同步 WebServer get请求 有参数 默认配置
	 * 
	 * @author gdpancheng@gmail.com 2013-5-20 下午2:41:20
	 * @param url
	 *            请求的连接
	 * @param params
	 *            请求的参数
	 * @return T 返回的数据
	 */
	public static ResponseEntity webServer(String url, LinkedHashMap<String, String> params, String method) {
		return webServer(url, params, InternetConfig.defaultConfig(), method);
	}

	/**
	 * 同步 WebServer get请求 有参数 自定义下载配置
	 * 
	 * @param url
	 *            请求连接
	 * @param params
	 *            参数
	 * @param config
	 *            下载的配置
	 * @return T 返回的数据
	 */
	public static ResponseEntity webServer(String url, LinkedHashMap<String, String> params, InternetConfig config, String method) {
		long t = new Date().getTime();
		config.setRequest_type(InternetConfig.request_webserver);
		ResponseEntity responseEntity = new ResponseEntity();
		responseEntity.setUrl(url);
		responseEntity.setParams(params);
		responseEntity.setKey(config.getKey());
		
//		// 判断是否需要离线
//		if (config.isSave()) {
//			if (!NetStatusUtil.isNetworkAvailable(MyApplications.getApplication())) {
//				MLogUtil.e("无法连接到网络 将获取离线数据");
//				String result = HttpCache.getUrlCache(url, params);
//				if (result != null) {
//					responseEntity.setContent(result, false);
//					responseEntity.setStatus(result_ok);
//					return responseEntity;
//				}
//			}
//		}

		try {
			config.setMethod(method);
			HttpURLConnection conn = getDefaultHttpClient(url, config);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.connect();
			OutputStream out = conn.getOutputStream();
			String content = "";
			if (params == null) {
				params = new LinkedHashMap<String, String>();
			}
			content = getXml(params, method, config.getName_space());
			content = content.replace(" standalone='yes' ", "");
			out.write(content.getBytes("UTF-8"));
			out.flush();
			out.close();
			InputStream inStream = conn.getInputStream();
			getCookies(config, responseEntity, conn);
			responseEntity.setContent(XMLtoJsonUtil.XMLtoJson(inputStreamToString(inStream, config.getCharset()), method, config.getCharset()), config.isSave());
			responseEntity.setKey(config.getKey());
			responseEntity.setConfig(config);
			conn.disconnect();
			responseEntity.setStatus(result_ok);
			if (responseEntity.getContentAsString().length() == 0) {
				responseEntity.setStatus(result_net_err);
			}
			MLogUtil.e("请求用了" + (new Date().getTime() - t) + "毫秒");
			return responseEntity;
		} catch (Exception e) {
			responseEntity.setStatus(result_net_err);
			e.printStackTrace();
		}
		
		return responseEntity;
	}

	/**
	 * 异步 post请求 无参数 默认下载配置器
	 * 
	 * @author gdpancheng@gmail.com 2013-5-20 下午2:49:57
	 * @param url
	 *            请求url
	 * @param callBack
	 *            回调函数
	 * @return void
	 */
	public static void ajaxWebServer(String url, String method, AjaxCallBack callBack) {
		new Thread(new AjaxTask(url, null, new InternetConfig(method, InternetConfig.request_webserver), callBack)).start();
	}

	/**
	 * 异步 post请求 有参数 默认下载配置
	 * 
	 * @author gdpancheng@gmail.com 2013-5-20 下午2:51:48
	 * @param url
	 *            请求url
	 * @param params
	 *            请求参数
	 * @param callBack
	 *            回调函数
	 * @return void
	 */
	public static void ajaxWebServer(String url, String method, LinkedHashMap<String, String> params, AjaxCallBack callBack) {
		new Thread(new AjaxTask(url, params, new InternetConfig(method, InternetConfig.request_webserver), callBack)).start();
	}
	
	/**
	 * 异步 post请求 有参数 默认下载配置
	 * 
	 * @author lee
	 * @param <T>
	 * @param url
	 *            请求url
	 * @param Object obj
	 *            请求java对象
	 * @param callBack
	 *            回调函数
	 * @return void
	 */
	public static <T> void ajaxBeanWebServer(Context context,String url, String method, Object obj,final AjaxCallBack callBack) {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		if(obj != null){
			params.put("str", JSON.toJSONString(obj));
		}
		new Thread(new AjaxTask(url, params, new InternetConfig(method, InternetConfig.request_webserver), callBack)).start();
	}
	

	/**
	 * 异步 post异步获取 有参数 自定义下载配置
	 * 
	 * @author gdpancheng@gmail.com 2013-5-20 下午2:54:08
	 * @param url
	 *            请求连接
	 * @param params
	 *            请求参数
	 * @param config
	 *            请求配置
	 * @param callBack
	 *            回调函数
	 * @return void
	 */
//	public static void ajaxWebServer(String url, String method, LinkedHashMap<String, String> params, InternetConfig config, final AjaxCallBack callBack) {
//		if (config == null) {
//			config = InternetConfig.defaultConfig();
//		}
//		config.setMethod(method);
//		config.setRequest_type(InternetConfig.request_webserver);
//		new Thread(new AjaxTask(url, params, config, callBack)).start();
//	}

	/**
	 * 异步 post异步获取 定时请求（轮询）有参数 默认下载配置 回调函数
	 * 
	 * @author gdpancheng@gmail.com 2013-5-20 下午2:52:52
	 * @param url
	 *            请求连接
	 * @param params
	 *            请求参数
	 * @param config
	 *            请求配置
	 * @param callBack
	 *            回调函数
	 * @return void
	 */
	public static void ajaxWebServer(String url, String method, LinkedHashMap<String, String> params, AjaxTimeCallBack callBack) {
		new Thread(new TimeTask(url, params, new InternetConfig(method, InternetConfig.request_webserver), callBack)).start();
	}

	/**
	 * 异步 post异步获取 定时请求（轮询）有参数 自定义下载配置 回调函数
	 * 
	 * @author gdpancheng@gmail.com 2013-5-20 下午2:52:52
	 * @param url
	 *            请求连接
	 * @param params
	 *            请求参数
	 * @param config
	 *            请求配置
	 * @param callBack
	 *            回调函数
	 * @return void
	 */
	public static void ajaxWebServer(String url, String method, LinkedHashMap<String, String> params, InternetConfig config, AjaxTimeCallBack callBack) {
		new Thread(new TimeTask(url, params, new InternetConfig(method, InternetConfig.request_webserver), callBack)).start();
	}

	/**
	 * 异步get获取
	 * 
	 * @author gdpancheng@gmail.com 2013-5-22 下午1:34:42
	 * @param url
	 *            请求路径
	 * @param callBack
	 *            回调函数
	 * @return void
	 */
	public static void ajaxGet(String url, AjaxCallBack callBack) {
		ajaxGet(url, null, InternetConfig.defaultConfig(), callBack);
	}
	
	public static void ajaxGetWithCookies(String url, String cookie,AjaxCallBack callBack) {
		InternetConfig config = new InternetConfig(InternetConfig.request_get);
		if(StringUtils.isEmpty(cookie)){
			config.setWithCookies(true);
		}else{
			config.setCookies(cookie);
		}
		
		ajaxGet(url, null, config, callBack);
	}
	
	public static void ajaxGetWithCookies(String url,AjaxCallBack callBack) {
		InternetConfig config = new InternetConfig(InternetConfig.request_get);
		config.setWithCookies(true);
		ajaxGet(url, null, config, callBack);
	}

	/**
	 * 异步get获取
	 * 
	 * @author gdpancheng@gmail.com 2013-5-22 下午1:35:20
	 * @param url
	 *            请求路径
	 * @param config
	 *            配置文件
	 * @param callBack
	 *            回调函数
	 * @return void
	 */
	public static void ajaxGet(String url, InternetConfig config, AjaxCallBack callBack) {
		ajaxGet(url, null, config, callBack);
	}

	/**
	 * 异步get获取
	 * 
	 * @author gdpancheng@gmail.com 2013-5-22 下午1:36:13
	 * @param url
	 *            请求路径
	 * @param params
	 *            参数
	 * @param callBack
	 *            回调函数
	 * @return void
	 */
	public static void ajaxGet(String url, LinkedHashMap<String, String> params, AjaxCallBack callBack) {
		ajaxGet(url, params, InternetConfig.defaultConfig(), callBack);
	}

	/**
	 * get异步获取
	 * 
	 * @author gdpancheng@gmail.com 2013-5-22 下午1:36:39
	 * @param url
	 *            请求路径
	 * @param params
	 *            参数
	 * @param config
	 *            下载配置
	 * @param callBack
	 *            回调函数
	 * @return void
	 */
	public static void ajaxGet(String url, LinkedHashMap<String, String> params, InternetConfig config, AjaxCallBack callBack) {
		if (config == null) {
			config = InternetConfig.defaultConfig();
		}
		config.setRequest_type(InternetConfig.request_get);
		new Thread(new AjaxTask(url, params, config, callBack)).start();
	}

	/**
	 * get异步获取 定时请求（轮询）
	 * 
	 * @author gdpancheng@gmail.com 2013-5-22 下午1:38:32
	 * @param url
	 *            请求路径
	 * @param params
	 *            参数
	 * @param config
	 *            下载配置
	 * @param callBack
	 *            回调函数
	 * @return void
	 */
	public static void ajaxGet(String url, LinkedHashMap<String, String> params, InternetConfig config, AjaxTimeCallBack callBack) {
		if (config == null) {
			config = InternetConfig.defaultConfig();
		}
		config.setRequest_type(InternetConfig.request_get);
		new Thread(new TimeTask(url, params, config, callBack)).start();
	}
	

	@SuppressLint("HandlerLeak")
	public static class AjaxTask extends basicRunable implements Runnable {
		private AjaxCallBack mCallBack;
		private Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (mCallBack.stop()) {
					return;
				}
				mCallBack.callBack((ResponseEntity) msg.obj);
			}
		};

		public AjaxTask(String url, LinkedHashMap<String, String> params, InternetConfig internetConfig, final AjaxCallBack callBack) {
			this.mCallBack = callBack;
			this.internetConfig = internetConfig;
			this.url = url;
			this.params = params;
		}

		public void run() {
			Message msg = new Message();
			switch (this.internetConfig.getRequest_type()) {
			case InternetConfig.request_post:
				msg.obj = post(url, params, this.internetConfig);
				break;
			case InternetConfig.request_get:
				msg.obj = get(url, params, this.internetConfig);
				break;
			case InternetConfig.request_file:
				break;
			case InternetConfig.request_webserver:
				msg.obj = webServer(url, params, this.internetConfig, this.internetConfig.getMethod());
				break;
			case InternetConfig.request_form:
				// if (internetConfig.getProgress()!=null) {
				msg.obj = formProgress(url, params, this.internetConfig.getFiles(), this.internetConfig, this.internetConfig.getProgress());
				// }else {
				// msg.obj = form(url, params, internetConfig.getFiles(), internetConfig);
				// }
				break;
			default:
				break;
			}
			mHandler.sendMessage(msg);
		}
	}

	static class basicRunable {
		InternetConfig internetConfig;
		String url;
		LinkedHashMap<String, String> params;
	}

	@SuppressLint("HandlerLeak")
	static class TimeTask extends basicRunable implements Runnable {
		private AjaxTimeCallBack mCallBack;

		private Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				mCallBack.callBack((ResponseEntity) msg.obj);
			}
		};

		public TimeTask(String url, LinkedHashMap<String, String> params, InternetConfig internetConfig, AjaxTimeCallBack callBack) {
			this.mCallBack = callBack;
			this.internetConfig = internetConfig;
			this.url = url;
			this.params = params;
		}

		public void run() {
			while (mCallBack.getIsContinue()) {
				Message msg = new Message();
				switch (internetConfig.getRequest_type()) {
				case InternetConfig.request_post:
					msg.obj = post(url, params, internetConfig);
					break;
				case InternetConfig.request_get:
					msg.obj = get(url, params, internetConfig);
					break;
				case InternetConfig.request_file:
					break;
				case InternetConfig.request_webserver:
					msg.obj = webServer(url, params, internetConfig, internetConfig.getMethod());
					break;
				default:
					break;
				}
				mHandler.sendMessage(msg);
				try {
					Thread.sleep(internetConfig.getTime());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static HttpURLConnection getDefaultHttpClient(String urls, InternetConfig config) throws ProtocolException, MalformedURLException, IOException {
		if (config.isHttps()) {
			SSLContext sslContext = null;
			SSLSocketFactoryEx socketFactoryEx = null;
			try {
				socketFactoryEx = new SSLSocketFactoryEx(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			sslContext = socketFactoryEx.sslContext;
			if (sslContext != null) {
				HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
			}
			// HttpsURLConnection.setDefaultHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			HttpsURLConnection.setDefaultHostnameVerifier(hnv);
		}
		URL url = new URL(urls);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		if (config.getFiles() != null) {
			conn.setFixedLengthStreamingMode((int) config.getAll_length());
		}
		conn.setConnectTimeout(config.getTimeout());
		conn.setReadTimeout(config.getTimeout());
		String method = "POST";
		if (Build.VERSION.SDK!=null&&Build.VERSION.SDK_INT>13) {
			conn.setRequestProperty("Connection", "close");
        }
		if (config.getRequest_type() == InternetConfig.request_get) {
			method = "GET";
			conn.setRequestMethod(method);
			conn.setRequestProperty("Charsert", config.getCharset());
			conn.setRequestProperty("Content-Type", config.getContent_type_web());
		} else if (config.getRequest_type() == InternetConfig.request_post) {
			method = "POST";
			conn.setRequestMethod(method);
			// conn.setRequestProperty("Charsert", config.getCharset());
			conn.setRequestProperty("Content-Type", config.getContent_type_web());
		} else if (config.getRequest_type() == InternetConfig.request_webserver) {
			method = "POST";
			conn.setRequestMethod(method);
			conn.setRequestProperty("Charsert", config.getCharset());
			conn.setRequestProperty("Content-Type", InternetConfig.content_type_xml);
			conn.setRequestProperty("SOAPAction", config.getName_space() + config.getMethod());
			conn.setRequestProperty(METHOD, config.getMethod());
		} else if (config.getRequest_type() == InternetConfig.request_form) {
			method = "POST";
			conn.setRequestMethod(method);
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Charsert", config.getCharset());
			conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
		}
		
		conn.setRequestProperty("User-Agent", InternetConfig.UA);
				
		if (!StringUtils.isEmpty(config.getCookies())) {
			conn.setRequestProperty("Cookie", config.getCookies());
		}else if(config.isWithCookies() && !StringUtils.isEmpty(cookies)){
			conn.setRequestProperty("Cookie", cookies);
		}

		if (config.getHead() != null) {
			HashMap<String, Object> head = config.getHead();
			for (String key : head.keySet()) {
				conn.setRequestProperty(key, head.get(key).toString());
			}
		}
		
		return conn;
	}

	public static String inputStreamToString(InputStream in, InternetConfig config) throws IOException {
		StringBuffer out = new StringBuffer();
		BufferedReader input = new BufferedReader(new InputStreamReader(in, config.getCharset()));
		String s;
		while ((s = input.readLine()) != null) {
			out.append(s);
		}
		
		return unicode2String(out.toString());
	}
	
	
	public static String inputStreamToString(InputStream in, String charset) throws IOException {
		StringBuffer out = new StringBuffer();
		BufferedReader input = new BufferedReader(new InputStreamReader(in, charset));
		String s;
		while ((s = input.readLine()) != null) {
			out.append(s);
		}
		
		return out.toString();
	}
	
	 /**
     * unicode编码解析输入流
     * 
     * @param str
     * @return
     */
    private static String unicode2String(String str) {
    	Pattern REG_UNICODE = Pattern.compile("[0-9A-Fa-f]{4}");
        StringBuilder sb = new StringBuilder();
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char c1 = str.charAt(i);
            if (c1 == '\\' && i < len - 1) {
                char c2 = str.charAt(++i);
                if (c2 == 'u' && i <= len - 5) {
                    String tmp = str.substring(i + 1, i + 5);
                    Matcher matcher = REG_UNICODE.matcher(tmp);
                    if (matcher.find()) {
                        sb.append((char) Integer.parseInt(tmp, 16));
                        i = i + 4;
                    } else {
                        sb.append(c1).append(c2);
                    }
                } else {
                    sb.append(c1).append(c2);
                }
            } else {
                sb.append(c1);
            }
        }
        return sb.toString();
    }

	private static String getXml(LinkedHashMap<String, String> data, String method, String name_space) {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "soap:Envelope");
			serializer.attribute("", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			serializer.attribute("", "xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
			serializer.attribute("", "xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
			serializer.startTag("", "soap:Body");
			serializer.startTag("", method);
			serializer.attribute("", "xmlns", name_space);
			for (String key : data.keySet()) {
				serializer.startTag("", key);
				serializer.text(data.get(key).toString());
				serializer.endTag("", key);
			}
			serializer.endTag("", method);
			serializer.endTag("", "soap:Body");
			serializer.endTag("", "soap:Envelope");
			serializer.endDocument();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer.getBuffer().toString();
	}

	static class MyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			MLogUtil.e("Warning: URL Host: " + hostname + " vs. " + session.getPeerHost());
			return true;
		}
	}

	private static void getCookies(InternetConfig config, ResponseEntity responseEntity, HttpURLConnection conn) {
		if (config.isWithCookies()) {
			if(!StringUtils.isEmpty(conn.getHeaderField("set-cookie"))){
				cookies = conn.getHeaderField("set-cookie");
			} 
		}
		responseEntity.setCookie(conn.getHeaderField("set-cookie"));
//		Map<String, List<String>> resHeaders = conn.getHeaderFields();
//		for (Map.Entry<String, List<String>> entry : resHeaders.entrySet()) {
//			String name = entry.getKey();
//			if (name == null) {
//				continue;
//			}
//
//			if (!name.equalsIgnoreCase("Set-Cookie")) {
//				continue;
//			}
//
//			for (String cok : entry.getValue()) {
//				String[] cook = cok.split(";");
//				for (String cook2 : cook) {
//					String[] cook3 = cook2.split("=");
//					if (cook3.length > 1) {
//						responseEntity.cookie(cook3[0], cook3[1]);
//					}
//				}
//			}
//		}
	}

	private static int getOrtherLength(LinkedHashMap<String, String> params, HashMap<String, File> files, InternetConfig config) {
		long count = 0;
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + config.getCharset() + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}
		count = count + sb.toString().getBytes().length;

		if (files != null) {
			for (Map.Entry<String, File> file : files.entrySet()) {
				if (!file.getValue().exists()) {
					continue;
				}
				count = count + file.getValue().length();
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				sb1.append("Content-Disposition: form-data; name=\"" + file.getKey() + "\"; filename=\"" + file.getValue().getName() + "\"" + LINEND);
				sb1.append("Content-Type: image/pjpeg; " + LINEND);
				sb1.append(LINEND);
				count = count + sb1.toString().getBytes().length;
				count = count + LINEND.getBytes().length;
			}
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			count = count + end_data.length;
		}
		return (int) count;
	}
}
