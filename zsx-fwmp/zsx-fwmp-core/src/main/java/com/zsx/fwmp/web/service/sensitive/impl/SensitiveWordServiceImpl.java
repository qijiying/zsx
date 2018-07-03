package com.zsx.fwmp.web.service.sensitive.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.zsx.dao.sensitive.SensitiveWordMapper;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.framework.redis.RedisUtil;
import com.zsx.fwmp.web.others.base.Log;
import com.zsx.fwmp.web.others.constant.RedisWebPreFixConstant;
import com.zsx.fwmp.web.service.sensitive.ISensitiveWordService;
import com.zsx.model.pojo.SensitiveWord;

/**
 * @ClassName SensitiveWordServiceImpl
 * @author lz
 * @description 敏感词库业务实现类
 * @date 2018年7月3日11:16:23
 */
@Service
public class SensitiveWordServiceImpl extends ServiceImpl<SensitiveWordMapper, SensitiveWord> implements ISensitiveWordService {

	@Autowired
	private SensitiveWordMapper sensitiveWordMapper;
	
	@Autowired
	private RedisUtil redisUtil;
	
	/**
	 * @Title sensitiveWord
	 * @description 初始化敏感词列表，加入缓存
	 * @see com.zsx.fwmp.web.service.sensitive.ISensitiveWordService#sensitiveWord(com.baomidou.mybatisplus.plugins.Page)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Page<SensitiveWord> sensitiveWord(Page<SensitiveWord> page) {
		Integer current = page.getCurrent();
		Integer size = page.getSize();
		String key = RedisWebPreFixConstant.WEB_SENSITIVE_LIST+current+""+size;
		List<SensitiveWord> list = new ArrayList<>();
		if(redisUtil.exists(key)){
			list = (List<SensitiveWord>) redisUtil.get(key);
			Log.debug("=================从缓存拿到敏感词====================", SensitiveWordServiceImpl.class);
		}else{
			String limit = " LIMIT "+(current-1)*size+","+size;
			list = sensitiveWordMapper.selectList(new EntityWrapper<SensitiveWord>().last(limit));
			if(list.size()>0){
				Log.debug("=================从数据库拿到敏感词，存入缓存====================", SensitiveWordServiceImpl.class);
				redisUtil.set(key, list, RedisWebPreFixConstant.REDIS_TIME);
			}
		}
		int count = sensitiveWordMapper.selectCount(new EntityWrapper<SensitiveWord>());
		page.setRecords(list);
		page.setTotal(count);
		return page;
	}

	/**
	 * @Title addSensitiveWord
	 * @description 新增敏感词
	 * @see com.zsx.fwmp.web.service.sensitive.ISensitiveWordService#addSensitiveWord(com.zsx.model.pojo.SensitiveWord)
	 */
	@Override
	public Object addSensitiveWord(SensitiveWord sensitiveWord) {
		Map<String,Object> map = Maps.newHashMap();
		map.put("code", 0);
		if(sensitiveWordMapper.insert(sensitiveWord)>0){
			map.replace("code", 1);
		}
		return map;
	}

	/**
	 * @Title uodateSensitiveWord
	 * @description 修改敏感词
	 * @see com.zsx.fwmp.web.service.sensitive.ISensitiveWordService#updateSensitiveWord(com.zsx.model.pojo.SensitiveWord)
	 */
	@Override
	public Object updateSensitiveWord(SensitiveWord sensitiveWord) {
		Map<String,Object> map = Maps.newHashMap();
		map.put("code", 0);
		if(sensitiveWordMapper.updateById(sensitiveWord)>0){
			map.replace("code", 1);
		}
		return map;
	}

	/**
	 * @Title deleteSensitiveWord
	 * @description 批量删除敏感词
	 * @see com.zsx.fwmp.web.service.sensitive.ISensitiveWordService#deleteSensitiveWord(java.lang.Integer[])
	 */
	@Override
	public Object deleteSensitiveWord(Integer[] ids) {
		Map<String,Object> map = Maps.newHashMap();
		map.put("code", 0);
		List<Integer> list = Arrays.asList(ids);
		if(sensitiveWordMapper.deleteBatchIds(list)>0){
			map.replace("code", 1);
		}
		return map;
	}


	/**
	 * @Title batchImport
	 * @description 从excel中导入敏感词
	 * @see com.zsx.fwmp.web.service.sensitive.ISensitiveWordService#batchImport(java.lang.String, org.springframework.web.multipart.MultipartFile)
	 */
	@Transactional(readOnly = false,rollbackFor = Exception.class)
    @Override
    public Object batchImport(String fileName, MultipartFile file) throws IOException{
        Map<String,Object> map = uploadExcel(fileName,file);
        String values = map.get("values")==null?null:map.get("values").toString();
        if(null!=values&&!values.isEmpty()){
            int count = sensitiveWordMapper.insertExcelValues(values);
            if(count>0) {
            	map.put("result", ResultEnum.SUCCESS);
            	map.replace("code", 1);
            }
        }
        return map;
    }

	/**
	 * @Title uploadExcel
	 * @description 获得excel信息
	 * @param fileName
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings({ "resource", "deprecation" })
	private Map<String,Object> uploadExcel(String fileName, MultipartFile file) throws IOException {
        Map<String,Object> map = Maps.newHashMap();map.put("code", 0);
        StringBuffer values = new StringBuffer();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            Log.debug("========上传文件格式不正确==========", SensitiveWordServiceImpl.class);
            return map;
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = file.getInputStream();
        Workbook wb = null;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        Sheet sheet = wb.getSheetAt(0);
        if(sheet==null){
            return map;
        }
        Row rowTitle = sheet.getRow(0);
        String columnTitle = rowTitle.getCell(0).getStringCellValue();
        if(!columnTitle.equals("name")){
        	return map;
        }
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null){
                continue;
            }
            
            if( row.getCell(0).getCellType() !=1){
                Log.debug("========导入失败(第"+(r+1)+"行,姓名请设为文本格式)==========", SensitiveWordServiceImpl.class);
            }
            String word = row.getCell(0).getStringCellValue();
 
            if(word == null || word.isEmpty()){
                Log.debug("========导入失败(第"+(r+1)+"行,姓名未填写)==========", SensitiveWordServiceImpl.class);
            }
            if(r==sheet.getLastRowNum()){
            	values.append("('"+word+"')");
            }else{
            	values.append("('"+word+"'),");
            }
        }
        Log.debug("========"+values+"==========", SensitiveWordServiceImpl.class);
        map.put("values", values);
        return map;
	}
	
	


}
