package cn.osworks.aos.system.modules.service.archive;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;

import cn.osworks.aos.core.redisUtils.PageData;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.TiffImage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

/**
 * 
 * PDF文件转换
 * 
 * @author shq
 *
 * @date 2017-1-13
 */
@Service
public class DocService {
	private static final int environment = 1;// 环境1：windows,2:linux(涉及pdf2swf路径问题)
	private String fileString;
	private String outputPath = "";// 输入路径，如果不设置就输出在默认位置
	private String fileName;
	private File pdfFile;
	private File jpegFile;
	private File swfFile;
	private File docFile;
	public static Jedis jedis;

	@Resource(name="redisTemplate")
	protected RedisTemplate<String, PageData> redisTemplate;
	/**
	 * 设置redisTemplate
	 * @param redisTemplate the redisTemplate to set
	 */
	public void setRedisTemplate(RedisTemplate<String, PageData> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	/**
	 * 获取 RedisSerializer
	 */
	public RedisSerializer<String> getRedisSerializer() {
		return redisTemplate.getStringSerializer();
	}

	public void DocConverter(String fileString) {
          ini(fileString);  
     }  
	/*
	 * 重新设置 file @param fileString
	 */
	public void setFile(String fileString) {
		ini(fileString);
	}

	/*
	 * 初始化 @param fileString
	 */
	private void ini(String fileString) {
		this.fileString = fileString;
		fileName = fileString.substring(0, fileString.lastIndexOf("."));
		docFile = new File(fileString);
		pdfFile = new File(fileName + ".pdf");
		swfFile = new File(fileName + ".swf");
	}

	/*
	 * 转为PDF @param file
	 */

	/*
	 * 转换成swf
	 */
	public Integer pdf2swf11(String file) throws Exception {
		Runtime r = Runtime.getRuntime();
		int num=0;
		String strdir = DocService.class.getResource("/").getFile().replace("%20", " ");
		String swfexe = strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"static/exe/pdf2swf.exe";
//		String outPath = strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"/temp/Paper%";
		String outPath = strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"/temp/page%";
		String filedir=strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"temp";
		pdfFile = new File(file);
		swfFile = new File(outPath + ".swf");
		delAllFile(filedir);
		if (!swfFile.exists()) {
			if (pdfFile.exists()) {
				if (environment == 1)// windows环境处理
				{
					try {
						// 这里根据SWFTools安装路径需要进行相应更改
						Process p=null;
						 List<String> command=new ArrayList();  
						 command.add(swfexe);
		                 command.add("\""+ pdfFile.getPath()+"\"");  
		                 command.add("\""+swfFile.getPath()+"\"");  
		                 command.add("-f");
		                 command.add("-T");  
		                 command.add("9");
		                // command.add("-t");
		                // command.add("-p 4");
		                // command.add("-I"); 
		                 p=new ProcessBuilder(command).start(); 
						System.out.print(loadStream(p.getInputStream()));
						System.err.print(loadStream(p.getErrorStream()));
						System.out.print(loadStream(p.getInputStream()));
						System.err.println("****swf转换成功，文件输出：" + swfFile.getPath() + "****");
						File dir = new File(filedir);
						File[] files = dir.listFiles();
						 num = files.length-1;
						//num=200;
						System.out.print("****swf文件个数**** "+num);
						if (pdfFile.exists()) {
//							pdfFile.delete();
						}
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}
				}
			} else {
				System.out.println("****文件不存在，无法转换****");
			}
		} else {
			System.out.println("****swf已存在不需要转换****");
		}
		return num;
	}

	public Integer pdf2swf(String file) throws Exception {
		Runtime r = Runtime.getRuntime();
		int num=0;
		String strdir = DocService.class.getResource("/").getFile().replace("%20", " ");
		String swfexe = strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"static/exe/pdf2swf.exe";
		String outPath = strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"/temp/page1";
		String filedir=strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"temp";
		jpegFile = new File(file);
		File outpath=new File(outPath);
		if(!outpath.exists()){
			outpath.mkdirs();
		}
		swfFile = new File(outPath +File.separator+ "page1.swf");
		delAllFile(outPath);
		if (!swfFile.exists()) {
			swfFile.createNewFile();
			if (jpegFile.exists()) {
				if (environment == 1)// windows环境处理
				{
					try {
						// 这里根据SWFTools安装路径需要进行相应更改
						Process p=null;
						List<String> command=new ArrayList();
						command.add(swfexe);
						command.add("\""+ jpegFile.getPath()+"\"");
						command.add("-o");
						command.add("\""+swfFile.getPath()+"\"");
						command.add("-T");
						command.add("9");
						p=new ProcessBuilder(command).start();
						System.out.print(loadStream(p.getInputStream()));
						System.err.print(loadStream(p.getErrorStream()));
						System.out.print(loadStream(p.getInputStream()));
						System.err.println("****swf转换成功，文件输出：" + swfFile.getPath() + "****");
						if (jpegFile.exists()) {
//							pdfFile.delete();
						}
						//此时判断转换是否完成
                        for(;;){
                            if(swfFile.length()>0){
                                return 1;

                            }
                        }

					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}
				}
			} else {
				System.out.println("****pdf不存在，无法转换****");
			}
		} else {
			System.out.println("****swf已存在不需要转换****");
		}
		return 1;
	}
	
	/**
	 * 
	 * jpg转swf
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public Integer jpeg2swf(String file) throws Exception {
		Runtime r = Runtime.getRuntime();
		int num=0;
		String strdir = DocService.class.getResource("/").getFile().replace("%20", " ");
		String swfexe = strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"static/exe/jpeg2swf.exe";
		String outPath = strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"/temp/page1";
		String filedir=strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"temp";
		jpegFile = new File(file);
		File outpath=new File(outPath);
		if(!outpath.exists()){
			outpath.mkdirs();
		}
		swfFile = new File(outPath +File.separator+ "page1.swf");
		delAllFile(outPath);
		if (!swfFile.exists()) {
			swfFile.createNewFile();
			if (jpegFile.exists()) {
				if (environment == 1)// windows环境处理
				{
					try {
						// 这里根据SWFTools安装路径需要进行相应更改
						Process p=null;
						 List<String> command=new ArrayList();  
						 command.add(swfexe);
		                 command.add("\""+ jpegFile.getPath()+"\""); 
		                 command.add("-o");
		                 command.add("\""+swfFile.getPath()+"\"");  
		                 command.add("-T");  
		                 command.add("9");
		                 p=new ProcessBuilder(command).start(); 
						System.out.print(loadStream(p.getInputStream()));
						System.err.print(loadStream(p.getErrorStream()));
						System.out.print(loadStream(p.getInputStream()));
						System.err.println("****swf转换成功，文件输出：" + swfFile.getPath() + "****");
						if (jpegFile.exists()) {
//							pdfFile.delete();
						}
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}
				}
			} else {
				System.out.println("****pdf不存在，无法转换****");
			}
		} else {
			System.out.println("****swf已存在不需要转换****");
		}
		return 1;
	}
	
	

	
	/**
	 * 
	 * png转swf
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public Integer png2swf(String file) throws Exception {
		Runtime r = Runtime.getRuntime();
		int num=0;
		String strdir = DocService.class.getResource("/").getFile().replace("%20", " ");
		String swfexe = strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"static/exe/png2swf.exe";
		String outPath = strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"/temp/page1";
		String filedir=strdir.substring(1,strdir.lastIndexOf("WEB-INF"))+"temp";
		jpegFile = new File(file);
		File outpath=new File(outPath);
		if(!outpath.exists()){
			outpath.mkdirs();
		}
		swfFile = new File(outPath +File.separator+ "page1.swf");
		delAllFile(outPath);
		if (!swfFile.exists()) {
			swfFile.createNewFile();
			if (jpegFile.exists()) {
				if (environment == 1)// windows环境处理
				{
					try {
						// 这里根据SWFTools安装路径需要进行相应更改
						Process p=null;
						List<String> command=new ArrayList();
						command.add(swfexe);
						command.add("\""+ jpegFile.getPath()+"\"");
						command.add("-o");
						command.add("\""+swfFile.getPath()+"\"");
						command.add("-T");
						command.add("9");
						p=new ProcessBuilder(command).start();
						System.out.print(loadStream(p.getInputStream()));
						System.err.print(loadStream(p.getErrorStream()));
						System.out.print(loadStream(p.getInputStream()));
						System.err.println("****swf转换成功，文件输出：" + swfFile.getPath() + "****");
						if (jpegFile.exists()) {
//							pdfFile.delete();
						}
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}
				}
			} else {
				System.out.println("****pdf不存在，无法转换****");
			}
		} else {
			System.out.println("****swf已存在不需要转换****");
		}
		return 1;
	}

	static String loadStream(InputStream in) throws IOException {
		int ptr = 0;
		//把InputStream字节流 替换为BufferedReader字符流 2013-07-17修改
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder buffer = new StringBuilder();
		while ((ptr = reader.read()) != -1) {
			buffer.append((char) ptr);
		}
		return buffer.toString();
	}
	
	
	/*
	 * 转换主方法
	 */
	public boolean conver() {
		if (swfFile.exists()) {
			System.out.println("****swf转换器开始工作，该文件已经转换为swf****");
			return true;
		}

		if (environment == 1) {
			System.out.println("****swf转换器开始工作，当前设置运行环境windows****");
		} else {
			System.out.println("****swf转换器开始工作，当前设置运行环境linux****");
		}

		try {
//			doc2pdf();
//			pdf2swf();
		} catch (Exception e) {
			// TODO: Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		if (swfFile.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * 返回文件路径 @param s
	 */
	public String getswfPath() {
		if (swfFile.exists()) {
			String tempString = swfFile.getPath();
			tempString = tempString.replaceAll("\\\\", "/");
			return tempString;
		} else {
			return "";
		}
	}

	/*
	 * 设置输出路径
	 */
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
		if (!outputPath.equals("")) {
			String realName = fileName.substring(fileName.lastIndexOf("/"), fileName.lastIndexOf("."));
			if (outputPath.charAt(outputPath.length()) == '/') {
				swfFile = new File(outputPath + realName + ".swf");
			} else {
				swfFile = new File(outputPath + realName + ".swf");
			}
		}
	}
	/**
	 * 清缓存文件
	 * 
	 * @author PX
	 * @param path
	 * @return
	 *
	 * 2019-3-28
	 */
	 public static boolean delAllFile(String path) {
	       boolean flag = false;
	       File file = new File(path);
	       if (!file.exists()) {
	         return flag;
	       }
	       if (!file.isDirectory()) {
	         return flag;
	       }
	       String[] tempList = file.list();
	       File temp = null;
	       for (int i = 0; i < tempList.length; i++) {
	          if (path.endsWith(File.separator)) {
	             temp = new File(path + tempList[i]);
	          } else {
	              temp = new File(path + File.separator + tempList[i]);
	          }
	          if (temp.isFile()) {
	             temp.delete();
	          }
	          if (temp.isDirectory()) {
	        	  
	             //delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
	             //flag = true;
	          }
	       }
	       return flag;
	     }

	/**
	 *
	 * @param tifPath tif保存的路径
	 * @param pdfPath pdf保存的路径
	 */
	public  String tifToPdf(String tifPath, String pdfPath) throws Exception {

		pdfPath ="f://dataaos/temp/" + tifPath.substring(tifPath.lastIndexOf("/"),tifPath.lastIndexOf("."))+".pdf";
		pdfPath ="f://dataaos/temp/temp.pdf";
		//如果文件不存在就创建
		File _toFile = new File(pdfPath);
		if (!_toFile.exists()) {
			//_toFile.mkdirs();
			_toFile.createNewFile();
		}
		tifTo2Pdf(tifPath,pdfPath);
		return pdfPath;
	}
	/**
	 *
	 * @param tifPath jpg保存的路径
	 * @param pdfPath pdf保存的路径
	 */
	public  String jpgToPdf(String jpgPath, String pdfPath) throws Exception {

		pdfPath ="f://dataaos/temp/" + jpgPath.substring(jpgPath.lastIndexOf("/"),jpgPath.lastIndexOf("."))+".pdf";
		pdfPath ="f://dataaos/temp/temp.pdf";
		//如果文件不存在就创建
		File _toFile = new File(pdfPath);
		if (!_toFile.exists()) {
			//_toFile.mkdirs();
			_toFile.createNewFile();
		}
		jpgTo2Pdf(jpgPath,pdfPath);
		return pdfPath;
	}
	public static void jpgTo2Pdf(String imagePath,String pdfPath) throws Exception{
		try {
			BufferedImage img = ImageIO.read(new File(imagePath));
			FileOutputStream fos = new FileOutputStream(pdfPath);
			Document doc = new Document(PageSize.A4,0,0,0,0);
			doc.setPageSize(new Rectangle(img.getWidth(), img.getHeight()));
			Image image = Image.getInstance(imagePath);
			PdfWriter.getInstance(doc, fos);
			doc.open();
			doc.add(image);
			Thread.sleep(1000);
			doc.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BadElementException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 使用pdfbox将jpg转成pdf
	 * @param jpgStream jpg输入流
	 * @param pdfPath pdf文件存储路径
	 * @throws IOException IOException
	 */
	/*public static void jpgTo2Pdf(String jpgpath, String pdfPath) throws Exception {

		InputStream jpgStream =new FileInputStream(new File(jpgpath));
		PDDocument pdDocument = new PDDocument();
		BufferedImage image = ImageIO.read(jpgStream);

		PDPage pdPage = new PDPage(new PDRectangle(image.getWidth(), image.getHeight()));
		pdDocument.addPage(pdPage);
		PDImageXObject pdImageXObject = LosslessFactory.createFromImage(pdDocument, image);
		PDPageContentStream contentStream = new PDPageContentStream(pdDocument, pdPage);
		contentStream.drawImage(pdImageXObject, 0, 0, image.getWidth(), image.getHeight());
		contentStream.close();
		pdDocument.save(pdfPath);
		pdDocument.close();
	}*/
	// 将多个jpg文件合并成一个pdf文件
	/*public static void  jpgTo2Pdf(String sFileJpg, String sFilePdf) {

		try {
		// 创建一个文档对象
		Document doc = new Document(PageSize.LETTER, 10, 10, 10, 10);
			// 定义输出位置并把文档对象装入输出对象中
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(sFilePdf));
			// 打开文档对象
			doc.open();//打开文档
			 doc.newPage();  //在pdf创建一页
			   Image png1 = Image.getInstance(sFileJpg); //通过文件路径获取image
				float heigth = png1.getScaledHeight();
				float width = png1.getScaledWidth();
			//image1.setAbsolutePosition(100f, 550f);
			//image1.setAbsolutePosition(0,0);
			//设置图片的宽度和高度
			//image1.scaleAbsolute(500, 500);

				 int percent = getPercent2(heigth, width);
				png1.setAlignment(Image.MIDDLE);
				//png1.scalePercent(percent+3);// 表示是原来图像的比例;
				png1.scalePercent(24);
				png1.setAbsolutePosition(0,-40);
				 doc.add(png1);
				 doc.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}*/

	public static int getPercent2(float h, float w) {
		int p = 0;
		float p2 = 0.0f;
		p2 = 550 / w * 100;
		p = Math.round(p2);
		return p;
	}
	public static void  tifTo2Pdf(String sFileTif, String sFilePdf) {
		// 创建一个文档对象
		Document doc = new Document();
		int pages = 0;
		int comps = 0;
		try {

			// 打开文档对象
			doc.open();

			RandomAccessFileOrArray ra = null;
			ra = new RandomAccessFileOrArray(sFileTif);
			comps = TiffImage.getNumberOfPages(ra);
			// System.out.println("Processing: " + sFileTif);
			for (int c = 0; c < comps; ++c) {
				//Image img = TiffImage.getTiffImage(ra, c + 1);
				Image img = Image.getInstance(sFileTif);
				//img = img.getInstance(sFileTif);
				if (img != null) {
					// System.out.println("page " + (c + 1));

					float x = 7200f / img.getDpiX();
					float y = 7200f / img.getDpiY();
					img.scalePercent(7200f / img.getDpiX(),
							7200f / img.getDpiY());
					float k=img.getHeight();
					float t=img.getWidth();
					//******************************************************************************************************
					doc.newPage();
					doc.setPageSize(new Rectangle(img.getWidth()/2.5f,img.getHeight()/2.5f));//img.getScaledWidth(),img.getScaledHeight()



					// 定义输出位置并把文档对象装入输出对象中
					PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(sFilePdf));
					doc.open();
					PdfContentByte cb = writer.getDirectContent();
					//******************************************************************************************************
					img.setAbsolutePosition(0, 0);
					img.setAlignment(Image.ALIGN_CENTER);
					doc.add(img);
					//doc.newPage();
					++pages;
				}
			}
			ra.close();// 关闭
			// 关闭文档对象，释放资源
			doc.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	// 将多个tif文件合并成一个pdf文件
		public static void  tifTo2Pdf2(String sFileTif, String sFilePdf) {

			// 创建一个文档对象
			Document doc = new Document();
			int pages = 0;
			int comps = 0;
			try {
				// 定义输出位置并把文档对象装入输出对象中
				PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(sFilePdf));
				// 打开文档对象
				doc.open();
					PdfContentByte cb = writer.getDirectContent();
					RandomAccessFileOrArray ra = null;
						ra = new RandomAccessFileOrArray(sFileTif);
						comps = TiffImage.getNumberOfPages(ra);
					// System.out.println("Processing: " + sFileTif);
					for (int c = 0; c < comps; ++c) {
							Image img = TiffImage.getTiffImage(ra, c + 1);
							if (img != null) {
								// System.out.println("page " + (c + 1));
								img.scalePercent(7200f / img.getDpiX(),
										7200f / img.getDpiY());
								float k=img.getScaledHeight();
								float t=img.getScaledWidth();
								doc.setPageSize(new Rectangle(img.getScaledWidth(),
										img.getScaledHeight()));
								img.setAbsolutePosition(0, 0);
								img.setAlignment(Image.ALIGN_CENTER);
								cb.addImage(img);
								doc.newPage();
								++pages;
							}
					}
					ra.close();// 关闭
				// 关闭文档对象，释放资源
				doc.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}

	static String host = "";
	public static String port = "";
	public static String isopen="";
	// 通过静态代码块读取配置文件
	static {
		try {
			Properties props = new Properties();
			/*InputStream in = DataService.class
					.getResourceAsStream("redis.properties");
			props.load(in);*/
			host = "127.0.0.1";
			port ="6379";
			isopen = "yes";
			if("yes".equals(isopen)){
				jedis = new Jedis(host,Integer.parseInt(port));
			}else{

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**新增(存储字符串)
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean addString(final String key, final String value) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] jkey  = serializer.serialize(key);
				byte[] jvalue = serializer.serialize(value);
				return connection.setNX(jkey, jvalue);
			}
		});
		return result;
	}

	/**新增(拼接字符串)
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean appendString(final String key, final String value) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] jkey  = serializer.serialize(key);
				byte[] jvalue = serializer.serialize(value);
				if(connection.exists(jkey)){
					connection.append(jkey, jvalue);
					return true;
				}else{
					return false;
				}
			}
		});
		return result;
	}

	/**新增(存储Map)
	 * @param key
	 * @param value
	 * @return
	 */
	public String addMap(String key, Map<String, String> map) {
		Jedis jedis = getJedis();
		String result = jedis.hmset(key,map);
		jedis.close();
		return result;
	}

	/**获取map
	 * @param key
	 * @return
	 */
	public Map<String,String> getMap(String key){
		Jedis jedis = getJedis();
		Map<String, String> map = new HashMap<String, String>();
		Iterator<String> iter=jedis.hkeys(key).iterator();
		while (iter.hasNext()){
			String ikey = iter.next();
			map.put(ikey, jedis.hmget(key,ikey).get(0));
		}
		jedis.close();
		return map;
	}

	/**新增(存储List)
	 * @param key
	 * @param pd
	 * @return
	 */
	public void addList(String key, List<String> list){
		Jedis jedis = getJedis();
		jedis.del(key); //开始前，先移除所有的内容
		for(String value:list){
			jedis.rpush(key,value);
		}
		jedis.close();
	}

	/**获取List
	 * @param key
	 * @return
	 */
	public List<String> getList(String key){
		Jedis jedis = getJedis();
		List<String> list = jedis.lrange(key,0,-1);
		jedis.close();
		return list;
	}

	/**新增(存储set)
	 * @param key
	 * @param set
	 */
	public void addSet(String key, Set<String> set){
		Jedis jedis = getJedis();
		jedis.del(key);
		for(String value:set){
			jedis.sadd(key,value);
		}
		jedis.close();
	}

	/**获取Set
	 * @param key
	 * @return
	 */
	public Set<String> getSet(String key){
		Jedis jedis = getJedis();
		Set<String> set = jedis.smembers(key);
		jedis.close();
		return set;
	}

	/**删除
	 * (non-Javadoc)
	 */
	public boolean delete(final String key) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] jkey  = serializer.serialize(key);
				if(connection.exists(jkey)){
					connection.del(jkey);
					return true;
				}else{
					return false;
				}
			}
		});
		return result;
	}

	/**删除多个
	 * (non-Javadoc)
	 */
	public void delete(List<String> keys) {
		redisTemplate.delete(keys);
	}

	/**修改
	 * (non-Javadoc)
	 */
	public boolean eidt(String key, String value) {
		if(delete(key)){
			addString(key,value);
			return true;
		}
		return false;
	}

	/**通过key获取值
	 * (non-Javadoc)
	 * @see com.fh.dao.redis.RedisDao#get(String)
	 */
	public String get(final String keyId) {
		String result = redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection)
					throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] jkey = serializer.serialize(keyId);
				byte[] jvalue = connection.get(jkey);
				if (jvalue == null) {
					return null;
				}
				return serializer.deserialize(jvalue);
			}
		});
		return result;
	}

	/**获取Jedis
	 * @return
	 */
	public Jedis getJedis(){

		if("yes".equals(isopen)){
			Jedis jedis = new Jedis(host,Integer.parseInt(port));
			//jedis.auth(pass);
			return jedis;
		}else{
			return null;
		}
	}
	/**
	 *
	 * @param tifPath tif保存的路径
	 * @param pdfPath pdf保存的路径
	 */
	public  String tifToPdfShq(String tifPath, String pdfPath) throws Exception {

		pdfPath ="f:\\dataaos\\temp\\" + tifPath.substring(tifPath.lastIndexOf("/")+1,tifPath.lastIndexOf("."))+".pdf";
		//如果文件不存在就创建
		File _toFile = new File(pdfPath);
		if (!_toFile.exists()) {
			//_toFile.mkdirs();
			_toFile.createNewFile();
		}
		tifTo2Pdf(tifPath,pdfPath);
		return pdfPath;
	}
}