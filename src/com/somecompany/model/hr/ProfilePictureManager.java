package com.somecompany.model.hr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**********************************
 * Profile Picture Manager
 ***********************************/

public class ProfilePictureManager {
	// 상수 필드
	private final static int MAX_FILE_SIZE = 512000;	// 500 * 1024 byte = 500kb
	
	// updateProfilePictureFile: temp에 저장된 파일 이동, 기존 이미지 파일 존재 여부 검사 & update
	public static void updateProfilePictureFile(
			File sourceFile, int employeeId, 
			String targetPath, ProfilePicture oldPicture) 
					throws Exception {
		
		if(sourceFile.exists() && sourceFile.isFile() 
				&& sourceFile.length() <= MAX_FILE_SIZE) {
			// 기존 사진이 있다면 삭제한다
			if(oldPicture != null) {
				String realFileName = employeeId + "_" + oldPicture.getPictureFileName();
				File oldPictureFile = new File(targetPath + "/" + realFileName);
				
				if(oldPictureFile.exists() && oldPictureFile.isFile())
					oldPictureFile.delete();
			}
			
			// 임시 저장된 soruceFile을 목적 디렉토리로 복사한다
			copyFile(sourceFile.getAbsolutePath(), 
					targetPath + "/" + employeeId + "_" + sourceFile.getName());
			
			// 임시파일 soruceFile을 삭제
			sourceFile.delete();
		} else {
			throw new Exception("file 에러: 업로드 파일이 존재하지 않거나 크기를 초과하였습니다");
		}
	}
	
	// uploadNewProfilePictureFile: temp에 저장된 파일 이동
	public static void uploadNewProfilePictureFile(
			File sourceFile, int employeeId, String targetPath) 
					throws Exception {
		
		if(sourceFile.exists() && sourceFile.isFile() 
				&& sourceFile.length() <= MAX_FILE_SIZE) {
			// 임시 저장된 soruceFile을 목적 디렉토리로 복사한다
			copyFile(sourceFile.getAbsolutePath(), 
					targetPath + "/" + employeeId + "_" + sourceFile.getName());
			
			// 임시파일 soruceFile을 삭제
			sourceFile.delete();
		} else {
			throw new Exception("file 에러: 업로드 파일이 존재하지 않거나 크기를 초과하였습니다");
		}
	}
		
	// deleteProfilePictureFile: delete
	public static void deleteProfilePictureFile(String target) {
		File targetFile = new File(target);
		
		if(targetFile.isFile() && targetFile.exists())
			targetFile.delete();
	}
	
	public static void copyFile(String source, String target) 
			throws IOException {
		FileOutputStream out = null;
		FileInputStream in = null;
		
		try {
			out = new FileOutputStream(target);
			in = new FileInputStream(source);
			byte[] buf = new byte[256];
			
			int len = 0;
			
			while((len = in.read(buf)) >= 0) {
				out.write(buf, 0, len);
			}
		} finally {
			try {
				out.close();
				in.close();
			} catch(Exception e) { }
		}
	}
}
