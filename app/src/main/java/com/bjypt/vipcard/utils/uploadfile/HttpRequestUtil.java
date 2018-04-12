package com.bjypt.vipcard.utils.uploadfile;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/* 
 * 此类用来发送HTTP请求 
 * */
public class HttpRequestUtil {

	public static String getHttpPostResult(String url, List<NameValuePair> values) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		try {
			if (values != null) {
				httpPost.setEntity(new UrlEncodedFormEntity(values));
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpPost);

			return EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 发送GET请求
	 * 
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public static String sendGetRequest(String url, Map<String, String> params) throws Exception {
		StringBuilder buf = new StringBuilder(url);
		Set<Entry<String, String>> entrys = null;
		// 如果是GET请求，则请求参数在URL中
		if (params != null && !params.isEmpty()) {
			buf.append("?");
			entrys = params.entrySet();
			for (Entry<String, String> entry : entrys) {
				buf.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
			}
			buf.deleteCharAt(buf.length() - 1);
		}
		HttpGet httpGet = new HttpGet(buf.toString());
		HttpClient httpClient = new DefaultHttpClient();
		InputStream inputStream = null;
		String result = "";
		// 发送请求的响应
		HttpResponse httpResponse = httpClient.execute(httpGet);
		// 代表接收的http消息，服务器返回的消息都在httpEntity
		HttpEntity httpEntity = httpResponse.getEntity();
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			inputStream = httpEntity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line = "";
			while ((line = reader.readLine()) != null) {
				result = result + line;
			}
		}
		return result;
	}

	/**
	 * 发送POST请求
	 * 
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public static URLConnection sendPostRequest(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
		StringBuilder buf = new StringBuilder();
		Set<Entry<String, String>> entrys = null;
		// 如果存在参数，则放在HTTP请求体，形如name=aaa&age=10
		if (params != null && !params.isEmpty()) {
			entrys = params.entrySet();
			for (Entry<String, String> entry : entrys) {
				buf.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
			}
			buf.deleteCharAt(buf.length() - 1);
		}
		URL url1 = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		OutputStream out = conn.getOutputStream();
		out.write(buf.toString().getBytes("UTF-8"));
		if (headers != null && !headers.isEmpty()) {
			entrys = headers.entrySet();
			for (Entry<String, String> entry : entrys) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		conn.getResponseCode(); // 为了发送成功
		return conn;
	}

	/**
	 * 直接通过HTTP协议提交数据到服务器,实现如下面表单提交功能: <FORM METHOD=POST
	 * ACTION="http://192.168.0.200:8080/ssi/fileload/test.do"
	 * enctype="multipart/form-data"> <INPUT TYPE="text" NAME="name"> <INPUT
	 * TYPE="text" NAME="id"> <input type="file" name="imagefile"/> <input
	 * type="file" name="zip"/> </FORM>
	 * 
	 * @param path
	 *            上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://
	 *            www.itcast.cn或http://192.168.1.10:8080这样的路径测试)
	 * @param params
	 *            请求参数 key为参数名,value为参数值
	 * @param file
	 *            上传文件
	 */
	public static boolean uploadFiles(String path, Map<String, String> params, FormFile[] files) throws Exception {
		// 数据分隔线
		final String BOUNDARY = "---------------------------7da2137580612";
		// 数据结束标志"---------------------------7da2137580612--"
		final String endline = "--" + BOUNDARY + "--\r\n";

		// 下面两个for循环都是为了得到数据长度参数，依据表单的类型而定
		// 首先得到文件类型数据的总长度(包括文件分割线)
		int fileDataLength = 0;
		if (files != null && files.length != 0) {
			for (FormFile uploadFile : files) {// 得到文件类型数据的总长度
				StringBuilder fileExplain = new StringBuilder();
				fileExplain.append("--");
				fileExplain.append(BOUNDARY);
				fileExplain.append("\r\n");
				fileExplain.append("Content-Disposition: form-data;name=\"" + uploadFile.getParameterName() + "\";filename=\"" + uploadFile.getFilname() + "\"\r\n");
				fileExplain.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");
				fileExplain.append("\r\n");
				fileDataLength += fileExplain.length();
				if (uploadFile.getInStream() != null) {
					fileDataLength += uploadFile.getFile().length();
				} else {
					fileDataLength += uploadFile.getData().length;
				}
			}
		}

		StringBuilder textEntity = new StringBuilder();
		if (params != null && !params.isEmpty()) {
			for (Entry<String, String> entry : params.entrySet()) {// 构造文本类型参数的实体数据
				textEntity.append("--");
				textEntity.append(BOUNDARY);
				textEntity.append("\r\n");
				textEntity.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
				textEntity.append(entry.getValue());
				textEntity.append("\r\n");
			}
		}
		// 计算传输给服务器的实体数据总长度(文本总长度+数据总长度+分隔符)
		int dataLength = textEntity.toString().getBytes().length + fileDataLength + endline.getBytes().length;

		URL url = new URL(path);
		// 默认端口号其实可以不写
		int port = url.getPort() == -1 ? 80 : url.getPort();
		// 建立一个Socket链接
		Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);
		// 获得一个输出流（从Android流到web）
		OutputStream outStream = socket.getOutputStream();
		// 下面完成HTTP请求头的发送
		String requestmethod = "POST " + url.getPath() + " HTTP/1.1\r\n";
		// 构建accept
		outStream.write(requestmethod.getBytes());
		// 构建language
		String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
		outStream.write(accept.getBytes());
		// 构建contenttype
		String language = "Accept-Language: zh-CN\r\n";
		outStream.write(language.getBytes());
		// 构建contenttype
		String contenttype = "Content-Type: multipart/form-data; boundary=" + BOUNDARY + "\r\n";
		outStream.write(contenttype.getBytes());
		// 构建contentlength
		String contentlength = "Content-Length: " + dataLength + "\r\n";
		outStream.write(contentlength.getBytes());
		// 构建alive
		String alive = "Connection: Keep-Alive\r\n";
		outStream.write(alive.getBytes());
		// 构建host
		String host = "Host: " + url.getHost() + ":" + port + "\r\n";
		outStream.write(host.getBytes());
		// 写完HTTP请求头后根据HTTP协议再写一个回车换行
		outStream.write("\r\n".getBytes());
		// 把所有文本类型的实体数据发送出来
		outStream.write(textEntity.toString().getBytes());
		// 把所有文件类型的实体数据发送出来
		if (files != null && files.length != 0) {
			for (FormFile uploadFile : files) {
				StringBuilder fileEntity = new StringBuilder();
				fileEntity.append("--");
				fileEntity.append(BOUNDARY);
				fileEntity.append("\r\n");
				fileEntity.append("Content-Disposition: form-data;name=\"" + uploadFile.getParameterName() + "\";filename=\"" + uploadFile.getFilname() + "\"\r\n");
				fileEntity.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");
				outStream.write(fileEntity.toString().getBytes());
				// 边读边写
				if (uploadFile.getInStream() != null) {
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = uploadFile.getInStream().read(buffer, 0, 1024)) != -1) {
						outStream.write(buffer, 0, len);
					}
					uploadFile.getInStream().close();
				} else {
					outStream.write(uploadFile.getData(), 0, uploadFile.getData().length);
				}
				outStream.write("\r\n".getBytes());
			}
		}
		// 下面发送数据结束标志，表示数据已经结束
		outStream.write(endline.getBytes());
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		// 读取web服务器返回的数据，判断请求码是否为200，如果不是200，代表请求失败
		if (reader.readLine().indexOf("200") == -1) {
			return false;
		}
		outStream.flush();
		outStream.close();
		reader.close();
		socket.close();
		return true;
	}

	/**
	 * 提交数据到服务器
	 * 
	 * @param path
	 *            上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://
	 *            www.itcast.cn或http://192.168.1.10:8080这样的路径测试)
	 * @param params
	 *            请求参数 key为参数名,value为参数值
	 * @param file
	 *            上传文件
	 */
	public static boolean uploadFile(String path, Map<String, String> params, FormFile file) throws Exception {
		return uploadFiles(path, params, new FormFile[] { file });
	}

	/**
	 * 将输入流转为字节数组
	 * 
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] read2Byte(InputStream inStream) throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}

	/**
	 * 将输入流转为字符串
	 * 
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	public static String read2String(InputStream inStream) throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return new String(outSteam.toByteArray(), "UTF-8");
	}

	/**
	 * 发送xml数据
	 * 
	 * @param path
	 *            请求地址
	 * @param xml
	 *            xml数据
	 * @param encoding
	 *            编码
	 * @return
	 * @throws Exception
	 */
	public static byte[] postXml(String path, String xml, String encoding) throws Exception {
		byte[] data = xml.getBytes(encoding);
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "text/xml; charset=" + encoding);
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));
		conn.setConnectTimeout(5 * 1000);
		OutputStream outStream = conn.getOutputStream();
		outStream.write(data);
		outStream.flush();
		outStream.close();
		if (conn.getResponseCode() == 200) {
			return read2Byte(conn.getInputStream());
		}
		return null;
	}

	/**
	 * 直接通过HTTP协议提交数据到服务器,实现表单提交功能
	 * 
	 * @param actionUrl
	 *            上传路径
	 * @param params
	 *            请求参数 key为参数名,value为参数值
	 * @param file
	 *            上传文件
	 */
	public static String uploadFilesHttp(String actionUrl, Map<String, String> params, FormFile[] files) {
		try {
			String BOUNDARY = "---------7d4a6d158c9"; // 数据分隔线
			String MULTIPART_FORM_DATA = "multipart/form-data";

			URL url = new URL(actionUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setChunkedStreamingMode(5);
			conn.setDoInput(true);// 允许输入
			conn.setDoOutput(true);// 允许输出
			conn.setUseCaches(false);// 不使用Cache
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);
			// conn.setConnectTimeout(Common.HTTP.TIMEOUT);
			// conn.setReadTimeout(Common.HTTP.TIMEOUT);
			StringBuilder sb = new StringBuilder();
			// 上传的表单参数部分，格式请参考文章
			for (Entry<String, String> entry : params.entrySet()) {// 构建表单字段内容
				sb.append("--");
				sb.append(BOUNDARY);
				sb.append("\r\n");
				sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
				sb.append(entry.getValue());
				sb.append("\r\n");
			}

			DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
			outStream.write(sb.toString().getBytes());// 发送表单字段数据
			// 上传的文件部分，格式请参考文章
			if (files != null) {
				int temp = 0;
				for (FormFile file : files) {
					StringBuilder split = new StringBuilder();
					split.append("--");
					split.append(BOUNDARY);
					split.append("\r\n");
					split.append("Content-Disposition: form-data;name=\"img" + temp + "\";filename=\"" + file.getFile().getName() + "\"\r\n");
					temp++;
					split.append("Content-Type: " + file.getContentType() + "\r\n\r\n");
					outStream.write(split.toString().getBytes());
					if (file.getInStream() != null) {
						byte[] buffer = new byte[2048];
						int len = 0;
						while ((len = file.getInStream().read(buffer, 0, 2048)) != -1) {
							outStream.write(buffer, 0, len);
						}
						file.getInStream().close();
					} else {
						outStream.write(file.getData(), 0, file.getData().length);
					}
					// outStream.write(file.getData(), 0,
					// file.getData().length);
					outStream.write("\r\n".getBytes());
				}
			}
			byte[] end_data = ("--" + BOUNDARY + "--\r\n").getBytes();// 数据结束标志
			outStream.write(end_data);
			outStream.flush();
			outStream.close();
			int cah = conn.getResponseCode();
			if (cah != 200) {
				System.out.println("请求Url 失败");
				return null;
			}
			if (cah == -1) {
				return "-1";
			}
			InputStream is = conn.getInputStream();
			String bb = inputStream2String(is);
			System.out.println(bb);
			return bb;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String inputStream2String(InputStream is) {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		try {
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	public static String uploadFileHttp(String actionUrl, Map<String, String> params, FormFile file) throws IOException {
		if (file == null) {
			return uploadFilesHttp(actionUrl, params, null);
		}
		return uploadFilesHttp(actionUrl, params, new FormFile[] { file });
	}

	// //测试函数
	// public static void main(String args[]) throws Exception {
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("name", "xiazdong");
	// params.put("age", "10");
	// HttpURLConnection conn = (HttpURLConnection) HttpRequestUtil
	// .sendGetRequest(
	// "http://192.168.0.103:8080/Server/PrintServlet",
	// params, null);
	// int code = conn.getResponseCode();
	// InputStream in = conn.getInputStream();
	// byte[]data = read2Byte(in);
	// }
}
