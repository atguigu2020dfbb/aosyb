package cn.osworks.aos.system.modules.controller.online;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;

import cn.osworks.aos.core.dao.SqlDao;
import cn.osworks.aos.core.typewrap.Dto;
import cn.osworks.aos.core.typewrap.Dtos;
import cn.osworks.aos.system.asset.DicCons;
import cn.osworks.aos.system.modules.service.auth.RoleService;

public class onlineTaskListenerImpl implements TaskListener{

	@Autowired
	private RoleService roleService;
	@Autowired
	private SqlDao sysDao;
	public void getrolelist() {
		// TODO Auto-generated method stub
		Dto qDto = Dtos.newDto();
		qDto.put("role_id_", "39a519b4bd5148c9a513711a5a8ce6cf");
		qDto.put("delete_flag_", DicCons.DELETE_FLAG.NO);
		RoleService roleService = new RoleService();
		List<Dto> list = roleService.listGrantedUsersOfRole(qDto);
		List<Dto> grantedList = sysDao.list("Auth.listGrantedUsersOfRole", qDto);
		List<String> userlist = new ArrayList<String>();
		for(Dto d :list){
			userlist.add(d.getString("name"));
			
		}
	//	delegateTask.addCandidateUsers(userlist);
	}
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		Dto qDto = Dtos.newDto();
		qDto.put("role_id_", "58c7e5ab8cf54eaebfee74b8a1670ab7");
		qDto.put("delete_flag_", DicCons.DELETE_FLAG.NO);
		ApplicationContext context = ContextLoader
				.getCurrentWebApplicationContext();
		roleService = (RoleService) context.getBean("roleService");

		List<Dto> list = roleService.listGrantedUsersOfRole(qDto);
		List<String> listUsers = new ArrayList<String>();
		for(Dto inDto : list){
			listUsers.add(inDto.getString("name_"));
		}
		delegateTask.addCandidateUsers(listUsers);
		
	}
	
	
	public void notifyTj(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		Dto qDto = Dtos.newDto();
		qDto.put("role_id_", "39a519b4bd5148c9a513711a5a8ce6cf");
		qDto.put("delete_flag_", DicCons.DELETE_FLAG.NO);
		ApplicationContext context = ContextLoader
				.getCurrentWebApplicationContext();

		roleService = (RoleService) context.getBean("roleService");

		List<Dto> list = roleService.listGrantedUsersOfRole(qDto);
		List<String> listUsers = new ArrayList<String>();
		for(Dto inDto : list){
			listUsers.add(inDto.getString("name_"));
		}
		delegateTask.addCandidateUsers(listUsers);
		
	}
	
	public void notifySp(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		Dto qDto = Dtos.newDto();
		qDto.put("role_id_", "39a519b4bd5148c9a513711a5a8ce6cf");
		qDto.put("delete_flag_", DicCons.DELETE_FLAG.NO);
		ApplicationContext context = ContextLoader
				.getCurrentWebApplicationContext();

		roleService = (RoleService) context.getBean("roleService");

		List<Dto> list = roleService.listGrantedUsersOfRole(qDto);
		List<String> listUsers = new ArrayList<String>();
		for(Dto inDto : list){
			listUsers.add(inDto.getString("name_"));
		}
		delegateTask.addCandidateUsers(listUsers);
		
	}
}
