package cn.osworks.aos.system.modules.service.batchUpload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import cn.osworks.aos.system.modules.service.archive.DataService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import cn.osworks.aos.core.asset.AOSJson;
import cn.osworks.aos.core.id.AOSId;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.dao.mapper.Archive_tablenameMapper;
import cn.osworks.aos.system.dao.po.Archive_tablenamePO;

/**
 *
 * 批量挂接
 * 
 * @author Sun
 * 
 *         2018-10-20
 */
@Service
public class BatchUploadService {

	@Autowired
	private Archive_tablenameMapper archive_tablenameMapper;

	@Autowired
	private  JdbcTemplate jdbcTemplate;

	public static String addr = "";
	public static String user = "";
	public static String password = "";
	public static String port = "";
	public static String ftpAddress = "";
	// 通过静态代码块读取配置文件
	static {
		try {
			Properties props = new Properties();
			InputStream in = DataService.class
					.getResourceAsStream("/config.properties");
			props.load(in);
			addr = props.getProperty("addr");
			user = props.getProperty("user");
			password = props.getProperty("password");
			port = props.getProperty("port");
			ftpAddress = props.getProperty("ftpAddress");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Dto> getCount(Dto qDto) {

		List<Archive_tablenamePO> list = archive_tablenameMapper.list(qDto);
		List<Dto> listCount = new ArrayList<Dto>();
		for (int i = 0; i < list.size(); i++) {
			Dto inDto = Dtos.newDto();
			int mls = jdbcTemplate.queryForInt(" select count(*) from "
					+ list.get(i).getTablename());
			int ygj = jdbcTemplate.queryForInt(" select count(*) from "
					+ list.get(i).getTablename() + " where _path>0");
			int a = mls - ygj;

			inDto.put("tablename", list.get(i).getTablename());
			inDto.put("tabledesc", list.get(i).getTabledesc());
			inDto.put("mls", mls);
			inDto.put("ygj", ygj);
			inDto.put("wgj", mls - ygj);

			listCount.add(inDto);

		}

		// System.out.print(list.get(0).getTablename());

		return listCount;
	}

	/**
	 * 预检操作
	 * 
	 * @author PX
	 * @param qDto
	 * 
	 *            2019-4-29
	 * @return
	 */
	public Dto startPreview(Dto qDto) {
		// TODO Auto-generated method stub
		Dto outDto = Dtos.newDto();
		String tablename = qDto.getString("tablename");
		String wjlx = qDto.getString("wjlx");
		String filedname = qDto.getString("filedname");
		String file_gridData = qDto.getString("file_gridData");
		int count = 0;
		int gjcount = 0;
		// 判断可以挂接的条目数据总数
		String sql = "select * from " + tablename;
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list != null && list.size() > 0) {
			count = list.size();
		}
		// 查询可挂接数量

		String sql2 = "select * from " + tablename + " where " + filedname
				+ " !=''";
		List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql2);
		if (list2 != null && list2.size() > 0) {
			gjcount = list2.size();
		}
		// 判断文件类型
		String filetype = booleanFileType(wjlx);
		// 可以挂接的文件总数
		int gjfilecount = findfilenameSum(file_gridData, filetype, filedname,
				tablename);
		// 选择的电子文件的总数
		int filecount = AOSJson.fromJson(file_gridData).size();
		// 设计一个返回的appmsg消息<a style="text-align: center;"></a>
		outDto.put("count", count);
		outDto.put("gjcount", gjcount);
		outDto.put("filecount", filecount);
		outDto.put("gjfilecount", gjfilecount);
		return outDto;
	}

	// 判断文件类型
	private String booleanFileType(String wjlx) {
		// TODO Auto-generated method stub
		if (wjlx.equals("PDF")) {
			return ".pdf";
		} else if (wjlx.equals("JPG")) {
			return ".jpg";
		} else if (wjlx.equals("JPG+PDF")) {
			return ".jpg.pdf";
		} else if (wjlx.equals("AVI")) {
			return ".avi";
		} else if (wjlx.equals("MP3")) {
			return ".mp3";
		} else if (wjlx.equals("MP4")) {
			return ".mp4";
		}
		return null;
	}

	/**
	 * 查找符合挂接条件的电子文件的数量
	 * 
	 * @author PX
	 * @param file_gridData
	 * @param filetype
	 * @param tablename
	 * @param filedname
	 * @return
	 * 
	 *         2019-4-29
	 */
	public int findfilenameSum(String file_gridData, String filetype,
			String filedname, String tablename) {
		int k = 0;
		List<Dto> listHeader = AOSJson.fromJson(file_gridData);
		if (listHeader != null || listHeader.size() > 0) {
			if (filetype == ".jpg.pdf") {
				for (int i = 0; i < listHeader.size(); i++) {
					String filename = listHeader.get(i).getString("filename");
					String size = listHeader.get(i).getString("size");
					if (filename.contains(".jpg") || filename.contains(".pdf")) {
						if (Integer.valueOf(size) > 0) {
							String sql = "select * from " + tablename
									+ " where " + filedname + " ='"
									+ filename.split("\\.")[0] + "'";
							List<Map<String, Object>> list = jdbcTemplate
									.queryForList(sql);
							if (list.size() > 0) {
								k++;
							}
						}
					}
				}
			} else {
				for (int i = 0; i < listHeader.size(); i++) {
					String filename = listHeader.get(i).getString("filename");
					String size = listHeader.get(i).getString("size");
					if (filename.contains(filetype)) {
						if (Integer.valueOf(size) > 0) {
							String sql = "select * from " + tablename
									+ " where " + filedname + " ='"
									+ filename.split("\\.")[0] + "'";
							List<Map<String, Object>> list = jdbcTemplate
									.queryForList(sql);
							if (list.size() > 0) {
								k++;
							}
						}
					}
				}
			}
		}
		return k;
	}

	/**
	 * 执行批量挂接操作
	 * 
	 * @param qDto
	 * @param request
	 * @throws IOException
	 * @throws FileUploadException
	 */
	public String previewfilename(Dto qDto, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		String message="";
		String filedname = qDto.getString("filedname");
		String file_gridData = qDto.getString("file_gridData");
		String tablename = qDto.getString("tablename");
		String wjlx = qDto.getString("wjlx");
		String wjy = qDto.getString("wjy");

		String list_map = qDto.getString("list_map");
		//
		wjy = URLDecoder.decode(wjy, "UTF-8");
		if (wjy.equals("服务器文件")) {
			// 此时采用ftp的方式进行上传操作
			// 此时普通拷贝的方式上传
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 设置内存缓冲区，超过后写入临时文件
			factory.setSizeThreshold(10240000);
			// 设置临时文件存储位置

			// 1.组合上传后的路径名称（也可以作为临时文件存储路径）
			factory.setRepository(new File(ftpAddress));
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 设置单个文件的最大上传值
			upload.setFileSizeMax(10002400000l);
			// 设置整个request的最大值
			upload.setSizeMax(10002400000l);
			upload.setHeaderEncoding("UTF-8");
			List<?> items = upload.parseRequest(request);
			// 2.
			FileItem item = null;
			// String imgurl=null;
			for (int j = 0; j < items.size(); j++) {
				item = (FileItem) items.get(j);
				// 保存文件
				if (!item.isFormField() && item.getName().length() > 0) {
					String str = item.getName();
					String path = compositeupdatepath(list_map, tablename, filedname,
							str.split("\\.")[0]);
					if (path == null||path.equals("")) {
						message="\n当前电子文件:"+str+",挂接结果:失败!";
						continue;
					}
					String base = ftpAddress + tablename + path;
					//执行文件上传并将其写入到数据库中
					boolean bb=connect(ftpAddress,tablename+path,path,str,item, addr, port, user, password,tablename,filedname,str);
					if(!bb){
						String filetype = AOSId.uuid()
								+ str.substring(str.lastIndexOf("."));
						//item.write(new File(base+"\\"+str));
						//挂接成功
						message="\n当前电子文件:"+str+",挂接结果:失败!";
					}else{
						String filetype = AOSId.uuid()
								+ str.substring(str.lastIndexOf("."));
						//item.write(new File(base+"\\"+str));
						//挂接成功
						message="\n当前电子文件:"+str+",挂接结果:成功!";
					}

				}
			}
		} else if (wjy.equals("本地文件")) {
			// 此时普通拷贝的方式上传
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 设置内存缓冲区，超过后写入临时文件
			factory.setSizeThreshold(10240000);
			// 设置临时文件存储位置
			Properties prop = PropertiesLoaderUtils
					.loadAllProperties("config.properties");
			String filePath = prop.getProperty("filePath");
			// 1.组合上传后的路径名称（也可以作为临时文件存储路径）
			factory.setRepository(new File(filePath));
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 设置单个文件的最大上传值
			upload.setFileSizeMax(10002400000l);
			// 设置整个request的最大值
			upload.setSizeMax(10002400000l);
			upload.setHeaderEncoding("UTF-8");
			List<?> items = upload.parseRequest(request);
			// 2.
			FileItem item = null;
			// String imgurl=null;
			for (int j = 0; j < items.size(); j++) {
				item = (FileItem) items.get(j);
				// 保存文件
				if (!item.isFormField() && item.getName().length() > 0) {
					String str = item.getName();
					String path = compositeupdatepath(list_map, tablename, filedname,
							str.split("\\.")[0]);
					if (path == null||path.equals("")) {
						message="\n当前电子文件:"+str+",挂接结果:失败!";
						continue;
					}
					String base = filePath + tablename + "\\" + path;
					File file = new File(base);
					if (!file.exists()) {
						file.mkdirs();
					}
					String filetype = AOSId.uuid()
							+ str.substring(str.lastIndexOf("."));
					item.write(new File(base+"\\"+str));
					//执行上传操作
					try{
						//此时需要在对应的数据表的配置文件中写入记录信息
						String sql="select * from "+tablename+" where "+filedname+"='"+str.split("\\.")[0]+"'";
						List<Map<String,Object>> list =jdbcTemplate.queryForList(sql);
						String id_=(String)list.get(0).get("id_");
						//设计sql语句
						//得到指定id的电子文件的数量
						String sql2="select count(*) from "+tablename+"_path where tid='"+id_+"'";
						int index=jdbcTemplate.queryForInt(sql2);
						String sql3="insert into "+tablename+"_path (id_,tid,_path,dirname,sdatetime,_s_path,_index) values('"+AOSId.uuid()+"','"+id_+"','"+str+"','"+path.replaceAll("\\\\","/")+"','"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +"','"+str+"','"+(index+1)+"')";
						jdbcTemplate.update(sql3);
						//查询条目_path值
						String sql5="select _path from "+tablename+" where id_='"+id_+"'";
								//在把条目的标记_path加一
								String sql4="update "+tablename+" set _path=_path+1 where id_='"+id_+"'";
								jdbcTemplate.update(sql4);
						//挂接成功
						message="\n当前电子文件:"+str+",挂接结果:成功!";
					}catch(Exception e){
						//挂接失败
						message="\n当前电子文件:"+str+",挂接结果:失败!";

					}


				}
			}
		}
		return message;
	}


	/**
	 * 组合上传路径名称
	 * 
	 * @param list_map
	 * @return
	 */
	private String compositeupdatepath(String list_map, String tablename,
			String filedname, String filename) {
		// TODO Auto-generated method stub
		String path = "";
		String[] split = list_map.split(",");
		for (int i = 0; i < split.length; i++) {
			String string = split[i];
			String[] sp = string.split(":");
			String key = sp[0];
			String value = sp[1];
			if (key.equals("cl")) {
				path += value + "\\";
			} else if (key.equals("sjbzd")) {
				String sql = "select " + value + " from " + tablename
						+ " where " + filedname + "  = '" + filename + "' ";
				List<Map<String, Object>> queryForList = jdbcTemplate
						.queryForList(sql);
				if (queryForList.size() == 0||queryForList.size()>1){
					return null;
				}
				// 此时查询到了
				String str = (String) queryForList.get(0).get(value);
				if (str != null && !str.trim().equals("")) {
					path += str + "\\";
				} else {
					return "\\"+path;
				}
			}
		}
		return "\\"+path;
	}

    /**
     * ftp建立连接
     *
	 *
	 *
	 * @param ftpAddress
	 * @param path
	 * @param item
	 * @param addr
	 * @param port
	 * @param username
	 * @param password
	 * @return
     */
    public boolean connect( String ftpAddress, String path,String dpath,String filename, FileItem item, String addr, String port, String username, String password,String tablename,String filedname,String strname) {
        try {

            FTPClient ftp = new FTPClient();
            int reply;
            ftp.connect(addr);
            System.out.println("连接到：" + addr + ":" + port);
            System.out.print(ftp.getReplyString());
            reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.err.println("FTP目标服务器积极拒绝.");
                System.exit(1);
                return false;
            }else{
                ftp.login(username, password);
                ftp.enterLocalPassiveMode();
                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                //ftp.changeWorkingDirectory(path);
				//changeWorkingDirectory切换到路径
				if (!ftp.changeWorkingDirectory(ftpAddress)) {
					//如果目录不存在创建目录
					String[] dirs = path.split("\\\\");
					String tempPath = "";
					String allpath=ftpAddress;
					for (String dir : dirs) {
						if (null == dir || "".equals(dir)) continue;
						tempPath += "/" + dir;
						allpath += "/" + dir;
						if (!ftp.changeWorkingDirectory(new String(tempPath.getBytes(), FTP.DEFAULT_CONTROL_ENCODING))) {
							if (!ftp.makeDirectory(new String(tempPath.getBytes(), FTP.DEFAULT_CONTROL_ENCODING))) {
								continue;
							} else {
								ftp.changeWorkingDirectory(new String(tempPath.getBytes(), FTP.DEFAULT_CONTROL_ENCODING));
							}
						}
					}
				}
				ftp.changeWorkingDirectory(new String(path.getBytes(), FTP.DEFAULT_CONTROL_ENCODING));
				ftp.storeFile(new String(filename.getBytes(), FTP.DEFAULT_CONTROL_ENCODING),item.getInputStream());
				ftp.logout();
				item.getInputStream().close();
				//此时需要在对应的数据表的配置文件中写入记录信息
				String sql="select * from "+tablename+" where "+filedname+"='"+strname.split("\\.")[0]+"'";
				List<Map<String,Object>> list =jdbcTemplate.queryForList(sql);
				String id_=(String)list.get(0).get("id_");
				//设计sql语句
				//得到指定id的电子文件的数量
				String sql2="select count(*) from "+tablename+"_path where tid='"+id_+"'";
				int index=jdbcTemplate.queryForInt(sql2);
				String sql3="insert into "+tablename+"_path (id_,tid,_path,dirname,sdatetime,_s_path,_index) values('"+AOSId.uuid()+"','"+id_+"','"+strname+"','"+dpath.replaceAll("\\\\","/")+"','"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +"','"+strname+"','"+(index+1)+"')";
				jdbcTemplate.update(sql3);
				//查询条目_path值
				//在把条目的标记_path加一
				String sql4="update "+tablename+" set _path=_path+1 where id_='"+id_+"'";
				jdbcTemplate.update(sql4);
				return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}