/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *//*


package com.mipo.db.util;

import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

*/
/**
 * 分页工具类
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月4日 下午12:59:00
 *//*

@ApiModel
public class PageUtils<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	//总记录数
	@ApiModelProperty(value = "总记录数")
	private int totalCount;
	//每页记录数
	@ApiModelProperty(value = "每页记录数")
	private int size;
	//总页数
	@ApiModelProperty(value = "总页数")
	private int totalPage;
	//当前页数
	@ApiModelProperty(value = "当前页数")
	private int current;
	//列表数据
	@ApiModelProperty(value = "列表数据")
	private List<T> list;
	
	*/
/**
	 * 分页
	 * @param list        列表数据
	 * @param totalCount  总记录数
	 * @param size    每页记录数
	 * @param current    当前页数
	 *//*

	public PageUtils(List<T> list, int totalCount, int size, int current) {
		this.list = list;
		this.totalCount = totalCount;
		this.size = size;
		this.current = current;
		this.totalPage = (int)Math.ceil((double)totalCount/size);
	}

	*/
/**
	 * 分页
	 *//*

	public PageUtils(Page<T> page) {
		this.list = page.getRecords();
		this.totalCount = page.getTotal();
		this.size = page.getSize();
		this.current = page.getCurrent();
		this.totalPage = page.getPages();
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
}
*/
