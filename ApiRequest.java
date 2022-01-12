package com.platzi.RandomPhoto;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiRequest {
	private static final OkHttpClient client = new OkHttpClient();

	public static void main(String[] args) {
		getUsers();
	}

	public static void getUsers() {
		try {
			Request request = new Request.Builder().url("http://jsonplaceholder.typicode.com/users").build();
			Response response = client.newCall(request).execute();
			System.out.println(response.body().string());
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
