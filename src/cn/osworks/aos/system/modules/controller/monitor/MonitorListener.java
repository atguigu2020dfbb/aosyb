package cn.osworks.aos.system.modules.controller.monitor;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
public class MonitorListener extends FileAlterationListenerAdaptor{
	
	private Logger log = Logger.getLogger(MonitorListener.class);
	 
	/**
	 * 文件创建执行
	 */
	@Override
	public void onFileCreate(File file) {
		
		
		log.info("[新建]:" + file.getAbsolutePath());
		File pdfFile = new File(file.getAbsolutePath());
		try {
			PDFTextStripper stripper = new PDFTextStripper();
			PDDocument pddocument = PDDocument.load(pdfFile);
			String body = stripper.getText(pddocument);
			System.out.println();
		} catch (InvalidPasswordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
	}
 
	/**
	 * 文件创建修改
	 */
	@Override
	public void onFileChange(File file) {
		log.info("[修改]:" + file.getAbsolutePath());
	}
 
	/**
	 * 文件删除
	 */
	@Override
	public void onFileDelete(File file) {
		log.info("[删除]:" + file.getAbsolutePath());
	}
 
	/**
	 * 目录创建
	 */
	@Override
	public void onDirectoryCreate(File directory) {
		log.info("[新建]:" + directory.getAbsolutePath());
	}
 
	/**
	 * 目录修改
	 */
	@Override
	public void onDirectoryChange(File directory) {
		log.info("[修改]:" + directory.getAbsolutePath());
	}
 
	/**
	 * 目录删除
	 */
	@Override
	public void onDirectoryDelete(File directory) {
		log.info("[删除]:" + directory.getAbsolutePath());
	}
 
	@Override
	public void onStart(FileAlterationObserver observer) {
		// TODO Auto-generated method stub
		super.onStart(observer);
	}
 
	@Override
	public void onStop(FileAlterationObserver observer) {
		// TODO Auto-generated method stub
		super.onStop(observer);
	}


}
