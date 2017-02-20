package com.scarabcoder.domination.main;

import com.scarabcoder.domination.enums.MessageType;

public class MessageLogger {
	
	public static void logMessage(MessageType type, String message){
		String prefix = "[Domination] ";
		
		prefix += (type.equals(MessageType.ERROR) ? "[ERROR]" : (type.equals(MessageType.INFO) ? "[INFO]" : "[SUCCESS]")) + " ";
		
		System.out.println(prefix + message);
		
	}
	
}
