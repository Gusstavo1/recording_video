package com.global.recordingvideo;

import java.util.Random;

public class IdAletorio {

	public int idAleatorio(){
		int respuesta=0;
		Random r = new Random(); // id random para notificacion
		respuesta = r.nextInt(1000+1);
		return respuesta;
	}

}
