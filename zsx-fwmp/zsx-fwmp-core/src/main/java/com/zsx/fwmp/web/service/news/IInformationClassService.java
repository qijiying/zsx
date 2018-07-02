package com.zsx.fwmp.web.service.news;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zsx.model.pojo.InformationClass;

/**
 * @ClassName IInformationClassService
 * @author lz
 * @description 资讯类型业务接口
 * @date 2018年6月26日20:22:37
 */
public interface IInformationClassService extends IService<InformationClass> {

	/**
	 * @Title dataGridInCl
	 * @param i
	 * @param j
	 * @description 资讯类型初始化列表业务接口
	 * @return
	 */
	Page<InformationClass> dataGridInCl(int i, int j);

	/**
	 * @Title deleteInCl
	 * @param ids
	 * @description 批量删除资讯类型业务接口
	 * @return
	 */
	Object deleteInCl(Integer[] ids);

}
