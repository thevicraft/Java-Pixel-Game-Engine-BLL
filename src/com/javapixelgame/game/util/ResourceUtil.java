package com.javapixelgame.game.util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class ResourceUtil {
	
	public static final File getResourceFile(String pathInsideJAR) throws URISyntaxException {
		File file = null;
//		
//		InputStream stream = ResourceUtil.class.getResourceAsStream(pathInsideJAR);
//		
//		try {
//			Reader fR = new FileReader("Saved/Text.txt");
//			
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		FileUtils.copyInputStreamToFile(inputStream, file);
//		File file = new File(ResourceUtil.class.getResource(pathInsideJAR).toExternalForm());
		file = new File(new URI(ResourceUtil.class.getResource(pathInsideJAR).toString().replace(" ","%20")).getSchemeSpecificPart());
		return file;
	}
	
	
//	public static File streamToFile(InputStream in) {
//	    if (in == null) {
//	        return null;
//	    }
//
//	    try {
//	        File f = File.createTempFile(String.valueOf(in.hashCode()), ".tmp");
//	        f.deleteOnExit();
//
//	        FileOutputStream out = new FileOutputStream(f);
//	        byte[] buffer = new byte[1024];
//
//	        int bytesRead;
//	        while ((bytesRead = in.read(buffer)) != -1) {
//	            out.write(buffer, 0, bytesRead);
//	        }
//
//	        return f;
//	    } catch (IOException e) {
//	        LOGGER.error(e.getMessage(), e);
//	        return null;
//	    }
//	}
}
