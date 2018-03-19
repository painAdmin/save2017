package com.http;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import com.secret.EncryptUtil; 

/* 
 * 利用HttpClient进行post请求的工具类 
 */  
public class HttpClientUtil {  
    public static String doPost(String url,Map<String,Object> map,String charset){  
    	HttpClient httpClient = null;  
        HttpPost httpPost = null;  
        String result = null;  
        try{  
        	 httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(url);  
            //设置参数  
            List<NameValuePair> list = new ArrayList<NameValuePair>();  
            Iterator iterator = map.entrySet().iterator();  
            while(iterator.hasNext()){  
                Entry<String,String> elem = (Entry<String, String>) iterator.next();  
                list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));  
            }  
            if(list.size() > 0){  
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);  
                httpPost.setEntity(entity);  
            }  
            HttpResponse response = httpClient.execute(httpPost);  
            if(response != null){  
                HttpEntity resEntity = response.getEntity();  
                if(resEntity != null){  
                    result = EntityUtils.toString(resEntity,charset);  
                    System.out.println("body:::::"+result);
                }  
            }  
        }catch(Exception ex){  
            ex.printStackTrace();  
        }  
        return result;  
    }
    
    public byte[] downPost(String url,Map<String,Object> map,String charset){  
        HttpClient httpClient = null;  
        HttpPost httpPost = null;  
        byte[] result = null;  
        try{  
            httpClient = new SSLClient();  
            httpPost = new HttpPost(url);  
            //设置参数  
            List<NameValuePair> list = new ArrayList<NameValuePair>();  
            Iterator iterator = map.entrySet().iterator();  
            while(iterator.hasNext()){  
                Entry<String,String> elem = (Entry<String, String>) iterator.next();  
                list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));  
            }  
            if(list.size() > 0){  
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);  
                httpPost.setEntity(entity);  
            }  
            HttpResponse response = httpClient.execute(httpPost);  
            if(response != null){  
                HttpEntity resEntity = response.getEntity();  
                if(resEntity != null){  
                    result = EntityUtils.toByteArray(resEntity);  
                    System.out.println(result);
                }  
            }  
        }catch(Exception ex){  
            ex.printStackTrace();  
        }  
        return result;  
    }
    public byte[] getImageCode(String url,Map<String,Object> map,String charset){  
        HttpClient httpClient = null;  
        HttpGet httpGet = null;  
        byte[] result = null;  
        try{  
            httpClient = new SSLClient();  
            //设置参数  
            List<NameValuePair> list = new ArrayList<NameValuePair>();  
            Iterator iterator = map.entrySet().iterator();  
            while(iterator.hasNext()){  
                Entry<String,String> elem = (Entry<String, String>) iterator.next();  
                list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));  
            }  
            String values="";
            if(list.size() > 0){  
                values=EntityUtils.toString(new UrlEncodedFormEntity(list,charset));
            }  
            httpGet = new HttpGet(url+"&"+values);  
            HttpResponse response = httpClient.execute(httpGet);  
            if(response != null){  
                HttpEntity resEntity = response.getEntity();  
                if(resEntity != null){  
                    result = EntityUtils.toByteArray(resEntity);  
                    System.out.println(result);
                }  
            }  
        }catch(Exception ex){  
            ex.printStackTrace();  
        }  
        return result;  
    }
    
    public String doPostZzg(String url,Map<String,Object> map){  
        HttpClient httpClient = null;  
        HttpPost httpPost = null;  
        String result = null;  
        try{  
            httpClient = new SSLClient();  
            httpPost = new HttpPost(url);  
            //设置参数  
            List<NameValuePair> list = new ArrayList<NameValuePair>();  
            Iterator iterator = map.entrySet().iterator();  
            while(iterator.hasNext()){  
                Entry<String,String> elem = (Entry<String, String>) iterator.next();  
                list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));  
            }  
            if(list.size() > 0){  
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");  
                httpPost.setEntity(entity);  
            }  
            HttpResponse response = httpClient.execute(httpPost);  
            if(response != null){  
                HttpEntity resEntity = response.getEntity();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if(resEntity != null){  
                    InputStream is = resEntity.getContent();
                    int len=0;
                    byte[] b=new byte[2014];
                    while((len = is.read(b)) !=  -1){
                       baos.write(b, 0, len);
                   }
                }  
                result=new String(baos.toByteArray());
            }  
            
        }catch(Exception ex){  
            ex.printStackTrace();  
        }   finally {
            httpClient.getConnectionManager().shutdown(); // 关闭连接,释放资源
        }
        return result;  
    }
    public static void main(String[] args) throws Exception {
    	
        String appKey ="6df61e17e0b71980";
        String query = "translation";
        String salt = String.valueOf(System.currentTimeMillis());
//        String from = "zh-CHS";
//        String to = "EN";
        String from = "EN";
        String to = "zh-CHS";
        String sign = EncryptUtil.encodeByMD5(appKey + query + salt+ "iiXpwwiH7lmJ48V00sookAlHBQdw179s");
        Map params = new HashMap();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);
        params.put("sign", sign);
        params.put("salt", salt);
        params.put("appKey", appKey);
        String result=doPost("http://openapi.youdao.com/api", params,"UTF-8");
   
        System.out.println("body:::::::::"+result);
    }
}  