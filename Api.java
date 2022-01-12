package com.platzi.RandomPhoto;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Api {

	public static void getDatasync(){
	    new Thread(new Runnable() {
	        @Override
	        public void run() {
	            try {
					OkHttpClient client = new OkHttpClient(); // Crear objeto OkHttpClient
					Request request = new Request.Builder().url("http://www.baidu.com") // Interfaz de solicitud. Si
																						// necesita pasar parámetros y
																						// empalmar a la parte posterior
																						// de la interfaz.
							.build(); // Crear objeto de solicitud
					Response response = null;
					response = client.newCall(request).execute(); // Obtiene el objeto Response
					if (response.isSuccessful()) {
						System.out.println(response.toString());
						System.out.println(response.body().string());
//	                Log.d("kwwl","response.code()=="+response.code());
//	                Log.d("kwwl","response.message()=="+response.message());
//	                Log.d("kwwl","res=="+response.body().string());
	                // El código en este momento se ejecuta en el subproceso. Para modificar la interfaz de usuario, utilice el controlador para saltar al hilo de la interfaz de usuario.
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }).start();
	}
	public static void main(String[] args) {
		getDatasync();
	}
}
