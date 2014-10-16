package com.example.ankurtest1;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import android.util.Log;

public class Tweet {
	
	private String nome;
	private String usuario;
	private String urlImagemPerfil;
	private String mensagem;
	private String dataEnvio;
	private String rtcount;
	private String favcount;
	
	public Tweet() {
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getRt() {
		return rtcount;
	}

	public void setRt(String rtcount) {
		this.rtcount = rtcount;
	}
	public String getFav() {
		return favcount;
	}

	public void setFav(String favcount) {
		this.favcount = favcount;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getUrlImagemPerfil() {
		return urlImagemPerfil;
	}

	public void setUrlImagemPerfil(String url) {
		this.urlImagemPerfil = url;
	}

	public String getMensagem() {
		return mensagem;
	}
	
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public String getData() {
		return dataEnvio;
	}
	
	public void setData(String data) {
		String dataSemTimeZone = removerTimeZone(data);
		this.dataEnvio = formatarData(dataSemTimeZone);
	}
	
	private String formatarData(String data){
		String strData = null;
		TimeZone tzUTC = TimeZone.getTimeZone("UTC");
		SimpleDateFormat formatoEntrada = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.US);
		formatoEntrada.setTimeZone(tzUTC);
		SimpleDateFormat formatoSaida = new SimpleDateFormat("EEE, dd/MM/yy, 'às' HH:mm");
		
		try {
			strData = formatoSaida.format(formatoEntrada.parse(data));
		} catch (ParseException e) {
		Log.e("Erro parser data", Log.getStackTraceString(e));
		}
		return strData;
	}
	
	
	private String removerTimeZone(String data){

		return data.replaceFirst("(\\s[+|-]\\d{4})", "");
	}

}
