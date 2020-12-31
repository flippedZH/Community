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
    private Integer page;
    //不开辟空间会抛异常
    private List<Integer> pages=new ArrayList<>();//一系列要展示的页码数

    //在前后端分离项目中这部分是不用写的

                                //记录数目                        每页包含的问题条数
    public void setPagination(Integer totalCount, Integer page, Integer size) {
        //分页逻辑部分

        Integer totalPage; //总的页数
        if(totalCount % size==0){
            totalPage=totalCount/size;//0
        }else{
            totalPage=totalCount/size+1;
        }
        System.out.println("页码："+page);

        //空指针异常
         pages.add(page);

         for(int i=0;i<pages.size();i++){
             System.out.println(pages.get(i));
         }

        for(int i=1;i<=3;i++){
            if(page-i>0){
                pages.add(page-1,0);
            }
            if(page+1<=totalCount){
                pages.add(page+1);
            }
        }
        //是否展示上一页
        if(page==1){
            showPrevious=false;
        }else{
            showPrevious=true;
        }

        //是否真实下一页
        if(page==1){
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
