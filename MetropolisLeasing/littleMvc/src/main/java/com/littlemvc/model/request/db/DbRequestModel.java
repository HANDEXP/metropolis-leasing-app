package com.littlemvc.model.request.db;

import java.util.List;

import com.littlemvc.db.sqlite.FinalDb;
import com.littlemvc.model.LMModelDelegate;
import com.littlemvc.model.request.LMRequestModel;

public class DbRequestModel extends LMRequestModel{
	
	//上下文共用一个工具
	public static FinalDb finalDb;
	
	
	//通过访问result，获取刚才查询出来的参数
	public List result;
	
	//最近一次插入操作的主键
	public int id;
	
	public String currentMethod;
	
	public DbRequestModel(LMModelDelegate delegate)
	{
		super();
		this.modelDelegate = delegate;
		
	}
	
	public void insert(Object obj)
	{
		currentMethod = "insert";
		finalDb.save(obj);
		
		id = finalDb.getLastKey(obj.getClass());
		requestDidFinishLoad(this);
	}
	
	public void query(Class clazz,String strWhere,
			String orderBy)
	{
		
		currentMethod = "query";
		
		 result = finalDb.findAllByWhere(clazz,strWhere,orderBy);
		
		requestDidFinishLoad(this);
		
	}
	
	public void update(Object obj,String strWhere)
	{
		currentMethod = "update";
		finalDb.update(obj, strWhere);
		requestDidFinishLoad(this);
	}
	
}
