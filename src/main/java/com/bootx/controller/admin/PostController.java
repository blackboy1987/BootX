
package com.bootx.controller.admin;

import com.bootx.common.Message;
import com.bootx.common.Pageable;
import com.bootx.entity.Post;
import com.bootx.service.DepartmentService;
import com.bootx.service.PostService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Controller - 部门
 *
 * @author blackboy
 * @version 1.0
 */
@RestController("adminPostController")
@RequestMapping("/post")
public class PostController extends BaseController {

    @Autowired
    private PostService postService;

    @Resource
    private DepartmentService departmentService;

    /**
     * 保存
     */
    @PostMapping("/save")
    public Message save(Post post, Long departmentId) {
        post.setDepartment(departmentService.find(departmentId));
        if (post.getIsEnabled() == null) {
            post.setIsEnabled(false);
        }
        if (!isValid(post)) {
            return Message.error("参数错误");
        }
        post.setAdmins(null);
        postService.save(post);
        return Message.success("操作成功");
    }

    /**
     * 编辑
     */
    @PostMapping("/edit")
    @JsonView(Post.EditView.class)
    public Post edit(Long id) {
        return postService.find(id);
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    public Message update(Post post, Long departmentId) {
        post.setDepartment(departmentService.find(departmentId));
        if (!isValid(post)) {
            return Message.error("参数错误");
        }
        postService.update(post, "admins");
        return Message.success("更新成功");
    }

    /**
     * 列表
     */
    @PostMapping("/list")
    @JsonView(Post.ListView.class)
    public Message list(Pageable pageable, Long departmentId, String name, String description, Integer level, Boolean isEnabled, Date beginDate, Date endDate) {
        return Message.success("查询成功",postService.findPage(pageable,departmentService.find(departmentId),name,description,level,isEnabled,beginDate,endDate));
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public Message delete(Long[] ids) {
        if (ids != null) {
            for (Long id : ids) {
                Post post = postService.find(id);
                if (post != null && CollectionUtils.isNotEmpty(post.getAdmins())) {
                    return Message.error("删除失败");
                }
            }
            postService.delete(ids);
        }
        return Message.success("操作成功");
    }

    /**
     * 列表
     */
    @PostMapping("/all")
    public List<Post> tree() {
        return postService.findAll();
    }

}
