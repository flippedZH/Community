package life.majiang.community.dto;

import life.majiang.community.model.Question;
import lombok.Data;
import org.apache.coyote.http2.HpackDecoder;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginnationDTO {
    private List<QuestionDto> questions;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;  //page的号码
    private Integer totalPage;

    //不开辟空间会抛异常
    private List<Integer> pages=new ArrayList<>();//一系列要展示的页码数

    //在前后端分离项目中这部分是不用写的

                                //记录数目                        每页包含的问题条数
    public void setPagination(Integer totalPage, Integer page) {
        //分页逻辑部分
        this.page=page; //传值就需要再次赋值
        this.totalPage=totalPage;
//        Integer totalPage; //总的页数

        //空指针异常
        //totalCount=11
         pages.add(page);


        //最多显示当前页面前三页，以及后三页
        for(int i=1;i<=3;i++){
            if(page-i>0){
                pages.add(0,page-i);//错误2
            }
            if(page+i<=totalPage){ //错误1
                pages.add(page+i);
            }
        }
        //是否展示上一页
        if(page==1){
            showPrevious=false;
        }else{
            showPrevious=true;
        }

        //是否展示下一页
        if(page==totalPage){
            showNext=false;
        }else {
            showNext=true;
        }

        //是否展示第一页
        if(pages.contains(1)){
            showFirstPage=false;
        }else{
            showFirstPage=true;
        }

        //是否展示最后一页
        if(pages.contains(totalPage)){
            showEndPage=false;
        }else {
            showEndPage=true;
        }
    }

}
