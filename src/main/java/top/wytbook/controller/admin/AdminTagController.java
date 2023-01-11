package top.wytbook.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import top.wytbook.db.Tag;
import top.wytbook.dto.Result;
import top.wytbook.service.TagService;

@RestController
@RequestMapping("/api/admin")
public class AdminTagController {

    @Autowired
    TagService tagService;

    @PostMapping(value = "/addTag",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result addTag(@RequestParam("tagName") String tagName,
                         @RequestParam("description") String description,
                         @RequestParam("route") String tagRoute) {
        Tag tag = new Tag();
        tag.setTagName(tagName);
        tag.setTagRoute(tagRoute);
        tag.setDescription(description);
        if (tagService.save(tag)) {
            return Result.operatorOk("添加成功");
        }
        return Result.operatorFail("添加失败");
    }

    @GetMapping("/delTag/{tagId}")
    public Result removeTag(@PathVariable("tagId") Long tagId) {
        if (tagService.removeById(tagId)) {
            return Result.operatorOk("删除成功");
        }
        return Result.operatorFail("删除失败");
    }

    @PostMapping(value = "/updateTag/{tagId}",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result updateTag(@RequestParam("tagName") String tagName,
                            @RequestParam("description") String description,
                            @RequestParam("route") String tagRoute,
                            @PathVariable("tagId") Long tagId) {
        Tag tag = new Tag();
        tag.setDescription(description);
        tag.setTagName(tagName);
        tag.setTagRoute(tagRoute);
        tag.setTid(tagId);
        if (tagService.updateById(tag)) {
            return Result.operatorOk("更新成功");
        }
        return Result.operatorFail("更新失败");
    }
}
