package com.platzi.RandomPhoto;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PhotoService {

	static TrustManager[] trustAllCerts = new TrustManager[]{
		    new X509TrustManager() {
		        @Override
		        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
		        }

		        @Override
		        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
		        }

		        @Override
		        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		            return new java.security.cert.X509Certificate[]{};
		        }
		    }
		};
	
	public static void seeImage() throws IOException, NoSuchAlgorithmException, KeyManagementException {
		// 1. Vamos a traer los datos de la API
//		OkHttpClient client = new OkHttpClient().newBuilder().build();
//		
//		Request request = new Request.Builder().url("https://random.imagecdn.app/500/150").method("GET", null).build();
//		
//		Response response = client.newCall(request).execute();
		
//		OkHttpClient client = new OkHttpClient().newBuilder().build();
//		Request request = new Request.Builder().url("https://api.thecatapi.com/v1/images/search").method("GET", null).build();
//		Response response = client.newCall(request).execute();
		
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
		
		OkHttpClient.Builder newBuilder = new OkHttpClient.Builder();
		newBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
		newBuilder.hostnameVerifier((hostname, session) -> true);
		
		OkHttpClient newClient = newBuilder.build();
		Response response= newClient.newCall(new Request.Builder().url("https://api.thecatapi.com/v1/images/search").build()).execute();
		
		String elJson = response.body().string();
		
		System.out.println(response.toString());
		System.out.println(response.body().string());
		//cortar corchetes
		elJson = elJson.substring(1, elJson.length());
		elJson = elJson.substring(0, elJson.length()-1);
		
		//crear objeto de clase Gson
		Gson gson = new Gson();
		Photo imagen = gson.fromJson(elJson, Photo.class);
		
		//Redimensionar 
		BufferedImage image = null;

		try {
			URL url = new URL(imagen.getUrl());
//			HttpURLConnection http = (HttpURLConnection) url.openConnection();
//			http.addRequestProperty("User-Agent", "");
			image = ImageIO.read(url);
			
			ImageIcon fondoImagen = new ImageIcon(image);
			
			HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
			httpcon.addRequestProperty("User-Agent","");
			
			BufferedImage bufferedImage = ImageIO.read(httpcon.getInputStream());
			if(fondoImagen.getIconWidth()> 800) {
	            Image fondo = fondoImagen.getImage();
	            Image modificada = fondo.getScaledInstance(800, 400, java.awt.Image.SCALE_SMOOTH);
	            fondoImagen = new ImageIcon(modificada);
			}
	        String menu =  "Opciones: \n1.- Cambiar Imagen \n2.-Favorito \n3.-Volver ";
	        String botones[] = { "Ver Otra imagen", "Favoritos", "Volver"};
	        String id_image = imagen.getId();
	        String opcion = (String) JOptionPane.showInputDialog(null,menu,id_image,JOptionPane.INFORMATION_MESSAGE
	                ,fondoImagen,botones,botones[0]);
	      
	        int seleccion = -1;
	        
	        for(int i = 0; i < botones.length;i++){
	            if(opcion.equals(botones[i])){
	                seleccion = i;
	            }
	        }
	        
	        switch(seleccion){
	            case 0:
	            	seeImage();
	                break;
	            case 1:
	            	favourite(imagen);
	                break;
	            default:
	                break;             
	        }
		}catch(IOException ex){
			System.out.println(ex);
		}
	}
	
	public static void favourite(Photo imagen) {
        try{
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			MediaType mediaType = MediaType.parse("text/plain");
			RequestBody body = RequestBody.create(mediaType, "");
			Request request = new Request.Builder()
					.url("https://api.thecatapi.com/v1/favourites?Content-Type=application/json&x-api-key="+imagen.getApiKey())
					.method("POST", body).build();
			Response response = client.newCall(request).execute();         
                  
        }catch(IOException e){
            System.out.println(e);
        }
	}
}
