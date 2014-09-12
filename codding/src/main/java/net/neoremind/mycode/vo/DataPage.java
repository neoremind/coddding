package net.neoremind.mycode.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页的数据结构
 * 
 * @author lixukun
 * @param <K>
 */
@SuppressWarnings("serial")
public class DataPage<K> implements Serializable {
	/** 数据 */
	protected List<K> record;
	/** 整个数据集总记录数 */
	protected int totalRecord;
	/** 分页每页大小 */
	protected int pageSize;
	/** 当前分页的页码 */
	protected int pageNo;
	
	/**
	 * 默认构造函数
	 */
	public DataPage() {
		this.record = new ArrayList<K>();
		this.totalRecord = 0;
		this.pageSize = 1;
		this.pageNo = 1;
	}	
	
	/**
	 * 构造函数
	 * @param record
	 * @param totalRecord
	 * @param pageSize
	 * @param pageNo
	 */
	public DataPage(List<K> record,int totalRecord,int pageSize,int pageNo) {
		this.record = record;
		this.totalRecord = totalRecord;
		this.pageSize = pageSize;
		this.pageNo = pageNo;
	}
	
	/**
	 * 取得分页数据
	 * @return List<K>
	 */
	public List<K> getRecord() {
		return this.record;
	}
	
	/**
	 * 取得每页大小
	 * @return int
	 */
	public int getPageSize() {
		return this.pageSize;
	}
	
	/**
	 * 取当前面页码
	 * @return int
	 */
	public int getPageNo() {
		return this.pageNo;
	}
	
	/**
	 * 取得整个数据集总页数
	 * @return int
	 */
	public int getPageCount() {
		return (int) Math.ceil(this.totalRecord * 1.000 / this.pageSize);	
	}
	
	/**
	 * 取得当前页总记录数
	 * @return int
	 */
	public int getRecordCount() {
		return this.record.size();	
	}
	
	/**
	 * 取得整个记录集总记录数
	 * @return int
	 */
	public int getTotalRecordCount() {
		return this.totalRecord;	
	}	
	
	/**
	 * 当前页是否有下一页
	 * @return boolean
	 */
	public boolean hasNextPage() {
		if(this.pageNo < getPageCount()) {
	    	return true;
	    } else {
	    	return false;
	    }
	}
	/**
	 * 当前页是否有上一页
	 * @return boolean
	 */	
	public boolean hasPrevPage() {
		if (this.pageNo > 1) {
	    	return true;
	    } else {
	    	return false;
	    }		
	}
	
	/**
	 * 取得List的第N页的subList
	 * @param list 要分页的list
	 * @param pageSize
	 * @param pageNo
	 * @return List
	 */
	public static<T> List<T> subList(List<T> list, int pageSize, int pageNo) {
		pageSize = (pageSize <= 0 ? 10 : pageSize);
		pageNo = (pageNo <= 0 ? 1 : pageNo);	
		int begin = (pageSize * (pageNo - 1) > list.size() ? list.size(): pageSize * (pageNo - 1));
		int end = (pageSize * pageNo > list.size() ? list.size() : pageSize * pageNo);		
		return new ArrayList<T>(list.subList(begin,end));
	}
	
	/**
	 * 取得List的第N页的DataPage对象
	 * @param list 要分页的list
	 * @param pageSize
	 * @param pageNo
	 * @return List
	 */
	public static<T> DataPage<T> getByList(List<T> list, int pageSize, int pageNo) {
		List<T> l = subList(list, pageSize, pageNo);
		return new DataPage<T>(l, list.size(), pageSize, pageNo);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ record: [");
		for(int i = 0; i < record.size(); ++i) {
			sb.append(record.get(i));
			if (i != record.size())
				sb.append(", ");
		}
		sb.append("], pageNo: ")
			.append(pageNo)
			.append(", pageSize: ")
			.append(pageSize)
			.append(", totalRecord: ")
			.append(totalRecord)
			.append(" }");
		return sb.toString();
	}
}
