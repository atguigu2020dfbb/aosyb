package cn.osworks.aos.system.modules.service.archive;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.modules.dao.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifImageDirectory;
import com.drew.metadata.exif.ExifSubIFDDescriptor;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.ExifThumbnailDirectory;
import com.drew.metadata.file.FileSystemDirectory;
import com.drew.metadata.jfif.JfifDirectory;
import com.drew.metadata.jpeg.JpegCommentDirectory;
import com.drew.metadata.jpeg.JpegDirectory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;


import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * 电子文件
 * 
 * @author Sun
 *
 * @date 2016-6-21
 */
@Service
public class UploadService extends JdbcDaoSupport {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Resource
	public void setJb(JdbcTemplate jb) {
		super.setJdbcTemplate(jb);
	}
	
	
	public void saveUploadInfo(String tid,String did,String imgurl,String tableinfo){
		//imgurl="C:/Users/Administrator/Desktop/触摸屏/111.jpg";
		 File jpegFile = new File(imgurl); 
		try {
			Metadata metadata = JpegMetadataReader.readMetadata(jpegFile);
	        
			ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

			String  sbzzs = directory.getString(ExifIFD0Directory.TAG_MAKE);
			String sbxh = directory.getString(ExifIFD0Directory.TAG_MODEL);
			String xdpi = directory.getString(ExifIFD0Directory.TAG_X_RESOLUTION);
			String ydpi = directory.getString(ExifIFD0Directory.TAG_Y_RESOLUTION);
		
			FileSystemDirectory fsdirectory = metadata.getFirstDirectoryOfType(FileSystemDirectory.class);
			
			String wjm = fsdirectory.getString(FileSystemDirectory.TAG_FILE_NAME);
			String wjdx = fsdirectory.getString(FileSystemDirectory.TAG_FILE_SIZE);
			String gsmc = wjm.substring(wjm.lastIndexOf(".") + 1);
			
			
			//String filesize = fsdirectory.getString(fsdirectory.TAG_WIN_KEYWORDS);
			
			
			ExifSubIFDDirectory esfddirectory =metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
			String imgh = esfddirectory.getString(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT);
			String imgw = esfddirectory.getString(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH);
			String sckj = esfddirectory.getDescription(ExifSubIFDDirectory.TAG_COLOR_SPACE);
			
			String id = AOSId.uuid();
			String sql="INSERT INTO  " +tableinfo+" (id_,did,tid,sbzzs,sbxh,sbggq,wjm,wjdx,gsmc,xdpi,ydpi,imgw,imgh,sckj,ycbcr,mxsybs,mybws,ysfa,ysl) " +
					"values('"+id+"','"+did+"','"+tid+"','"+sbzzs+"','"+sbxh+"','','"+wjm+"','"+wjdx+"','"+gsmc+"','"+xdpi+"','"+ydpi+"','"+imgw+"','"+imgh+"','"+sckj+"','','','','','')";
			jdbcTemplate.execute(sql);
			//String a="111";
//			for (Directory directory : metadata.getDirectories()){ 
//			    for (Tag tag : directory.getTags()) {     
//			    	System.out.println(tag.getTagName()+"-----------"+tag.getDirectoryName()+"------------"+tag.getDescription());
//			        }  
//			}
		} catch (ImageProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 
		
	}
	
	public void updateZpPath(String tablename,String tid,String dirname){
		//String strrq =dirname.substring(0,8);
		//String strtm = dirname.substring(8,dirname.length()-4);
		String strwjgs =dirname.substring(dirname.lastIndexOf('.')+1);
		String strrq=dirname.substring(0,8);
		String strtm = dirname.substring(8,dirname.lastIndexOf("-"));
		String strsyz = dirname.substring(dirname.lastIndexOf("-")+1,dirname.lastIndexOf("."));
		String sql="";
		if(strwjgs.equals("jpg")||strwjgs.equals("JPG")){
			strwjgs="JPEG";
			 sql="update "+tablename+" set _path=(select count(tid) from "+tablename+"_path where tid='"+tid+"'),rq='"+strrq+"',tm='"+strtm+"',wjgs='"+strwjgs+"',syz='"+strsyz+"' where id_='"+tid+"'";
			 jdbcTemplate.execute(sql);
		}
		if(strwjgs.equals("doc")|| strwjgs.equals("docx")){
		return;
		}
		
		
//		String sql="update "+tablename+" set _path=(select count(tid) from "+tablename+"_path where tid='"+tid+"'),rq='"+strrq+"',tm='"+strtm+"',wjgs='"+strwjgs+"' where id_='"+tid+"'";
//		jdbcTemplate.execute(sql);
		
	}
	
	/**
	 * 
	 * pdf添加水印
	 * 
	 * @author Sun
	 * @param srcFile
	 * @param destFile
	 * @param text
	 * @param textWidth
	 * @param textHeight
	 * @throws DocumentException
	 * @throws IOException
	 *
	 * 2018-9-4
	 */
	public void addWaterMark(Dto qDto) throws DocumentException, IOException{
		// 待加水印的文件
		String srcFile=qDto.getString("fileName");
		String destFile=qDto.getString("markPath")+"\\mark";
		File file = new File(destFile);
		if(!file.exists())
			file.mkdirs();
		String text="江西省人大常委会";
		int textWidth=100;
		int textHeight=100;
		//float pageWidth =0;
		//float pageHeight=0;
        PdfReader reader = new PdfReader(srcFile);
        //reader.close();
        // 加完水印的文件
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
                destFile+"\\"+qDto.getString("filetype")));
        int total = reader.getNumberOfPages() + 1;
        
        PdfContentByte content;
        // 设置字体
        BaseFont font = BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,1", "Identity-H", true);
        
        //Image img = Image.getInstance("C:/Users/Administrator/Desktop/水印/002.pdf");
        
       // img.setAbsolutePosition(30, 100);
        // 循环对每页插入水印
        for (int i = 1; i < total; i++)
        {
            // 水印的起始
            content = stamper.getOverContent(i);
            //content.addImage(image);
            // 开始
            content.beginText();
            // 设置颜色 默认为蓝色
            content.setColorFill(BaseColor.GRAY);
            // content.setColorFill(Color.GRAY);
            // 设置字体及字号
            content.setFontAndSize(font, 38);
            //content.setTextRise(20);//斜度
            // 设置起始位置
            // content.setTextMatrix(400, 880);
            //content.setTextMatrix(80, 500);
            //content.s
            //横向居中
            //content.setTextMatrix((pageWidth-38*text.length())/2,pageHeight-80);
            
            PdfGState gs = new PdfGState();
              gs.setFillOpacity(0.2f);// 设置透明度为0.8
              content.setGState(gs);
            
            // 开始写入水印
            content.showTextAligned(Element.ALIGN_LEFT, text, 100, 400, 30);  
                   
            content.endText();
        }
        //reader.close();
        stamper.close();
        reader.close();
		
	}
	
	public void addJpgWaterMark(Dto qDto){
		try {
			//String srcImgPath="";
			//String tarImgPath="";
			String waterMarkContent="江西省人大常委会";
			String srcImgPath=qDto.getString("fileName");
			String tarImgPath=qDto.getString("markPath")+"\\mark"+"\\"+qDto.getString("filetype");
			
            // 读取原图片信息
			BufferedImage srcImg = ImageIO.read(new File(srcImgPath));
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),
                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = buffImg.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            int srcImgWidth = srcImg.getWidth(null);//获取图片的宽
            int srcImgHeight = srcImg.getHeight(null);//获取图片的高
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            //设置水印颜色
            Color color=Color.gray;                               //水印图片色彩以及透明度
            g.setColor(color); //根据图片的背景设置水印颜色
            // 4、设置水印旋转
           // if (null != degree) {
              //  g.rotate(Math.toRadians(-30),
                 //       (double) buffImg.getWidth() / 2,
                  //      (double) buffImg.getHeight() / 2);
          //  }
            // 设置字体
            Font font = new Font("Identity-H", Font.PLAIN, 100);
            // Font font = BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,1", "Identity-H", true);
            g.setFont(font);              //设置字体

            //设置水印的坐标
            int x = srcImgWidth - 2*getWatermarkLength(waterMarkContent, g);  
            int y = srcImgHeight - 2*getWatermarkLength(waterMarkContent, g);  
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));
            g.drawString(waterMarkContent, srcImgWidth-900, srcImgHeight-14);  //画出水印
           
            g.dispose();  
            // 输出图片  
            FileOutputStream outImgStream = new FileOutputStream(tarImgPath);  
            ImageIO.write(buffImg, "jpg", outImgStream);
            System.out.println("添加水印完成");  
            outImgStream.flush();  
            outImgStream.close();  

        } catch (Exception e) {
            // TODO: handle exception
        }
	}
	/**
     * 给图片添加水印文字、可设置水印文字的旋转角度
     *
     */
    public static void markImageByText() {

    	String logoText="江西省人大常委会";
    	String srcImgPath="D:/dataaos/zpda/mark/833e05b2c0be46fc935cc4f7a10a5798.JPG";
    	String targerPath="D:/dataaos/zpda/mark/0000000001.jpg";
    	Integer degree=-30;
    	InputStream is = null;
        OutputStream os = null;
        try {
            // 1、源图片
            BufferedImage srcImg = ImageIO.read(new File(srcImgPath));
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),
                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);

            // 2、得到画笔对象
            Graphics2D g = buffImg.createGraphics();
            // 3、设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            int srcImgWidth = srcImg.getWidth(null);//获取图片的宽
            int srcImgHeight = srcImg.getHeight(null);//获取图片的高
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            
          //  g.drawImage(
                //    srcImg.getScaledInstance(srcImg.getWidth(null),
                  //          srcImg.getHeight(null), srcImg.SCALE_SMOOTH), 0, 0,
                 //   null);
            // 4、设置水印旋转
            if (null != degree) {
                g.rotate(Math.toRadians(degree),
                        (double) buffImg.getWidth() / 2,
                        (double) buffImg.getHeight() / 2);
            }
            // 5、设置水印文字颜色
            Color color = Color.red;
            g.setColor(color);
            // 6、设置水印文字Font
            Font font = new Font("Identity-H", Font.PLAIN, 100);
            g.setFont(font);
            // 7、设置水印文字透明度
            float alpha = 0.5f;
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            // 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
            
//            int srcImgWidth = srcImg.getWidth(null);//获取图片的宽
//            int srcImgHeight = srcImg.getHeight(null);//获取图片的高
          //设置水印的坐标
            //int x = srcImgWidth - 2*getWatermarkLength(logoText, g);  
            int x = srcImgWidth - getWatermarkLength(logoText, g);  
            int y = srcImgHeight - 2*getWatermarkLength(logoText, g);  
            
            g.drawString(logoText, x, y);
            // 9、释放资源
            g.dispose();
            // 10、生成图片
            os = new FileOutputStream(targerPath);
            ImageIO.write(buffImg, "JPG", os);

            System.out.println("图片完成添加水印文字");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	 public static int getWatermarkLength(String waterMarkContent, Graphics2D g) {  
	        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());  
	    }  
	
	public void updatePath(String tablename,String tid,String dirname){
		String sql="update "+tablename+" set _path=(select count(tid) from "+tablename+"_path where tid='"+tid+"') where id_='"+tid+"'";
		jdbcTemplate.execute(sql);
		
	}
	public String selectOne(String tid,String tablename){
		String sql=" select smzph from "+tablename+" where id_='"+tid+"'";
		
		//List list=null;
		String  smzph=jdbcTemplate.queryForObject(sql, java.lang.String.class);
		return smzph;
	}
	
	public void executeSQL(String sql){
		jdbcTemplate.execute(sql);
		
	}

	//保存操作信息
	public boolean saveReturnMessageData(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		Dto inDto = Dtos.newDto(request);
		try{
			UserInfoVO userInfoVO=(UserInfoVO)session.getAttribute("_userInfoVO");
			Date date=new Date();
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sql="insert into archive_return_login(id_,operator_time,operator_en_person,operator_cn_person,operator_name) values('"+ AOSId.uuid()+"','"+simpleDateFormat.format(date)+"','"+userInfoVO.getName_()+"','"+userInfoVO.getAccount_()+"','备份数据库')";
			jdbcTemplate.execute(sql);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

    public void updateByrw(String id_, String fileName) {
			String sql="update archive_compilation_rw set _path='"+fileName+"' where id_='"+id_+"'";
			jdbcTemplate.execute(sql);
    }
	public void updateBycg(String id_, String fileName) {
		String sql="update archive_compilation_rw set bycg_path='"+fileName+"' where id_='"+id_+"'";
		jdbcTemplate.execute(sql);
	}

	public void updateTemporaryrw(String id_, String fileName) {
		String sql="update archive_receive set yjssc='"+fileName+"' where id_='"+id_+"'";
		jdbcTemplate.execute(sql);
	}
	public void updateEntityrw(String id_, String fileName) {
		String sql="update stjs_n set yjssc='"+fileName+"' where id_='"+id_+"'";
		jdbcTemplate.execute(sql);
	}
	public void updateZiYuanJieShouYjs( String tablename, String id_,String filename) {
		String sql="update "+tablename+" set yjssc='"+filename+"' where id_='"+id_+"'";
		jdbcTemplate.execute(sql);
	}
	public void updateSiTiJieShouYjs( String tablename, String id_,String filename) {
		String sql="update "+tablename+" set yjssc='"+filename+"' where id_='"+id_+"'";
		jdbcTemplate.execute(sql);
	}
    public void updateZiLiaoYjs( String tablename, String id_,String filename) {
		String sql="update "+tablename+" set yjssc='"+filename+"' where id_='"+id_+"'";
		jdbcTemplate.execute(sql);
    }
	public void updateTemporaryYjs( String tablename, String id_,String filename) {
		String sql="update "+tablename+" set yjssc='"+filename+"' where id_='"+id_+"'";
		jdbcTemplate.execute(sql);
	}
	public void addData_path(String originalFilename, String tablename, String id_) {
    	String sql="insert into "+tablename+"_path (id_,tid,_path,dirname,sdatetime,_s_path) values('"+AOSId.uuid()+"','"+id_+"','"+originalFilename+"','"+tablename+"/"+id_+"/"+"','"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"','"+originalFilename+"')";
		jdbcTemplate.execute(sql);
    }

	public void updateData_path(String tablename, String id_, String s) {
    	String sql="update "+ tablename+" set _path="+s+" where id_='"+id_+"'";
    	jdbcTemplate.execute(sql);
	}

    public void updateSjfb(String id_, String originalFilename) {
    	String sql="update lysjfb_detail set fbs='"+originalFilename+"' where id_='"+id_+"'";
    	jdbcTemplate.execute(sql);
    }
}
