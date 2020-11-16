package cn.osworks.aos.system.dao;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import cn.osworks.aos.system.dao.mapper.LogMapper;
import cn.osworks.aos.system.dao.po.LogPO;

public class LogUtils {
		@Autowired
		private static LogMapper logMapper;	
		//存入日志
		public static LogPO InsertLog(String id_,String party, String category, String title,
				String take,String Ip_address, String create_time) {
			// TODO Auto-generated method stub
			LogPO logPO=new LogPO();
			logPO.setId(id_);
			logPO.setParty(party);
			logPO.setCategory(category);
			logPO.setTitle(title);
			logPO.setTake(take);
			logPO.setIp_address(Ip_address);
			logPO.setCreate_time(create_time);
			return logPO;
		}
		
}
