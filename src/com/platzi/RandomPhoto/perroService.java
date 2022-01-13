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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class perroService {
	
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
	
	public static void SSL() throws NoSuchAlgorithmException, KeyManagementException, IOException {
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
		
		OkHttpClient.Builder newBuilder = new OkHttpClient.Builder();
		newBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
		newBuilder.hostnameVerifier((hostname, session) -> true);
		
		OkHttpClient newClient = newBuilder.build();
		Response response= newClient.newCall(new Request.Builder().url("https://dog.ceo/api/breeds/image/random").build()).execute();
		
//		System.out.println(response.toString());
//		System.out.println(response.body().string());
		String elJson = response.body().string(); 
		Gson gson = new Gson();
		perroPhoto imagen = gson.fromJson(elJson, perroPhoto.class);
		
		//Redimensionar 
		BufferedImage image = null;
		try {
			URL url = new URL(imagen.getMessage());

//			image = ImageIO.read(url);
			
			HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
			httpcon.addRequestProperty("User-Agent","");
			
			BufferedImage bufferedImage = ImageIO.read(httpcon.getInputStream());
			
			ImageIcon fondoImagen = new ImageIcon(bufferedImage);
			

			if(fondoImagen.getIconWidth()> 800) {
	            Image fondo = fondoImagen.getImage();
	            Image modificada = fondo.getScaledInstance(800, 400, java.awt.Image.SCALE_SMOOTH);
	            fondoImagen = new ImageIcon(modificada);
			}
	        String menu =  "Opciones: \n1.- Cambiar Imagen \n2.-Favorito \n3.-Volver ";
	        String botones[] = { "Ver Otra imagen", "Favoritos", "Volver"};
	        String id_image = imagen.getStatus();
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
	            	SSL();
	                break;
	            case 1:
//	            	favourite(imagen);
	                break;
	            default:
	                break;             
	        }
		}catch(IOException ex){
			System.out.println(ex);
		}
	}
	
	public static void seeDogImage() throws IOException, KeyManagementException, NoSuchAlgorithmException {
	
		OkHttpClient client = new OkHttpClient().newBuilder()
				  .build();
		Request request = new Request.Builder()
				  .url("https://dog.ceo/api/breeds/image/random")
				  .method("GET", null)
				  .build();
		Response response = client.newCall(request).execute();
				
		String OJson = response.body().string();
		Gson gson = new Gson();
		perroPhoto dogImage = gson.fromJson(OJson, perroPhoto.class);
		System.out.println(dogImage.getMessage());
				
	}
	
	public static void main(String[] args) throws IOException {
		try {
			SSL();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		seeDogImage();
	}
}
