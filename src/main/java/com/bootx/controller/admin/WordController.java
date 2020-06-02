
package com.bootx.controller.admin;

import com.bootx.common.Message;
import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.entity.BaseEntity;
import com.bootx.entity.Word;
import com.bootx.service.WordService;
import com.bootx.service.WordTypeService;
import com.fasterxml.jackson.annotation.JsonView;
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
@RequestMapping("/word")
public class WordController extends BaseController {

	@Autowired
	private WordService wordService;
  @Autowired
  private WordTypeService wordTypeService;

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public Message save(Word word, Long wordTypeId) {
    word.setWordType(wordTypeService.find(wordTypeId));
    Map<String,String> validResults = isValid1(word, BaseEntity.Save.class);
    if(!validResults.isEmpty()){
      return Message.error("参数错误",validResults);
    }
    wordService.save(word);
    return Message.success("添加成功");

	}

	/**
	 * 编辑
	 */
	@PostMapping("/edit")
	@JsonView(BaseEntity.EditView.class)
	public Word edit(Long id) {
		return wordService.find(id);
	}

  /**
   * 保存
   */
  @PostMapping("/update")
  public Message update(Word word,Long wordTypeId) {
    word.setWordType(wordTypeService.find(wordTypeId));
    Map<String,String> validResults = isValid1(word, BaseEntity.Save.class);
    if(!validResults.isEmpty()){
      return Message.error("参数错误",validResults);
    }
    wordService.update(word,"admins");
    return Message.success("添加成功");

  }

	/**
	 * 列表
	 */
	@PostMapping("/list")
	@JsonView(BaseEntity.ListView.class)
	public Page<Word> list(Pageable pageable,String name,Boolean isEnabled,Date beginDate,Date endDate) {
		return wordService.findPage(pageable,name,isEnabled,beginDate,endDate);
	}


	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public Message delete(Long[] ids) {
    wordService.delete(ids);
		return Message.success("删除成功");
	}

  /**
   * 列表
   */
  @PostMapping("/all")
  @JsonView(BaseEntity.TreeView.class)
  public List<Word> all() {
    return wordService.findAll();
  }
}
