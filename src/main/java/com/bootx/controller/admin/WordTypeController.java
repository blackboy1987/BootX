
package com.bootx.controller.admin;

import com.bootx.common.Message;
import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.entity.BaseEntity;
import com.bootx.entity.WordType;
import com.bootx.service.WordTypeService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller - 管理员
 *
 * @author blackboy
 * @version 1.0
 */
@RestController
@RequestMapping("/word_type")
public class WordTypeController extends BaseController {

	@Autowired
	private WordTypeService wordTypeService;

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public Message save(WordType wordType) {
    Map<String,String> validResults = isValid1(wordType, BaseEntity.Save.class);
    if(!validResults.isEmpty()){
      return Message.error1("参数错误",validResults);
    }
    wordTypeService.save(wordType);
    return Message.success("添加成功");

	}

	/**
	 * 编辑
	 */
	@PostMapping("/edit")
	@JsonView(BaseEntity.EditView.class)
	public WordType edit(Long id) {
		return wordTypeService.find(id);
	}

  /**
   * 保存
   */
  @PostMapping("/update")
  public Message update(WordType wordType) {
    Map<String,String> validResults = isValid1(wordType, BaseEntity.Update.class);
    if(!validResults.isEmpty()){
      return Message.error1("参数错误",validResults);
    }
    wordTypeService.update(wordType,"words");
    return Message.success("添加成功");

  }

	/**
	 * 列表
	 */
	@PostMapping("/list")
	@JsonView(BaseEntity.ListView.class)
	public Page<WordType> list(Pageable pageable,String name,Boolean isEnabled,Date beginDate,Date endDate) {
		return wordTypeService.findPage(pageable,name,isEnabled,beginDate,endDate);
	}


	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public Message delete(Long[] ids) {
		for (Long id:ids){
		  WordType wordType = wordTypeService.find(id);
		  if(wordType!=null){
        if (CollectionUtils.isNotEmpty(wordType.getWords())) {
          return Message.error("删除失败");
        }
        wordTypeService.delete(wordType);
      }
    }
		return Message.success("删除成功");
	}

  /**
   * 列表
   */
  @PostMapping("/all")
  @JsonView(BaseEntity.TreeView.class)
  public List<WordType> all() {
    return wordTypeService.findAll();
  }
}
